package com.example.pro2.repositories;

import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.User;
import com.example.pro2.entities.UserFavorite;
import com.example.pro2.entities.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u.userId FROM User u WHERE u.username=:username")
    Long findUserIdByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(uf) > 0 THEN true ELSE false END FROM UserFavorite uf WHERE uf.user = :user AND uf.touristAttraction = :touristAttraction")
    boolean userFavoriteExists(TouristAttraction touristAttraction, User user);

}