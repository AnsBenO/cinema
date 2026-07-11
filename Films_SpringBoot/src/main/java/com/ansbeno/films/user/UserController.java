package com.ansbeno.films.user;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ansbeno.films.shared.security.Role;
import com.ansbeno.films.shared.service.PagedResultDto;

import jakarta.validation.Valid;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

      private final UserServiceImpl userService;

      @GetMapping
      public String getAllUsers(@RequestParam(defaultValue = "1") int page,
                  @RequestParam(required = false, defaultValue = "") String keyword,
                  @RequestParam(required = false, defaultValue = "") String role,
                  Model model, HttpServletRequest request) {
            PagedResultDto<UserDto> users = userService.getAll(page, keyword, role);
            model.addAttribute("users", users);
            model.addAttribute("keyword", keyword);
            model.addAttribute("role", role); // Add role to the model
            model.addAttribute("roles", Arrays.asList(Role.values())); // Add roles to the model for the dropdown
            return request.getHeader("HX-Request") != null ? "views/users/list-users :: usersTable"
                        : "views/users/list-users";
      }

      @GetMapping("/add")
      public String showAddUserForm(Model model) {
            model.addAttribute("user", new RegistrationDto());
            model.addAttribute("roles", Role.values());
            return "views/users/add-user";
      }

      @PostMapping
      public String createUser(@Valid @ModelAttribute("user") RegistrationDto registrationDto,
                  BindingResult bindingResult,
                  Model model,
                  RedirectAttributes redirectAttributes,
                  HttpServletRequest request,
                  HttpServletResponse response) {
            if (bindingResult.hasErrors()) {
                  model.addAttribute("roles", Role.values());
                  response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                  return "views/users/add-user";
            }
            try {
                  userService.save(registrationDto);
                  if (request.getHeader("HX-Request") != null) {
                        response.setHeader("HX-Redirect", "/users");
                        return "views/users/add-user";
                  }
                  redirectAttributes.addFlashAttribute("message", "User added successfully!");
                  redirectAttributes.addFlashAttribute("status", true);
            } catch (Exception e) {
                  if (request.getHeader("HX-Request") != null) {
                        model.addAttribute("errorMessage", "Failed to add user: " + e.getMessage());
                        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                        return "views/users/add-user";
                  }
                  redirectAttributes.addFlashAttribute("message", "Failed to add user: " + e.getMessage());
                  redirectAttributes.addFlashAttribute("status", false);
            }
            return "redirect:/users";
      }

      @GetMapping("/delete/{id}")
      public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
            redirectAttributes.addFlashAttribute("status", true);

            return "redirect:/users";
      }

      @GetMapping("/edit/{id}")
      public String showEditUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
            try {
                  UserDto user = userService.getOne(id);
                  model.addAttribute("user", user);
                  model.addAttribute("roles", Role.values());
                  return "views/users/edit-user";
            } catch (Exception e) {
                  redirectAttributes.addFlashAttribute("message", "User not found: " + e.getMessage());
                  redirectAttributes.addFlashAttribute("status", false);
                  return "redirect:/users";
            }
      }

      @PostMapping("/edit/{id}")
      public String updateUser(@PathVariable Long id,
                  @Valid @ModelAttribute("user") UserDto user,
                  BindingResult bindingResult,
                  Model model,
                  RedirectAttributes redirectAttributes,
                  HttpServletRequest request,
                  HttpServletResponse response) {
            if (bindingResult.hasErrors()) {
                  user.setId(id);
                  model.addAttribute("roles", Role.values());
                  response.setStatus(422);
                  return "views/users/edit-user";
            }
            try {
                  UserDto existingUser = userService.getOne(id);
                  existingUser.setUsername(user.getUsername());
                  existingUser.setEmail(user.getEmail());
                  existingUser.setRole(user.getRole());
                  userService.update(existingUser);
                  if (request.getHeader("HX-Request") != null) {
                        response.setHeader("HX-Redirect", "/users");
                        return "views/users/edit-user";
                  }
                  redirectAttributes.addFlashAttribute("message", "User updated successfully!");
                  redirectAttributes.addFlashAttribute("status", true);
            } catch (Exception e) {
                  redirectAttributes.addFlashAttribute("message", "Failed to update user: " + e.getMessage());
                  redirectAttributes.addFlashAttribute("status", false);
            }
            return "redirect:/users";
      }
}
