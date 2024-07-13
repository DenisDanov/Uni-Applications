package bg.duosoft.uniapplicationdemo.events;

import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {

    private final UserDTO userDTO;

    public UserRegisteredEvent(Object source, UserDTO userDTO) {
        super(source);
        this.userDTO = userDTO;
    }

}
