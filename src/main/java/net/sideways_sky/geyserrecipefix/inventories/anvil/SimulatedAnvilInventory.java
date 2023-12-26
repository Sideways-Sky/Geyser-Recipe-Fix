package net.sideways_sky.geyserrecipefix.inventories.anvil;

import net.sideways_sky.geyserrecipefix.inventories.SimulatedInventory;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimulatedAnvilInventory extends SimulatedInventory implements AnvilInventory {
    private int RepairCost;
    private int MaximumRepairCost;

    private final String RenameText;

    public SimulatedAnvilInventory(@NotNull Inventory inventory, int repairCost, int maximumRepairCost, String renameText) {
        super(inventory);
        RepairCost = repairCost;
        MaximumRepairCost = maximumRepairCost;
        RenameText = renameText;
    }

    @Override
    public @Nullable String getRenameText() {
        return "";
    }

    @Override
    public int getRepairCostAmount() {
        return RepairCost;
    }

    @Override
    public void setRepairCostAmount(int amount) {
        RepairCost = amount;
    }

    @Override
    public int getRepairCost() {
        return RepairCost;
    }

    @Override
    public void setRepairCost(int levels) {
        RepairCost = levels;
    }

    @Override
    public int getMaximumRepairCost() {
        return MaximumRepairCost;
    }

    @Override
    public void setMaximumRepairCost(int levels) {
        MaximumRepairCost = levels;
    }

    @Override
    public @Nullable ItemStack getFirstItem() {
        return inventory.getItem(AnvilSlot.FIRST.i);
    }

    @Override
    public void setFirstItem(@Nullable ItemStack firstItem) {
        inventory.setItem(AnvilSlot.FIRST.i, firstItem);
    }

    @Override
    public @Nullable ItemStack getSecondItem() {
        return inventory.getItem(AnvilSlot.SECOND.i);
    }

    @Override
    public void setSecondItem(@Nullable ItemStack secondItem) {
        inventory.setItem(AnvilSlot.SECOND.i, secondItem);
    }

    @Override
    public @Nullable ItemStack getResult() {
        return inventory.getItem(AnvilSlot.RESULT.i);
    }

    @Override
    public void setResult(@Nullable ItemStack result) {
        inventory.setItem(AnvilSlot.RESULT.i, result);
    }
}
