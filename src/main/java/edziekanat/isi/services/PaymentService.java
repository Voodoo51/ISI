package edziekanat.isi.services;

import edziekanat.isi.dto.*;
import edziekanat.isi.exceptions.*;
import edziekanat.isi.models.Payment;
import edziekanat.isi.models.PaymentStatus;
import edziekanat.isi.repositories.PaymentRepository;
import edziekanat.isi.repositories.PaymentStatusRepository;
import edziekanat.isi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentStatusRepository paymentStatusRepository;
    @Value("${PAYU_POS_ID}")
    private String posId;
    @Value("${PAYU_CLIENT_ID}")
    private String clientId;
    @Value("${PAYU_CLIENT_SECRET}")
    private String clientSecret;
    @Value("${PAYU_REDIRECT}")
    private String redirectUri;

    public PaymentService(PaymentRepository paymentRepository, UserRepository usersRepository){
        this.paymentRepository = paymentRepository;
        this.userRepository = usersRepository;
    }
/*
    public Payments crateNewPayment(Long userId, Long amount){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException();

        Payments payment = new Payments();
        payment.setUser(user.get());
        payment.setAmount(amount);
        payment.setStatus("Pending");

        return paymentsRepository.save(payment);
    }
    public boolean updatePayment(){
        return false;
    }
*/
    public List<PaymentDTO> getAllUserPayments(Long userId) {
        return paymentRepository.findAllByUserId(userId).stream().map(PaymentDTO::new).toList();
    }

    public String processPayment(PaymentRequest paymentRequest) {
        Optional<Payment> payment = paymentRepository.findById(paymentRequest.getId());
        if(payment.isEmpty()) throw new PaymentNotFoundException("Payment not found.");
        if(payment.get().getPaymentStatus().getName().equals("paid")) throw new PaymentAlreadyPaid("Payment already paid.");

        //1 is pending, 2 is paid
        Optional<PaymentStatus> pendingStatus = paymentStatusRepository.findById(1); // should already exist but just in case
        if (pendingStatus.isEmpty()) throw new InternalServerErrorException("Payment status not found.");

        RestClient client = RestClient.create();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        ResponseEntity<PayUAccessTokenResponse> payUAccessTokenRequest = client.post()
                .uri("https://secure.snd.payu.com/pl/standard/user/oauth/authorize")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(form)
                .retrieve().toEntity(PayUAccessTokenResponse.class);

        PayUAccessTokenResponse payUAccessTokenResponse = payUAccessTokenRequest.getBody();
        if (payUAccessTokenResponse == null) {
            throw new BadRequestException("PayU user data error.");
        }


        System.out.println(payUAccessTokenResponse.getAccess_token());

        CreateOrderRequest order = new CreateOrderRequest();

        order.setCustomerIp("127.0.0.1");
        order.setMerchantPosId(posId);
        order.setDescription(payment.get().getDescription());
        order.setCurrencyCode("PLN");
        order.setTotalAmount(payment.get().getAmount().toString());
        order.setNotifyUrl("https://skiing-sandfish-purple.ngrok-free.dev/payment/notify");
        order.setContinueUrl("http://localhost:3000/payment");
        order.setProducts(List.of(
                new Product(
                        payment.get().getTitle(),
                        payment.get().getAmount().toString(),
                        "1"
                )
        ));
        client = RestClient.create();
        ResponseEntity<PayUPaymentResponse> paymentResponse = client.post()
                .uri("https://secure.snd.payu.com/api/v2_1/orders")
                .header("Authorization", "Bearer " + payUAccessTokenResponse.getAccess_token())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(order)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (req, res) -> {
                            throw new RuntimeException(res.getStatusText());
                        }
                )
                .toEntity(PayUPaymentResponse.class);


        PayUPaymentResponse payUPaymentResponse = paymentResponse.getBody();
        if(payUPaymentResponse == null)
        {
            throw new PaymentProcessingErrorException("PayU api response error.");
        }

        payment.get().setOrderId(paymentResponse.getBody().getOrderId());
        payment.get().setPaymentStatus(pendingStatus.get());
        paymentRepository.save(payment.get());

        System.out.println(paymentResponse);
        return payUPaymentResponse.getRedirectUri();
        //payment.get().setPaymentStatus(paidStatus.get());
        //paymentRepository.save(payment.get());

        //return redirectUri;
    }

    public void finalizePayment(PayUNotification notification) {
        String orderId = notification.getOrder().getOrderId();
        String status = notification.getOrder().getStatus();

        System.out.println("TESTSE T");

        Optional<Payment> paymentOp = paymentRepository.findByOrderId(orderId);
        if(paymentOp.isEmpty()) throw new PaymentNotFoundException("Payment not found.");


        Payment payment = paymentOp.get();
        System.out.println(status);
        switch (status) {
            case "COMPLETED":
                Optional<PaymentStatus> paidStatus = paymentStatusRepository.findById(2); // should already exist but just in case
                if(paidStatus.isEmpty()) throw new InternalServerErrorException("Status not found.");
                payment.setPaymentStatus(paidStatus.get());
                break;

            case "PENDING":
                Optional<PaymentStatus> pendingStatus = paymentStatusRepository.findById(1); // should already exist but just in case
                if(pendingStatus.isEmpty()) throw new InternalServerErrorException("Status not found.");
                payment.setPaymentStatus(pendingStatus.get());
                break;

            case "CANCELED":
                Optional<PaymentStatus> canceledStatus = paymentStatusRepository.findById(3); // should already exist but just in case
                if(canceledStatus.isEmpty()) throw new InternalServerErrorException("Status not found.");
                payment.setPaymentStatus(canceledStatus.get());
                break;
        }

        paymentRepository.save(payment);
    }
}