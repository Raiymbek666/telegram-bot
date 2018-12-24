import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class YTMail implements Runnable {
    private String mail="*****";  //Введите имейл с какого отправлять
    private String recmail="******"; //Введите имейл на который отправлять
    private String emailServerPort="465";
    private StringBuilder emailBody=new StringBuilder();
    private String emailSubject;
    private String botToken="******"; //Введите токен бота
    private String emailReseiver;
    private String file_path;
    private String user_id;
    private String city;
    private String park_name;
    private String amount;

    public void prepareToSendAttachment (String emailReseiver, String file_path, String user_id, String city, String park_name, String amount) {
        this.emailReseiver=emailReseiver;
        this.file_path=file_path;
        this.user_id=user_id;
        this.city=city;
        this.park_name=park_name;
        this.amount=amount;
    }
    public void sendMail(String mailReceiver) {
        Properties props=new Properties();
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", emailServerPort);
        props.put("mail.smtp.socketFactory.port", emailServerPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        try {
            Authenticator auth=new SMTPAuthenticator();
            Session session= Session.getDefaultInstance(props,auth);
            Message msg=new MimeMessage(session);
            msg.setText(emailBody.toString());
            msg.setSubject(emailSubject);
            msg.setFrom(new InternetAddress(mail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recmail));
            Transport.send(msg);
            System.out.println("Otpravil");
        }
        catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        }


    }


    public void setEmailBody(String text){
        this.emailBody.append(text);
    }
    public void setEmailSubject(String mailSubject){
        this.emailSubject=mailSubject;
    }

    @Override
    public void run() {

        Properties props=new Properties();
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", emailServerPort);
        props.put("mail.smtp.socketFactory.port", emailServerPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        try {
            Authenticator auth=new SMTPAuthenticator();
            Session session= Session.getDefaultInstance(props,auth);
            Message msg=new MimeMessage(session);
            msg.setSubject(emailSubject);
            msg.setFrom(new InternetAddress(mail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recmail));
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText("Город: "+city+"\nПарк: "+park_name+"\nСумма: "+amount);

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();

            String finalURL="https://api.telegram.org/file/bot"+botToken+"/"+file_path;
            String filename = "/Users/raiymbek/NetBeansProjects/YTbot/src/ytbot/files/image"+user_id+".jpg"; //изменить на правильное место хранения
            try(InputStream in = new URL(finalURL).openStream()){
                Files.copy(in, Paths.get(filename));

            } catch (IOException e) {

            }
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("image"+user_id);
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);
            File fileToDelete = new File(filename);

            Transport.send(msg);

            if(fileToDelete.delete()){

            } else {

            }

        }
        catch (MessagingException ex) {

            ex.printStackTrace();
        }

    }
    private static class SMTPAuthenticator extends Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("*****", "*****"); //Введите логин и пароль от имейла для отправки сообщеший
        }
    }
}


