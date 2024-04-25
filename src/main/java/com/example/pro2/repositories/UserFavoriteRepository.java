package com.example.pro2.repositories;

import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.User;
import com.example.pro2.entities.UserFavorite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {

    boolean existsByUserAndTouristAttraction(User user, TouristAttraction touristAttraction);

    @Query("SELECT uf FROM UserFavorite uf WHERE uf.user.username = :username")
    List<UserFavorite> findAllByUserUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserFavorite uf WHERE uf.user.username = :username AND uf.touristAttraction = :touristAttraction")
    void removeFromFavorites(String username, TouristAttraction touristAttraction);

    void deleteAllByTouristAttraction(TouristAttraction touristAttraction);
}
