package kr.co.lionkorea.listener;

import kr.co.lionkorea.event.EmailEvent;
import kr.co.lionkorea.service.EmailService;
import kr.co.lionkorea.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EmailEventListener {
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailEvent(EmailEvent event) {
        emailService.sendUpdatePasswordEmail(event.getTo(), event.getSubject(), event.getSubject());
    }

}
