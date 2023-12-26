package net.sideways_sky.geyserrecipefix.inventories.anvil;

public enum AnvilSlot {
    FIRST(11),
    SECOND(13),
    RESULT(15);
    public final int i;
    AnvilSlot(int i) {
        this.i = i;
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

