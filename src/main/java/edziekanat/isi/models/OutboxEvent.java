package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
    @Table(name = "outbox_events")
    public class OutboxEvent {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String topic;

        @Column(columnDefinition = "TEXT")
        private String payload;

        private boolean sent = false;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean getsent() {
        return sent;
    }
    public void setsent(boolean sent) {
        this.sent = sent;
    }

    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    }

