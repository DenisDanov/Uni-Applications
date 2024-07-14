package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TestStateDTO {
    private String username;
    private ArrayList<Integer> answers;
    private long testStartTime;
    private String testName;
}
