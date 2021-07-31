package phone.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phone.shop.dto.profile.*;
import phone.shop.service.ProfileService;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;
import phone.shop.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // User
    @GetMapping("/detail")
    public ResponseEntity<?> getProfileDetail(HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request);
        ProfileDetailDTO dto = profileService.getDetail(userDetails.getId());
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/detail")
    public ResponseEntity<?> updateDetail(@Valid @RequestBody ProfileUpdateDTO dto, HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request);
        profileService.profileUpdateDetail(userDetails.getId(), dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/detail")
    public ResponseEntity<?> updateEmail(@RequestParam("email") String email, HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request);
        profileService.profileUpdateEmail(userDetails.getId(), email);
        return ResponseEntity.ok().build();
    }

    // Admin
    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody ProfileCreateDTO dto, HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        // 1.  repository -> ROLE
        //2. userDetails.getRole().equals(ProfileRole.ADMIN)
        profileService.create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody ProfileCreateDTO dto, HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        profileService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id, HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        profileService.getById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/adm/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Integer id, @RequestParam("status") ProfileStatus profileStatus, HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        profileService.changeStatus(id, profileStatus);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/adm/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileFilterDTO dto, HttpServletRequest request) {
        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        Page<ProfileDetailDTO> result = profileService.filter(dto);
        return ResponseEntity.ok().body(result);
    }
}
