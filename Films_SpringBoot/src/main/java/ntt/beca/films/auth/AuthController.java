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
import ntt.beca.films.user.UserDto;
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
            UserDto user = new UserDto();
            model.addAttribute("user", user);
            return "views/auth/register";
      }

      @PostMapping("/register")
      String registerUser(
                  @Valid @ModelAttribute("user") UserDto user,
                  BindingResult result) {
            if (isUserAuthenticated()) {
                  return "redirect:/";
            }

            if (result.hasErrors()) {
                  return "views/auth/register";
            }
            boolean existsByEmail = userService.existsByEmail(user.getEmail());

            if (existsByEmail) {
                  return "redirect:/register?fail";
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
            log.info("Authentication {}", authentication.getName());
            boolean isAuthenticated = (authentication != null
                        && !(authentication instanceof AnonymousAuthenticationToken));
            log.info("Is user authenticated {}, name :{}", isAuthenticated, authentication.getName());
            return isAuthenticated;
      }

}
