package org.example.multiplelogin.security;

import org.example.multiplelogin.domain.owner.entity.Owner;
import org.example.multiplelogin.domain.owner.repository.OwnerRepository;
import org.example.multiplelogin.domain.user.entity.User;
import org.example.multiplelogin.domain.user.repository.UserRepository;
import org.example.multiplelogin.security.owner.OwnerDetailsImpl;
import org.example.multiplelogin.security.user.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final OwnerRepository ownerRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, OwnerRepository ownerRepository) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (ownerRepository.existsByEmail(username)) {
            Owner owner = ownerRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
            return new OwnerDetailsImpl(owner);
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        return new UserDetailsImpl(user);
    }
}