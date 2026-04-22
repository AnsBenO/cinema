package ntt.beca.films.user;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.security.Role;
import ntt.beca.films.shared.service.PagedResultDto;

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
            model.addAttribute("user", new UserEntity());
            model.addAttribute("roles", Role.values());
            return "views/users/add-user";
      }

      @PostMapping
      public String createUser(@ModelAttribute UserDto user, RedirectAttributes redirectAttributes) {
            try {
                  userService.save(user);
                  redirectAttributes.addFlashAttribute("message", "User added successfully!");
                  redirectAttributes.addFlashAttribute("status", true);
            } catch (Exception e) {
                  redirectAttributes.addFlashAttribute("message", "Failed to add user: " + e.getMessage());
                  redirectAttributes.addFlashAttribute("status", false);
            }
            return "redirect:/users";
      }

      @GetMapping("/delete/{id}")
      public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            // try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
            redirectAttributes.addFlashAttribute("status", true);
            // } catch (Exception e) {
            // redirectAttributes.addFlashAttribute("message", "Failed to delete user: " +
            // e.getMessage());
            // redirectAttributes.addFlashAttribute("status", false);
            // }
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
                  @ModelAttribute UserDto user,
                  RedirectAttributes redirectAttributes) {
            try {
                  UserDto existingUser = userService.getOne(id);
                  existingUser.setUsername(user.getUsername());
                  existingUser.setEmail(user.getEmail());
                  existingUser.setPassword(user.getPassword());
                  existingUser.setRole(user.getRole());
                  userService.save(existingUser);
                  redirectAttributes.addFlashAttribute("message", "User updated successfully!");
                  redirectAttributes.addFlashAttribute("status", true);
            } catch (Exception e) {
                  redirectAttributes.addFlashAttribute("message", "Failed to update user: " + e.getMessage());
                  redirectAttributes.addFlashAttribute("status", false);
            }
            return "redirect:/users";
      }
}
