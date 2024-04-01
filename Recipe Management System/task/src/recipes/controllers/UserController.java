package recipes.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import recipes.models.AppUser;
import recipes.repositories.AppUserRepository;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Controller
public class UserController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/register")
    public ResponseEntity<Void> registerChef(@RequestBody @Valid RegistrationRequest request) {
        if (appUserRepository.existsById(request.email)) {
            return ResponseEntity.badRequest().build();
        }

        AppUser user = new AppUser();
        user.setUsername(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setAuthority("CHEF");

        appUserRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @Data
    public static class RegistrationRequest {
        @Pattern(regexp = ".+@.+\\..+")
        @NotBlank
        private String email;
        @NotBlank
        @Size(min = 8)
        private String password;
    }
}
