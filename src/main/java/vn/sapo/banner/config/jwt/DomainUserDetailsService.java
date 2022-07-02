package vn.sapo.banner.config.jwt;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.sapo.banner.common.Constants;
import vn.sapo.banner.common.Util;
import vn.sapo.banner.config.UserProperties;

@Slf4j
@RequiredArgsConstructor
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final UserProperties userProperties;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        var users = userProperties.getUsers().get(login);
        if (CollectionUtils.isNotEmpty(users)) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(users.get(2)));
            return new User(users.get(0), new BCryptPasswordEncoder().encode(users.get(1)), authorities);
        }
        throw new UserNotActivatedException("User " + login + " was not activated");
    }

}
