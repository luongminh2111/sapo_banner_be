package vn.sapo.banner.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.sapo.banner.common.Util;
import vn.sapo.banner.config.UserProperties;
import vn.sapo.banner.config.jwt.JWTFilter;
import vn.sapo.banner.config.jwt.TokenProvider;
import vn.sapo.banner.dto.request.LoginRequest;
import vn.sapo.banner.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")

public class LoginController {

    private final UserProperties userProperties;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;


    @PostMapping
    public ResponseEntity authorize(@Valid @RequestBody LoginRequest request) {
        var users = userService.getUserInfoByUsername(request.getUsername());
        if(CollectionUtils.isNotEmpty(users)) {
            List<String> userInfos =  Arrays.asList(users.get(0).split(","));
            boolean check = Util.checkAuthenticate(userInfos, request);
            if (check) {
                var authenticationToken = new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                );
                var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                var jwt = tokenProvider.createToken(authentication);
                return ResponseEntity.ok(new JWTToken(JWTFilter.BEARER + jwt));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Getter
    @Setter
    static class JWTToken {
        private String accessToken;

        public JWTToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
