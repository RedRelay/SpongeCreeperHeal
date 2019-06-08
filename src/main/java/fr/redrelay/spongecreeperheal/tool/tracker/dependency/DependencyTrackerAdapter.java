package fr.redrelay.spongecreeperheal.tool.tracker.dependency;

import net.minecraft.block.*;
import org.spongepowered.api.block.BlockType;

import java.lang.reflect.Field;
import java.util.*;

public class DependencyTrackerAdapter {

    public interface CustomTrackerRule {
        boolean hasDependency(BlockType block, Class<?> clazz);
    }

    private final Set<Class<?>> withDependency = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            //BlockCocoa implements a specific rule named "canBlockStay"
            BlockCocoa.class
    )));

    private final Set<Class<?>> withoutDependency = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            //BlockEndRod implements canPlaceBlockAt for ... nothing :|
            //Always return true like parent block Block.class
            BlockEndRod.class,
            //For BlockStairs : modelBlock is supposed to manage dependency
            BlockStairs.class,
            //For BlockFenceGate, BlockTrapDoor and BlockPumpkin : they cannot be placed on non solid block by default
            //But this is a valid position, if you place a solid block then place a fence gate or a trap door
            //then replace the solid block by a non solid so the fence gate stay
            BlockFenceGate.class,
            BlockTrapDoor.class,
            BlockPumpkin.class,
            //For BlockChest : only double chest is checked, we can ignore it when we restore
            //Moreover I don't have any simple clue of fallback in case canBePlaced
            // return false when healed
            BlockChest.class,
            //Technical Blocks
            BlockPistonExtension.class,
            BlockPistonMoving.class

    )));

    private final Map<Class<?>, CustomTrackerRule> customRules;

    protected DependencyTrackerAdapter() {
        final Map<Class<?>, CustomTrackerRule> customRules = new HashMap<>();

        //For BlockStairs : modelBlock is supposed to manage dependency
        customRules.put(BlockStairs.class, new CustomTrackerRule() {
            @Override
            public boolean hasDependency(BlockType block, Class<?> stairsClass) {
                try {
                    final Field field = stairsClass.getDeclaredField("modelBlock");
                    field.setAccessible(true);
                    final BlockType blockModel = (BlockType) field.get(block);
                    return DependencyTracker.hasDependencies(blockModel);
                }catch(ReflectiveOperationException e) {
                    return false;
                }

            }
        });
        this.customRules = Collections.unmodifiableMap(customRules);
    }

    protected Optional<Boolean> hasDependency(BlockType block, Class<?> clazz) {

        if(this.withDependency.contains(clazz)) {
            return Optional.of(true);
        }

        if(this.withoutDependency.contains(clazz)) {
            return Optional.of(false);
        }

        final CustomTrackerRule customRule = this.customRules.get(clazz);

        if(customRule != null) {
            return Optional.of(customRule.hasDependency(block, clazz));
        }

        return Optional.empty();
    }


}
