package logic;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
/*Sends mail over SSL*/
public class Mail {
	/*Trololooololollol*/
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Mail("", "Samuel L Jackson up in this" + i, "The path of the righteous man is beset on all sides by the iniquities of the selfish and the tyranny of evil men. Blessed is he who, in the name of charity and good will, shepherds the weak through the valley of darkness, for he is truly his brother's keeper and the finder of lost children. And I will strike down upon thee with great vengeance and furious anger those who would attempt to poison and destroy My brothers. And you will know My name is the Lord when I lay My vengeance upon thee.");
		}
	}
	private static Properties props;

	public Mail() {
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
	}
	public Mail(String reciever, String subject, String msg){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("fellesprosjektet@gmail.com","gruppe22");
				}
			});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("fellesprosjektet@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(reciever));
			message.setSubject(subject, "utf-8");
			message.setText(msg, "utf-8");
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static void sendMail(String reciever, String subject, String msg) {
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("fellesprosjektet@gmail.com","gruppe22");
				}
			});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("fellesprosjektet@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(reciever));
			message.setSubject(subject, "utf-8");
			message.setText(msg, "utf-8");
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}