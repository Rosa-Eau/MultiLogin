package org.example.multiplelogin.domain.owner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.multiplelogin.domain.owner.dto.SignupRequestRecord;
import org.example.multiplelogin.domain.owner.service.OwnerService;
import org.example.multiplelogin.handler.exception.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j(topic = "회원가입, 로그인")
@Tag(name = "User API", description = "회원가입, 로그인")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }

    @GetMapping("/v2/signup")
    public String signupPage(Model model) {
        model.addAttribute("ErrorCode", null);
        model.addAttribute("ErrorMessage", null);
        return "v2Signup";
    }

    @GetMapping("/v2/login-page")
    public String loginPage() {
        return "v2Login";
    }

    @Operation(summary = "회원가입", description = "회원 가입시 필요한 정보를 입력합니다.")
    @PostMapping("/v2/signup")
    public String signup(SignupRequestRecord requestDto, Model model) {
        try {
            ownerService.signUp(requestDto);
        } catch (BusinessException ex) {
            model.addAttribute("ErrorCode", ex.getErrorCode().getStatus());
            model.addAttribute("ErrorMessage", ex.getErrorCode().getMessage());
            return "v2Signup";
        }
        return "redirect:/v2/login-page";
    }


}
