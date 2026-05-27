package edziekanat.isi.controllers;

import edziekanat.isi.dto.PaymentsDto;
import edziekanat.isi.services.KafkaProducerService;
import edziekanat.isi.services.PaymentsService;
import edziekanat.isi.services.PaymentsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaController {

    private final KafkaProducerService producerService;

    public KafkaController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);
        return "Message sent successfully!";
    }

    @PostMapping("/payment")
    public String sendPayment(@RequestBody PaymentsDto request) {
        PaymentsService.crateNewPayment(request.getId(), request.getAmount());
        return "Payment sent yey!";
    }
}