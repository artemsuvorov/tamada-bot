package commands.buttonMarkup;

import commands.CommandResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DynamicButtonMarkupFactory extends ButtonMarkupFactory implements IButtonMarkupFactory {

    private final Map<Predicate<CommandResult>, ButtonMarkupFactory> markupsByConditions;

    public DynamicButtonMarkupFactory() {
        this(new HashMap<>());
    }

    private DynamicButtonMarkupFactory(Map<Predicate<CommandResult>, ButtonMarkupFactory> markupsByConditions) {
        this.markupsByConditions = markupsByConditions;
    }

    @Override
    public DynamicButtonMarkupFactory addButtonRow(String... buttonTitles) {
        return addButtonsOnCondition(result -> true, super.addButtonRow(buttonTitles));
    }

    public DynamicButtonMarkupFactory addButtonsOnCondition(Predicate<CommandResult> predicate, ButtonMarkupFactory markup) {
        markupsByConditions.put(predicate, markup);
        return new DynamicButtonMarkupFactory(markupsByConditions);
    }

    public InlineKeyboardMarkup build(CommandResult result) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (var entry : markupsByConditions.entrySet())
            if (entry.getKey().test(result))
                buttons.addAll(entry.getValue().buttons);
        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

}