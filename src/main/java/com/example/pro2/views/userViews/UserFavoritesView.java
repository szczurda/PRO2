package com.example.pro2.views.userViews;

import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.UserFavorite;
import com.example.pro2.security.SecurityService;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.security.RolesAllowed;

import java.util.Iterator;
import java.util.List;

@RolesAllowed("ROLE_USER")
public class UserFavoritesView extends VerticalLayout {
    private UserServiceImpl userService;
    private EntityManagerService service;

    private SecurityService securityService;

    private String currentUser;

    private TextField name, category, region, price, city;
    private NumberField rating;
    private List<UserFavorite> userFavoriteList;

    private Iterator<UserFavorite> userFavoriteIterator;

    public UserFavoritesView(UserServiceImpl userService, EntityManagerService service, SecurityService securityService) {
        this.securityService = securityService;
        this.userService = userService;
        this.service = service;
        currentUser = securityService.getAuthenticatedUser().getUsername();
        userFavoriteList = service.findAllUserFavoritesByUsername(currentUser);
        userFavoriteIterator = userFavoriteList.iterator();
        add(new H1("Oblíbené turistické atrakce"));
        while (userFavoriteIterator.hasNext()){
            add(addItemContainer(userFavoriteIterator.next().getTouristAttraction()));
        }
        setSizeFull();
        setAlignItems(Alignment.CENTER);

    }

    private VerticalLayout addItemContainer(TouristAttraction item) {
        //main
        VerticalLayout itemContainer = new VerticalLayout();
        itemContainer.setAlignItems(Alignment.CENTER);
        itemContainer.setWidth("50%");
        itemContainer.setHeight("50%");
        itemContainer.getStyle().setBorderTop("3px solid blue").setMarginBottom("15px");
        //fields
        initFields();
        name.setValue(item.getName());
        category.setValue(item.getCategory());
        rating.setValue(item.getRating());
        region.setValue(item.getRegion());
        city.setValue(item.getCity());
        price.setValue(item.getPrice());
        //inner
        HorizontalLayout innerContainer = new HorizontalLayout();
        innerContainer.setSizeFull();
        innerContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        //left
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setSizeFull();
        leftLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        leftLayout.add(name, category, rating);
        // right
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setSizeFull();
        rightLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        // heading and button
        HorizontalLayout headingContainer = new HorizontalLayout();
        headingContainer.setSizeFull();
        headingContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        headingContainer.getStyle().setPosition(Style.Position.RELATIVE);
        Button removeButton = new Button();
        removeButton.addClickListener(e -> {
            removeFromFavorites(item);
            remove(itemContainer);

        });
        removeButton.getStyle()
                        .setPosition(Style.Position.ABSOLUTE)
                        .setRight("0");
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeButton.setIcon(VaadinIcon.TRASH.create());
        rightLayout.add(region, city, price);
        innerContainer.add(leftLayout, rightLayout);
        headingContainer.add(new H2(item.getName()), removeButton);
        itemContainer.add(headingContainer, innerContainer);
        return itemContainer;
    }

    private void removeFromFavorites(TouristAttraction touristAttraction) {
        if(currentUser != null){
            service.removeFromFavorites(currentUser, touristAttraction);
        } else {
            Notification.show("Current user is null").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

    }

    private void initFields() {
        name = new TextField("Název");
        name.setReadOnly(true);
        name.setWidthFull();
        category = new TextField("Kategorie destinace");
        category.setReadOnly(true);
        category.setWidthFull();
        region = new TextField("Kraj");
        region.setReadOnly(true);
        region.setWidthFull();
        city = new TextField("Město");
        city.setReadOnly(true);
        city.setWidthFull();
        price = new TextField("Cena");
        price.setReadOnly(true);
        price.setWidthFull();
        rating = new NumberField("Hodnocení");
        rating.setReadOnly(true);
        rating.setWidthFull();
        rating.setPrefixComponent(new Icon(VaadinIcon.STAR));
    }

}
