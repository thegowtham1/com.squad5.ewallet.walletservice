package com.squad5.ewallet.walletservice.controller;

import com.squad5.ewallet.walletservice.dto.WalletDetailsResponse;
import com.squad5.ewallet.walletservice.entity.Wallet;
import com.squad5.ewallet.walletservice.service.WalletService;
import com.squad5.ewallet.walletservice.dto.AmountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Create demo wallets for user ids 1,2,3
    @PostMapping("/create-demo")
    public ResponseEntity<String> createDemo() {
        walletService.createDemoWalletsForUsers(Arrays.asList(1L, 2L, 3L), "INR", "system");
        return ResponseEntity.ok("Demo wallets ensured for users [1,2,3]");
    }

    // Get balance by userId
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam("userId") Long userId) {
        BigDecimal bal = walletService.getBalanceForUser(userId);
        return ResponseEntity.ok(bal);
    }

    // Credit wallet
    @PostMapping("/credit")
    public ResponseEntity<BigDecimal> credit(@RequestParam("userId") Long userId,
                                             @RequestBody AmountRequest req,
                                             @RequestParam(value = "updatedBy", defaultValue = "system") String updatedBy) {
        BigDecimal newBal = walletService.credit(userId, req.getAmount(), updatedBy);
        return ResponseEntity.ok(newBal);
    }

    // Debit wallet
    @PostMapping("/debit")
    public ResponseEntity<BigDecimal> debit(@RequestParam("userId") Long userId,
                                            @RequestBody AmountRequest req,
                                            @RequestParam(value = "updatedBy", defaultValue = "system") String updatedBy) {
        BigDecimal newBal = walletService.debit(userId, req.getAmount(), updatedBy);
        return ResponseEntity.ok(newBal);
    }

    @GetMapping("/userdetails")
    public ResponseEntity<WalletDetailsResponse> getUserDetails(@RequestParam("userId") Long userId) {
        Wallet w = walletService.getWalletDetails(userId);

        WalletDetailsResponse res = new WalletDetailsResponse(
                w.getUserId(),
                w.getAccountNumber(),
                w.getBalance(),
                w.getCurrency()
        );

        return ResponseEntity.ok(res);
    }

}

