package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="form_template")
public class FormTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String json;

    public FormTemplate() {
    }

    public FormTemplate(int id, String title, String json) {
        this.id = id;
        this.title = title;
        this.json = json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
