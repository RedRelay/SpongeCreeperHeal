package fr.redrelay.spongecreeperheal.tool.tracker.dependency;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import org.spongepowered.api.block.BlockType;

import java.util.Objects;
import java.util.Optional;

public final class DependencyTrackerItem {

    public enum Status {
        DEPENDENCY_REGISTERED,
        DEPENDENCY_REGISTRED_WITH_NO_MATCH,
        NO_DEPENDENCY_REGISTERED
    }

    private final BlockType block;
    private final Optional<DependencyFactory> dependencyFactory;
    private final Status status;

    protected DependencyTrackerItem(BlockType block, Optional<DependencyFactory> dependencyFactory, Status status) {
        this.block = block;
        this.dependencyFactory = dependencyFactory;
        this.status = status;
    }

    public BlockType getBlock() {
        return block;
    }

    public Optional<DependencyFactory> getDependencyFactory() {
        return dependencyFactory;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependencyTrackerItem that = (DependencyTrackerItem) o;
        return block.equals(that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block);
    }

}
