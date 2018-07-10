package com.vibrant.jobmanager.medicareeligibility.manager;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailManager {
	
	private Session session;
	
	public EmailManager(String host, String port, String username, String password, boolean enableSSL) {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.setProperty("mail.smtp.auth", "true");
		if (enableSSL) 
			properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		this.session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	public void sendTextEmail(String from, List<String> recipientList, String subject, String content) {
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
			System.out.println("Emails have been sent successfully.");
		}
		catch (MessagingException e) {
			System.out.println("Failed to send emails.");
		}
	}
}
