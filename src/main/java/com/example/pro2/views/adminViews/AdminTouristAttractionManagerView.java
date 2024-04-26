package com.example.pro2.views.adminViews;

import com.example.pro2.components.TouristAttractionDetail;
import com.example.pro2.components.TouristAttractionEditDialog;
import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.security.SecurityService;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Route(value = "/adminView/attractionManager", layout = AdminParentView.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminTouristAttractionManagerView extends HorizontalLayout {

    EntityManagerService service;

    UserServiceImpl userService;

    SecurityService securityService;

    Grid<TouristAttraction> grid = new Grid<>(TouristAttraction.class, false);

    List<TouristAttraction> dataProvider;

    VerticalLayout mainContainer;
    VerticalLayout leftInnerContainer, rightInnerContainer;
    Button cancelButton, submitButton;

    HorizontalLayout fieldsContainer, buttonsContainer;
    private TextField nameField, categoryField, regionField, priceField, cityField;
    private NumberField ratingField;

    Button editButton, deleteButton;

    public AdminTouristAttractionManagerView(EntityManagerService service, UserServiceImpl userService, SecurityService securityService) {
        this.service = service;
        this.securityService = securityService;
        this.userService = userService;
        initAttractionsGrid();
        setSizeFull();
        add(grid);
    }

    private void initAttractionsGrid(){
        grid.addColumn(TouristAttraction::getName).setHeader("Název");
        grid.addColumn(TouristAttraction::getCategory).setHeader("Kategorie destinace");
        grid.addColumn(TouristAttraction::getRegion).setHeader("Kraj");
        grid.addColumn(TouristAttraction::getCity).setHeader("Město");
        grid.addColumn(TouristAttraction::getPrice).setHeader("Cena");
        grid.addColumn(TouristAttraction::getRating).setHeader("Hodnocení");
        grid.addComponentColumn((ValueProvider<TouristAttraction, Component>) touristAttraction -> {
            editButton = new Button();
            editButton.setIcon(VaadinIcon.EDIT.create());
            deleteButton = new Button();
            deleteButton.setIcon(VaadinIcon.TRASH.create());
            initListeners(touristAttraction);
            return new HorizontalLayout(editButton, deleteButton);
        });
        grid.setSizeFull();
        grid.getStyle().setMargin("50px");
        dataProvider = service.findAllTouristAttractions();
        grid.setItems(dataProvider);
    }

    private void initListeners(TouristAttraction touristAttraction) {
        editButton.addClickListener(e -> {
            createDialog(touristAttraction).open();
        });

        deleteButton.addClickListener(e -> {
            service.deleteAttraction(touristAttraction);
            grid.setItems(service.findAllTouristAttractions());
        });
    }

    private void initButtons() {
        cancelButton = new Button("Zrušit");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        submitButton = new Button("Potvrdit změny");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private Dialog createDialog(TouristAttraction touristAttraction){
        initButtons();
        initFields();
        Dialog dialog = new Dialog();
        ratingField.setValue(touristAttraction.getRating());
        priceField.setValue(touristAttraction.getPrice());
        nameField.setValue(touristAttraction.getName());
        cityField.setValue(touristAttraction.getCity());
        regionField.setValue(touristAttraction.getRegion());
        categoryField.setValue(touristAttraction.getCategory());
        fieldsContainer = new HorizontalLayout(leftInnerContainer, rightInnerContainer);
        fieldsContainer.getStyle().setJustifyContent(Style.JustifyContent.CENTER);
        buttonsContainer = new HorizontalLayout(submitButton, cancelButton);
        buttonsContainer.getStyle().setJustifyContent(Style.JustifyContent.CENTER);
        buttonsContainer.setWidthFull();
        mainContainer = new VerticalLayout(new H2("Upravit atrakci"), fieldsContainer, buttonsContainer);
        mainContainer.getStyle().setTextAlign(Style.TextAlign.CENTER);
        cancelButton.addClickListener(e -> dialog.close());
        submitButton.addClickListener(e -> {

        });
        dialog.add(mainContainer);
        return dialog;
    }

    private void initFields() {
        categoryField = new TextField("Kategorie");
        cityField = new TextField("Město");
        nameField = new TextField("Název atrakce");
        priceField = new TextField("Cena");
        regionField = new TextField("Kraj");
        ratingField = new NumberField("Hodnocení");
        leftInnerContainer = new VerticalLayout();
        leftInnerContainer.add(nameField, cityField, regionField);
        rightInnerContainer = new VerticalLayout();
        rightInnerContainer.add(categoryField, ratingField, priceField);
        ratingField.setReadOnly(true);
    }
}
