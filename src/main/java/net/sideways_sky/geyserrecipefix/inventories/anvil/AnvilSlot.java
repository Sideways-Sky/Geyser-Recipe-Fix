package net.sideways_sky.geyserrecipefix.inventories.anvil;

public enum AnvilSlot {
    FIRST(10, true),
    SECOND(13, true),
    RESULT(16, true),
    COST(25, false),
    FORWARD(26, false);
    public final int i;
    public final boolean open;
    AnvilSlot(int i, boolean open) {
        this.i = i;
        this.open = open;
    }
    public static AnvilSlot getSlot(int i){
        for (AnvilSlot slot : values()){
            if(slot.i == i){
                return slot;
            }
        }
        return null;
    }
}

