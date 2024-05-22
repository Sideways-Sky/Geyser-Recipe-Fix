package net.sideways_sky.geyserrecipefix.inventories.smithing;

public enum SmithingSlot {
    TEMPLATE(10, 0),
    BASE(11, 1),
    ADDITION(12, 2),
    RESULT(15, 3);
    public final int i;
    public final int OG_i;
    SmithingSlot(int i, int og) {
        this.i = i;
        OG_i = og;
    }
    public static SmithingSlot getSlot(int i){
        for (SmithingSlot slot : values()){
            if(slot.i == i){
                return slot;
            }
        }
        return null;
    }
}

