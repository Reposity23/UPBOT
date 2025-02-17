package com.pricebot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class PriceGuessBot extends TelegramLongPollingBot {
    private Set<Long> loggedInUsers = new HashSet<>();
    private static final String MINI_APP_URL = "https://t.me/guess_price_coin_bot/guess_the_price";

    @Override
    public String getBotUsername() {
        return "guess_price_coin_bot";
    }

    @Override
    public String getBotToken() {
        return "7092102400:AAHAfK2TPubNLZc2Sd3pDQdyYwCiDX6bHpM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                loggedInUsers.add(chatId);
                sendMainMenu(chatId, update.getMessage().getFrom().getFirstName());
            }
        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callData) {
                case "play":
                    sendPlayMessage(chatId);
                    break;
                case "author":
                    sendAuthorInfo(chatId);
                    break;
                case "logout":
                    handleLogout(chatId);
                    break;
                case "back":
                    sendMainMenu(chatId, update.getCallbackQuery().getFrom().getFirstName());
                    break;
                case "login":
                    handleLogin(chatId, update.getCallbackQuery().getFrom().getFirstName());
                    break;
            }
        }
    }

    private void sendMainMenu(long chatId, String username) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("HI! " + username + "\n" +
                "Welcome to game xyz,\n" +
                "This game project was built using Java and Telegram Bot API\n" +
                "Using algorithms like Bubble Sort and Heap Sort\n" +
                "Try out my commands!");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // First row
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton playButton = createInlineButton("Play", "play");
        playButton.setUrl(MINI_APP_URL);  // Set the URL for the play button
        row1.add(playButton);
        row1.add(createInlineButton("Author", "author"));
        rowsInline.add(row1);

        // Second row
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(createInlineButton("Need help? Open a ticket", "help"));
        rowsInline.add(row2);

        // Third row
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(createInlineButton("Log out", "logout"));
        rowsInline.add(row3);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardButton createInlineButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private void sendPlayMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Opening mini app...");
        addBackButton(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAuthorInfo(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("The author of this game is: John Doe");
        addBackButton(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout(long chatId) {
        loggedInUsers.remove(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("You are successfully logged out");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(createInlineButton("Start", "login"));
        rowsInline.add(row1);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleLogin(long chatId, String username) {
        loggedInUsers.add(chatId);
        sendMainMenu(chatId, username);
    }

    private void addBackButton(SendMessage message) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(createInlineButton("<- Back", "back"));
        rowsInline.add(row);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
    }

    // Bubble Sort Algorithm
    public void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Heap Sort Algorithm
    public void heapSort(int[] arr) {
        int n = arr.length;

        // Build heap
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
    }

    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest])
            largest = left;

        if (right < n && arr[right] > arr[largest])
            largest = right;

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }
}