package com.example.pro2.views.adminViews;


import com.example.pro2.entities.User;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "/adminView/userManager", layout = AdminParentView.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminUserManagerView extends VerticalLayout {

    EntityManagerService service;

    UserServiceImpl userService;

    Grid<User> grid  = new Grid<>(User.class);

    public AdminUserManagerView(EntityManagerService service, UserServiceImpl userService) {
        this.service = service;
        this.userService = userService;
        setSizeFull();
        configGrid();
        getStyle().setDisplay(Style.Display.FLEX)
                .setFlexDirection(Style.FlexDirection.COLUMN)
                .setAlignItems(Style.AlignItems.CENTER)
                .setJustifyContent(Style.JustifyContent.CENTER)
                .setMargin("50px");
        add(grid);

    }

    public void configGrid(){
        ListDataProvider<User> dataProvider = new ListDataProvider<>(userService.findAll());
        grid.setWidth("80%");
        grid.setHeight("80%");
        grid.setColumns("userId", "username");
        grid.getColumnByKey("userId").setHeader("ID uživatele");
        grid.getColumnByKey("username").setHeader("Uživatelské jméno");
        grid.getColumns().forEach(column -> grid.setClassName("columns"));
        grid.setItems(dataProvider);
    }



}
