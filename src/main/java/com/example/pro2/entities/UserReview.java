package com.example.pro2.entities;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_reviews")
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userReviewId;

    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "touristAttractionId")
    private TouristAttraction touristAttraction;

    private int rating;

    private String reviewText;

    public UserReview() {
    }

    public Long getUserReviewId() {
        return userReviewId;
    }

    public void setUserReviewId(Long userReviewId) {
        this.userReviewId = userReviewId;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate= reviewDate;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
