package net.sideways_sky.geyserrecipefix.inventories.smithing;

public enum SmithingSlot {
    TEMPLATE(10),
    BASE(11),
    ADDITION(12),
    RESULT(15);
    public final int i;
    SmithingSlot(int i) {
        this.i = i;
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

