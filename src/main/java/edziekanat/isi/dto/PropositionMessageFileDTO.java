package edziekanat.isi.dto;

import edziekanat.isi.models.PropositionMessageFile;

public class PropositionMessageFileDTO {
    private Long id;
    private String fileName;
    private Long fileSize;

    public PropositionMessageFileDTO() {
    }

    public PropositionMessageFileDTO(Long id, String fileName, Long fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public PropositionMessageFileDTO(PropositionMessageFile propositionMessageFile) {
        this.id = propositionMessageFile.getId();
        this.fileName = propositionMessageFile.getFileName();
        this.fileSize = propositionMessageFile.getFileSize();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
