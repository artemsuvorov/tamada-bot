package commands.buttonMarkup;

import commands.CommandResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonMarkupFactory implements IButtonMarkupFactory {

    protected final List<List<InlineKeyboardButton>> buttons;

    public ButtonMarkupFactory() {
        this(new ArrayList<>());
    }

    private ButtonMarkupFactory(List<List<InlineKeyboardButton>> buttons) {
        this.buttons = buttons;
    }

    public InlineKeyboardMarkup build(CommandResult result) {
        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    public ButtonMarkupFactory addButtonRow(String... buttonTitles) {
        InlineButton[] buttons = Arrays.stream(buttonTitles)
                .map(buttonTitle -> new InlineButton(buttonTitle, buttonTitle))
                .toArray(InlineButton[]::new);
        return addButtonRow(buttons);
    }

    public ButtonMarkupFactory addButtonRow(InlineButton... buttons) {
        List<InlineKeyboardButton> singleRowButtons = new ArrayList<>(buttons.length);
        for (InlineButton button : buttons)
            singleRowButtons.add(buildInlineButton(button));
        this.buttons.add(singleRowButtons);
        return new ButtonMarkupFactory(this.buttons);
    }

    protected InlineKeyboardButton buildInlineButton(InlineButton button) {
        return InlineKeyboardButton.builder()
                .text(button.Title)
                .callbackData(button.CallbackData)
                .build();
    }

}