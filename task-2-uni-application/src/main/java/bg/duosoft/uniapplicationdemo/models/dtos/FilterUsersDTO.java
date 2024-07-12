package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterUsersDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private int maxResults;
}
