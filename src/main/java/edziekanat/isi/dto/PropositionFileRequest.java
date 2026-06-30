package edziekanat.isi.dto;

import edziekanat.isi.models.PropositionFile;
import edziekanat.isi.models.PropositionMessageFile;

public class PropositionFileRequest {
    private String fileName;
    private byte[] data;

    public PropositionFileRequest() {
    }

    public PropositionFileRequest(PropositionFile propositionFile) {
        this.fileName = propositionFile.getFileName();
        this.data = propositionFile.getData();
    }

    public PropositionFileRequest(PropositionMessageFile propositionMessageFile) {
        this.fileName = propositionMessageFile.getFileName();
        this.data = propositionMessageFile.getData();
    }

    public PropositionFileRequest(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
