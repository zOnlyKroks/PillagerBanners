package de.zonlykroks.pillagerbanners;

import de.zonlykroks.pillagerbanners.config.PillagerBannersConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.List;

public class PillagerBanners implements DedicatedServerModInitializer {
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitializeServer() {
        MidnightConfig.init("pillagerbanners", PillagerBannersConfig.class);

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            List<String> allowedEntitiesIdentifiers = PillagerBannersConfig.allowedEntities;

            System.out.println(allowedEntitiesIdentifiers);
            System.out.println(entity.getType().getUntranslatedName());

            if(allowedEntitiesIdentifiers.contains(entity.getType().getUntranslatedName()) && entity instanceof LivingEntity livingEntity && hand == Hand.MAIN_HAND && !world.isClient && hitResult != null) {
                List<String> allowedItemsIdentifiers = PillagerBannersConfig.allowedItems;
                List<Item> items = new ArrayList<>();

                allowedItemsIdentifiers.forEach(s -> {
                    items.add(Registries.ITEM.get(Identifier.tryParse(s)));
                });

                ItemStack mainHandStack = player.getInventory().getMainHandStack();

                boolean hasNoItemOnHead = livingEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty();

                if(hasNoItemOnHead) {
                    if(!items.contains(mainHandStack.getItem())) return ActionResult.PASS;

                    livingEntity.equipStack(EquipmentSlot.HEAD,mainHandStack.copyWithCount(1));
                    mainHandStack.decrement(1);
                    return ActionResult.CONSUME;
                }else {
                    livingEntity.dropStack(livingEntity.getEquippedStack(EquipmentSlot.HEAD).copyWithCount(1));
                    livingEntity.equipStack(EquipmentSlot.HEAD, Items.AIR.getDefaultStack());
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }
}
