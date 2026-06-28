package edziekanat.isi.dto;

import edziekanat.isi.models.Proposition;
import edziekanat.isi.models.PropositionFile;

public class PropositionFileDTO {
    private Long id;
    private String fileName;
    private Long fileSize;

    public PropositionFileDTO() {
    }

    public PropositionFileDTO(Long id, String fileName, Long fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public PropositionFileDTO(PropositionFile propositionFile) {
        this.id = propositionFile.getId();
        this.fileName = propositionFile.getFileName();
        this.fileSize = propositionFile.getFileSize();
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
