package edziekanat.isi.misc;

public class FormField {
    private Integer id;
    private String label;
    private String placeholder;
    private String type;
    private Integer page;
    private Float x;
    private Float y;
    private Float fontSize;

    public FormField() {}

    public FormField(Integer id, String label, String placeholder, String type, Integer page, Float x, Float y, Float fontSize) {
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
        this.type = type;
        this.page = page;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getFontSize() {
        return fontSize;
    }

    public void setFontSize(Float fontSize) {
        this.fontSize = fontSize;
    }
}
