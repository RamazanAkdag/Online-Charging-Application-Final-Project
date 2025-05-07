package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.service.concrete.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ResetPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @Autowired
    public ResetPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password"; // templates/reset-password.html
    }
    @PostMapping("/reset-password")
    public String handlePasswordReset(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword,
            Model model) {

        boolean result = forgotPasswordService.resetPassword(token, newPassword);

        if (result) {
            model.addAttribute("message", "Şifreniz başarıyla sıfırlandı. Giriş yapabilirsiniz.");
        } else {
            model.addAttribute("message", "Geçersiz veya süresi dolmuş bağlantı.");
        }

        return "reset-result";
    }
}
