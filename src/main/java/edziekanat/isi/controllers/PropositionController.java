package edziekanat.isi.controllers;

import edziekanat.isi.dto.CreateMessageRequest;
import edziekanat.isi.dto.CreatePropositionRequest;
import edziekanat.isi.dto.PropositionDTO;
import edziekanat.isi.dto.PropositionMessageDTO;
import edziekanat.isi.services.PropositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/proposition")
public class PropositionController {
    @Autowired
    private PropositionService propositionService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<PropositionDTO>> getAllPropositions(@PathVariable Long userId) {
        return ResponseEntity.ok(propositionService.getAllStudent(userId));
    }

    @GetMapping("/{propositionId}")
    public ResponseEntity<PropositionDTO> getStudentPropositionInfo(@PathVariable Long propositionId) {
        return ResponseEntity.ok(propositionService.getStudentPropositionInfo(propositionId));
    }

    @GetMapping("/messages/{propositionId}")
    private ResponseEntity<List<PropositionMessageDTO>> getAllPropositionMessages(@PathVariable Long propositionId) {
        return ResponseEntity.ok(propositionService.getAllPropositionMessages(propositionId));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<PropositionDTO> createProposition(
            @RequestPart("request") CreatePropositionRequest request,
            @RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(propositionService.create(request, files));
    }

    @PostMapping(value = "/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<PropositionMessageDTO> createPropositionMessage(
            @RequestPart("request") CreateMessageRequest request,
            @RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(propositionService.create(request, files));
    }
}
