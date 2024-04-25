package com.example.pro2.views.userViews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout{

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);
        Button goToRegistrationButton = new Button("Registrovat");
        goToRegistrationButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        goToRegistrationButton.addClickListener(e -> {
            UI.getCurrent().navigate("registration");
        });
        add(new H1("Přihlašte se"), loginForm, goToRegistrationButton);
    }

}
