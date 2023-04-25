package com.tkhmm.application.views.login;

import com.tkhmm.application.data.Role;
import com.tkhmm.application.data.entity.User;
import com.tkhmm.application.security.AuthenticatedUser;
import com.tkhmm.application.security.UserDetailsServiceImpl;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    private final UserDetailsServiceImpl userDetailsService;

    public LoginView(AuthenticatedUser authenticatedUser, UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.authenticatedUser = authenticatedUser;
        this.userDetailsService = userDetailsService;

        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Welcome to my application");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        User user = new User();
        user.setHashedPassword(passwordEncoder.encode("channel4"));
        user.setUsername("Channel4");
        user.setRoles(Set.of(Role.USER));
        user.setName("Channel 4");
        userDetailsService.save(user);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
