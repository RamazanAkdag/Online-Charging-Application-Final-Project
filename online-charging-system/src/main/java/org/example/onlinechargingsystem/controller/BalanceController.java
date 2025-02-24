package org.example.onlinechargingsystem.controller;

import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private IBalanceService balanceService;

    @PostMapping("/use")
    public String useService(
            @RequestParam Long subscriberId,
            @RequestParam int minutes,
            @RequestParam int sms,
            @RequestParam int data) {       try {
            balanceService.deductBalance(subscriberId, minutes, sms, data);
            return "Usage successfully recorded.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}

