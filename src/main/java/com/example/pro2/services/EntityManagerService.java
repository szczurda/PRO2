package com.example.pro2.services;

import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.User;
import com.example.pro2.entities.UserFavorite;
import com.example.pro2.entities.UserReview;
import com.example.pro2.repositories.TouristAttractionRepository;
import com.example.pro2.repositories.UserFavoriteRepository;
import com.example.pro2.repositories.UserReviewRepository;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EntityManagerService {

    TouristAttractionRepository touristAttractionRepository;

    UserReviewRepository userReviewRepository;

    UserFavoriteRepository userFavoriteRepository;



    @Autowired
    public EntityManagerService(TouristAttractionRepository touristAttractionRepository, UserReviewRepository userReviewRepository, UserFavoriteRepository userFavoriteRepository) {
        this.touristAttractionRepository = touristAttractionRepository;
        this.userReviewRepository = userReviewRepository;
        this.userFavoriteRepository = userFavoriteRepository;
    }

    public Optional<TouristAttraction> findAttractionByName(String name) {
        return Optional.ofNullable(touristAttractionRepository.findByName(name));
    }

    public List<TouristAttraction> findAllTouristAttractions(){
        return touristAttractionRepository.findAll();
    }

    public List<String> findAllRegions(){
        return touristAttractionRepository.findAllDistinctRegions();
    }

    public List<String> findAllCategories(){
        return touristAttractionRepository.findAllDistinctCategories();
    }

    public void saveUserReview(User user, String reviewText, int rating, TouristAttraction touristAttraction){
        UserReview review = new UserReview();
        review.setReviewText(reviewText);
        review.setUser(user);
        review.setRating(rating);
        review.setTouristAttraction(touristAttraction);
        review.setReviewDate(Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime());
        userReviewRepository.save(review);
    }

    public UserReview findUserReviewByUserIdAndAttractionId(Long userId, Long attractionId){
        return userReviewRepository.findUserReviewByUserIdAndAttractionId(userId, attractionId);
    }

    public void updateReview(UserReview review){
        Optional<UserReview> optionalUserReview = userReviewRepository.findById(review.getUserReviewId());
        if(optionalUserReview.isPresent()){
            UserReview existingReview = optionalUserReview.get();
            existingReview.setReviewText(review.getReviewText());
            existingReview.setRating(review.getRating());
            userReviewRepository.save(existingReview);
        }

    }

    public void saveToFavorites(TouristAttraction touristAttraction, User user) {
        if(!userFavoriteRepository.existsByUserAndTouristAttraction(user, touristAttraction)){
            userFavoriteRepository.save(new UserFavorite(user, touristAttraction));
            Notification.show("Přidáno do oblíbených: " + touristAttraction.getName()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            Notification.show("Již v oblíbených").addThemeVariants(NotificationVariant.LUMO_WARNING);
        }
    }

    public List<UserFavorite> findAllUserFavoritesByUsername(String username){
        List<UserFavorite> favoritesList = userFavoriteRepository.findAllByUserUsername(username);
        if(!favoritesList.isEmpty()){
            return favoritesList;
        } else return Collections.emptyList();
    }

    public void removeFromFavorites(String username, TouristAttraction touristAttraction){
        userFavoriteRepository.removeFromFavorites(username, touristAttraction);
    }


    @Transactional
    public void deleteAttraction(TouristAttraction touristAttraction) {
            if(touristAttractionRepository.existsById(touristAttraction.getTouristAttractionId())){
                userFavoriteRepository.deleteAllByTouristAttraction(touristAttraction);
                userReviewRepository.deleteAllByTouristAttraction(touristAttraction);
                touristAttractionRepository.delete(touristAttraction);
            }
    }

    public void editTouristAttraction(TouristAttraction touristAttraction) {
        touristAttractionRepository.save(touristAttraction);
    }
}
