package fr.redrelay.spongecreeperheal.tool.tracker.dependency;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngine;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.GravityAffectedDependencyRule;
import net.minecraft.block.Block;
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
 * Except when "modelBlock" field exist because it is supposed to be managed by this modelBlock.
 *
 * Note that block with GravityAffected property will always have dependency because they fall
 * DragonEgg seems to be affected by gravity but does not have the property : https://github.com/SpongePowered/SpongeCommon/issues/2280
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
            boolean hasCanPlaceBlockAtMethod = false;
            boolean hasBlockModel = false;
            boolean hasDependencies = gravityAffectedDependencyRule.matches(blockType);
            while(!hasDependencies && clazz != null && !clazz.equals(Block.class)) {

                try {
                    clazz.getDeclaredMethod("canPlaceBlockAt", World.class, BlockPos.class);
                    hasCanPlaceBlockAtMethod = true;
                } catch (NoSuchMethodException e) {
                    hasCanPlaceBlockAtMethod = false;
                }

                try {
                    hasBlockModel = clazz.getDeclaredField("modelBlock").getType().equals(Block.class);;
                }catch(NoSuchFieldException e) {
                    hasBlockModel = false;
                }

                clazz = clazz.getSuperclass();

                hasDependencies = hasCanPlaceBlockAtMethod && !hasBlockModel;
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
            this.items.stream().forEachOrdered(item -> {
                Optional<String> optColor = Optional.empty();
                Optional<Boolean> optIsChecked = Optional.empty();

                switch(item.getStatus()) {
                    case DEPENDENCY_REGISTERED:
                        optColor = Optional.of("green");
                        optIsChecked = Optional.of(true);
                        break;
                    case DEPENDENCY_REGISTRED_WITH_NO_MATCH :
                        optColor = Optional.of("orange");
                        optIsChecked = Optional.of(true);
                        break;
                    case NO_DEPENDENCY_REGISTERED :
                        optColor = Optional.of("red");
                        optIsChecked = Optional.of(false);
                        break;
                    default : break;
                }

                final StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("* [");
                if(optIsChecked.isPresent() && optIsChecked.get().booleanValue()) {
                    stringBuilder.append('x');
                }else {
                    stringBuilder.append(' ');
                }
                stringBuilder.append("] ");

                optColor.ifPresent(color -> {
                    stringBuilder.append("<span style=\"color:").append(color).append(";\">");
                });

                stringBuilder.append(item.getBlock().getName());

                if(item.getDependencyFactory().isPresent()) {
                    stringBuilder.append(" : ").append(item.getDependencyFactory().get().getSource().getName());
                }
                optColor.ifPresent(color -> {
                    stringBuilder.append("</span>");
                });

                writer.println(stringBuilder.toString());
            });
        }finally {
            if(writer != null) writer.close();
        }
    }
}
