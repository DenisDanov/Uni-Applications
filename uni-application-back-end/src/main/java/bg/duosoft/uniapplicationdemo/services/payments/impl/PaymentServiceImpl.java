package bg.duosoft.uniapplicationdemo.services.payments.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.PaymentDTO;
import bg.duosoft.uniapplicationdemo.services.payments.PaymentService;
import bg.duosoft.uniapplicationdemo.services.payments.clients.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentClient paymentClient;

    @Override
    public ResponseEntity<String> processPayment(PaymentDTO paymentRequest) {
        return paymentClient.processPayment(paymentRequest);
    }
}
