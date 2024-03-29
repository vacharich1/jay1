package com.pengrad.telegrambot;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * stas
 * 5/2/16.
 */
public class TelegramBotTest {

    TelegramBot bot;
    Integer chatId, forwardMessageId;
    String channelName = "@bottest";
    Long channelId = -1001002720332L;
    String stickerId;
    String imagefile = getClass().getClassLoader().getResource("image.png").getFile();
    String audioFile = getClass().getClassLoader().getResource("beep.mp3").getFile();
    String docFile = getClass().getClassLoader().getResource("doc.txt").getFile();
    String videoFile = getClass().getClassLoader().getResource("tabs.mp4").getFile();

    public TelegramBotTest() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("local.properties"));
        bot = TelegramBotAdapter.buildDebug(properties.getProperty("TEST_TOKEN"));
        chatId = Integer.parseInt(properties.getProperty("CHAT_ID"));
        forwardMessageId = Integer.parseInt(properties.getProperty("FORWARD_MESSAGE"));
        stickerId = properties.getProperty("STICKER_FILE_ID");
    }

    @Test
    public void getMe() {
        bot.execute(new GetMe());
    }

    @Test
    public void kickChatMember() {
        BaseResponse response = bot.execute(new KickChatMember(channelName, chatId));
        System.out.println(response);
    }

    @Test
    public void editMessageText() {
        String text = "Update " + System.currentTimeMillis();
        BaseResponse response = bot.execute(new EditMessageText(chatId, 925, text));
        System.out.println(response);
        bot.execute(new EditMessageText(channelName, 306, text));
        System.out.println(response);
        bot.execute(new EditMessageText("AgAAAJUtAQCj_Q4D87e3E-gkx2A", text));
        System.out.println(response);
    }

    @Test
    public void answerInline() {
        String inlineQueryId = getLastInlineQuery().id();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(new InlineKeyboardButton[]{new InlineKeyboardButton("inline ok").callbackData("callback ok"), new InlineKeyboardButton("inline cancel").callbackData("callback cancel")});
        InlineQueryResult r1 = new InlineQueryResultArticle("1", "title", "message").replyMarkup(keyboardMarkup);
        InlineQueryResult r2 = new InlineQueryResultArticle("2", "2 title", "2 message").replyMarkup(keyboardMarkup);
        bot.execute(new AnswerInlineQuery(inlineQueryId, r1, r2));
    }

    private InlineQuery getLastInlineQuery() {
        GetUpdatesResponse updatesResponse = bot.getUpdates(0, 10, 0);
        List<Update> updates = updatesResponse.updates();
        Collections.reverse(updates);
        for (Update update : updates) {
            if (update.inlineQuery() != null) {
                return update.inlineQuery();
            }
        }
        return null;
    }

    @Test
    public void answerCallback() {
        bot.execute(new AnswerCallbackQuery("220392309269028729").text("answer callback"));
    }
}
