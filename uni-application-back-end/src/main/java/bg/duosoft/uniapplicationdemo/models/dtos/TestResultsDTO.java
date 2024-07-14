package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.Data;

@Data
public class TestResultsDTO {
    private String username;
    private String testName;
    private int result;
}
