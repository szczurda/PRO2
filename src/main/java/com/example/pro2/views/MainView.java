package com.example.pro2.views;

import com.example.pro2.TouristAttractionFilter;
import com.example.pro2.components.MainNavbar;
import com.example.pro2.components.SearchInputBar;
import com.example.pro2.components.TouristAttractionDetail;
import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.security.SecurityService;
import com.example.pro2.services.EntityManagerService;
import com.example.pro2.services.UserServiceImpl;
import com.example.pro2.views.adminViews.AdminTouristAttractionManagerView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class MainView extends VerticalLayout implements BeforeEnterObserver{
    private SecurityService securityService;
    private MainNavbar navbar;
    private SearchInputBar searchInputBar = new SearchInputBar();
    private Grid<TouristAttraction> touristAttractionGrid = new Grid<>(TouristAttraction.class, false);
    private EntityManagerService entityManagerService;
    private final UserServiceImpl userService;
    private ListDataProvider<TouristAttraction> dataProvider;
    private TouristAttractionFilter touristAttractionFilter;

    private TouristAttractionDetail touristAttractionDetail;


    @Autowired
    public MainView(EntityManagerService entityManagerService, UserServiceImpl userService, SecurityService securityService){
        this.entityManagerService = entityManagerService;
        this.userService = userService;
        this.securityService = securityService;
        navbar = new MainNavbar(securityService);
        setDataProviders();
        GridListDataView<TouristAttraction> dataView = touristAttractionGrid.setItems(dataProvider);
        touristAttractionFilter = new TouristAttractionFilter(dataView);
        setHeightFull();
        configGridLayout();
        initListeners();
        detailsConfig();
        add(navbar, searchInputBar, touristAttractionGrid);
    }

    private void configGridLayout(){
        touristAttractionGrid.addColumn(TouristAttraction::getName).setHeader("Název");
        touristAttractionGrid.addColumn(TouristAttraction::getCategory).setHeader("Kategorie destinace");
        touristAttractionGrid.addColumn(TouristAttraction::getRegion).setHeader("Kraj");
        touristAttractionGrid.addColumn(TouristAttraction::getCity).setHeader("Město");
        touristAttractionGrid.addColumn(TouristAttraction::getPrice).setHeader("Cena");
        touristAttractionGrid.addColumn(TouristAttraction::getRating).setHeader("Hodnocení");
        touristAttractionGrid.setWidthFull();
        setWidthFull();
        setAlignItems(Alignment.CENTER);
    }

    private void setDataProviders(){
        dataProvider = DataProvider.ofCollection(entityManagerService.findAllTouristAttractions());
        ListDataProvider<String> regionDataProvider = new ListDataProvider<>(entityManagerService.findAllRegions());
        ListDataProvider<String> categoryDataProvider = new ListDataProvider<>(entityManagerService.findAllCategories());
        searchInputBar.getCategory().setItems(categoryDataProvider);
        searchInputBar.getRegion().setItems(regionDataProvider);
    }

    private void detailsConfig(){
        touristAttractionGrid.addColumn(new ComponentRenderer<>(attraction -> {
            Button openDetailButton = new Button("Otevřít detail");
            openDetailButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            openDetailButton.addClickListener(e -> {
                touristAttractionDetail = new TouristAttractionDetail(userService, entityManagerService, attraction, securityService);
                touristAttractionDetail.open();
            });
            return openDetailButton;
        }));
    }

    private void initListeners(){
        searchInputBar.addClickShortcut(Key.ENTER);
        searchInputBar.getDestinationName().addValueChangeListener(e -> {
            touristAttractionFilter.setName(e.getValue());
        });
        searchInputBar.getCity().addValueChangeListener(e -> {
            touristAttractionFilter.setCity(e.getValue());
        });
        searchInputBar.getCategory().addValueChangeListener(e-> {
            touristAttractionFilter.setCategory(e.getValue());
        });
        searchInputBar.getPrice().addValueChangeListener(e -> {
            touristAttractionFilter.setPrice(e.getValue());
        });

        searchInputBar.getRating().addValueChangeListener(e -> {
            touristAttractionFilter.setRating(e.getValue());
        });
        searchInputBar.getRegion().addValueChangeListener(e-> {
            touristAttractionFilter.setRegion(e.getValue());
        });
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals("admin")){
            beforeEnterEvent.forwardTo("/adminView/userManager");
        } else {
            beforeEnterEvent.forwardTo("");
        }
    }
}
