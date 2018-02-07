package org.ssh.boot.notification.service;

import org.ssh.boot.notification.domain.NotificationType;
import org.ssh.boot.notification.domain.Recipient;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

	void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException;

}
