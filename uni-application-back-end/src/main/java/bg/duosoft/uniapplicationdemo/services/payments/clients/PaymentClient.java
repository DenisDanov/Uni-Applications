package bg.duosoft.uniapplicationdemo.services.payments.clients;

import bg.duosoft.uniapplicationdemo.models.dtos.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:8083/api/v1/payments")
public interface PaymentClient {

    @PostMapping("/process")
    ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentRequest);
}
