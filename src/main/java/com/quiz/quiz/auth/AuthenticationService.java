package com.quiz.quiz.auth;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.quiz.quiz.Role.Role;
import com.quiz.quiz.Role.RoleRepository;
import com.quiz.quiz.Role.URole;
import com.quiz.quiz.User.JwtService;
import com.quiz.quiz.User.Token;
import com.quiz.quiz.User.TokenRepository;
import com.quiz.quiz.User.User;
import com.quiz.quiz.User.Userrepository;
import com.quiz.quiz.handler.AccountNotVerfiedException;
import com.quiz.quiz.handler.DuplicateEmailException;
import com.quiz.quiz.handler.PasswordNotMatchException;
import com.quiz.quiz.handler.ResourceNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Slf4j
public class AuthenticationService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Userrepository userrepository;

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired

    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    private final OkHttpClient client = new OkHttpClient();

    public String register(RegistrationRequest request) throws MessagingException, IOException {

        if (userrepository.existsByContactNumber(request.getContactNumber())) {
            throw new DuplicateEmailException(request.getContactNumber() + " already Exist");
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .contactNumber(request.getContactNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .build();

        Role role = roleRepository.findByName(URole.ROLE_PATIENT).orElseGet(() -> {

            Role patientRole = new Role();
            patientRole.setName(URole.ROLE_PATIENT);
            return roleRepository.save(patientRole);

        });

        user.setRole(role);

        role.addUser(user);
        userrepository.save(user);
        sendValidationEmail(user);

        return "Registration Successfull";

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .verified(false)
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    private void sendValidationEmail(User user) throws MessagingException, IOException {
        String otp = generateAndSaveActivationToken(user);
        sendSms(otp, user.getContactNumber());
    }

    public void sendSms(String otp, String email) throws IOException {
        String authKey = "8352f3d95c43bde89bf39a38c37685a";
        String senderId = "MDANTK";
        String routeId = "1";
        String message = String.format(
                "Please use the code %s to log in on the Nodex App. Please do not share this code with anyone for security reasons. For more detail visit https://medantrik.com/ Team Medantrik",
                otp);
        String smsContentType = "English";
        String templateId = "OTP";

        String url = String.format(
                "https://msg.msgclub.net/rest/services/sendSMS/sendGroupSms?AUTH_KEY=%s&senderId=%s&routeId=%s&message=%s&mobileNos=%s&smsContentType=%s&templateid=%s",
                authKey, senderId, routeId, message, email, smsContentType, templateId);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            } else {
                throw new RuntimeException("Failed to send SMS: " + response.code());
            }
        }
    }

    public AuthenticationResponse authenticate(@Valid AuthenticaionRequest request)
            throws MessagingException, IOException {

        User user1 = userrepository.findByContactNumber(request.getContactNumber())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid PhoneNumber or password"));

        if (!user1.isEnabled()) {
            sendValidationEmail(user1);
            throw new AccountNotVerfiedException(
                    "Account not verified. Please check your email for the verification code.");
        }
        List<Token> allSavedToken = tokenRepository.findAllByUserId(user1.getId());
        tokenRepository.deleteAll(allSavedToken);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getContactNumber(),
                        request.getPassword()));

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // @Transactional

    public String resendOtp(ResendOtpRequest resendOtpRequest) throws MessagingException, IOException {
        User user = userrepository.findByContactNumber(resendOtpRequest.getContactNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        resendOtpRequest.getContactNumber() + " is Not Registered"));

        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already verified, Please login");
        }
        String otp = generateAndSaveActivationToken(user);
        sendSms(otp, resendOtpRequest.getContactNumber());

        return "Otp send";

    }

    public void changeAuthenticationPassword(ChangePasswordRequest changePasssword, Authentication connnectedUser)
            throws BadRequestException {

        User user = (User) connnectedUser.getPrincipal();

        if (!passwordEncoder.matches(changePasssword.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Password Donot Match");
        }

        if (!(changePasssword.getNewPassword().equals(changePasssword.getConfirmationPassword()))) {
            throw new PasswordNotMatchException("Password Not Match");
        }
        user.setPassword(passwordEncoder.encode(changePasssword.getNewPassword()));
        userrepository.save(user);

    }

    public String forgetPassword(ForgetPasswordRequest forgetPassword) throws MessagingException, IOException {
        User user = userrepository.findByContactNumber(forgetPassword.getContactNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Not Registered"));
        String otp = generateAndSaveActivationToken(user);
        sendSms(otp, forgetPassword.getContactNumber());
        // verifyOtpforPasswordReset(otp, forgetPassword.getContactNumber());

        return "Otp Send";
    }

    public String resetForgottendPassword(ResetPasswordRequest resetPasswordRequest) {

        User user = userrepository.findByContactNumber(resetPasswordRequest.getContactNumber())
                .orElseThrow(() -> new ResourceNotFoundException("user not Found"));

        List<Token> allsavedTokens = tokenRepository.findAllByUserId(user.getId());
        for (Token token : allsavedTokens) {
            if (!token.isVerified()) {
                throw new AccountNotVerfiedException("Not allowed to ResetPassword");
            }
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setEnabled(true);
        userrepository.save(user);

        return "Password successfully reset";
    }

    @Scheduled(fixedRate = 60000)
    public void cleanUpExpiredTokens() {
        tokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void cleanUpRegisteredUser() {
        // log.info("schedule is running : ");
        List<User> users = userrepository.findAllByEnabled(false);
        if (!users.isEmpty()) {
            userrepository.deleteAll(users);
        }
    }

    public String otpVerification(OtpVerficationRequest otpVerficationRequest) {

        Token savedToken = tokenRepository.findByToken(otpVerficationRequest.getOtp())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Otp"));

        User user = userrepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        if (!user.getContactNumber().equals(otpVerficationRequest.getContactNumber())) {
            throw new IllegalArgumentException("Invalid Otp");
        }

        List<Token> allSavedToken = tokenRepository.findAllByUserId(user.getId());
        for (Token token : allSavedToken) {
            token.setVerified(true);
        }
        tokenRepository.saveAll(allSavedToken);

        if (!user.isEnabled()) {
            user.setEnabled(true);
            userrepository.save(user);
        }

        return "Otp Verified";
    }
}
