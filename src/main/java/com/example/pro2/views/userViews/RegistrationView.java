package com.example.pro2.views.userViews;

import com.example.pro2.entities.User;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("registration")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {
    TextField userNameField = new TextField("Uživatelské jméno");
    PasswordField passwordField = new PasswordField("Heslo");
    PasswordField confirmPassField = new PasswordField("Potvrzení hesla");
    Button confirmRegistrationButton = new Button("Potvrdit registraci");

    Button goToLoginButton = new Button("Přihlášení");
    private UserServiceImpl userService;

    @Autowired
    public RegistrationView(UserServiceImpl userService){
        this.userService = userService;
        setClassName("user-reg-form");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        initComponents();
        add(new H1("Registrace nového uživatele"), userNameField,passwordField, confirmPassField, confirmRegistrationButton, goToLoginButton);
    }
    private void register(String userName, String password, String confirmPassword) {
        if(userName.trim().isEmpty()){
            Notification.show("Pole se jménem nesmí být prázdné");
        } else if(password.trim().length() < 6){
            Notification.show("Heslo musí mít alespoň 6 znaků");
        } else if (password.isEmpty()) {
            Notification.show("Pole s heslem nesmí být prázdné");
        } else if (confirmPassword.isEmpty()) {
            Notification.show("Potvrzení hesla nesmí být prázdné");
        } else if(!password.equals(confirmPassword)){
            Notification.show("Hesla se neshodují");
        } else {
            if (!userService.userAlreadyExists(userName)) {
                User newUser = new User(password, userName, "USER");
                userService.saveUser(newUser);
                Notification success = new Notification();
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                success.setDuration(5000);
                success.setText("Registrace proběhla úspěšně");

                UI.getCurrent().navigate("main");

            } else {
                Notification.show("Uživatelské jméno již existuje");
            }
        }


    }

    private void initComponents(){
        goToLoginButton.addClickListener(e -> {
           UI.getCurrent().navigate("login");
        });
        confirmRegistrationButton.addClickListener(e -> register(userNameField.getValue(), passwordField.getValue(), confirmPassField.getValue()));
        passwordField.getElement().setAttribute("autocomplete", "new-password");
        goToLoginButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        confirmRegistrationButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        confirmRegistrationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        userNameField.setWidth("400px");
        passwordField.setWidth("400px");
        confirmPassField.setWidth("400px");
        confirmRegistrationButton.addClickShortcut(Key.ENTER);
    }
}
