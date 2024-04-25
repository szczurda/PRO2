package com.example.pro2.services;

import com.example.pro2.MyUserDetails;
import com.example.pro2.entities.TouristAttraction;
import com.example.pro2.entities.User;
import com.example.pro2.entities.UserFavorite;
import com.example.pro2.repositories.TouristAttractionRepository;
import com.example.pro2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.core.userdetails.UserDetailsMapFactoryBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final TouristAttractionRepository touristAttractionRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TouristAttractionRepository touristAttractionRepository) {
        this.touristAttractionRepository = touristAttractionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public List<TouristAttraction> findTouristAttractionsWithUserRating(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return  touristAttractionRepository.findAttractionsRatedByUserId(userId);
    }

    public boolean userAlreadyExists(String userName){
        return userRepository.findByUsername(userName) != null;
    }

    public Long findUserIdByUsername(String username){
        return userRepository.findUserIdByUsername(username);
    };

    public List<User> findAll() {
        return userRepository.findAll();
    }


}
