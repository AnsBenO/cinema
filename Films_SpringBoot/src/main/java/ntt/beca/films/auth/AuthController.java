package ntt.beca.films.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntt.beca.films.user.RegistrationDto;
import ntt.beca.films.user.UserService;

@Slf4j
@RequiredArgsConstructor
@Controller
class AuthController {

      private final UserService userService;

      @GetMapping("/register")
      String getRegisterForm(Model model) {
            if (isUserAuthenticated()) {
                  return "redirect:/";
            }
            RegistrationDto user = new RegistrationDto();
            model.addAttribute("user", user);
            return "views/auth/register";
      }

      @PostMapping("/register")
      String registerUser(
                  @Valid @ModelAttribute("user") RegistrationDto user,
                  BindingResult result,
                  Model model) {
            if (isUserAuthenticated()) {
                  return "redirect:/";
            }

            if (result.hasErrors()) {
                  return "views/auth/register";
            }

            if (userService.existsByEmail(user.getEmail())) {
                  model.addAttribute("error", "Email is already registered: " + user.getEmail());
                  return "views/auth/register";
            }

            if (userService.existsByUsername(user.getUsername())) {
                  model.addAttribute("error", "Username is already taken: " + user.getUsername());
                  return "views/auth/register";
            }

            userService.save(user);
            return "redirect:/login";
      }

      @GetMapping("/login")
      String showLoginForm(Model model,
                  @RequestParam(required = false) String error,
                  @RequestParam(required = false) String logout,
                  RedirectAttributes redirectAttributes) {
            if (error != null) {
                  model.addAttribute("error", "Invalid credentials");
            }
            if (logout != null) {
                  model.addAttribute("logout", "You have been logged out");
            }
            if (isUserAuthenticated()) {
                  redirectAttributes.addFlashAttribute("error", "You Are Already Authenticated");
                  return "redirect:/";
            }
            return "views/auth/login";
      }

      @GetMapping("/post-logout")
      String postLogout(RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("success", "You have been logged out successfully!");
            return "redirect:/";
      }

      private boolean isUserAuthenticated() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (authentication != null
                        && !(authentication instanceof AnonymousAuthenticationToken));
      }

}
