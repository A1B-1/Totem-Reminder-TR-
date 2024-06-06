package name.modid.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import name.modid.TotemReminder1201Client;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ExampleClientMixin {
    private static final LiteralText TOTEM_REMINDER_TEXT = new LiteralText("TOTEM REMINDER \uD83D\uDDE3").formatted(Formatting.RED);
    private static final LiteralText NO_TOTEM_TEXT = new LiteralText("YOU RAN OUT OF TOTEMS.").formatted(Formatting.RED);

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (TotemReminder1201Client.isTotemUsed()) {
            long currentTime = client.world.getTime();
            if (currentTime - TotemReminder1201Client.getTotemUseTime() <= 1) {
                client.player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 100000F, 2F);
            }
            if (client.player.getInventory().count(Items.TOTEM_OF_UNDYING) == 0) {
                client.inGameHud.setOverlayMessage(NO_TOTEM_TEXT, false);
            } else {
                client.inGameHud.setOverlayMessage(TOTEM_REMINDER_TEXT, false);
            }
        } else {
            client.inGameHud.setOverlayMessage(null, false);
        }
    }
}
