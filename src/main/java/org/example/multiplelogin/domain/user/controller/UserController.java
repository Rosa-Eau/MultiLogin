package org.example.multiplelogin.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.multiplelogin.domain.user.dto.SignupRequestRecord;
import org.example.multiplelogin.domain.user.service.UserService;
import org.example.multiplelogin.handler.exception.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@Slf4j(topic = "회원가입, 로그인")
@Tag(name = "User API", description = "회원가입, 로그인")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/v1/signup")
    public String signupPage(Model model) {
        model.addAttribute("ErrorCode", null);
        model.addAttribute("ErrorMessage", null);
        return "signup";
    }

    @GetMapping("/v1/login")
    public String loginPage() {
        return "login";
    }

    @Operation(summary = "회원가입", description = "회원 가입시 필요한 정보를 입력합니다.")
    @PostMapping("/signup")
    public String signup(SignupRequestRecord requestDto, Model model) {
        try {
            userService.signUp(requestDto);
        } catch (BusinessException ex) {
            model.addAttribute("ErrorCode", ex.getErrorCode().getStatus());
            model.addAttribute("ErrorMessage", ex.getErrorCode().getMessage());
            return "signup";
        }
        return "redirect:/v1/login";
    }

}