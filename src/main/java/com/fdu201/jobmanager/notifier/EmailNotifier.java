package com.fdu201.jobmanager.notifier;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.fdu201.jobmanager.manager.EmailManager;
import com.fdu201.jobmanager.manager.PropertyManager;
import org.apache.log4j.Logger;

public class EmailNotifier {
    private final static Logger logger = Logger.getLogger(EmailNotifier.class);
	
	public static boolean send(String from, List<String> recipientList, String subject, String message) {

		try {
            String host = String.valueOf(PropertyManager.getProperty("support_email_host"));
            String port = String.valueOf(PropertyManager.getProperty("support_email_port"));
            String username = String.valueOf(PropertyManager.getProperty("support_email_username"));
            String password = String.valueOf(PropertyManager.getProperty("support_email_password"));
            EmailManager emailManager = new EmailManager(host, port, username, password, true);
            emailManager.sendTextEmail(from, recipientList, subject, message);
            return true;
        } catch (Exception e) {
            logger.error("Unknown exception when sending email.", e);
            return false;
        }
	}
}
