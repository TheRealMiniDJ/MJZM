package nl.minidj.mjzm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Shadow @Final @Mutable private SplashTextRenderer splashText;

    @Inject(method = "init", at = @At("TAIL"))
    private void mjzm$customizeTitleScreen(CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;

        // Replace Minecraft's rotating splash text with the MJZM credit.
        this.splashText = new SplashTextRenderer(Text.literal("MJZM was made By DeMiniDJ"));

        int buttonX = screen.width / 2 - 100;
        int buttonY = screen.height / 4 + 48;

        // Put the MJZM button exactly where vanilla's Multiplayer button is,
        // and hide the original instead of moving it outside the screen.
        for (Element element : screen.children()) {
            if (element instanceof ButtonWidget oldButton
                    && oldButton.getMessage().getString().equalsIgnoreCase("Multiplayer")) {
                buttonX = oldButton.getX();
                buttonY = oldButton.getY();
                oldButton.visible = false;
                oldButton.active = false;
                break;
            }
        }

        ButtonWidget joinButton = ButtonWidget.builder(
                Text.literal("Join MJZM"),
                clicked -> {
                    MinecraftClient client = MinecraftClient.getInstance();

                    // Use Minecraft's own multiplayer screen and its normal
                    // server-entry connection flow, avoiding a custom direct connect.
                    client.setScreen(new MultiplayerScreen(screen));
                })
                .dimensions(buttonX, buttonY, 200, 20)
                .build();

        ((ScreenAccessor) screen).mjzm$addDrawableChild(joinButton);
    }
}
