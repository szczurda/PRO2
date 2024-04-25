package com.example.pro2;

import com.example.pro2.components.SearchInputBar;
import com.example.pro2.entities.TouristAttraction;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.List;
import java.util.stream.Collectors;

public class TouristAttractionFilter {

        GridListDataView<TouristAttraction> dataView;
        private String name;
        private String category;
        private String region;
        private String city;
        private String price;
        private String rating;


    public TouristAttractionFilter(GridListDataView<TouristAttraction> dataView) {
            this.dataView = dataView;
            dataView.addFilter(this::test);
        }

        public void setName(String name) {
            this.name = name;
            dataView.refreshAll();
        }

        public void setCategory(String category) {
            this.category = category;
            dataView.refreshAll();
        }

        public void setRegion(String region) {
            this.region = region;
            dataView.refreshAll();
        }

        public void setCity(String city) {
            this.city = city;
            dataView.refreshAll();
        }

        public void setPrice(String price) {
            this.price = price;
            dataView.refreshAll();
        }

        public void setRating(String rating) {
            this.rating = rating;
            dataView.refreshAll();
        }

        public boolean test(TouristAttraction touristAttraction) {

            boolean matchesName = matches(touristAttraction.getName(), name);
            boolean matchesCategory = matches(touristAttraction.getCategory(), category);
            boolean matchesRegion = matches(touristAttraction.getRegion(), region);
            boolean matchesCity = matches(touristAttraction.getCity(), city);
            boolean matchesPrice = matchesPrice(touristAttraction.getPrice(), price);
            boolean matchesRating = matches(touristAttraction.getRating(), rating);


            return matchesName && matchesCategory && matchesRegion && matchesCity && matchesPrice && matchesRating;
        }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private boolean matchesPrice(String value, String searchTerm){
        return searchTerm == null || searchTerm.isEmpty()
                || value.equals(searchTerm);
    }

    private boolean matches(double value, String searchTerm){
        return searchTerm == null || searchTerm.isEmpty() || value > Double.valueOf(searchTerm);
    }
}
