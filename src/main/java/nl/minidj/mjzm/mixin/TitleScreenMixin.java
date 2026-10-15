package nl.minidj.mjzm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
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
        this.splashText = new SplashTextRenderer(Text.literal("MJZM was made By DeMiniDJ"));

        int buttonX = screen.width / 2 - 100;
        int buttonY = screen.height / 4 + 48;

        for (Element element : screen.children()) {
            if (element instanceof ButtonWidget oldButton
                    && oldButton.getMessage().getString().equalsIgnoreCase("Multiplayer")) {
                buttonX = oldButton.getX();
                buttonY = oldButton.getY();
                oldButton.setPosition(-10000, -10000);
                break;
            }
        }

        ButtonWidget joinButton = ButtonWidget.builder(
                Text.literal("Join MJZM"),
                clicked -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    ServerInfo serverInfo = new ServerInfo(
                            "MJZM",
                            "mjzm.minehost.gg",
                            ServerInfo.ServerType.OTHER
                    );

                    // Use Minecraft's normal MultiplayerScreen connection path.
                    // This mirrors how joining a server from the Multiplayer menu works.
                    MultiplayerScreen multiplayerScreen = new MultiplayerScreen(screen);
                    client.setScreen(multiplayerScreen);
                    multiplayerScreen.connect(serverInfo);
                })
                .dimensions(buttonX, buttonY, 200, 20)
                .build();

        ((ScreenAccessor) screen).mjzm$addDrawableChild(joinButton);
    }

}
