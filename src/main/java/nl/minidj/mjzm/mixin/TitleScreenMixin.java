package nl.minidj.mjzm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    private static final Identifier MJZM$BACKGROUND =
            Identifier.of("mjzm", "textures/gui/menu_background.png");

    @Shadow @Final @Mutable private SplashTextRenderer splashText;

    @Inject(method = "init", at = @At("TAIL"))
    private void mjzm$customizeTitleScreen(CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;
        this.splashText = new SplashTextRenderer("MJZM was made By DeMiniDJ");

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
                    ServerAddress address = ServerAddress.parse("mjzm.minehost.gg");
                    ServerInfo serverInfo = new ServerInfo(
                            "MJZM",
                            "mjzm.minehost.gg",
                            ServerInfo.ServerType.OTHER
                    );
                    ConnectScreen.connect(screen, client, address, serverInfo, false, null);
                })
                .dimensions(buttonX, buttonY, 200, 20)
                .build();

        ((ScreenAccessor) screen).mjzm$addDrawableChild(joinButton);
    }

    @Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
    private void mjzm$drawCustomBackground(DrawContext context, float delta, CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;
        context.drawTexture(RenderLayer::getGuiTextured, MJZM$BACKGROUND,
                0, 0, 0.0F, 0.0F, screen.width, screen.height,
                screen.width, screen.height);
        ci.cancel();
    }
}