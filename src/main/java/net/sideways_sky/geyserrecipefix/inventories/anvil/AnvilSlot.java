package net.sideways_sky.geyserrecipefix.inventories.anvil;

public enum AnvilSlot {
    FIRST(10, 0),
    SECOND(13, 1),
    RESULT(16, 2),
    COST(25, -1),
    FORWARD(26, -1);
    public final int i;
    public final int OG_i;
    AnvilSlot(int i, int og) {
        this.i = i;
        OG_i = og;
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

