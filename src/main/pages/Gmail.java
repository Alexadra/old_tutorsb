package main.pages;


import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.mail.BodyPart;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;

import main.Driver.DataProperties;


import com.sun.mail.pop3.POP3SSLStore;


public class Gmail extends BaseClass {
    private Session session = null;
    private Store store = null;
    private String username, password;
    private Folder folder;
    public String urlRecover;
    public String newPass;
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    String url = DataProperties.get("url");


    public Gmail() {}

    public void setUserPass(String username, String password) {
	this.username = username;
	this.password = password;
	System.out.println(username + " "+ password);
    }


    public void connect() throws Exception {
	System.out.println("1");
	String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	Properties pop3Props = new Properties();
	pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
	System.out.println("2");
	pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
	pop3Props.setProperty("mail.pop3.port",  "995");
	pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
	System.out.println("3-"+username +  password);

	URLName url = new URLName("pop3", "pop.gmail.com", 995, "",
		username, password);
	System.out.println("4");
	session = Session.getInstance(pop3Props, null);
	store = new POP3SSLStore(session, url);
	store.connect();
    }


    public void openFolder(String folderName) throws Exception {
	// Open the Folder
	folder = store.getDefaultFolder();
	folder = folder.getFolder(folderName);
	if (folder == null) {
	    throw new Exception("Invalid folder");
	}
	// try to open read/write and if that fails try read-only
	try {
	    folder.open(Folder.READ_WRITE);
	} catch (MessagingException ex) {
	    folder.open(Folder.READ_ONLY);
	}
    }


    public void closeFolder() throws Exception {
	folder.close(false);
    }


    public Message[] getMessages(String sFrom, Boolean bSEEN) {
	Message[] messages = null;
	try {
	    SearchTerm totalTerm = new FromStringTerm(sFrom);
	    if (bSEEN!=null){
		FlagTerm flagSeen = new FlagTerm(new Flags(Flag.SEEN), bSEEN);
		totalTerm = new AndTerm(flagSeen, totalTerm);
		System.out.println("Seen?  "+ bSEEN);
	    }
	    messages = folder.search(totalTerm);
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
	System.out.println("messages! "+ messages);
	return messages;
    }


    public String getMessageFullInfo(Message msg) throws IOException, MessagingException{
	String from = InternetAddress.toString(msg.getFrom());
	String sBody = null;
	if (from != null) {
	    write("From: " + from);
	}
	String replyTo = InternetAddress.toString(msg.getReplyTo());
	if (replyTo != null) {
	    write("Reply-to: " + replyTo);
	}
	String to = InternetAddress.toString(msg
		.getRecipients(Message.RecipientType.TO));
	if (to != null) {
	    write("To: " + to);
	}
	String subject = msg.getSubject();
	if (subject != null) {
	    write("Subject: " + subject);
	}
	Date sent = msg.getSentDate();		//date
	if (sent != null) {
	    write("Sent: " + sent);
	}
	write();
	write("Message : ");
	try {
	    Multipart multipart = (Multipart) msg.getContent();
	    for (int x = 0; x < multipart.getCount(); x++) {
		BodyPart bodyPart = multipart.getBodyPart(x);
		String sMultiBody = bodyPart.getContent().toString();
		sBody = sBody + sMultiBody;
	    }
	    return sBody;
	} catch (ClassCastException e) {
	    sBody = (String) msg.getContent();
	    System.out.println("sBody:"+sBody);
	    return sBody;
	}
    }


    public String getLastMessageText(String login, String pass, String sFrom, Boolean bSEEN) throws Exception{
	Gmail gmail = new Gmail();
	gmail.setUserPass(login,pass);
	gmail.connect();
	gmail.openFolder("INBOX");
	LOG.info("Number of new messages = " + gmail.getMessages(sFrom, bSEEN).length);
	Message lastMess = gmail.getMessages(sFrom, bSEEN)[gmail.getMessages(sFrom, bSEEN).length-1];
	String lastMail = gmail.getMessageFullInfo(lastMess);
	write("Last mail"+lastMail);
	gmail.closeFolder();
	return lastMail;
    }


    public String getPasswordFromMail(String mail){
	Pattern pattern = Pattern.compile(":");
	String mailSub[] = pattern.split(mail,2);
	pattern = Pattern.compile("\\.");
	String passSub[] = pattern.split(mailSub[1],2);			//find new password in letter
	return passSub[0];
    }


    public String getUrlFromMail(String mail){
	Pattern pattern = Pattern.compile(url+"login/\\S+");	//find url for password recover
	Matcher matcher = pattern.matcher(mail);
	if (matcher.find()) {
	    return matcher.group();
	} else return null;
    }


    public void gmailRecoverMess(String login, String pass, String sFrom, Boolean bSEEN) throws Exception {
	try {
	    String mail = getLastMessageText(login, pass, sFrom, bSEEN);
	    urlRecover = getUrlFromMail(mail);
	    newPass = getPasswordFromMail(mail);
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }


    public String gmailGetPassword(String login, String pass, String sFrom, Boolean bSEEN) throws Exception{
	String mail = getLastMessageText(login, pass, sFrom, bSEEN);
	return getPasswordFromMail(mail);
    }


}



