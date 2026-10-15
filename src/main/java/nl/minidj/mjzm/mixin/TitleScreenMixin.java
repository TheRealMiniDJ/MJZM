package nl.minidj.mjzm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void mjzm$addButton(CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;

        ButtonWidget button = ButtonWidget.builder(Text.literal("MJZM"), clicked -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ServerAddress address = ServerAddress.parse("mjzm.minehost.gg");
            ServerInfo serverInfo = new ServerInfo(
                    "MJZM",
                    "mjzm.minehost.gg",
                    ServerInfo.ServerType.OTHER
            );

            ConnectScreen.connect(screen, client, address, serverInfo, false, null);
        }).dimensions(screen.width - 110, 10, 100, 20).build();

        screen.addDrawableChild(button);
    }
}
