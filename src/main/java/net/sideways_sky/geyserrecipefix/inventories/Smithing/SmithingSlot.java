package net.sideways_sky.geyserrecipefix.inventories.Smithing;

public enum SmithingSlot {
    TEMPLATE(0),
    BASE(1),
    ADDITION(2),
    RESULT(3);


    public final int i;

    private SmithingSlot(int i) {
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
