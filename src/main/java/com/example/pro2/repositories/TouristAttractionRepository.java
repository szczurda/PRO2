package com.example.pro2.repositories;

import com.example.pro2.entities.TouristAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristAttractionRepository extends JpaRepository<TouristAttraction, Long> {

    TouristAttraction findByName(String name);

    @Query("SELECT DISTINCT t.region FROM TouristAttraction t")
    List<String> findAllDistinctRegions();

    @Query("SELECT DISTINCT t.category FROM TouristAttraction t")
    List<String> findAllDistinctCategories();

    @Query("SELECT DISTINCT ta FROM TouristAttraction ta JOIN UserReview ur ON ta.touristAttractionId = ur.touristAttraction.touristAttractionId WHERE ur.user.userId = :userId")
    List<TouristAttraction> findAttractionsRatedByUserId(Long userId);



}
