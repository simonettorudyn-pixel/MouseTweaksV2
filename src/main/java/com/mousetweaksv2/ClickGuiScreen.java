package com.mousetweaksv2;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.ArrayList;

public class ClickGuiScreen extends Screen {
    private TextFieldWidget allyInputField;
    private final ArrayList<ButtonWidget> slotButtons = new ArrayList<>();

    public ClickGuiScreen() {
        super(Text.literal("Mouse Tweaks V2 - Menu"));
    }

    @Override
    protected void init() {
        // Campo di testo per inserire il nome dell'alleato
        this.allyInputField = new TextFieldWidget(this.textRenderer, 10, 30, 110, 20, Text.literal("Nome Alleato"));
        this.addSelectableChild(this.allyInputField);

        // Bottone "+" per confermare l'alleato
        this.addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> {
            String name = allyInputField.getText().trim();
            if (!name.isEmpty() && !MouseTweaksV2.alliesList.contains(name)) {
                MouseTweaksV2.alliesList.add(name);
                allyInputField.setText("");
            }
        }).dimensions(125, 30, 20, 20).build());

        // Bottoni per attivare/disattivare gli slot 1-9
        slotButtons.clear();
        for (int i = 0; i < 9; i++) {
            final int slotIndex = i;
            ButtonWidget btn = ButtonWidget.builder(Text.literal(String.valueOf(i + 1)), button -> {
                if (MouseTweaksV2.validTriggerSlots.contains(slotIndex)) {
                    MouseTweaksV2.validTriggerSlots.remove((Integer) slotIndex);
                } else {
                    MouseTweaksV2.validTriggerSlots.add(slotIndex);
                }
            }).dimensions(10 + (i * 22), 65, 20, 20).build();
            
            this.addDrawableChild(btn);
            slotButtons.add(btn);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Sfondo scuro del menu
        context.fill(5, 5, 220, 180, 0x88000000);
        context.drawText(this.textRenderer, "§9§lMouse Tweaks V2 §7- 1.20.1 PvP", 10, 10, 0xFFFFFF, false);
        context.drawText(this.textRenderer, "Aggiungi Alleato:", 10, 22, 0xAAAAAA, false);
        context.drawText(this.textRenderer, "Slot Triggerbot attivi:", 10, 55, 0xAAAAAA, false);
        context.drawText(this.textRenderer, "Lista Alleati:", 10, 92, 0xAAAAAA, false);

        this.allyInputField.render(context, mouseX, mouseY, delta);

        // Disegna la lista con il pallino verde
        int yOffset = 105;
        for (String ally : MouseTweaksV2.alliesList) {
            if (yOffset > 170) break;
            context.drawText(this.textRenderer, "§a● §f" + ally, 15, yOffset, 0xFFFFFF, false);
            yOffset += 12;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false; // Fondamentale nei server PvP per non bloccarsi
    }
}
