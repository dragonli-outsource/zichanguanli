package com.dt.core.tool.util;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;

public class MailUtil {

	/**
	 * @param args
	 */

	private String mailstmp = "";
	private int mailport = 25;
	private String mailuser = "";
	private String mailpwd = "";

	public MailUtil(String stmpin, String portin, String userin, String pwdin) {
		mailstmp = stmpin;
		mailport = Integer.parseInt(portin);
		mailuser = userin;
		mailpwd = pwdin;
	}

	public void Send(String subject, String htmlbody, String tomail) {

		// stmp:smtp.yeah.net,port:25,user:ygrdba007@yeah.net,pwd:007ygrdba
		Email email = Email.create();
		email.from(mailuser).to(tomail);
		email.subject(subject);
		email.addHtml(htmlbody);
		@SuppressWarnings("rawtypes")
		SmtpServer smtpServer = SmtpServer.create(mailstmp, mailport).authenticateWith(mailuser, mailpwd)
				.timeout(30000);
		SendMailSession session = smtpServer.createSession();
		session.open();
		session.sendMail(email);
		session.close();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Email email = Email.create();
		email.from("ygrdba007@yeah.net").to("792014416@qq.com");
		email.subject("我的主题我做主");
		email.addHtml("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>你好v</h1></body></html>");
		@SuppressWarnings("rawtypes")
		SmtpServer smtpServer = SmtpServer.create("smtp.yeah.net", 25)
				.authenticateWith("ygrdba007@yeah.net", "007ygrdba").timeout(100000);
		SendMailSession session = smtpServer.createSession();
		session.open();
		session.sendMail(email);
		session.close();
	}

}
