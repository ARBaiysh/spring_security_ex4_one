package kg.baiysh.TemplateForTheProject.controller;

import kg.baiysh.TemplateForTheProject.domain.UserEntity;
import kg.baiysh.TemplateForTheProject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestSecurityController {
    private final UserService userService;

    public TestSecurityController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/admin/get")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdmin() {
        return "Hi admin";
    }


    @GetMapping("/user/get")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getUser() {
        return "Hi user";
    }


    @GetMapping("/admin/getUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers() {
        List<UserEntity> userEntityList = userService.findByAll();
        return ResponseEntity.ok(userEntityList);
    }
}