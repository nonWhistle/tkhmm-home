package com.tkhmm.application.views.cvview;

import com.tkhmm.application.broadcaster.CvViewBroadcaster;
import com.tkhmm.application.data.entity.User;
import com.tkhmm.application.events.GetJokeEvent;
import com.tkhmm.application.events.PostTextEvent;
import com.tkhmm.application.security.AuthenticatedUser;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.shared.Registration;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;
import java.util.logging.Logger;

@PageTitle("Tom Milton CV")
@Route(value = "tom-milton-cv")
@RouteAlias(value = "")
@CssImport("./styles/cv-view.css")
@CssImport("./styles/shared-styles.css")
@RolesAllowed("USER")
public class CvView extends Div {

    private static final Logger log = Logger.getLogger(CvView.class.getSimpleName());

    private static final String appInfoText = "This is a Spring Boot Application that runs on an AWS EC2 instance. " +
            "The backend is connected to a PostgreSQL database hosted on an AWS RDS instance and communicates via a " +
            "JDBC connector. The front end is built using Vaadin libarays and custom styling is applied with CSS. " +
            "To view the source code for this application click the link below.";

    private static final String restApiText = "Click the button below to receive a joke from api-ninjas. The button " +
            "will trigger a Spring Application Event which will request a joke from the endpoint " +
            "'https://jokes-by-api-ninjas.p.rapidapi.com'. The joke will then be Broadcasted to this views id and the" +
            " joke will be displayed beneath the button";

    private static final String restApiTextSectionTwo = "Enter text in the area below and press the button. This will trigger " +
            "a Spring Application event which will use Unirest to send a post request to the endpoint " +
            "'https://theclimbersclimber.co.uk/tkhmm/labeltext/{message}'. This endpoint will then broadcast the message which " +
            "this view is subscribed to and the message will be displayed below the button.";

    private final AuthenticatedUser authenticatedUser;
    private final User user;
    private final UI ui;
    private Registration broadcasterRegistration;
    private Label fromEndPoint;
    private Label fromLocalEndpoint;
    private CvViewData cvViewData;

    private final ApplicationEventPublisher applicationEventPublisher;

    public CvView(AuthenticatedUser authenticatedUser, ApplicationEventPublisher applicationEventPublisher) {
        log.info("Loading Cv View");
        this.authenticatedUser = authenticatedUser;
        this.applicationEventPublisher = applicationEventPublisher;
        user = authenticatedUser.get().isPresent()? authenticatedUser.get().get() : null;

        addClassNames("top-level-parent");

        ui = UI.getCurrent();
        initialiseHeader();
        initialiseRestApiSection();
        initialiseRestApiSectionTwo();
        initialiseFooter();
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
        //Initialise the app Rest section one
        Section restApiSection = new Section();
        restApiSection.addClassName("app-section");

        fromEndPoint = new Label();
        fromEndPoint.setId("joke-line");

        Button sendToEndPoint = new Button("Get a joke");
        sendToEndPoint.addClassName("rest-tools");
        sendToEndPoint.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendToEndPoint.addClickListener(click -> {
            applicationEventPublisher.publishEvent(new GetJokeEvent(this, user != null? user.getId() : 0L));
        });

        restApiSection.add(new H3("Spring Application Events, RestAPI and Vaadin Broadcaster"), new Paragraph(restApiText),
                sendToEndPoint, fromEndPoint);

        add(restApiSection);
    }

    private void initialiseRestApiSectionTwo() {
        //Initialise the app Rest section two
        Section restApiSection = new Section();
        restApiSection.addClassName("app-section");

        TextArea textArea = new TextArea("Enter text here");
        textArea.addClassName("rest-tools");

        fromLocalEndpoint = new Label();

        Button sendToLocalEndPoint = new Button("Post text");
        sendToLocalEndPoint.addClassName("rest-tools");
        sendToLocalEndPoint.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendToLocalEndPoint.addClickListener(click -> {
            if (!textArea.isEmpty()) {
                applicationEventPublisher.publishEvent(new PostTextEvent(this, textArea.getValue(),
                        user != null? user.getId() : 0L));
            } else {
                Notification note = new Notification("Please enter some text and try again", 5000, Notification.Position.MIDDLE);
                note.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                note.open();
            }
        });

        restApiSection.add(new H3("Spring Rest Controller"), new Paragraph(restApiTextSectionTwo), textArea,
                sendToLocalEndPoint, fromLocalEndpoint);

        add(restApiSection);
    }

    private void initialiseFooter() {
        Footer footer = new Footer();
        footer.setId("footer");
        H4 contactMe = new H4("Contact me");
        Label email = new Label("Email:  tom.milton92@outlook.com");
        Label mobile = new Label("Mobile: 07415711109");
        footer.add(contactMe, email, mobile);
        add(footer);

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        broadcasterRegistration = CvViewBroadcaster.register((theLatestCvViewDataObject -> {
            cvViewData = theLatestCvViewDataObject;
            log.info(theLatestCvViewDataObject.getAJoke());
            ui.access(this::updateSections);
        }), user != null? user.getId() : 0L);
    }

    private void updateSections() {
        if (cvViewData.getAJoke() != null) {
            fromEndPoint.setText(cvViewData.getAJoke());
        }

        if (cvViewData.getAMessage() != null) {
            fromLocalEndpoint.setText(cvViewData.getAMessage());
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }
}
