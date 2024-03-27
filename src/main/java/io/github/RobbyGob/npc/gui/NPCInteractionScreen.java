package io.github.RobbyGob.npc.gui;
import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NPCInteractionScreen extends Screen {
    private final EntityNPC npc;

    public NPCInteractionScreen(EntityNPC npc) {
        super(Component.literal("NPC Interaction"));
        this.npc = npc;
    }

    @Override
    protected void init() {
        // Add a button with label "Interact" at position (x, y)
        this.addRenderableWidget(Button.builder(Component.literal("Interact"), (button) -> {
            // Close the current screen when the button is clicked
            this.minecraft.setScreen(null);
        }).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // Your custom rendering code here, kurio net nera
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}
