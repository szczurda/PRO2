package com.example.pro2.views.userViews;

import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.UserReview;
import com.example.pro2.security.SecurityService;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;

@RolesAllowed("ROLE_USER")
public class UserReviewsView extends HorizontalLayout {

    private VerticalLayout leftLayout, rightLayout;
    private TextArea review;
    private NumberField rating;
    private Grid<TouristAttraction> reviewGrid = new Grid<>(TouristAttraction.class, false);

    private VerticalLayout ratingPanel = new VerticalLayout();

    private HorizontalLayout buttonLayout = new HorizontalLayout();

    private Button editButton, saveButton, cancelButton;
    private UserServiceImpl userService;
    private EntityManagerService service;
    private SecurityService securityService;
    private String currentUser;
    private ListDataProvider<TouristAttraction> dataProvider;
    private HorizontalLayout starsLayout;
    private int ratingIndex = 0;


    public UserReviewsView(UserServiceImpl userService, EntityManagerService service, SecurityService securityService) {
        this.userService = userService;
        this.service = service;
        this.securityService = securityService;
        currentUser = securityService.getAuthenticatedUser().getUsername();
        setSizeFull();
        setSpacing(true);
        configRating();
        configButtons();
        initFields();
        initGrid();
        initLayouts();
    }

    private void configButtons() {
        cancelButton = new Button("Zrušit úpravu");
        editButton = new Button("Upravit hodnocení");
        saveButton = new Button("Uložit");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addClickListener(e -> {
            disableEditor();
        });
        editButton.addClickListener(e -> {
            enableEditor();
        });
        saveButton.addClickListener(e -> {
            TouristAttraction selectedAttraction = reviewGrid.asSingleSelect().getValue();
            if (selectedAttraction != null) {
                Long attractionId = selectedAttraction.getTouristAttractionId();
                Long userId = userService.getUserByUsername(currentUser).getUserId();
                UserReview userReview = service.findUserReviewByUserIdAndAttractionId(userId, attractionId);
                if (userReview != null) {
                        userReview.setReviewText(review.getValue());
                        userReview.setRating(ratingIndex);
                        service.updateReview(userReview);
                        dataProvider.refreshItem(selectedAttraction);
                        disableEditor();
                }
            }
        });
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.add(editButton);
        buttonLayout.setWidthFull();
    }

    private void initLayouts() {
        //left
        leftLayout = new VerticalLayout();
        leftLayout.getStyle()
                .set("padding", "50px")
                .set("margin", "20px");
        leftLayout.setSizeFull();
        leftLayout.add(new H1("Přehled hodnocených míst"), reviewGrid);

        //right
        rightLayout = new VerticalLayout();
        rightLayout.setSizeFull();
        rightLayout.getStyle()
                .set("padding", "50px")
                .set("margin", "20px");
        review.setReadOnly(true);
        ratingPanel.add(review, starsLayout, buttonLayout);
        rightLayout.add(new H2("Upravit hodnocení"), ratingPanel);
        add(leftLayout, rightLayout);
    }

    private void initGrid(){
        reviewGrid.setSizeFull();
        Long userId = userService.getUserByUsername(currentUser).getUserId();
        dataProvider = DataProvider.ofCollection(userService.findTouristAttractionsWithUserRating(userId));
        reviewGrid.setItems(dataProvider);
        reviewGrid.addColumn(TouristAttraction::getName).setHeader("Název");
        reviewGrid.addColumn(TouristAttraction::getCategory).setHeader("Kategorie destinace");
        reviewGrid.addColumn(TouristAttraction::getRegion).setHeader("Kraj");
        reviewGrid.addColumn(TouristAttraction::getCity).setHeader("Město");
        reviewGrid.addColumn(TouristAttraction::getPrice).setHeader("Cena");
        reviewGrid.addColumn(TouristAttraction::getRating).setHeader("Hodnocení");
        reviewGrid.asSingleSelect().addValueChangeListener(e -> {
            disableEditor();
            if(e.getValue() != null){
                UserReview userReview = service.findUserReviewByUserIdAndAttractionId(userId, e.getValue().getTouristAttractionId());
                review.setValue(userReview.getReviewText());
                ratingPanel.remove(starsLayout);
                configRating(userReview);
                ratingPanel.addComponentAtIndex(1, starsLayout);
            }
        });


    }

    private void configRating(UserReview userReview) {
        ratingIndex = userReview.getRating();
        starsLayout = new HorizontalLayout();
        starsLayout.setEnabled(false);
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

    private void configRating() {
        starsLayout = new HorizontalLayout();
        starsLayout.setEnabled(false);
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

    private void initFields() {
        review = new TextArea("Napište recenzi");
        review.setMaxLength(200);
        review.setMaxHeight("200");
        review.setWidthFull();
        review.setHeightFull();
        rating = new NumberField("Hodnocení");
        rating.setReadOnly(true);
        rating.setWidth("50%");
        rating.setPrefixComponent(new Icon(VaadinIcon.STAR));
    }

    private void enableEditor(){
        buttonLayout.add(saveButton, cancelButton);
        buttonLayout.remove(editButton);
        starsLayout.setEnabled(true);
        review.setReadOnly(false);
    }

    private void disableEditor(){
        buttonLayout.remove(saveButton, cancelButton);
        buttonLayout.add(editButton);
        starsLayout.setEnabled(false);
        review.setReadOnly(true);
    }
}
