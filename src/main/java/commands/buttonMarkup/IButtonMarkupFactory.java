package commands.buttonMarkup;

import commands.CommandResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface IButtonMarkupFactory {

    InlineKeyboardMarkup build(CommandResult result);

}