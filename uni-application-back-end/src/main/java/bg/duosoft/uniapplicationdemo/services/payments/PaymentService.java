package bg.duosoft.uniapplicationdemo.services.payments;

import bg.duosoft.uniapplicationdemo.models.dtos.PaymentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentService {
    ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentRequest);
}
