package com.github.maxomys.webstore.auth;

import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.exceptions.UnableToRemoveUserException;
import com.github.maxomys.webstore.exceptions.UsernameAlreadyExistException;
import com.github.maxomys.webstore.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found!");
        }

        return userOptional.get();
    }

    public User createNewUser(User user) {
        //check if username is in use
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistException(String.format("Username %s already exists", user.getUsername()));
        }

        //encrypt password todo check if this is ok
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //enhance user with admin authority, each user is an admin
        user.setUserRole(ApplicationUserRole.ROLE_ADMIN);

        return userRepository.save(user);
    }

    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    public void deleteUserById(Long userId) {
        if (userRepository.findAll().size() <= 1) {
            throw new UnableToRemoveUserException("Username already exists!");
        }

        userRepository.deleteById(userId);
    }

}
