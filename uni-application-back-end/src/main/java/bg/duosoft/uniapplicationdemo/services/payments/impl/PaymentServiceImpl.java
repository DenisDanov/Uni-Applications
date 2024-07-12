package bg.duosoft.uniapplicationdemo.services.payments.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.PaymentDTO;
import bg.duosoft.uniapplicationdemo.services.payments.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> processPayment(PaymentDTO paymentRequest) {
        String url = "http://localhost:8083/api/v1/payments/process";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<PaymentDTO> request = new HttpEntity<>(paymentRequest, headers);

        return restTemplate.postForEntity(url, request, String.class);
    }
}
