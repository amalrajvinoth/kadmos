package io.kadmas.auth.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import io.kadmas.auth.entity.UserEntity;
import io.kadmas.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUserName(username);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        UserEntity user = optionalUser.get();
        return new User(user.getUserName(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRoles())));
    }
}
