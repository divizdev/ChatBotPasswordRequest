package ru.divizdev;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();
        System.out.print("Start");
        // TODO Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();


        // TODO Register our bot

        try {
            botsApi.registerBot(new ChatBotPasswordRequest());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

