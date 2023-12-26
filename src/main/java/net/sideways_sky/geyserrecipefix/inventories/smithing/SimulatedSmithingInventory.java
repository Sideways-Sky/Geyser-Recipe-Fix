package net.sideways_sky.geyserrecipefix.inventories.smithing;

import net.sideways_sky.geyserrecipefix.inventories.SimulatedInventory;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimulatedSmithingInventory extends SimulatedInventory implements SmithingInventory {
    @Nullable
    private final Recipe recipe;

    public SimulatedSmithingInventory(@NotNull Inventory inventory, @Nullable Recipe recipe){
        super(inventory);
        this.recipe = recipe;
    }
    @Override
    public @Nullable ItemStack getResult() {
        return inventory.getItem(SmithingSlot.RESULT.i);
    }

    @Override
    public void setResult(@Nullable ItemStack newResult) {
        inventory.setItem(SmithingSlot.RESULT.i, newResult);
    }

    @Override
    public @Nullable Recipe getRecipe() {
        return recipe;
    }

    @Override
    public @Nullable ItemStack getInputTemplate() {
        return inventory.getItem(SmithingSlot.TEMPLATE.i);
    }

    @Override
    public void setInputTemplate(@Nullable ItemStack itemStack) {
        inventory.setItem(SmithingSlot.TEMPLATE.i, itemStack);
    }

    @Override
    public @Nullable ItemStack getInputEquipment() {
        return inventory.getItem(SmithingSlot.BASE.i);
    }

    @Override
    public void setInputEquipment(@Nullable ItemStack itemStack) {
        inventory.setItem(SmithingSlot.BASE.i, itemStack);
    }

    @Override
    public @Nullable ItemStack getInputMineral() {
        return inventory.getItem(SmithingSlot.ADDITION.i);
    }

    @Override
    public void setInputMineral(@Nullable ItemStack itemStack) {
        inventory.setItem(SmithingSlot.ADDITION.i, itemStack);
    }
}
