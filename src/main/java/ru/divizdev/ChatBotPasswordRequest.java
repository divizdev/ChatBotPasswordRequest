package ru.divizdev;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatBotPasswordRequest extends TelegramLongPollingBot {


    private static final String COMMAND_START = "/start";
    private static final String COMMAND_USERS_LIST = "Мне нужна твоя одежда";
    private HashMap<Integer, HashMap<String, String>> _passwords = new HashMap<>();
    private RandomPassword rndPassword = new RandomPassword();

    private  IBotToken _botToken;

    public ChatBotPasswordRequest(IBotToken botToken){
        _botToken = botToken;
    }



    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            SendMessage message = null;
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().hasText()) {
                String textMessage = update.getMessage().getText();
                switch (textMessage) {
                    case COMMAND_START:
                        message = getMessageCommandStart(chatId);
                        break;
                    case COMMAND_USERS_LIST:
                        message = getMessageUsersList(chatId);
                        break;
                    default:
                        message = getMessageNullCommand(chatId);
                        break;
                }
            } else {
                Contact contact = update.getMessage().getContact();
                if (contact != null && update.getMessage().getFrom().getId().equals(contact.getUserID())) {
                    message = getMessageAnswerContact(chatId);
                }
            }
            if (message != null) {
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }  else if (update.hasCallbackQuery()) {


        }
    }


    private SendMessage getMessageAnswerContact(long chatId) {
        SendMessage message;
        message = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText("Спасибо мы вас запомнили").setReplyMarkup(new ReplyKeyboardRemove());
        return message;
    }

    private SendMessage getMessageNullCommand(long chatId) {
        SendMessage message;
        message = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText("Для получения пароля введите команду /start");
        return message;
    }

    private SendMessage getMessageUsersList(long chatId) {
        SendMessage message;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Пользователи: ");
        stringBuilder.append("\n");
        for (Map.Entry<Integer, HashMap<String, String>> user : _passwords.entrySet()) {
            stringBuilder.append(user.getKey().toString());
            stringBuilder.append("\n");
        }

        message = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .enableHtml(true)
                .setText(stringBuilder.toString());
        return message;
    }

    private SendMessage getMessageCommandStart(long chatId) {
        SendMessage message;
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();

        KeyboardButton keyboardButton = new KeyboardButton("Отправить номер телефона");
        keyboardButton.setRequestContact(true);


        keyboardButtons.add(keyboardButton);
        keyboardRows.add(keyboardButtons);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        replyKeyboardMarkup.setResizeKeyboard(true);


        message = new SendMessage()
                .setChatId(chatId)
                .setText("Для авторизации нужен Ваш телефон").setReplyMarkup(replyKeyboardMarkup);
        return message;
    }

    @Override
    public String getBotUsername() {
        return "ChatBotPasswordRequest";
    }

    @Override
    public String getBotToken() {
        return _botToken.getToken();
    }
}
