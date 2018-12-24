import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String mail2send="*****"; //Изменить на email получателя
        Message mainMessage = update.getMessage();
        User user = new User();
        user.setId_user(mainMessage.getChat().getId());
        SendMessage sendMessage = new SendMessage().setText("Спасибо! Ваш запрос был принят. С Вами свяжутся в ближайшее время.");
        sendMessage.setChatId(mainMessage.getChatId());
        YTMail ytm1=new YTMail();
        YTKeyboards keyboard = new YTKeyboards();

        try {
            YTDatabase database = new YTDatabase("***", "***"); //Изменить на логин и пароль базы

            ResultSet rs = database.getResultSet(user);
            if (rs.next()) {
                String status = rs.getString("Status");
                user.setStatus(status);
                user.setUser_name(mainMessage.getFrom().getUserName());

                if (status.equals("0") && mainMessage.hasContact()){
                    user.setStatus(1);
                    user.setPhone_number(mainMessage.getContact().getPhoneNumber());
                    database.upds(user);
                    database.updp(user);
                    database.setUserName(user);
                    execute(keyboard.showKeyboard(user, mainMessage));
                }
                else if (status.equals("0")) {
                    execute(keyboard.showKeyboard(user, mainMessage));
                }
                else if (status.equals("11")) {

                    switch (mainMessage.getText().toLowerCase()) {
                        case "не поступает безналичная оплата":
                            user.setStatus(111);
                            database.upds(user);
                            break;
                        case "не поступают бонусы (долларовые платежи)":
                            user.setStatus(112);
                            database.upds(user);
                            break;
                        case "не поступает оплата по корпоративным заказам":
                            user.setStatus(113);
                            database.upds(user);
                            break;
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            break;
                    }
                    execute(keyboard.showKeyboard(user, mainMessage));
                }
                else if (status.equals("111")){
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:
                            ResultSet rs2=database.pdAgent_agreement(mainMessage.getText());
                            String clid = "";
                            String city = "";
                            String park = "";
                            String agent_agreement = "";

                            while (rs2.next()){
                                clid=rs2.getString("clid");
                                agent_agreement=rs2.getString("agent_agreement");
                                park=rs2.getString("park_name");
                                city=rs2.getString("city");

                            }

                            if (!clid.equals("")){
                                ytm1.setEmailSubject("["+park+"] ["+city+"] Не поступает безналичная оплата");
                                ytm1.setEmailBody("Clid: "+clid+"\nPark: "+park+"\nCity: "+city+"\nAgent Agreement: "+agent_agreement+"\nPhoneNumber: "+database.getPhoneNumber(user)+"\nTelegramContact: "+mainMessage.getFrom().getUserName());
                                ytm1.sendMail(mail2send);
                                user.setStatus(1);
                                database.upds(user);
                                execute(sendMessage);
                                execute(keyboard.showKeyboard(user, mainMessage));
                            }
                            else {
                                sendMessage.setText("Неправильный номер договора. Введите еще раз: ");
                                execute(sendMessage);
                            }
                            break;
                    }
                } else if (status.equals("112")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:
                            ResultSet rs2=database.pdInfo_adv(mainMessage.getText());
                            String clid = "";
                            String city = "";
                            String park = "";
                            String info_adv = "";
                            while (rs2.next()){
                                clid=rs2.getString("clid");
                                info_adv=rs2.getString("info_adv");
                                park=rs2.getString("park_name");
                                city=rs2.getString("city");
                            }

                            if (!clid.equals("")){
                                ytm1.setEmailSubject("["+park+"] ["+city+"] Не приходят субсидии");
                                ytm1.setEmailBody("Clid: "+clid+"\nPark: "+park+"\nCity: "+city+"\nInfo_adv: "+info_adv+"\nPhoneNumber: "+database.getPhoneNumber(user)+"\nTelegramContact: "+mainMessage.getFrom().getUserName());
                                ytm1.sendMail(mail2send);
                                user.setStatus(1);
                                database.upds(user);
                                execute(sendMessage);
                                execute(keyboard.showKeyboard(user, mainMessage));
                            }

                            else {
                                sendMessage.setText("Неправильный номер договора. Введите еще раз: ");
                                execute(sendMessage);
                            }
                            break;
                    }
                }
                else if (status.equals("113")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:
                            ResultSet rs2=database.pdB2b(mainMessage.getText());
                            String clid = "";
                            String city = "";
                            String park = "";
                            String b2b = "";
                            while (rs2.next()){
                                clid=rs2.getString("clid");
                                b2b=rs2.getString("b2b");
                                park=rs2.getString("park_name");
                                city=rs2.getString("city");
                            }

                            if (!clid.equals("")){
                                ytm1.setEmailSubject("["+park+"] ["+city+"] Не поступает оплата по корпоративным заказам");
                                ytm1.setEmailBody("Clid: "+clid+"\nPark: "+park+"\nCity: "+city+"\nB2B: "+b2b+"\nPhoneNumber: "+database.getPhoneNumber(user)+"\nTelegramContact: "+mainMessage.getFrom().getUserName());
                                ytm1.sendMail(mail2send);
                                user.setStatus(1);
                                database.upds(user);
                                execute(sendMessage);
                                execute(keyboard.showKeyboard(user, mainMessage));
                            }

                            else {
                                sendMessage.setText("Неправильный номер договора. Введите еще раз: ");
                                execute(sendMessage);
                            }
                            break;
                    }
                }



                else if (status.equals("12")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "платеж не зачислился на баланс":
                            user.setStatus(121);
                            database.upds(user);
                            break;
                        case "неправильный расчет":
                            user.setStatus(122);
                            database.upds(user);
                            break;
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            break;
                    }
                    execute(keyboard.showKeyboard(user, mainMessage));


                } else if (status.equals("121")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            break;
                        case "прошло менее 5 дней":
                            sendMessage.setText("Пожалуйста, подождите минимум 5 рабочих дней. "
                                    + "Если платеж не зачислится на баланс парка в течение "
                                    + "указанного периода, отправьте обращение повторно, выбрав "
                                    + "раздел \"Прошло более 5 дней\". "
                                    + "Спасибо"
                            );
                            user.setStatus(1);
                            database.upds(user);
                            execute(sendMessage);
                            break;
                        case "прошло более 5 дней":
                            user.setStatus(1212);
                            database.upds(user);
                            break;
                    }
                    execute(keyboard.showKeyboard(user, mainMessage));
                } else if (status.equals("1212")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            database.deleteIMPD(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:
                            try{
                                user.setStatus(12121);
                                database.upds(user);
                                database.setPDSaas(user, mainMessage.getText());
                            } catch(SQLException ex){
                                execute(sendMessage.setText("Неправильный номер договора. Введите еще раз: "));
                            }

                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                    }
                } else if (status.equals("12121")) {

                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            database.deleteIMPD(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:
                            ResultSet rs2=database.pdSaas2(user);
                            String clid = "";
                            String city = "";
                            String park = "";
                            String saas = "";
                            String pat=mainMessage.getText();

                            while (rs2.next()){
                                clid=rs2.getString("clid");
                                saas=rs2.getString("saas");
                                park=rs2.getString("park_name");
                                city=rs2.getString("city");


                            }

                            if (!clid.equals("")){
                                ytm1.setEmailSubject("["+park+"] ["+city+"] Проблема с комиссией - Деньги не сели");
                                ytm1.setEmailBody("Clid: "+clid+"\nPark: "+park+"\nCity: "+city+"\nPat: "+ pat+"\nSaas: "+saas+"\nPhoneNumber: "+database.getPhoneNumber(user)+"\nTelegramContact: "+mainMessage.getFrom().getUserName());
                                ytm1.sendMail(mail2send);
                                user.setStatus(1);
                                database.upds(user);
                                database.deleteIMPD(user);
                                execute(sendMessage);
                                execute(keyboard.showKeyboard(user, mainMessage));
                            }

                            else {
                                sendMessage.setText("Неправильный номер договора. Введите еще раз: ");
                                execute(sendMessage);
                            }

                    }

                } else if (status.equals("122")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:

                            ResultSet rs2=database.pdSaas(mainMessage.getText());
                            String clid = "";
                            String city = "";
                            String park = "";
                            String saas = "";

                            while (rs2.next()){
                                clid=rs2.getString("clid");
                                saas=rs2.getString("saas");
                                park=rs2.getString("park_name");
                                city=rs2.getString("city");

                            }

                            if (!clid.equals("")){
                                ytm1.setEmailSubject("["+park+"] ["+city+"] Проблема с комиссией - Неправильный рассчет");
                                ytm1.setEmailBody("Clid: "+clid+"\nPark: "+park+"\nCity: "+city+"\nSaas: "+saas+"\nPhoneNumber: "+database.getPhoneNumber(user)+"\nTelegramContact: "+mainMessage.getFrom().getUserName());
                                ytm1.sendMail(mail2send);
                                user.setStatus(1);
                                database.upds(user);
                                execute(sendMessage);
                                execute(keyboard.showKeyboard(user, mainMessage));
                            }

                            else {
                                sendMessage.setText("Неправильный номер договора. Введите еще раз: ");
                                execute(sendMessage);
                            }
                            break;
                    }
                }

                else if (status.equals("13")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":

                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:
                            database.createUser(user.getId_user());
                            database.setCity(user.getId_user(), mainMessage.getText());
                            user.setStatus(131);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                    }
                } else if (status.equals("131")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            database.deleteUserEwA(user.getId_user());
                            break;
                        default:
                            database.setPark(user.getId_user(), mainMessage.getText());
                            user.setStatus(1311);
                            database.upds(user);
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;

                    }
                } else if (status.equals("1311")) {
                    if (mainMessage.hasText()){
                        switch (mainMessage.getText().toLowerCase()){
                            case "назад":
                                int str = (user.getStatus() / 10);
                                user.setStatus(str);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            default:
                                try {
                                    int amount = Integer.valueOf(mainMessage.getText());
                                    database.setAmount(user.getId_user(), amount);
                                    user.setStatus(13111);
                                    database.upds(user);
                                    execute(keyboard.showKeyboard(user, mainMessage));
                                    break;
                                }
                                catch (NumberFormatException ex) {
                                    sendMessage.setText("Введите сумму в цифрах: ");
                                    execute(sendMessage);
                                    break;
                                }

                        }
                    }
                } else if (status.equals("13111")) {
                    if (mainMessage.hasPhoto()){
                        List<PhotoSize> photos = update.getMessage().getPhoto();
                        String f_id = photos.stream()
                                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                                .findFirst()
                                .orElse(null).getFileId();
                        ParserFilePath pfp=new ParserFilePath(getBotToken(), f_id);
                        String url=pfp.getfile_path();
                        ResultSet rsa = database.getAttachmentResult(user);
                        String city = "";
                        String park_name = "";
                        String amount = "";
                        while(rsa.next()){
                            city=rsa.getString("city");
                            park_name=rsa.getString("park_name");
                            amount=rsa.getString("amount");
                        }
                        ytm1.setEmailSubject("["+park_name+"] ["+city+"]" + "["+amount+"]");
                        ytm1.prepareToSendAttachment(mail2send, url, String.valueOf(user.getId_user()), city, park_name, amount);
                        Thread thr=new Thread(ytm1);
                        thr.start();
                        user.setStatus(1);
                        database.upds(user);
                        execute(sendMessage);
                        database.deleteUserEwA(user.getId_user());
                        execute(keyboard.showKeyboard(user, mainMessage));
                    }
                    else if (mainMessage.hasText()){
                        switch (mainMessage.getText().toLowerCase()){
                            case "назад":
                                int str = (user.getStatus() / 10);
                                user.setStatus(str);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            default:
                                user.setStatus(13111);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                        }
                    }
                    else if (mainMessage.hasDocument()) {
                        Document doc  = mainMessage.getDocument();
                        String f_id = doc.getFileId();

                        user.setStatus(1);
                        database.upds(user);
                        execute(sendMessage);
                        execute(keyboard.showKeyboard(user, mainMessage));
                        ParserFilePath pfp=new ParserFilePath(getBotToken(), f_id);
                        String url=pfp.getfile_path();

                        ytm1=new YTMail();
                        ResultSet rsa = database.getAttachmentResult(user);
                        String city = "";
                        String park_name = "";
                        String amount = "";

                        while(rsa.next()){
                            city=rsa.getString("city");
                            park_name=rsa.getString("park_name");
                            amount=rsa.getString("amount");
                        }
                        ytm1.setEmailSubject("["+park_name+"] ["+city+"]" + "["+amount+"]");
                        ytm1.prepareToSendAttachment("mail2send", url, String.valueOf(user.getId_user()), city, park_name, amount);
                        Thread thr=new Thread(ytm1);
                        thr.start();
                        database.deleteUserEwA(user.getId_user());

                    }
                    else {
                        execute(sendMessage.setText("Отправьте пожалуйста фотографию платежки"));
                    }
                }
                else if (status.equals("14")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            System.out.println(user.getStatus());
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            break;
                        default :
                            rs=database.pdSaas(mainMessage.getText());
                            if (rs.next()){
                                database.setSaasEwA(user, mainMessage.getText());
                                user.setStatus(141);
                                database.upds(user);
                            }
                            else {
                                sendMessage.setText("Неправильный номер договора. Введите еще раз: ");
                                execute(sendMessage);
                            }
                            break;
                    }
                    execute(keyboard.showKeyboard(user, mainMessage));
                }
                else if (status.equals("141")) {
                    switch (mainMessage.getText().toLowerCase()) {
                        case "назад":
                            System.out.println(user.getStatus());
                            int str = (user.getStatus() / 10);
                            user.setStatus(str);
                            database.upds(user);
                            database.deleteUserEwA(user.getId_user());
                            execute(keyboard.showKeyboard(user, mainMessage));
                            break;
                        default:
                            System.out.println(mainMessage.getText());
                            user.setStatus(1);
                            database.upds(user);
                            rs=database.getAttachmentResult(user);
                            String saas="";
                            if (rs.next()){
                                saas=rs.getString("saas");
                            }
                            ResultSet rs2=database.pdSaas(saas);
                            if (rs2.next()) {
                                String park_name=rs2.getString("park_name");
                                String city=rs2.getString("city");
                                ytm1 = new YTMail();
                                ytm1.setEmailBody(mainMessage.getText());
                                ytm1.setEmailSubject("["+ park_name+"]"+"["+city+"] Другое");
                                ytm1.sendMail(mail2send);
                                SendMessage sm = new SendMessage();
                                sm.setChatId(mainMessage.getChatId());
                                sm.setText("Спасибо! Ваш запрос был принят. С Вами свяжутся в ближайшее время.");
                                database.deleteUserEwA(user.getId_user());
                                execute(sm);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            }

                    }
                }

                else  {
                    user.setStatus(Integer.valueOf(rs.getString("Status")));
                    if(mainMessage.hasText()){
                        switch (mainMessage.getText().toLowerCase()) {
                            case "проблема с платежами":
                                user.setStatus(11);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            case "проблема с комиссией":
                                user.setStatus(12);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            case "подтвердить факт оплаты комиссии":
                                user.setStatus(13);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            case "другое":
                                user.setStatus(14);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            case "поменять номер телефона":
                                user.setStatus(0);
                                database.upds(user);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                            default:
                                user.setStatus(1);
                                execute(keyboard.showKeyboard(user, mainMessage));
                                break;
                        }

                    }
                    else {
                        execute(keyboard.showKeyboard(user, mainMessage));
                    }

                }
            } else {
                SendMessage declineSendMessage = new SendMessage();
                declineSendMessage.setChatId(mainMessage.getChatId());
                declineSendMessage.setText("Вам отказано в доступе");
                execute(declineSendMessage);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TelegramApiException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String getBotUsername() {
        return "*****"; //Введите имя Бота
    }

    @Override
    public String getBotToken() {
        return "******";  //Введите токен бота
    }


    public void sendMessage(String ans, Message message) {
        SendMessage message1 = new SendMessage();
        message1.setChatId(message.getChatId());
        System.out.println(message1.getChatId());
        message1.setText(ans);
        try {
            execute(message1);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(Message message) throws IOException, TelegramApiException {
        List<PhotoSize> photos = message.getPhoto();

        String f_id = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getFileId();
        ParserFilePath parserFilePath = new ParserFilePath(getBotToken(), f_id);
        String file_path = parserFilePath.getfile_path();
        YTMail ytm1 = new YTMail();
        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId());
        sm.setText("Спасибо! Ваш файл был принят");
        execute(sm);
    }
}
