package org.example.multiplelogin.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.multiplelogin.domain.user.dto.SignupRequestRecord;
import org.example.multiplelogin.domain.user.entity.User;
import org.example.multiplelogin.domain.user.repository.UserRepository;
import org.example.multiplelogin.handler.exception.BusinessException;
import org.example.multiplelogin.handler.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignupRequestRecord requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        if (userRepository.findByNickname(requestDto.nickname()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXIST_NICKNAME);
        }
        String password = passwordEncoder.encode(requestDto.password());

        User user = User.builder()
                .email(requestDto.email())
                .password(password)
                .nickname(requestDto.nickname())
                .address(requestDto.address())
                .build();
        userRepository.save(user);
    }

}