package ru.divizdev;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

public class ChatBotPasswordRequest extends TelegramLongPollingBot {

    static final String KEY_G38 = "g38";
    static final String KEY_G70 = "g70";
    private HashMap<Integer, HashMap<String, String>> _passwords = new HashMap<>();
    private RandomPassword rndPassword = new RandomPassword();

    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            SendMessage message;
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {


                message = new SendMessage()
                        .setChatId(chatId)
                        .setText("Для какой системы вам нужен пароль?");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("СУРВ GUI (G38)").setCallbackData(KEY_G38));
                rowInline.add(new InlineKeyboardButton().setText("СУРВ Fiori (G70)").setCallbackData(KEY_G70));

                rowsInline.add(rowInline);

                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);

            } else {
                if (update.getMessage().getText().equals("Мне нужна твоя одежда")){
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
                }else {
                    message = new SendMessage() // Create a message object object
                            .setChatId(chatId)
                            .setText("Для получения пароля введите команду /start");
                }
            }

            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.hasCallbackQuery()) {
            // Set variables
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer userId = update.getCallbackQuery().getFrom().getId();
            String password = "";

            HashMap<String, String> systemKey = _passwords.get(userId);
            String typeKey = "";

            if (callData.equals(KEY_G38)) {
                typeKey = KEY_G38;

            } else if (callData.equals(KEY_G70)) {
                typeKey = KEY_G70;
            }

            if (!typeKey.isEmpty()) {

                if (systemKey != null) {
                    password = systemKey.get(typeKey);
                }
                if (password == null || password.isEmpty()) {
                    password = rndPassword.nextString();
                    if (systemKey == null) {
                        systemKey = new HashMap<>();
                    }
                    systemKey.put(typeKey, password);
                    _passwords.put(userId, systemKey);
                }
            }


            if (!password.isEmpty()) {
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chatId)
                        .setMessageId(toIntExact(messageId))
                        .setText(password);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public String getBotUsername() {
        return "ChatBotPasswordRequest";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
