package name.modid;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TotemReminder1201Client implements ClientModInitializer {
    private static boolean totemUsed = false;
    private static long totemUseTime = 0;
    private static final long TOTEM_REMINDER_DURATION = 15 * 60 * 20; // 15 minutes in ticks

    @Override
    public void onInitializeClient() {
        // Register event listeners
        UseItemCallback.EVENT.register(this::onUseItem);
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private ActionResult onUseItem(PlayerEntity player, World world, Hand hand) {
        if (!world.isClient) return ActionResult.PASS;

        if (player.getStackInHand(hand).getItem() == Items.TOTEM_OF_UNDYING) {
            // Totem used, display reminder
            totemUsed = true;
            totemUseTime = MinecraftClient.getInstance().world.getTime();
            displayTotemReminder(player);
        }

        return ActionResult.PASS;
    }

    private void onClientTick(MinecraftClient client) {
        if (totemUsed && (client.world.getTime() - totemUseTime > TOTEM_REMINDER_DURATION)) {
            totemUsed = false;
            clearTotemReminder();
        }

        ClientPlayerEntity player = client.player;
        if (player != null && totemUsed && player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            // Totem placed in offhand, clear reminder
            totemUsed = false;
            clearTotemReminder();
        }
    }

    private void displayTotemReminder(PlayerEntity player) {
        // Implemented in mixin
    }

    private void clearTotemReminder() {
        // Implemented in mixin
    }

    public static boolean isTotemUsed() {
        return totemUsed;
    }

    public static long getTotemUseTime() {
        return totemUseTime;
    }
}
