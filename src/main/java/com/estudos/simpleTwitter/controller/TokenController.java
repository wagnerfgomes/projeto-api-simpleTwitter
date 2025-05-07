package com.estudos.simpleTwitter.controller;

import com.estudos.simpleTwitter.controller.dto.LoginRequest;
import com.estudos.simpleTwitter.controller.dto.LoginResponse;
import com.estudos.simpleTwitter.entity.Role;
import com.estudos.simpleTwitter.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;


@RestController
public class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public TokenController(JwtEncoder jwtEncoder, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginResponseResponseEntity(@RequestBody LoginRequest loginRequest) {
        logger.info("Iniciando o processo de login para o usuário:{}", loginRequest.username());

        var user = userRepository.findByUserName(loginRequest.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            logger.warn("Credenciais inválidas para o usuário: {}", loginRequest.username());
            throw new BadCredentialsException("user or password is invalid");
        }

        logger.info("Usuário autenticado com sucesso:{}", loginRequest.username());

        var now = Instant.now();
        var expireIn = 3600L;

        var scope = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(""));

        var claims = JwtClaimsSet.builder()
                .issuer("simpleTwitter")
                .subject(user.get().getUserId().toString())
                .expiresAt(now.plusSeconds(expireIn))
                .issuedAt(now)
                .claim("scope", scope)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        logger.info("Token JWT gerado com sucesso para o usuário: {}", loginRequest.username());

        return ResponseEntity.ok(new LoginResponse(jwtValue, expireIn));
    }

}
