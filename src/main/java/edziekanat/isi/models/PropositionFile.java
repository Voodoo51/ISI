package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="proposition_file")
public class PropositionFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="proposition_id")
    private Proposition proposition;

    @Column(name="file_name")
    private String fileName;

    @Column(name="file_size")
    private Long fileSize;

    @Column
    private byte[] data;

    public PropositionFile() {
    }

    public PropositionFile(Long id, Proposition proposition, String fileName, long fileSize, byte[] data) {
        this.id = id;
        this.proposition = proposition;
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

    public Proposition getProposition() {
        return proposition;
    }

    public void setProposition(Proposition proposition) {
        this.proposition = proposition;
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