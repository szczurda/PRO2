package com.example.pro2.views.adminViews;

import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
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

    Grid<TouristAttraction> grid = new Grid<>(TouristAttraction.class, false);

    List<TouristAttraction> dataProvider;

    Button editButton, deleteButton;

    public AdminTouristAttractionManagerView(EntityManagerService service, UserServiceImpl userService) {
        this.service = service;
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

        });

        deleteButton.addClickListener(e -> {
            service.deleteAttraction(touristAttraction);
            grid.setItems(service.findAllTouristAttractions());
        });
    }
}
