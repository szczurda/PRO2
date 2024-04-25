package com.example.pro2.views.adminViews;

import com.example.pro2.security.SecurityService;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ROLE_ADMIN")
public class AdminParentView extends AppLayout {

    HorizontalLayout mainLayout = new HorizontalLayout();
    Button logout = new Button("Odhlásit se");
    SecurityService securityService;

    public AdminParentView(SecurityService securityService) {
        this.securityService = securityService;
        logout.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logout.getStyle().setMarginRight("30px");
        logout.addClickListener(e -> {
            securityService.logout();
        });
        createHeader();
        createDrawer();
        mainLayout.setSizeFull();
        mainLayout.getStyle().set("background-color", "gray");

        setContent(mainLayout);

    }

    private void createHeader() {
        H1 logo = new H1("Správa systému - Admin");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);
        var header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(header, logout);
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Přehled uživatelů", AdminUserManagerView.class),
                new RouterLink("Správa atrakcí", AdminTouristAttractionManagerView.class)
        ));


    }
}
