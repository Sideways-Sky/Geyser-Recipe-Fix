package net.sideways_sky.geyserrecipefix.inventories.Smithing;

public enum SmithingSlot {
    TEMPLATE(9),
    BASE(10),
    ADDITION(11),
    RESULT(14);
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

