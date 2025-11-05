package com.monarca.appointments.controller;

import com.monarca.appointments.model.User;
import com.monarca.appointments.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public static class ChargeRequest {
        @NotBlank(message = "Token ID is required")
        private String tokenId;

        @Min(value = 100, message = "Amount must be at least 100 centavos")
        private Integer amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotBlank(message = "Description is required")
        private String description;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        private Map<String, Object> metadata;

        // Getters and Setters
        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
    }

    @PostMapping("/charge")
    public ResponseEntity<?> createCharge(
            @Valid @RequestBody ChargeRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            Map<String, Object> result = paymentService.createCharge(
                    request.getTokenId(),
                    request.getAmount(),
                    request.getCurrency(),
                    request.getDescription(),
                    request.getEmail(),
                    request.getMetadata()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("detail", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{chargeId}")
    public ResponseEntity<?> getCharge(
            @PathVariable String chargeId,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            Map<String, Object> result = paymentService.getCharge(chargeId);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("detail", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String email,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            // If user is not admin, only show their own payments
            String filterEmail = email;
            if (!currentUser.getRole().equals("admin") && !currentUser.getEmail().equals(email)) {
                filterEmail = currentUser.getEmail();
            }

            Map<String, Object> result = paymentService.listCharges(filterEmail, limit);
            
            // Extract charges data
            List<Map<String, Object>> charges = (List<Map<String, Object>>) result.get("data");
            
            return ResponseEntity.ok(charges);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("detail", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
