package edziekanat.isi.controllers;

import edziekanat.isi.dto.PaymentsDto;
import edziekanat.isi.services.KafkaProducerService;
import edziekanat.isi.services.PaymentsService;
import edziekanat.isi.services.PaymentsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaController {

    private final KafkaProducerService producerService;
    private final PaymentsService paymentsService;

    public KafkaController(KafkaProducerService producerService, PaymentsService paymentsService) {
        this.producerService = producerService;
        this.paymentsService = paymentsService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);
        return "Message sent successfully!";
    }

    @PostMapping("/payment")//zapomnialam o dto, trzeba zmienic
    public String sendPayment(@RequestBody PaymentsDto request) {
        paymentsService.crateNewPayment(request.getId(), request.getAmount());
        return "Payment sent yey!";
    }
}