package net.sideways_sky.geyserrecipefix.inventories.Smithing;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class SimulatedSmithingInventory implements SmithingInventory {
    @NotNull
    private final Inventory inventory;
    @Nullable
    private final Recipe recipe;

    public SimulatedSmithingInventory(@NotNull Inventory inventory, @Nullable Recipe recipe){
        this.inventory = inventory;
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

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public @Nullable ItemStack getItem(int index) {
        return inventory.getItem(index);
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item) {
        inventory.setItem(index, item);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        return inventory.addItem(items);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        return inventory.removeItem(items);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> removeItemAnySlot(@NotNull ItemStack... items) throws IllegalArgumentException {
        return inventory.removeItemAnySlot(items);
    }

    @Override
    public @Nullable ItemStack @NotNull [] getContents() {
        return inventory.getContents();
    }

    @Override
    public void setContents(@Nullable ItemStack @NotNull [] items) throws IllegalArgumentException {
        inventory.setContents(items);
    }

    @Override
    public @Nullable ItemStack @NotNull [] getStorageContents() {
        return inventory.getStorageContents();
    }

    @Override
    public void setStorageContents(@Nullable ItemStack @NotNull [] items) throws IllegalArgumentException {
        inventory.setStorageContents(items);
    }

    @Override
    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        return inventory.contains(material);
    }

    @Override
    public boolean contains(@Nullable ItemStack item) {
        return inventory.contains(item);
    }

    @Override
    public boolean contains(@NotNull Material material, int amount) throws IllegalArgumentException {
        return inventory.contains(material, amount);
    }

    @Override
    public boolean contains(@Nullable ItemStack item, int amount) {
        return inventory.contains(item, amount);
    }

    @Override
    public boolean containsAtLeast(@Nullable ItemStack item, int amount) {
        return inventory.containsAtLeast(item, amount);
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        return inventory.all(material);
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item) {
        return inventory.all(item);
    }

    @Override
    public int first(@NotNull Material material) throws IllegalArgumentException {
        return inventory.first(material);
    }

    @Override
    public int first(@NotNull ItemStack item) {
        return inventory.first(item);
    }

    @Override
    public int firstEmpty() {
        return inventory.firstEmpty();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void remove(@NotNull Material material) throws IllegalArgumentException {
        inventory.remove(material);
    }

    @Override
    public void remove(@NotNull ItemStack item) {
        inventory.remove(item);
    }

    @Override
    public void clear(int index) {
        inventory.clear(index);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public int close() {
        return inventory.close();
    }

    @Override
    public @NotNull List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    @Override
    public @NotNull InventoryType getType() {
        return inventory.getType();
    }

    @Override
    public @Nullable InventoryHolder getHolder() {
        return inventory.getHolder();
    }

    @Override
    public @Nullable InventoryHolder getHolder(boolean useSnapshot) {
        return inventory.getHolder(useSnapshot);
    }

    @Override
    public @NotNull ListIterator<ItemStack> iterator() {
        return inventory.iterator();
    }

    @Override
    public @NotNull ListIterator<ItemStack> iterator(int index) {
        return inventory.iterator(index);
    }

    @Override
    public @Nullable Location getLocation() {
        return inventory.getLocation();
    }
}
