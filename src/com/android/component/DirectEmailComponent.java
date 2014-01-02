package com.android.component;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean(scope = Scope.Singleton)
public class DirectEmailComponent {

	@RootContext
	Activity activity;

	private static final String mailFrom = "support@weebo.com.sg";
	private static final String mailFromName = "Support";
	private static final String username = "support@weebo.com.sg";
	private static final String password = "sgweeboteam";

	public static final String RECEIVE_EMAIL = "possupport@weebo.com.sg";

	/**
	 * 直接发送信息
	 * 
	 * @param mailTo
	 * @param subject
	 * @param messageBody
	 */
	public void sendMail(String subject, String messageBody) {
		Session session = createSessionObject();
		try {
			Message message = createMessage(RECEIVE_EMAIL, subject, messageBody, session);
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 带提示框的发送
	 * 
	 * @param mailTo
	 * @param subject
	 * @param messageBody
	 */
	public void sendMailTask(String mailTo, String subject, String messageBody) {
		Session session = createSessionObject();
		try {
			Message message = createMessage(mailTo, subject, messageBody, session);
			new SendMailTask().execute(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private Message createMessage(String mailTo, String subject, String messageBody, Session session) throws MessagingException,
			UnsupportedEncodingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mailFrom, mailFromName));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo, mailTo));
		message.setSubject(subject);
		message.setText(messageBody);
		return message;
	}

	private Session createSessionObject() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		return Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	private class SendMailTask extends AsyncTask<Message, Void, Void> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(activity, "Please wait", "Sending mail to report the issue.", true, false);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progressDialog.dismiss();
		}

		@Override
		protected Void doInBackground(Message... messages) {
			try {
				Transport.send(messages[0]);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}