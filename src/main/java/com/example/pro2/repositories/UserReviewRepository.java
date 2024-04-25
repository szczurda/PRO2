package com.example.pro2.repositories;

import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    @Query("SELECT ur FROM UserReview ur WHERE ur.user.userId = :userId AND ur.touristAttraction.touristAttractionId = :attractionId")
    UserReview findUserReviewByUserIdAndAttractionId(Long userId, Long attractionId);
    void deleteAllByTouristAttraction(TouristAttraction touristAttraction);
}
