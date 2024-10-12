package bg.duosoft.uniapplicationdemo.events.listeners;

import bg.duosoft.uniapplicationdemo.events.UserRegisteredEvent;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationListener.class);

    private final EmailService emailService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        UserDTO userDTO = event.getUserDTO();
        String to = userDTO.getEmail();
        String subject = "Welcome to our University Application Portal!";
        String body = """
                Dear %s,
                
                Welcome to our University Application Portal!
                
                You can begin your journey by applying to the available specialties from the 'Apply' section!""".formatted(userDTO.getFirstName());

        // Send email asynchronously
        emailService.sendEmailAsync(to, subject, body);
        logger.info("Email sent asynchronously to: {}", to);
    }
}
