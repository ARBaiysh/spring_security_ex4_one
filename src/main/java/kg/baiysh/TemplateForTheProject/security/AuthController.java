package kg.baiysh.TemplateForTheProject.security;

import kg.baiysh.TemplateForTheProject.security.jwt.JwtProvider;
import kg.baiysh.TemplateForTheProject.domain.UserEntity;
import kg.baiysh.TemplateForTheProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;

    }

    @PostMapping("/register")
    public AuthResponse registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        if (!userService.existsByLogin(registrationRequest.getLogin())) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPassword(registrationRequest.getPassword());
            userEntity.setLogin(registrationRequest.getLogin());
            userService.saveUser(userEntity);
            String token = jwtProvider.generateToken(userEntity.getLogin());
            return new AuthResponse(token);
        }
        return new AuthResponse("!!! login " + registrationRequest.getLogin() + " already taken");
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody AuthRequest request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (userEntity != null) {
            String token = jwtProvider.generateToken(userEntity.getLogin());
            return ResponseEntity.ok(new AuthResponse("Bearer " + token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

