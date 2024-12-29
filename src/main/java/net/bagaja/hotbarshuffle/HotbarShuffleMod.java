package net.bagaja.hotbarshuffle;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.core.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.List;
import java.util.Random;

@Mod(HotbarShuffleMod.MODID)
public class HotbarShuffleMod {
    public static final String MODID = "hotbarshuffle";

    public HotbarShuffleMod() {
        init();
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new HotbarShuffleEvents());
    }
}

class HotbarShuffleEvents {
    private final Random random = new Random();

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.level().isClientSide) {
                shuffleHotbar(player);
            }
        }
    }

    private void shuffleHotbar(Player player) {
        Inventory inventory = player.getInventory();

        // Determine random amount of items to add/replace (1-64)
        int itemCount = random.nextInt(64) + 1;

        // Get all possible items from the game's registry
        List<Item> allItems = ForgeRegistries.ITEMS.getValues().stream().toList();

        // Process each hotbar slot (0-8)
        for (int slot = 0; slot < 9; slot++) {
            if (itemCount > 0) {
                // Get a random item from all possible items
                Item randomItem = allItems.get(random.nextInt(allItems.size()));

                // Create a new stack with random count (1-64)
                ItemStack newStack = new ItemStack(randomItem, random.nextInt(64) + 1);

                // Set the item in the hotbar slot
                inventory.setItem(slot, newStack);

                itemCount--;
            }
        }
    }
}