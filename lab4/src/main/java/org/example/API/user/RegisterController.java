package org.example.API.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        UserClass created = registerService.register(request.username, request.password);
        RegisterResponse body = new RegisterResponse();
        body.id = created.getId();
        body.username = created.getUsername();
        body.role = created.getRole();
        body.status = created.getStatus();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static class RegisterRequest {
        public String username;
        public String password;
    }

    public static class RegisterResponse {
        public Long id;
        public String username;
        public String role;
        public String status;
    }
}