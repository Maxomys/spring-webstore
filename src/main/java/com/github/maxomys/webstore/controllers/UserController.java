package com.github.maxomys.webstore.controllers;

import com.github.maxomys.webstore.auth.UserService;
import com.github.maxomys.webstore.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users/new")
    public String getUserForm(Model model) {
        model.addAttribute("user", new User());

        return "/admin/userForm";
    }

    @PostMapping("/admin/users")
    public String createUser(@ModelAttribute("user") @Valid User user) {
        userService.createNewUser(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model, Authentication authentication) {
        User activeUser = (User) authentication.getPrincipal();

        model.addAttribute("user", activeUser);
        model.addAttribute("users", userService.getAllUsers());

        return "/admin/users";
    }

    @GetMapping("/admin/users/{userId}")
    public String getUserById(Model model, @PathVariable("userId") Long userId) {
        model.addAttribute("user", userService.getUserById(userId));

        return "/admin/userDetails";
    }

    @PostMapping("/admin/users/{userId}")
    public String deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);

        return "redirect:/admin/users";
    }

}
