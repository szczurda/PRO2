package com.example.pro2.components;

import com.example.pro2.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainNavbar extends HorizontalLayout {
    Button logoutButton = new Button("Odhlásit se");

    SecurityService securityService;
    Button profileButton = new Button();


    public MainNavbar(SecurityService securityService){
        this.securityService = securityService;
        setClassName("navbar");
        setVisible(true);
        configButtons();
        H1 header = new H1("Katalog turistických atrakcí");
        header.getStyle().set("flex-grow", "3");
        add(header, profileButton, logoutButton);
        setSpacing(false);
        getThemeList().add("spacing-s");
        setWidthFull();
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        getStyle().set("border-top", "5px solid royalblue")
                .set("padding", "10px")
                .set("border-bottom", "5px solid royalblue")
                .set("display", "flex");

    }
    private void configButtons() {
        String currentUser = securityService.getAuthenticatedUser().getUsername();
        profileButton.setIcon(VaadinIcon.USER.create());
        profileButton.setText(currentUser);
        profileButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        profileButton.addClickListener(e -> {
           UI.getCurrent().navigate("profile/" + currentUser);
        });
        logoutButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logoutButton.addClickListener(e -> {
            securityService.logout();
           Notification.show("Byli jste úspěšně odhlášeni");
        });
    }
}
