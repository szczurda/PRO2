package com.example.pro2.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class SearchInputBar extends HorizontalLayout {

    private TextField destinationName, city;


    private Select<String> rating, price, category, region;

    private Button searchButton;

    public SearchInputBar() {
        configFields();
        configButton();
        setSpacing(true);
        setAlignItems(Alignment.BASELINE);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidthFull();
        getStyle().set("padding", "20px");
        add(destinationName, category, region, city, price, rating, searchButton);
    }

    private void configFields(){
        destinationName = new TextField("Název turistické atrakce");
        city = new TextField("Město");
        category = new Select<>();
        rating = new Select<>();
        region = new Select<>();
        price = new Select<>();
        rating.setLabel("Hodnocení");
        rating.setItems("1", "2", "3", "4");
        rating.setItemLabelGenerator(item -> item != null ? item + " ⭐ a více" : "Všechna hodnocení");
        rating.setEmptySelectionCaption("Všechna hodnocení");
        rating.setEmptySelectionAllowed(true);
        region.setEmptySelectionCaption("Všechny kraje");
        region.setLabel("Kraj");
        region.setEmptySelectionAllowed(true);
        price.setLabel("Cena");
        price.setItems("$", "$$", "$$$", "$$$$");
        price.setEmptySelectionAllowed(true);
        price.setEmptySelectionCaption("Všechny cenové kategorie");
        category.setEmptySelectionAllowed(true);
        category.setEmptySelectionCaption("Všechny kategorie");
        category.setLabel("Kategorie destinace");
    }

    private void configButton(){
        searchButton = new Button("Vyhledat");
        searchButton.setIcon(VaadinIcon.SEARCH.create());
        searchButton.getStyle()
                .set("background-color", "blue")
                .set("color", "white");
        searchButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        searchButton.addClickShortcut(Key.ENTER);
    }

    public TextField getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(TextField destinationName) {
        this.destinationName = destinationName;
    }

    public TextField getCity() {
        return city;
    }

    public void setCity(TextField city) {
        this.city = city;
    }

    public void setCategory(Select<String> category) {
        this.category = category;
    }

    public Select<String> getRegion() {
        return region;
    }

    public void setRegion(Select<String> region) {
        this.region = region;
    }

    public Select<String> getRating() {
        return rating;
    }

    public void setRating(Select<String> rating) {
        this.rating = rating;
    }

    public Select<String> getPrice() {
        return price;
    }

    public void setPrice(Select<String> price) {
        this.price = price;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public Select<String> getCategory() {
        return category;
    }
}
