package functions;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	
<<<<<<< HEAD
	private static String email = "tempustecinterno@gmail.com";
	private static String recipients = "conrado.perini.fracacio@gmail.com, rosangela@tempustec.ind.br, valdir@tempustec.ind.br";
=======
	private static final String email = "tempustecinterno@gmail.com";
	private static final String password = "jakh cqvx ublh tlzt";
	
	//Os Emails vão ser separados com um ', '
	private static final String subjects = "conrado.perini.fracacio@gmail.com, rosangela@tempustec.ind.br";
>>>>>>> 96b63fb (Algumas alterações para a criação de uma pagina de adição de parte e de montagem)

	public Email() {
		
	}
	
	public static void sendReport(String header, String body) {
<<<<<<< HEAD
		Properties configs = new Properties();
		
		configs.put("mail.smtp.host", "smtp.gmail.com");
		configs.put("mail.smtp.socketFactory.port", "465");
		configs.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		configs.put("mail.smtp.auth", "true");
		configs.put("mail.smtp.port", "465");
		
		Session online = Session.getInstance(configs, 
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(email, "jakh cqvx ublh tlzt");
=======
		if(DBConector.getDB().equals("jdbc:mysql://localhost:3306/Tempustec")) {
			Properties configs = new Properties();
			
			configs.put("mail.smtp.host", "smtp.gmail.com");
			configs.put("mail.smtp.socketFactory.port", "465");
			configs.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			configs.put("mail.smtp.auth", "true");
			configs.put("mail.smtp.port", "465");
			
			Session online = Session.getInstance(configs, 
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(email, password);
					}
>>>>>>> 96b63fb (Algumas alterações para a criação de uma pagina de adição de parte e de montagem)
				}
			);
			
			online.setDebug(true);
			
			try {
				Message message = new MimeMessage(online);
				message.setFrom(new InternetAddress(email));
				
				Address[] recivers = InternetAddress.parse(subjects);
				
				message.setRecipients(Message.RecipientType.TO, recivers);
				message.setSubject(header);
				message.setText(body);
				
				Transport.send(message);
				
				System.out.println("Envio do Relatório de Arquivo");
				
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
<<<<<<< HEAD
		);
		
		online.setDebug(true);
		
		try {
			Message message = new MimeMessage(online);
			message.setFrom(new InternetAddress(email));
			
			Address[] recivers = InternetAddress.parse(recipients);
			
			message.setRecipients(Message.RecipientType.TO, recivers);
			message.setSubject(header);
			message.setText(body);
			
			Transport.send(message);
			
			System.out.println("Envio do Relatório de Arquivo");
			
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
=======
>>>>>>> 96b63fb (Algumas alterações para a criação de uma pagina de adição de parte e de montagem)
		}
	}

}
