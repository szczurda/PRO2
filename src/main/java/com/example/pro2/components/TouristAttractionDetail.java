package com.example.pro2.components;
import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.User;
import com.example.pro2.entities.UserFavorite;
import com.example.pro2.entities.UserReview;
import com.example.pro2.security.SecurityService;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.dom.Style;
import org.springframework.security.core.Authentication;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class TouristAttractionDetail extends Dialog {
    private BeanValidationBinder<TouristAttraction> binder = new BeanValidationBinder<>(TouristAttraction.class);
    private TextField name, category, region, price, city;
    private NumberField rating;
    private TextArea review, description;
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout horizontalContainer = new HorizontalLayout();
    private HorizontalLayout headingContainer = new HorizontalLayout();
    private HorizontalLayout buttonContainer = new HorizontalLayout();
    private VerticalLayout rightContainer = new VerticalLayout();
    private VerticalLayout leftContainer = new VerticalLayout();
    private TouristAttraction attraction;
    private final SecurityService securityService;
    private Button closeButton, submitButton, favoriteButton;
    private HorizontalLayout starsLayout = new HorizontalLayout();
    private int ratingIndex;
    private EntityManagerService service;
    private UserServiceImpl userService;
    public TouristAttractionDetail(UserServiceImpl userService, EntityManagerService service, TouristAttraction attraction, SecurityService securityService) {
        this.securityService = securityService;
        this.service = service;
        this.userService = userService;
        this.attraction = attraction;
        setWidth("75%");
        initButtons();
        initFields();
        configRating();
        initContainers();
        initBinders();
        add(mainLayout);
    }

    private void initContainers() {
        leftContainer.setHeightFull();
        leftContainer.setAlignItems(FlexComponent.Alignment.BASELINE);
        rightContainer.setWidthFull();
        horizontalContainer.setSizeFull();
        buttonContainer.add(submitButton);
        buttonContainer.setWidthFull();
        buttonContainer.setHeightFull();
        buttonContainer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        buttonContainer.getStyle().set("justify-content", "end");
        leftContainer.add(new HorizontalLayout(name, category)
                ,new HorizontalLayout(city, region)
                ,new HorizontalLayout(price, rating),
                description,
                buttonContainer);
        rightContainer.add(new H2("Přidat recenzi"),  starsLayout, review, buttonContainer);
        horizontalContainer.add(leftContainer, rightContainer);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        H1 heading = new H1("Detail atrakce");
        headingContainer.add(heading, favoriteButton, closeButton);
        headingContainer.setWidthFull();
        headingContainer.getStyle()
                        .set("display", "flex")
                        .set("text-align", "center");
        heading.getStyle().set("flex-grow", "1");
        mainLayout.add(headingContainer, horizontalContainer);
    }

    private void initFields() {
        review = new TextArea("Napište recenzi");
        review.setMaxLength(200);
        review.setMaxHeight("200");
        review.setWidthFull();
        review.setHeightFull();
        name = new TextField("Název");
        name.setReadOnly(true);
        name.setWidth("50%");
        category = new TextField("Kategorie destinace");
        category.setReadOnly(true);
        category.setWidth("50%");
        region = new TextField("Kraj");
        region.setReadOnly(true);
        region.setWidth("50%");
        city = new TextField("Město");
        city.setReadOnly(true);
        city.setWidth("50%");
        price = new TextField("Cena");
        price.setReadOnly(true);
        price.setWidth("50%");
        rating = new NumberField("Hodnocení");
        rating.setReadOnly(true);
        rating.setWidth("50%");
        rating.setPrefixComponent(new Icon(VaadinIcon.STAR));
        description = new TextArea("Popis");
        description.setReadOnly(true);
        description.setWidth("85%");
        description.setHeightFull();
    }

    private void initBinders(){
        binder.bind(name, TouristAttraction::getName, TouristAttraction::setName);
        binder.bind(category, TouristAttraction::getCategory, TouristAttraction::setCategory);
        binder.bind(region, TouristAttraction::getRegion, TouristAttraction::setRegion);
        binder.bind(price, TouristAttraction::getPrice, TouristAttraction::setPrice);
        binder.bind(city, TouristAttraction::getCity, TouristAttraction::setCity);
        binder.bind(rating, TouristAttraction::getRating, TouristAttraction::setRating);
        binder.bind(description, TouristAttraction::getDescription, TouristAttraction::setDescription);
        binder.setBean(attraction);
    }

    private void initButtons(){
        closeButton = new Button();
        closeButton.addClickShortcut(Key.ESCAPE);
        closeButton.setIcon(VaadinIcon.CLOSE.create());
        closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeButton.addClickListener(e -> {
            this.close();
        });
        submitButton = new Button("Uložit recenzi");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.getStyle().set("float", "right");
        submitButton.addClickListener(e -> {
            if(ratingIndex != 0){
                saveReview();
            }
        });
        favoriteButton = new Button("Přidat do oblíbených");
        favoriteButton.setIcon(new Icon(VaadinIcon.HEART));
        favoriteButton.getStyle().setPosition(Style.Position.ABSOLUTE);
        favoriteButton.addClickListener(e -> {
            String username = securityService.getAuthenticatedUser().getUsername();
            service.saveToFavorites(binder.getBean(), userService.getUserByUsername(username));
            System.out.println(service.findAllUserFavoritesByUsername(username));
        });
    }

    private void saveReview() {
        Authentication authentication = securityService.getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            String currentUser = authentication.getName();
            try{
                User user = userService.getUserByUsername(currentUser);
                service.saveUserReview(user, review.getValue(), ratingIndex, attraction);
                Notification.show("Recenze přidána").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception e){
                Notification.show("Chyba - nedostatečné oprávnění").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            this.close();
        } else Notification.show("Chyba v ověření uživatele");


    }

    private void configRating() {
        String userName = securityService.getAuthenticatedUser().getUsername();
        Long userId = userService.getUserByUsername(userName).getUserId();
        UserReview userReview = service.findUserReviewByUserIdAndAttractionId(userId, attraction.getTouristAttractionId());
        if(userReview != null){
            review.setValue(userReview.getReviewText());
            ratingIndex = userReview.getRating();
            submitButton.setEnabled(false);
            submitButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        } else ratingIndex = 0;
        starsLayout = new HorizontalLayout();
        starsLayout.setWidthFull();
        starsLayout.getStyle().set("border", "2px solid blue")
                .set("margin-top", "20px");
        starsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        starsLayout.addClassName("stars-layout");
        List<Button> stars = new ArrayList<>();
        Button star1 = new Button();
        Button star2 = new Button();
        Button star3 = new Button();
        Button star4 = new Button();
        Button star5 = new Button();
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);
        if (ratingIndex != 0) {
            for (int i = 0; i < ratingIndex; i++) {
                stars.get(i).setIcon(new Icon(VaadinIcon.STAR));
            }
            for (int i2 = stars.size() - 1; i2 >= ratingIndex; i2--) {
                stars.get(i2).setIcon(new Icon(VaadinIcon.STAR_O));
            }
        } else {
            for (Button star : stars) {
                star.setIcon(new Icon(VaadinIcon.STAR_O));
            }
        }

        for (int i = 0, starsSize = stars.size(); i < starsSize; i++) {
            Button star = stars.get(i);
            star.getStyle().set("background-color", "white");
            int finalI = i;
            star.addClickListener(e -> {
                for (int j = 0; j <= finalI; j++) {
                    stars.get(j).setIcon(new Icon(VaadinIcon.STAR));
                    if (j == finalI) {
                        ratingIndex = j + 1;
                    }
                }
                for (int k = stars.size() - 1; k > finalI; k--) {
                    stars.get(k).setIcon(new Icon(VaadinIcon.STAR_O));
                }

            });
        }
        starsLayout.add(star1, star2, star3, star4, star5);
    }



}

