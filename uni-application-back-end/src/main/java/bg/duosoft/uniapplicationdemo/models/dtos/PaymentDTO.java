package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String username;
    private String facultyName;
    private String specialtyName;
    private BigDecimal amount;
}
