package com.company;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class GameBot extends TelegramLongPollingBot {

    private static final Random random = new Random();
    private static final Map< Long, Game1> gameMap1 = new HashMap<>();
    private static final Map< Long, Game2 > gameMap2 = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {


        if ( update.hasMessage() ){

            Message message = update.getMessage();

            if ( message.hasText() ){

                Long chatId = message.getChatId();
                User user = message.getFrom();
                System.out.println(message.getText());

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId( chatId );

                String text = message.getText();

                if ( text.equals( "/start" ) ){
                    sendMessage.setText( "Welcome " + user.getFirstName() + "\nPlay game -> /game" );
                    send( sendMessage );
                }else if ( text.equals( "/game" ) ){
                    sendMessage.setText( "O'yin qoidasi siz 0-100 gacha son o'ylaysiz men o'ylagan sonizni topaman! \n Tayyor bo'lsang boshlaymiz. /ready");
                    send( sendMessage );
                }else if ( text.equals( "/ready" ) || text.equals( "t" ) || text.equals( "+" )  || text.equals( "-" ) ){
                    game1Method( message );
                }else {
                    game2Method( message );
                }
            }
        }
    }


    private void game1Method(Message message) {

        Long chatId = message.getChatId();
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        Game1 game1 = gameMap1.get(chatId);


        if ( text.equals("/ready") ){
            game1 = new Game1();
            game1.setN( random.nextInt(game1.getLow(), game1.getHigh() + 1) );
            gameMap1.put( chatId, game1 );
            sendMessage.setText( game1.getN() + "\n\nAgar to'g'ri bo'lsa (t), o'ylagan soniz katta bo'lsa (+), kichik bo'lsa (-)" );
        } else if ( game1 != null ) {
            game1.setAttampts(game1.getAttampts() + 1 );
            if ( text.equals( "t" ) ){
                sendMessage.setText( "Men siz o'ylagan sonni " + game1.getAttampts() + " urinishda topdim. \n\nEndi men son o'ylayman siz topasiz \n\n Tayyor bo'lsayiz boshlaymiz /go" );
                gameMap1.remove( chatId );
            }else if ( text.equals( "+" ) ){
                game1.setLow(game1.getN() + 1 );
                game1.setN( random.nextInt(game1.getLow(), game1.getHigh()) + 1 );
                sendMessage.setText( game1.getN() + "\n\nAgar to'g'ri bo'lsa (t), o'ylagan soniz katta bo'lsa (+), kichik bo'lsa (-)" );
            }else if ( text.equals( "-" ) ){
                game1.setHigh( game1.getN() - 1 );
                game1.setN( random.nextInt(game1.getLow(), game1.getHigh()) + 1);
                sendMessage.setText( game1.getN() + "\n\nAgar to'g'ri bo'lsa (t), o'ylagan soniz katta bo'lsa (+), kichik bo'lsa (-)" );
            }else {
                sendMessage.setText( "\n\nAgar to'g'ri bo'lsa (t), o'ylagan soniz katta bo'lsa (+), kichik bo'lsa (-)" );
            }
        }
        send( sendMessage );
    }


    private void game2Method(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        Game2 game2 = gameMap2.get(chatId);
        Game1 game1 = gameMap1.get(chatId);

        if ( text.equals( "/go" ) ){
            game2 = new Game2();
            game2.setTarget( random.nextInt( 0, 101 ) );
            gameMap2.put( chatId, game2 );
            sendMessage.setText( "Son kiriting" );

        }else if ( game2 != null  ){
            try {
                game2.setAttampts(game2.getAttampts() + 1 );
                int target = game2.getTarget();
                int inputNumber = Integer.parseInt(text);

                if ( target > inputNumber ){
                    sendMessage.setText( "Men o'ylagan son " + inputNumber + " kattaroq" );
                } else if ( target < inputNumber ){
                    sendMessage.setText( "Men o'ylagan son " + inputNumber + " kichikroq" );
                } else {
                    sendMessage.setText( "Siz men o'ylagan sonni " + game2.getAttampts() + " urinishda topdingiz!" );
                    send(sendMessage); gameMap2.remove( chatId );
                    if ( game2.getAttampts() > game1.getAttampts() ){
                        sendMessage.setText( "O'yinda men siz o'ylagan sonni " + game2.getAttampts() + " urinish topib yutdim 🎉🎉🎉 " );
                    }else if ( game1.getAttampts() > game2.getAttampts() ){
                        sendMessage.setText( "Siz men o'ylagan sonni " + game2.getAttampts() + " urinishda topib g'olib bo'ldingiz 🎉🎉🎉" );
                    }else {
                        sendMessage.setText( "O'yin durrang tarzda yakunlandi" );
                    }
                }
            }catch ( NumberFormatException e ){
                sendMessage.setText( "Iltimos faqat son kiriting!" );
            }
        }
        send(sendMessage);
    }

    public void send( SendMessage sendMessage ){
        try {
            execute( sendMessage );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getBotUsername() {
        return "find_the_number_bot";
    }
    @Override
    public String getBotToken() {
        return "8704262473:AAHUOSkjU-p2skLimv5FXUQt8F6DZHFRBMI";
    }
}
