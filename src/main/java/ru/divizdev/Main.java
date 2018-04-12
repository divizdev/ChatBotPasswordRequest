package ru.divizdev;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();
        System.out.print("Start");

        TelegramBotsApi botsApi = new TelegramBotsApi();



        try {
            botsApi.registerBot(new ChatBotPasswordRequest(new BotTokenConstant(), new PasswordStoreHardcode()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

