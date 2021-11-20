package commands;

import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.botCommands.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Представляет собой хранилище, в котором каждому названию команды бота
 * поставлена в соответствие команда боту BotCommand.
 */
public class CommandStorage {

    private final BotConfiguration config;
    private final IAnecdoteBot bot;
    private final PrintStream out;

    private final Map<String, BotCommand> commandsByCommandNames;
    private final BotCommand startConversation, notUnderstand;

    public CommandStorage(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        this.bot = bot;
        this.config = config;
        this.out = out;

        this.commandsByCommandNames = new HashMap<>();
        fillCommandsByNames();

        startConversation = new StartConversationCommand(this.bot, this.config, this.out);
        notUnderstand = new NotUnderstandCommand(this.bot, this.config, this.out);
    }

    /**
     * Возвращает команду боту - начать диалог.
     * @return Команда боту - начать диалог.
     */
    public BotCommand getStartConversationCommand() {
        return startConversation;
    }

    /**
     * Возвращает команду боту - сообщить пользователю, что бот его не понял.
     * @return Команда боту - сообщить пользователю, что бот его не понял.
     */
    public BotCommand getNotUnderstandCommand() {
        return notUnderstand;
    }

    /**
     * Возвращает команду боту BotCommand по указанному названию.
     * @param commandName название команды бота, по которой будет найдена команда BotCommand.
     * @return Команда бота BotCommand.
     * Если такой команды не было найдено, возвращает null.
     */
    public BotCommand getCommandOrNull(String commandName) {
        if (commandName == null) return null;
        return commandsByCommandNames.getOrDefault(commandName, null);
    }

    /**
     * Заполняет hash map названиями команд бота, каждой из которых
     * ставится в соответствие команда боту BotCommand.
     */
    private void fillCommandsByNames() {
        commandsByCommandNames.put("onWhatCanYouDo", new WhatCanYouDoCommand(bot, config, out));
        commandsByCommandNames.put("introduce", new IntroduceCommand(bot, config, out));
        commandsByCommandNames.put("greet", new GreetCommand(bot, config, out));
        commandsByCommandNames.put("onHowAreYou", new HowAreYouCommand(bot, config, out));
        commandsByCommandNames.put("getNextAnecdote", new TellAnecdoteCommand(bot, config, out));
        commandsByCommandNames.put("onUserLaughed", new OnLaughCommand(bot, config, out));
        commandsByCommandNames.put("showFavorites", new ShowFavoritesCommand(bot, config, out));
        commandsByCommandNames.put("onRatingSubmitted_Like", new OnLikeCommand(bot, config, out));
        commandsByCommandNames.put("onRatingSubmitted_Dislike", new OnDislikeCommand(bot, config, out));
        commandsByCommandNames.put("onRatingSubmitted", new OnUserRatingCommand(bot, config, out));
        commandsByCommandNames.put("showAnecdotesOfRating", new ShowAnecdotesCommand(bot, config, out));
        commandsByCommandNames.put("deactivate", new StopCommand(bot, config, out));
    }

}