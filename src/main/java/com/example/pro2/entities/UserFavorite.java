package com.example.pro2.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_favorites")
public class UserFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userFavoriteId;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @JoinColumn(name = "touristAttractionId")
    @ManyToOne
    private TouristAttraction touristAttraction;

    public UserFavorite(User user, TouristAttraction touristAttraction) {
        this.user = user;
        this.touristAttraction = touristAttraction;
    }

    public UserFavorite() {
    }

    public Long getUserFavoriteId() {
        return userFavoriteId;
    }

    public void setUserFavoriteId(Long userFavoriteId) {
        this.userFavoriteId = userFavoriteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TouristAttraction getTouristAttraction() {
        return touristAttraction;
    }

    public void setTouristAttraction(TouristAttraction touristAttraction) {
        this.touristAttraction = touristAttraction;
    }
}
