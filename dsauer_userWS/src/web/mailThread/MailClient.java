/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.mailThread;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import web.services.dsauer_userWS;

/**
 *
 * @author sheky
 */
public class MailClient {

    public static Collection readMail(String pop3, String userName, String userPass, boolean deleteNewMail) {
        Collection recmail = receive(pop3, userName, userPass, deleteNewMail);
        return recmail;
    }

    public static void sendMail(String smtpServer, String to, String from, String subject, String body) {
        send(smtpServer, to, from, subject, body);
    }

    private static Collection receive(String popServer, String popUser, String popPassword, boolean deleteNewMail) {
        Store store = null;
        Folder folder = null;
        boolean err = false;
        Collection mailMesg = new LinkedList();
        try {
            // -- Get hold of the default session --
            Properties props = System.getProperties();
            Session session = Session.getDefaultInstance(props, null);
            // -- Get hold of a POP3 message store, and connect to it --
            store = session.getStore("pop3");
            store.connect(popServer, popUser, popPassword);

            // -- Try to get hold of the default folder --
            folder = store.getDefaultFolder();
            if (folder == null) {
                throw new Exception("No default folder");
            }
            // -- ...and its INBOX --
            folder = folder.getFolder("INBOX");
            if (folder == null) {
                throw new Exception("No POP3 INBOX");
            }
            // -- Open the folder for read only --
            if (deleteNewMail == true) {
                folder.open(Folder.READ_WRITE);
            } else {
                folder.open(Folder.READ_ONLY);
            }
            // -- Get the message , cita samo tekst, necita attachmente --
            Message[] msgs = folder.getMessages();

            for (int msgNum = 0; msgNum < msgs.length; msgNum++) {
                if (deleteNewMail == true) {
                    msgs[msgNum].setFlag(Flags.Flag.DELETED, true);
                }
                Address[] adr = msgs[msgNum].getFrom();
                mailMesg.add(adr[0].toString());
                mailMesg.add(msgs[msgNum].getContent());
            }            
        } catch (Exception ex) {
            err = true;
            ex.printStackTrace();
        } finally {
            // -- Close down nicely --
            try {
                if (folder != null) {
                    folder.close(deleteNewMail);
                }
                if (store != null) {
                    store.close();
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            } finally {
                if (err==true) {
                    System.out.println("error: NO MAIL SERVER  pop3:" + popServer + " username:" + popUser + "  ");                    
                    System.out.println("is mail thread alive: " + dsauer_userWS.mailTh.isAlive());
                    dsauer_userWS.mailTh.interrupt();
                    return null;
                }
            }        
        }
        return mailMesg;
    }

    private static void send(String smtpServer, String to, String from, String subject, String body) {
        try {
            Properties props = System.getProperties();
            // -- Attaching to default Session, or we could start a new one --
            props.put("mail.smtp.host", smtpServer);
            Session session = Session.getDefaultInstance(props, null);
            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            // -- We could include CC recipients too --
            // if (cc != null)
            // msg.setRecipients(Message.RecipientType.CC
            // ,InternetAddress.parse(cc, false));
            // -- Set the subject and body text --
            msg.setSubject(subject);
            msg.setText(body);
            // -- Set some other header information --
            msg.setHeader("X-Mailer", "LOTONtechEmail");
            msg.setSentDate(new Date());
            // -- Send the message --
            Transport.send(msg);
            System.out.println("Message sent OK.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
