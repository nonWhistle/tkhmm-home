package com.tkhmm.application.views.helloworld;

import com.tkhmm.application.data.entity.User;
import com.tkhmm.application.security.AuthenticatedUser;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

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
            "application click the button below.";

    private AuthenticatedUser authenticatedUser;

    public CvView(AuthenticatedUser authenticatedUser) {
        log.info("Loading Cv View");

        addClassNames("top-level-parent");

        //Initialise the header
        Header header = new Header();
//        header.setId("header");
//        Image image = new Image();
//        image.setSrc("https://en.wikipedia.org/wiki/Coca-Cola#/media/File:Coca-Cola_logo.svg");
//        image.setMaxHeight("175px");
//        Button logout = new Button("Logout");
//        header.add(image, logout);

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            avatar.setMaxHeight("175px");

            MenuBar userMenu = new MenuBar();
            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            header.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            header.add(loginLink);
        }

        //Initialise the app information section
        Section appinfo = new Section();
        appinfo.setId("app-info");
        H2 appInfoTitle = new H2("Welcome to my application");
        Paragraph paragraph = new Paragraph(appInfoText);
        Anchor anchor = new Anchor("https://github.com/nonWhistle/tkhmm-home.git", "Source Code");
        appinfo.add(appInfoTitle, paragraph, anchor);

        add(header, appinfo);
    }

}
