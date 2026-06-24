package edziekanat.isi.dto;

public class PayUNotification {
    private Order order;

    public static class Order {

        private String orderId;
        private String status;

        public Order() {
        }

        public Order(String orderId, String status) {
            this.orderId = orderId;
            this.status = status;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public PayUNotification() {
    }

    public PayUNotification(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}