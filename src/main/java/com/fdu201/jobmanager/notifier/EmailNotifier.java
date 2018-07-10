package com.vibrant.jobmanager.medicareeligibility.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import main.java.com.vibrant.email.EmailInfoClass;
import main.java.com.vibrant.email.EmailResetPassword;
import main.java.com.vibrant.email.EmailUserInfo;
import main.java.com.vibrant.email.EmailUtility;
import main.java.common.utils.EventLog;

public class EmailNotifier2 {
	private static final String FROM_VA_EMAIL = "support@vibrant-america.com";
	private static final String FROM_VA_EMAIL_PASSWORD = "Vibrantst12";
	private static final String HOST_VA = "smtp.office365.com";
	private static final int PORT_VA = 587;
	private static final boolean ENABLE_TLS_VA = true;
	private static final boolean ENABLE_SSL_VA = false;
	private static final EmailUserInfo EMAIL_VA_SUPPORT = new EmailUserInfo(FROM_VA_EMAIL, FROM_VA_EMAIL_PASSWORD,
			HOST_VA, PORT_VA, ENABLE_TLS_VA, ENABLE_SSL_VA);
	private static final EventLog eventLog = new EventLog(EmailResetPassword.class.getName());
	
	public static boolean send(String emailAddress, String subject, String message) {

		try {
            String host = "mail.75000g.com";
            String port = "465";
            String username = "support@75000g.com";
            String password = "Dh4pk7kin6!";
            EmailManager emailManager = new EmailManager(host, port, username, password, true);
            String recipient = "fdu@vibrantsci.com";
            List<String> recipientList =  new ArrayList<>();
            recipientList.add(recipient);
            emailManager.sendTextEmail("support@75000g.com", recipientList, subject, "Test email");
            return true;
        } catch (Exception e) {
            eventLog.errorLog(Level.SEVERE, "Unknown exception when sending email.", e);
            return false;
        }
	}
}
