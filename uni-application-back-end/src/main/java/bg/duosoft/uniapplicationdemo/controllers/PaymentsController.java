package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.models.dtos.PaymentDTO;
import bg.duosoft.uniapplicationdemo.services.StudentApplicationService;
import bg.duosoft.uniapplicationdemo.services.payments.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentService paymentService;

    private final StudentApplicationService studentApplicationService;

    @PostMapping("/process")
    private ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentDTO) {
        if (studentApplicationService.findByStudentsApplicationsIdAndStatusAccepted(paymentDTO.getUsername(), paymentDTO.getFacultyName(), paymentDTO.getSpecialtyName()) != null) {
            try {
                return paymentService.processPayment(paymentDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Payment processing failed.");
            }
        }
        return ResponseEntity.notFound().build();
    }
}
