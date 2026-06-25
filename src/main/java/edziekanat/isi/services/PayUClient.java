package edziekanat.isi.services;

import edziekanat.isi.dto.CreateOrderRequest;
import edziekanat.isi.dto.PayUAccessTokenResponse;
import edziekanat.isi.dto.PayUPaymentResponse;
import edziekanat.isi.dto.Product;
import edziekanat.isi.exceptions.BadRequestException;
import edziekanat.isi.exceptions.PaymentProcessingErrorException;
import edziekanat.isi.models.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PayUClient {
    @Value("${NGROK_URL}")
    private String proxyUrl;

    public PayUPaymentResponse createPayment(Payment payment, String clientId, String clientSecret, String posId) {
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
        order.setDescription(payment.getDescription());
        order.setCurrencyCode("PLN");
        order.setTotalAmount(payment.getAmount().toString());
        order.setNotifyUrl(proxyUrl + "/payment/notify");
        order.setContinueUrl("http://localhost:3000/payment");
        order.setProducts(List.of(
                new Product(
                        payment.getTitle(),
                        payment.getAmount().toString(),
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

        return payUPaymentResponse;
    }
}
