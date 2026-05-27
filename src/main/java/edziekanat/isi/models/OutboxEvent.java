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

         @Column(columnDefinition = "retry_count")
        private int retryCount = 0;

         @Column(columnDefinition = "payment_id")
        private long paymentId = 0;

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }

        public Long getPaymentId() {
            return id;
        }
        public void setPaymentId(Long id) {
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

        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    }

