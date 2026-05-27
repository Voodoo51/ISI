package edziekanat.isi.misc;

public class FormField {
    private Integer id;
    private String label;
    private String placeholder;
    private String type;

    public FormField() {}

    public FormField(Integer id, String label, String placeholder, String type) {
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
