package bg.duosoft.uniapplicationdemo.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserDetails {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
