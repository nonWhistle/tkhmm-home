package com.tkhmm.application.views.cvview;

import com.tkhmm.application.broadcaster.CvViewBroadcaster;
import com.tkhmm.application.data.entity.User;
import com.tkhmm.application.events.GetJokeEvent;
import com.tkhmm.application.security.AuthenticatedUser;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.shared.Registration;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.logging.Logger;

@PageTitle("Tom Milton CV")
@Route(value = "tom-milton-cv")
@RouteAlias(value = "")
@CssImport("./styles/cv-view.css")
@CssImport("./styles/shared-styles.css")
//@RolesAllowed("USER")
@AnonymousAllowed
public class CvView extends Div {

    private static final Logger log = Logger.getLogger(CvView.class.getSimpleName());

    private static final String appInfoText = "This is a Spring Boot Application that runs on an AWS EC2 instance. " +
            "The backend is connected to a PostgreSQL database and communicates via a JDBC connector. The front end is " +
            "built using Vaadin libarays and custom styling is applied with CSS. To view the source code for this " +
            "application click the link below.";

    private static final String restApiText = "Click the button below to receive a joke from api-ninjas. The button " +
            "will trigger a Spring Application Event which will request a joke from the endpoint " +
            "'https://jokes-by-api-ninjas.p.rapidapi.com'. The joke will then be Broadcasted to this views id and the" +
            " joke will be displayed beneath the button";

    private final AuthenticatedUser authenticatedUser;
    private final UI ui;
    private Registration broadcasterRegistration;
    private Label fromEndPoint;
    private CvViewData cvViewData;

    private final ApplicationEventPublisher applicationEventPublisher;

    public CvView(AuthenticatedUser authenticatedUser, ApplicationEventPublisher applicationEventPublisher) {
        log.info("Loading Cv View");
        this.authenticatedUser = authenticatedUser;
        this.applicationEventPublisher = applicationEventPublisher;

        addClassNames("top-level-parent");

        ui = UI.getCurrent();
        initialiseHeader();
        initialiseRestApiSection();
    }

    private void initialiseHeader() {
        //Initialise the header
        Header header = new Header();
        header.setId("header");

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Div avatarNameContainer = new Div();
            avatarNameContainer.setId("avatar-and-name-container");

            Avatar avatar = new Avatar(user.getName());
            avatar.setColorIndex(2);
            avatar.setMaxHeight("175px");

            Label userNameLabel = new Label(user.getName());
            userNameLabel.setId("user-name-label");
            avatarNameContainer.add(avatar, userNameLabel);

            Button signOut = new Button("Sign out");
            signOut.addClickListener(click -> authenticatedUser.logout());
            signOut.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            header.add(avatarNameContainer, signOut);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            header.add(loginLink);
        }

        //Initialise the app information section
        Section appinfo = new Section();
        appinfo.addClassName("app-section");
        H2 appInfoTitle = new H2("Welcome to my application");
        Paragraph paragraph = new Paragraph(appInfoText);
        Anchor anchor = new Anchor("https://github.com/nonWhistle/tkhmm-home.git", "Source Code");
        appinfo.add(appInfoTitle, paragraph, anchor);

        add(header, appinfo);
    }

    private void initialiseRestApiSection() {
        //Initialise the app information section
        Section restApiSection = new Section();
        restApiSection.addClassName("app-section");

        fromEndPoint = new Label();
        fromEndPoint.setId("joke-line");

        Button sendToEndPoint = new Button("Get a joke");
        sendToEndPoint.addClassName("rest-tools");
        sendToEndPoint.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendToEndPoint.addClickListener(click -> {
            applicationEventPublisher.publishEvent(new GetJokeEvent(this));
        });

        restApiSection.add(new H3("Spring Application Events, RestAPI and Vaadin Broadcaster"), new Paragraph(restApiText),
                sendToEndPoint, fromEndPoint);

        add(restApiSection);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        broadcasterRegistration = CvViewBroadcaster.register((theLatestCvViewDataObject -> {
            cvViewData = theLatestCvViewDataObject;
            log.info(theLatestCvViewDataObject.getAJoke());
            ui.access(this::updateJoke);
        }), 1L);
    }

    private void updateJoke() {
        log.info("Entered here");
        fromEndPoint.setText(cvViewData.getAJoke());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }
}
