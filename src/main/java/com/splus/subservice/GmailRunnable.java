package com.splus.subservice;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailRunnable extends Thread {
	
	private String from = "";
	private String to = "";
    private String subject = "";
    private MimeMessage message = null;
    private String text = "";
    private Session session = null;

	@Override
    public void run() {
    	setPriority(MIN_PRIORITY);
        this.sendMail();
    }
    
    public GmailRunnable(String from, String to, String subject, String text) {
    	
    	this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
    
    public Session setSession(String mailServer, String mailPort, final String user, final String password){
		
		Properties newProp = new Properties();

		newProp.put("mail.smtp.host", mailServer);  
		newProp.put("mail.smtp.socketFactory.port", mailPort);  
		newProp.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
		newProp.put("mail.smtp.auth", "true");  
//		newProp.put("mail.smtp.port", "25");
		
		session = Session.getDefaultInstance(newProp, new javax.mail.Authenticator() 
		{  
			protected PasswordAuthentication getPasswordAuthentication()
			{  
				return new PasswordAuthentication(user,password);  
			}  

		});
    	return session;    	
    }
    
    private void sendMail() {

		try {
			if(session!=null && to!=null && text!=null){
				
				message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(to));  
				message.setSubject(subject);
				message.setContent(text, "text/html; charset=utf-8");
				Transport.send(message);				
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
 
    }
}