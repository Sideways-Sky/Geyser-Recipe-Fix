package net.sideways_sky.geyserrecipefix.inventories;


import com.github.retrooper.packetevents.protocol.item.ItemStack;
import net.sideways_sky.geyserrecipefix.config.WorkstationMode;

import java.util.ArrayList;
import java.util.List;

public class SmithingSim extends SimInventory {
    public static WorkstationMode mode;
    public enum SmithingSlot {
        TEMPLATE(10, 0),
        BASE(11, 1),
        ADDITION(12, 2),
        RESULT(15, 3);
        public final int frontIdx;
        public final int backIdx;
        SmithingSlot(int front, int back) {
            this.frontIdx = front;
            this.backIdx = back;
        }
        public static SmithingSlot getFromFrontIdx(int i){
            for (SmithingSlot slot : values()){
                if(slot.frontIdx == i){
                    return slot;
                }
            }
            return null;
        }
        public static SmithingSlot getFromBackIdx(int i){
            for (SmithingSlot slot : values()){
                if(slot.backIdx == i){
                    return slot;
                }
            }
            return null;
        }
    }
    protected static final int backSize = 40;
    protected static final int backTopSize = 4; // backSize - playerInvSize

    public SmithingSim() {
        super(backSize);
    }

    @Override
    protected List<ItemStack> getFront(List<ItemStack> items) {
        List<ItemStack> res = new ArrayList<>();
        for (int i = 0; i < frontTopSize; i++) {
            SmithingSlot slot = SmithingSlot.getFromFrontIdx(i);
            res.add( slot == null ? filler : items.get(slot.backIdx));
        }
        return res;
    }

    @Override
    public int getBackIdxFromFrontIdx(int frontIdx) {
        if(frontIdx >= frontTopSize ){
            return frontIdx - (frontTopSize - backTopSize); //Clicked player inventory
        }
        SmithingSlot slot = SmithingSlot.getFromFrontIdx(frontIdx);
        return slot == null ? -1 : slot.backIdx;
    }

    @Override
    public int getFrontIdxFromBackIdx(int backIdx) {
        if(backIdx >= backTopSize){
            return backIdx + (frontTopSize - backTopSize); //idx in player inventory
        }
        SmithingSlot slot = SmithingSlot.getFromBackIdx(backIdx);
        return slot == null ? -1 : slot.frontIdx;
    }

}
