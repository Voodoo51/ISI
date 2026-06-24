package edziekanat.isi.controllers;

import edziekanat.isi.dto.PaymentDTO;
import edziekanat.isi.services.KafkaProducerService;
import edziekanat.isi.services.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaController {

    private final KafkaProducerService producerService;
    private final PaymentService paymentService;

    public KafkaController(KafkaProducerService producerService, PaymentService paymentService) {
        this.producerService = producerService;
        this.paymentService = paymentService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);
        return "Message sent successfully!";
    }

    @PostMapping("/payment")
    public String sendPayment(@RequestBody PaymentDTO request) {
        //paymentService.crateNewPayment(request.getId(), request.getAmount());
        return "Payment sent yey!";
    }
}