package com.basic.operations.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.basic.operations.models.User;

public class MailUtility {
	/**
	 * <p>
	 * this method sends email for verification code
	 * 
	 * @param userDetails
	 * @param verificationCode
	 * @return
	 */

	public boolean emailForForgetPassword(User objUsers, String password) {

		String body = "<body style=\"box-sizing: border-box; font-size: 14px; -webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100% !important; height: 100%; line-height: 1.6; background-color: #f6f6f6; margin: 0; padding-left: 10px;\" bgcolor=\"#f6f6f6\">";
		/*String content = "<a href=\"https://app.sapeverywhere.com:8090/#/validateResetPassword?key=" + objUsers.getEmail()
				+ "&id=" + objUsers.getId() + "\""
				+ " style=\"font-family\": 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; box-sizing: border-box; font-size: 14px; color: #FFF; text-decoration: none; line-height: 2; font-weight: bold; text-align: center; cursor: pointer; display: inline-block; border-radius: 5px; text-transform: capitalize; background-color: #348eda; margin: 0; padding: 0; border-color: #348eda; border-style: solid; border-width: 10px 20px;\">"
				+ "Click here.</a>" + "</table>";*/
		try {
			String from = "sapeverywhere247@gmail.com";
			final String username = "sapeverywhere247@gmail.com";// change accordingly
			final String password1 = "sape_movement247";// change accordingly
			String host = "smtp.gmail.com";
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password1);
				}
			});
			String to = objUsers.getEmail();
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Forgot password request for Sape");
			message.setContent(body + "<h3>Hello Dear "
					+ " , </h3>You have requested for forgot password for Sape app !<br>Your new password is <b> "
					+ password + "<b> <br><br>Thank you", "text/html");
			Transport.send(message);
			System.out.println("returning true");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("returning false");
			return false;
		}
	}
}
