package org.example.multiplelogin.domain.owner.service;

import lombok.RequiredArgsConstructor;
import org.example.multiplelogin.domain.owner.dto.SignupRequestRecord;
import org.example.multiplelogin.domain.owner.entity.Owner;
import org.example.multiplelogin.domain.owner.repository.OwnerRepository;
import org.example.multiplelogin.handler.exception.BusinessException;
import org.example.multiplelogin.handler.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignupRequestRecord requestDto) {
        if (ownerRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        String password = passwordEncoder.encode(requestDto.password());

        Owner owner = Owner.builder()
                .email(requestDto.email())
                .password(password)
                .name(requestDto.name())
                .build();
        ownerRepository.save(owner);
    }

}
