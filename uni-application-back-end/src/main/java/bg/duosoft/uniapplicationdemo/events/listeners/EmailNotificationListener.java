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
        String body = "Dear " + userDTO.getFirstName() + ",\n\nWelcome to our University Application Portal!\n\nYou can begin your journey by applying to the available specialties from the 'Apply' section!";

        // Send email asynchronously
        emailService.sendEmailAsync(to, subject, body);
        logger.info("Email sent asynchronously to: {}", to);
    }
}
