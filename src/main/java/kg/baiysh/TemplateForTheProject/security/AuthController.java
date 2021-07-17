package kg.baiysh.TemplateForTheProject.security;

import kg.baiysh.TemplateForTheProject.domain.UserEntity;
import kg.baiysh.TemplateForTheProject.security.jwt.JwtProvider;
import kg.baiysh.TemplateForTheProject.service.UserService;
import kg.baiysh.TemplateForTheProject.validation.ToDoValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        if (!userService.existsByLogin(registrationRequest.getLogin())) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPassword(registrationRequest.getPassword());
            userEntity.setLogin(registrationRequest.getLogin());
            userService.saveUser(userEntity);
            String token = jwtProvider.generateToken(userEntity.getLogin());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("!!! login " + registrationRequest.getLogin() + " already taken");
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

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        return new ToDoValidationError(exception.getMessage());
    }

}

