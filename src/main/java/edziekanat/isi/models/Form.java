package edziekanat.isi.models;

public class Form {
    private int id;
    private String formName;
    private String json;

    public Form(int id, String formName, String json) {
        this.id = id;
        this.formName = formName;
        this.json = json;
    }

    public Form(String formName, String json) {
        this.formName = formName;
        this.json = json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
