package fr.redrelay.spongecreeperheal.tool.tracker.dependency;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngine;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.GravityAffectedDependencyRule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndRod;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Try to locate all blocks which have at least one dependency
 * By looking for implementation of method "canPlaceBlockAt" in child class of Block
 * When it has "canPlaceBlockAt" method, it means there are specifics rules
 *
 * Note that block with GravityAffected property will always have dependency because they fall
 * DragonEgg seems to be affected by gravity but does not have the property : https://github.com/SpongePowered/SpongeCommon/issues/2280
 *
 *
 * */
public class DependencyTracker {

    private final Set<DependencyTrackerItem> items;

    public DependencyTracker() {
        final Collection<BlockType> allBlocks = Sponge.getRegistry().getAllOf(BlockType.class);
        final Map<BlockType, DependencyFactory> dependencyMap = DependencyEngine.getInstance().getDependencyMap();
        final Set<DependencyTrackerItem> tracker = new HashSet<>();

        final GravityAffectedDependencyRule gravityAffectedDependencyRule = new GravityAffectedDependencyRule();

        allBlocks.forEach(blockType -> {

            final Optional<DependencyFactory> factory = Optional.ofNullable(dependencyMap.get(blockType));

            Class<?> clazz = blockType.getClass();
            boolean hasDependencies = gravityAffectedDependencyRule.matches(blockType);
            while(!hasDependencies && clazz != null && !clazz.equals(Block.class)) {

                //BlockEndRod implements canPlaceBlockAt for ... nothing :|
                //Always return true like parent block Block.class
                if(clazz.equals(BlockEndRod.class)) {
                    break;
                }
                //For BlockStairs : modelBlock is supposed to manage dependency
                if(clazz.equals(BlockStairs.class)) {
                    // TODO : Use Mixin : modelBlock is private
                    //clazz = ((BlockStairs) blockType).modelBlock.getClass();
                    break;
                }

                //For BlockFenceGate : they cannot be placed on non solid block by default
                //But this is a valid position, if you place a solid block then place a fence gate
                //then replace the solid block by a non solid so the fence gate stay
                if(clazz.equals(BlockFenceGate.class)) {
                    break;
                }

                try {
                    clazz.getDeclaredMethod("canPlaceBlockAt", World.class, BlockPos.class);
                    hasDependencies = true;
                } catch (NoSuchMethodException e) {
                    clazz = clazz.getSuperclass();
                }
            }

            if(hasDependencies) {

                if(factory.isPresent()) {
                    tracker.add(new DependencyTrackerItem(blockType, factory, DependencyTrackerItem.Status.DEPENDENCY_REGISTERED));
                }else {
                    tracker.add(new DependencyTrackerItem(blockType, factory, DependencyTrackerItem.Status.NO_DEPENDENCY_REGISTERED));
                }
            }else {
                if(factory.isPresent()) {
                    tracker.add(new DependencyTrackerItem(blockType, factory, DependencyTrackerItem.Status.DEPENDENCY_REGISTRED_WITH_NO_MATCH));
                }
            }
        });

        this.items = Collections.unmodifiableSet(tracker);
    }

    public void createMarkdown(File file) throws IOException {
        final PrintWriter writer = new PrintWriter(new FileWriter(file));
        try {
            this.items.stream().sorted().forEachOrdered(item -> {

                final StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("* [");
                if(item.getStatus().isDependencyImplemented()) {
                    stringBuilder.append('x');
                }else {
                    stringBuilder.append(' ');
                }
                stringBuilder.append("] ");

                item.getStatus().getColor().ifPresent(color -> {
                    stringBuilder.append("<span style=\"color:").append(color).append(";\">");
                });

                stringBuilder.append(item.getBlock().getName());

                item.getDependencyFactory().ifPresent(factory -> {
                    stringBuilder.append(" : ").append(factory.getSource().getName());
                });

                item.getStatus().getColor().ifPresent(color -> {
                    stringBuilder.append("</span>");
                });

                writer.println(stringBuilder.toString());
            });
        }finally {
            if(writer != null) writer.close();
        }
    }
}
