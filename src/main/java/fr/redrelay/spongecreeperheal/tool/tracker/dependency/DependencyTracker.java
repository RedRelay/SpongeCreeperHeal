package fr.redrelay.spongecreeperheal.tool.tracker.dependency;

import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.impl.GravityAffectedDependencyRule;
import fr.redrelay.spongecreeperheal.explosion.ExplosionSnapshotFactory;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
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
 * By looking for implementation of method "canPlaceBlockAt" or "canPlaceBlockOnSide" in child class of Block
 * When it has "canPlaceBlockAt" method, it means there are specifics rules
 *
 * Note that block with GravityAffected property will always have dependency because they fall
 * DragonEgg seems to be affected by gravity but does not have the property : https://github.com/SpongePowered/SpongeCommon/issues/2280
 *
 *
 * */
public class DependencyTracker {

    private static final GravityAffectedDependencyRule gravityAffectedDependencyRule = new GravityAffectedDependencyRule();
    private static final DependencyTrackerAdapter dependencyAdapter = new DependencyTrackerAdapter();

    private final Set<DependencyTrackerItem> items;

    public DependencyTracker() {
        final Collection<BlockType> allBlocks = Sponge.getRegistry().getAllOf(BlockType.class);
        final Map<BlockType, DependencyProvider> dependencyMap = ExplosionSnapshotFactory.getInstance().getDependencyMap();
        final Set<DependencyTrackerItem> tracker = new HashSet<>();

        allBlocks.forEach(blockType -> {

            final Optional<DependencyProvider> factory = Optional.ofNullable(dependencyMap.get(blockType));

            if(DependencyTracker.hasDependencies(blockType)) {

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

    protected static boolean hasDependencies(Class<?> clazz) {
        try {
            clazz.getDeclaredMethod("canPlaceBlockAt", World.class, BlockPos.class);
            return true;
        } catch (ReflectiveOperationException e) {
            try {
                clazz.getDeclaredMethod("canPlaceBlockOnSide", World.class, BlockPos.class, EnumFacing.class);
                return true;
            }catch (ReflectiveOperationException e2) {
                return false;
            }
        }
    }

    protected static boolean hasDependencies(BlockType blockType) {

        Class<?> clazz = blockType.getClass();
        boolean hasDependencies = gravityAffectedDependencyRule.matches(blockType);
        while(!hasDependencies && !clazz.equals(Block.class)) {

            final Optional<Boolean> forcedRule = dependencyAdapter.hasDependency(blockType, clazz);

            if(forcedRule.isPresent()) {
                return forcedRule.get();
            }

            hasDependencies = DependencyTracker.hasDependencies(clazz);
            if(!hasDependencies) {
                clazz = clazz.getSuperclass();
            }
        }

        return hasDependencies;
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
