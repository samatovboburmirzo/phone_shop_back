package phone.shop.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phone.shop.dto.*;
import phone.shop.dto.profile.ProfileCreateDTO;
import phone.shop.dto.profile.ProfileDetailDTO;
import phone.shop.dto.profile.UserDetails;
import phone.shop.entity.ProfileEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.exp.ProfileNotFoundException;
import phone.shop.exp.ServerBadRequestException;
import phone.shop.repository.ProfileRepository;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;
import phone.shop.util.RandomStringUtil;
import phone.shop.util.TokenProcess;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;

    public ProfileDetailDTO auth(AuthorizationDTO dto) {
        String email = dto.getEmail(); // ali@mail.ru
        String pswd = DigestUtils.md5Hex(dto.getPassword()); // abcd123
        Optional<ProfileEntity> optional = this.profileRepository.findByEmailAndPassword(email, pswd);

        if (!optional.isPresent()) {
            throw new ProfileNotFoundException("Login yoki porol xato :). Tupoy. ");
        }

        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new ProfileNotFoundException("Not Active");
        }

        UserDetails userDetails = new UserDetails();
        userDetails.setId(profileEntity.getId());
        userDetails.setName(profileEntity.getName());
        userDetails.setRole(profileEntity.getRole());

        String jwt = TokenProcess.generateJwt(userDetails);

        ProfileDetailDTO responseDTO = new ProfileDetailDTO();
        responseDTO.setToken(jwt);
        responseDTO.setName(profileEntity.getName());
        responseDTO.setSurname(profileEntity.getSurname());
        responseDTO.setContact(profileEntity.getContact());

        return responseDTO;
    }

    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.getByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new ServerBadRequestException("Email already exists.");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setContact(dto.getContact());
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.INACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPassword(DigestUtils.md5Hex(dto.getPassword()));

        this.profileRepository.save(entity);
        String jwt = TokenProcess.generateJwt(entity.getId());
        String link = "http://localhost:8081/auth/verification/" + jwt;
        try {
            mailSenderService.sendEmail(dto.getEmail(),
                    "TestKun uz verification",
                    "Salom jigar shu linkni bos." + link);
        } catch (Exception e) {
            this.profileRepository.delete(entity);
        }

        return "Tabriklimiz.Jigar Emailga borda verificatsiyadan o'tivor. Understud";
    }

    public String verification(String jwt) {
        Integer profileId = TokenProcess.encodeJwt(jwt);
        Optional<ProfileEntity> optional = this.profileRepository.findById(profileId);
        if (!optional.isPresent()) {
            throw new ItemNotFoundException("Wrong key");
        }
        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new ServerBadRequestException("You are in wrong status");
        }

        profileEntity.setStatus(ProfileStatus.ACTIVE);
        this.profileRepository.save(profileEntity);
        return "Successfully verified";
    }
}
