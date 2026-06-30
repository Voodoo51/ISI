package edziekanat.isi.controllers;

import edziekanat.isi.dto.*;
import edziekanat.isi.services.PropositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/proposition")
public class PropositionController {
    @Autowired
    private PropositionService propositionService;

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    @GetMapping("/all")
    public ResponseEntity<Page<PropositionDTO>> getAllPropositions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return ResponseEntity.ok(propositionService.getAll(pageable));
    }

    @GetMapping("/all/search")
    public ResponseEntity<Page<PropositionDTO>> searchPropositions(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return ResponseEntity.ok(propositionService.search(query, pageable));
    }

    @GetMapping("/all/search/{userId}")
    public ResponseEntity<Page<PropositionDTO>> searchUserPropositions(
            @PathVariable Long userId,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return ResponseEntity.ok(propositionService.searchUser(userId, query, pageable));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<Page<PropositionDTO>> getAllUserPropositions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return ResponseEntity.ok(propositionService.getAllStudent(userId, pageable));
    }

    @GetMapping("/messages/{propositionId}")
    public ResponseEntity<Page<PropositionMessageDTO>> getAllPropositionMessages(
            @PathVariable Long propositionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size
        );

        return ResponseEntity.ok(propositionService.getAllPropositionMessages(propositionId, pageable));
    }

    @GetMapping("/{propositionId}")
    public ResponseEntity<PropositionDTO> getStudentPropositionInfo(@PathVariable Long propositionId) {
        return ResponseEntity.ok(propositionService.getStudentPropositionInfo(propositionId));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<PropositionDTO> createProposition(
            @RequestPart("request") CreatePropositionRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ResponseEntity.ok(propositionService.create(request, files));
    }

    @PostMapping(value = "/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<PropositionMessageDTO> createPropositionMessage(
            @RequestPart("request") CreateMessageRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ResponseEntity.ok(propositionService.create(request, files));
    }

    @GetMapping("message/file/{fileId}")
    public ResponseEntity<byte[]> getMessageFile(@PathVariable Long fileId) {
        PropositionFileRequest file = propositionService.getMessageFile(fileId);
        MediaType mediaType = MediaTypeFactory
                .getMediaType(file.getFileName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getData());
    }

    @GetMapping("file/{fileId}")
    public ResponseEntity<byte[]> getPropositionFile(@PathVariable Long fileId) {
        PropositionFileRequest file = propositionService.getPropositionFile(fileId);
        MediaType mediaType = MediaTypeFactory
                .getMediaType(file.getFileName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getData());
    }
}
