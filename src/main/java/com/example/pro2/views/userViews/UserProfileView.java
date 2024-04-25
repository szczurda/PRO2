package com.example.pro2.views.userViews;

import com.example.pro2.security.SecurityService;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.example.pro2.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("profile")
@RolesAllowed("ROLE_USER")
public class UserProfileView extends AppLayout implements HasUrlParameter<String> {
    Button homeButton = new Button("Domů");
    Button favoritesButton = new Button("Oblíbené");
    Button reviewsButton = new Button("Recenze");

    Button profileButton = new Button("Profil");
    private String username;
    private SecurityService securityService;
    private UserServiceImpl userService;

    private EntityManagerService service;

    private VerticalLayout favoritesView;
    private HorizontalLayout reviewsView;
    private VerticalLayout profileContentContainer = new VerticalLayout();
    private TextField usernameField;
    private PasswordField passwordField;

    private Avatar avatar = new Avatar();
    private H1 title = new H1();
    @Autowired
    public UserProfileView(UserServiceImpl userService, EntityManagerService service, SecurityService securityService) {
        this.securityService = securityService;
        this.service = service;
        this.userService = userService;
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)").set("margin", "0")
                .set("position", "absolute");
        HorizontalLayout navigation = getNavigation();
        navigation.getElement();
        addToNavbar(navigation);
        setContent(profileContentContainer);
    }

    private HorizontalLayout getNavigation() {
        HorizontalLayout navigationLayout = new HorizontalLayout();
        navigationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        navigationLayout.add(homeButton, profileButton, favoritesButton, reviewsButton);
        navigationLayout.setWidthFull();
        initButtons();
        return navigationLayout;
    }


    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String usernameParameter) {
        try {
            String retrievedUsername = userService.getUserByUsername(usernameParameter).getUsername();
            String authenticatedUser = securityService.getAuthenticatedUser().getUsername();
            if (retrievedUsername != null && retrievedUsername.equals(authenticatedUser)) {
                this.username = retrievedUsername;
                initFields();
                initLayouts();
            } else {
                Notification.show("Neautorizovaný uživael, je nutné přihlášení").setDuration(5000);
                event.forwardTo("login");

            }
        } catch (Exception e) {

            Notification.show("Nastala chyba při ověřování uživatele")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            event.forwardTo("login");
        }
    }

    private void initButtons(){
        favoritesButton.setIcon(VaadinIcon.HEART.create());
        favoritesButton.addClickListener(e -> setContent(favoritesView));
        homeButton.addClickListener(e -> UI.getCurrent().navigate(MainView.class));
        homeButton.setIcon(VaadinIcon.HOME.create());
        reviewsButton.addClickListener(e -> setContent(reviewsView));
        reviewsButton.setIcon(VaadinIcon.STAR.create());
        profileButton.addClickListener(e -> setContent(profileContentContainer));
        profileButton.setIcon(VaadinIcon.USER.create());
    }

    private void initLayouts(){
        favoritesView = new UserFavoritesView(userService, service, securityService);
        reviewsView = new UserReviewsView(userService, service, securityService);
        profileContentContainer.add(new H1("Profil uživatele " + username));
        profileContentContainer.setSizeFull();
        initFields();
        profileContentContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        profileContentContainer.add(title, avatar, usernameField, passwordField);
    }

    private void initFields() {
        usernameField = new TextField("Uživatelské jméno");
        passwordField = new PasswordField("Heslo");
        usernameField.setValue(username);
        usernameField.setReadOnly(true);
        passwordField.setReadOnly(true);
        passwordField.setValue(userService.getUserByUsername(username).getPassword());
    }



}