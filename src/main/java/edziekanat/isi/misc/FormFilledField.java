package edziekanat.isi.misc;

public class FormFilledField {
    private Integer id;
    private String value;

    public FormFilledField() {
    }

    public FormFilledField(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
