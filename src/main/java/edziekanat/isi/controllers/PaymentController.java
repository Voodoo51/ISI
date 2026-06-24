package edziekanat.isi.controllers;

import edziekanat.isi.dto.PayUNotification;
import edziekanat.isi.dto.PaymentDTO;
import edziekanat.isi.dto.PaymentRequest;
import edziekanat.isi.models.Payment;
import edziekanat.isi.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<PaymentDTO>> getAllPayments(@Validated @PathVariable(required = true) Long userId) {
        return ResponseEntity.ok(paymentService.getAllUserPayments(userId));
    }

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> sentFormPayment(@Validated @RequestBody PaymentRequest paymentRequest) {
        String redirectUrl = paymentService.processPayment(paymentRequest);

        return ResponseEntity.ok(
                Map.of("redirectUrl", redirectUrl)
        );
    }

    @GetMapping("/callback")
    public RedirectView paymentCallback() {
        return new RedirectView("http://localhost:3000");
    }

    @PostMapping("/notify")
    public ResponseEntity<Void> payuNotification(@RequestBody PayUNotification notification) {
        System.out.println("DOCHODZISZ");
        paymentService.finalizePayment(notification);
        return ResponseEntity.ok().build();
    }
}
