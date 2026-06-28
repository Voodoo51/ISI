package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="proposition_message_file")
public class PropositionMessageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="proposition_message_id")
    private PropositionMessage propositionMessage;

    @Column(name="file_name")
    private String fileName;

    @Column(name="file_size")
    private Long fileSize;

    @Column
    private byte[] data;

    public PropositionMessageFile() {
    }

    public PropositionMessageFile(Long id, PropositionMessage propositionMessage, String fileName, Long fileSize, byte[] data) {
        this.id = id;
        this.propositionMessage = propositionMessage;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PropositionMessage getPropositionMessage() {
        return propositionMessage;
    }

    public void setPropositionMessage(PropositionMessage propositionMessage) {
        this.propositionMessage = propositionMessage;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}