package phone.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phone.shop.dto.AuthorizationDTO;
import phone.shop.dto.profile.ProfileCreateDTO;
import phone.shop.dto.RegistrationDTO;
import phone.shop.dto.profile.ProfileDetailDTO;
import phone.shop.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/authorization")
    public ResponseEntity<?> auth(@Valid @RequestBody AuthorizationDTO dto) {
        System.out.println("Authorization: {} " + dto);
        ProfileDetailDTO result = this.authService.auth(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody RegistrationDTO dto) {
        System.out.println("Registration: {} " + dto);
        String result = this.authService.registration(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/verification/{jwt}")
    public ResponseEntity<?> registration(@PathVariable("jwt") String jwt) {
        System.out.println("verification: jwt " + jwt);
        String result = this.authService.verification(jwt);
        return ResponseEntity.ok(result);
    }

}
