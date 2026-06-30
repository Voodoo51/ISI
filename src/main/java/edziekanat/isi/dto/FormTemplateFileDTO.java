package edziekanat.isi.dto;

import edziekanat.isi.models.FormTemplate;

public class FormTemplateFileDTO {
    private byte[] pdfFile;
    private String title;

    public FormTemplateFileDTO() {
    }

    public FormTemplateFileDTO(byte[] pdfFile, String title) {
        this.pdfFile = pdfFile;
        this.title = title;
    }

    public FormTemplateFileDTO(FormTemplate formTemplate) {
        this.pdfFile = formTemplate.getForm_pdf();
        this.title = formTemplate.getTitle();
    }

    public byte[] getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(byte[] pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
