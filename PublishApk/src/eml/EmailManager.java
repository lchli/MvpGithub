package eml;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailManager {

	private Properties props; // 系统属性
	private Session session; // 邮件会话对象
	private MimeMessage mimeMsg; // MIME邮件对象
	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

	/**
	 * Constructor
	 * 
	 * @param smtp
	 *            邮件发送服务器
	 */
	public EmailManager() {
		props = System.getProperties();
		props.put("mail.smtp.auth", "false");
		session = Session.getDefaultInstance(props, null);
		session.setDebug(true);
		mimeMsg = new MimeMessage(session);
		mp = new MimeMultipart();
	}

	/**
	 * Constructor
	 * 
	 * @param smtp
	 *            邮件发送服务器
	 */
	public EmailManager(String smtp, String username, String password) {
		props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtp);
		props.put("username", username);
		props.put("password", password);
		session = Session.getDefaultInstance(props, null);
		session.setDebug(true);
		mimeMsg = new MimeMessage(session);
		mp = new MimeMultipart();
	}

	/**
	 * 发送邮件
	 */
	public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content,
			String filename) {
		try {
			// 设置发信人
			mimeMsg.setFrom(new InternetAddress(from));
			// 设置接收人
			if (to != null) {
				for (int i = 0; i < to.length; i++) {
					mimeMsg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
				}
			}
			// 设置抄送人
			if (copyto != null) {
				for (int i = 0; i < copyto.length; i++) {
					mimeMsg.addRecipient(Message.RecipientType.CC, new InternetAddress(copyto[i]));
				}
			}
			// 设置主题
			mimeMsg.setSubject(subject);
			// 设置正文
			BodyPart bp = new MimeBodyPart();
			bp.setContent(content, "text/html;charset=utf-8");
			mp.addBodyPart(bp);
			// 设置附件
			bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(MimeUtility.encodeText(fileds.getName(), "UTF-8", "B"));
			mp.addBodyPart(bp);

			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			// 发送邮件
			if (props.get("mail.smtp.auth").equals("true")) {
				Transport transport = session.getTransport("smtp");
				transport.connect((String) props.get("mail.smtp.host"), (String) props.get("username"),
						(String) props.get("password"));

				Address[] toReceivers = mimeMsg.getRecipients(Message.RecipientType.TO);
				if (toReceivers != null && toReceivers.length > 0) {
					transport.sendMessage(mimeMsg, toReceivers);
				}

				Address[] ccReceivers = mimeMsg.getRecipients(Message.RecipientType.CC);
				if (ccReceivers != null && ccReceivers.length > 0) {
					transport.sendMessage(mimeMsg, ccReceivers);
				}

				transport.close();
			} else {
				Transport.send(mimeMsg);
			}
			System.out.println("邮件发送成功");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}