import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class YTKeyboards {
    public SendMessage showKeyboard(User user,Message message) {
        int status=user.getStatus();
        SendMessage ans = new SendMessage();
        KeyboardRow rowback=new KeyboardRow();
        rowback.add("Назад");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(Boolean.TRUE);
        ans.setChatId(message.getChatId());
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        ans.setText("Выберите раздел");
        KeyboardRow row1=new KeyboardRow();
        KeyboardRow row2=new KeyboardRow();
        KeyboardRow row3=new KeyboardRow();
        KeyboardRow row4=new KeyboardRow();
        if (status==0) {
            ans.setText("Нажмите на кнопку \"Поделиться номером телефона\", если хотите указать свой номер. \n\nЛибо нажмите на \"Скрепку\" и отправьте контакт менеджера парка.");
            KeyboardButton keyBoardButton = new KeyboardButton().setText("Поделиться номером телефона").setRequestContact(Boolean.TRUE);
            row1.add(keyBoardButton);
            keyboardRows.add(row1);
        }

        else if (status==1) {
            row1.add("Проблема с платежами");
            row1.add("Проблема с комиссией");
            row2.add("Подтвердить факт оплаты комиссии");
            row3.add("Поменять номер телефона");
            row3.add("Другое");
            keyboardRows.add(row1);
            keyboardRows.add(row2);
            keyboardRows.add(row3);


        }
        else if (status==11) {
            row1.add("Не поступает безналичная оплата");
            row2.add("Не поступают бонусы (долларовые платежи)");
            row3.add("Не поступает оплата по корпоративным заказам");
            keyboardRows.add(row1);
            keyboardRows.add(row2);
            keyboardRows.add(row3);
            keyboardRows.add(rowback);
        }
        else if(status == 12){
            row1.add("Платеж не зачислился на баланс");
            row2.add("Неправильный расчет");
            keyboardRows.add(row1);
            keyboardRows.add(row2);
            keyboardRows.add(rowback);
        }

        else if(status == 121){
            ans.setText("Дата платежа");
            row1.add("Прошло менее 5 дней");
            row1.add("Прошло более 5 дней");
            keyboardRows.add(row1);
            keyboardRows.add(rowback);
        }

        else if (status == 111) {
            ans.setText("Введите номер Агентского договора(безнал) в формате X...X/XX: ");
            keyboardRows.add(rowback);
        }
        else if (status == 112) {
            ans.setText("Введите номер Информационно-рекламного договора  в формате OF-X...X/XX: ");
            keyboardRows.add(rowback);
        }
        else if (status==113) {
            ans.setText("Введите номер Корпоративного договора в формате PAC-ХХХ...");
            keyboardRows.add(rowback);
        }
        else if(status == 122){
            ans.setText("Введите номер основного договора SAAS в формате X...X/XX: ");
            keyboardRows.add(rowback);
        }
        else if (status == 13) {
            ans.setText("Введите город");
            keyboardRows.add(rowback);
        }
        else if (status == 131) {
            ans.setText("Введите название Вашего парка");
            keyboardRows.add(rowback);
        }
        else if (status == 1311) {
            ans.setText("Введите сумму платежа");
            keyboardRows.add(rowback);
        }
        else if (status == 13111) {
            ans.setText("Отправьте фотографию платежки");
            keyboardRows.add(rowback);
        }
        else if(status == 14){
            ans.setText("Введите номер основного договора SAAS в формате X...X/XX: ");
            keyboardRows.add(rowback);
        }
        else if (status == 141) {
            ans.setText("Напишите Ваш вопрос:");
            keyboardRows.add(rowback);
        }
        else if (status==1212) {
            ans.setText("Введите номер основного договора SAAS в формате X...X/XX: ");
            keyboardRows.add(rowback);
        }
        else if (status==12121) {
            ans.setText("Введите номер счета в формате PAT-XXX...-01 ");
            keyboardRows.add(rowback);
        }

        else {
            row1.add("Проблема с платежами");
            row1.add("Проблема с комиссией");
            row2.add("Подтвердить факт оплаты комиссии");
            row3.add("Поменять номер телефона");
            row3.add("Другое");
            keyboardRows.add(row1);
            keyboardRows.add(row2);
            keyboardRows.add(row3);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        ans.setReplyMarkup(replyKeyboardMarkup);
        return ans;
    }
}

