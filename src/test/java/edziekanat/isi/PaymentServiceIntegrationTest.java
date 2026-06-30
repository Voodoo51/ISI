package edziekanat.isi;

import edziekanat.isi.dto.*;
import edziekanat.isi.exceptions.PaymentAlreadyPaid;
import edziekanat.isi.exceptions.PaymentNotFoundException;
import edziekanat.isi.models.Payment;
import edziekanat.isi.models.PaymentStatus;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.PaymentRepository;
import edziekanat.isi.repositories.PaymentStatusRepository;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.repositories.UserRoleRepository;
import edziekanat.isi.services.PayUClient;
import edziekanat.isi.services.PaymentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class PaymentServiceIntegrationTest {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentStatusRepository paymentStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @MockitoBean
    private PayUClient payUClient;

    @Test
    void shouldReturnAllPaymentsForUser() {
        PaymentStatus unpaid = paymentStatusRepository.findByName("unpaid").orElseThrow();
        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        User user = userRepository.save(
                new User(
                        role,
                        "john",
                        "",
                        "john@test.com",
                        ""
                )
        );

        Payment payment1 = new Payment();
        payment1.setUser(user);
        payment1.setAmount(100L);
        payment1.setPaymentStatus(unpaid);

        Payment payment2 = new Payment();
        payment2.setUser(user);
        payment2.setAmount(250L);
        payment2.setPaymentStatus(unpaid);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        User anotherUser = userRepository.save(
                new User(
                        role,
                        "john2",
                        "",
                        "john2@test.com",
                        ""
                )
        );

        Payment ignored = new Payment();
        ignored.setUser(anotherUser);
        ignored.setAmount(999L);
        ignored.setPaymentStatus(unpaid);

        paymentRepository.save(ignored);

        List<PaymentDTO> result = paymentService.getAllUserPayments(user.getId());

        assertEquals(2, result.size());

        List<Long> amounts = result.stream()
                .map(PaymentDTO::getAmount)
                .toList();

        assertTrue(amounts.contains(100L));
        assertTrue(amounts.contains(250L));
        assertFalse(amounts.contains(999L));
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoPayments() {
        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        User user = userRepository.save(
                new User(
                        role,
                        "john",
                        "",
                        "john@test.com",
                        ""
                )
        );

        List<PaymentDTO> result = paymentService.getAllUserPayments(user.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void finalizePaymentShouldMarkAsPaid() {
        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        User user = userRepository.save(new User(role, "john", "", "john@test.com", ""));

        PaymentStatus pending = paymentStatusRepository.findById(1).orElseThrow();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setTitle("Tuition");
        payment.setDescription("Semester fee");
        payment.setAmount(1000L);
        payment.setOrderId("ORDER123");
        payment.setPaymentStatus(pending);

        payment = paymentRepository.save(payment);

        PayUNotification notification = new PayUNotification();

        PayUNotification.Order order = new PayUNotification.Order();

        order.setOrderId("ORDER123");
        order.setStatus("COMPLETED");

        notification.setOrder(order);

        paymentService.finalizePayment(notification);

        Payment updated = paymentRepository.findById(payment.getId()).orElseThrow();

        assertEquals("paid", updated.getPaymentStatus().getName());
    }

    @Test
    void finalizePaymentShouldMarkAsCanceled() {
        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        User user = userRepository.save(new User(role, "john", "", "john@test.com", ""));

        PaymentStatus pending = paymentStatusRepository.findById(1).orElseThrow();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrderId("ORDER321");
        payment.setPaymentStatus(pending);

        payment = paymentRepository.save(payment);

        PayUNotification notification = new PayUNotification();

        PayUNotification.Order order = new PayUNotification.Order();

        order.setOrderId("ORDER321");
        order.setStatus("CANCELED");

        notification.setOrder(order);

        paymentService.finalizePayment(notification);

        Payment updated = paymentRepository.findById(payment.getId()).orElseThrow();

        assertEquals("canceled", updated.getPaymentStatus().getName());
    }

    @Test
    void finalizePaymentShouldThrowWhenPaymentNotFound() {

        PayUNotification notification = new PayUNotification();
        PayUNotification.Order order = new PayUNotification.Order();

        order.setOrderId("DOES_NOT_EXIST");
        order.setStatus("COMPLETED");

        notification.setOrder(order);

        assertThrows(PaymentNotFoundException.class, () -> paymentService.finalizePayment(notification));
    }

    @Test
    void shouldProcessPayment() {
        UserRole role = userRoleRepository.findByName("student")
                .orElseThrow();

        User user = userRepository.save(
                new User(role, "john", "", "john@test.com", "")
        );

        PaymentStatus unpaid = paymentStatusRepository.findByName("unpaid")
                .orElseThrow();


        Payment payment = new Payment();

        payment.setUser(user);
        payment.setTitle("Tuition fee");
        payment.setDescription("Semester payment");
        payment.setAmount(10000L);
        payment.setPaymentStatus(unpaid);


        payment = paymentRepository.save(payment);


        PayUPaymentResponse payUResponse = new PayUPaymentResponse();

        payUResponse.setOrderId("ORDER123");
        payUResponse.setRedirectUri(
                "https://secure.snd.payu.com/pay"
        );


        when(payUClient.createPayment(
                eq(payment),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(payUResponse);


        String redirectUrl =
                paymentService.processPayment(
                        new PaymentRequest(payment.getId())
                );


        assertEquals(
                "https://secure.snd.payu.com/pay",
                redirectUrl
        );

        assertEquals(
                "ORDER123",
                payment.getOrderId()
        );


        Payment updated =
                paymentRepository.findById(payment.getId())
                        .orElseThrow();


        assertEquals(
                "ORDER123",
                updated.getOrderId()
        );


        assertEquals(
                "initiated",
                updated.getPaymentStatus().getName()
        );
    }

    @Test
    void processPaymentShouldThrowWhenAlreadyPaid() {
        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        User user = userRepository.save(new User(role, "john", "", "john@test.com", ""));

        PaymentStatus paid = paymentStatusRepository.findByName("paid").orElseThrow();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setTitle("Tuition");
        payment.setAmount(1000L);
        payment.setPaymentStatus(paid);

        payment = paymentRepository.save(payment);
        Payment finalPayment = payment;

        assertThrows(PaymentAlreadyPaid.class, () -> paymentService.processPayment(new PaymentRequest(finalPayment.getId())));
    }

    @Test
    void finalizePaymentShouldKeepPaymentPending() {
        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        User user = userRepository.save(new User(role, "john", "", "john@test.com", ""));

        PaymentStatus pending = paymentStatusRepository.findByName("pending").orElseThrow();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrderId("ORDER555");
        payment.setPaymentStatus(pending);

        payment = paymentRepository.save(payment);

        PayUNotification notification = new PayUNotification();
        PayUNotification.Order order = new PayUNotification.Order();

        order.setOrderId("ORDER555");
        order.setStatus("PENDING");

        notification.setOrder(order);

        paymentService.finalizePayment(notification);

        Payment updated = paymentRepository.findById(payment.getId()).orElseThrow();

        assertEquals("pending", updated.getPaymentStatus().getName());
    }

    @Test
    void processPaymentShouldThrowWhenPaymentNotFound() {
        assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.processPayment(
                        new PaymentRequest(99999L)
                )
        );
    }
}