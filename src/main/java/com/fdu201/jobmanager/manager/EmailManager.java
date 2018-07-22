package com.fdu201.jobmanager.manager;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailManager {
	
	private Session session;
	private final static Logger logger = Logger.getLogger(EmailManager.class);
	
	public EmailManager(String host, String port, String username, String password, boolean enableSSL) {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.setProperty("mail.smtp.auth", "true");
		if (enableSSL)
			properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		this.session = Session.getDefaultInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	public void sendTextEmail(String from, List<String> recipientList, String subject, String content) {
	    logger.info("Start sending emails.");
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			InternetAddress[] addresses = new InternetAddress[recipientList.size()];
			int idx = 0;
			for (String recipient : recipientList)
				addresses[idx++] = new InternetAddress(recipient);
			message.addRecipients(Message.RecipientType.BCC, addresses);

			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);
			logger.info("Emails have been sent successfully.");
		}
		catch (MessagingException e) {
			logger.error("Failed to send emails.", e);
		}
	}
}
