package fr.redrelay.spongecreeperheal.tool.tracker.dependency;

import fr.redrelay.spongecreeperheal.dependency.DependencyFactory;
import org.spongepowered.api.block.BlockType;

import java.util.Objects;
import java.util.Optional;

public final class DependencyTrackerItem implements Comparable<DependencyTrackerItem>{

    @Override
    public int compareTo(DependencyTrackerItem o) {
        return this.getStatus().priority - o.getStatus().priority;
    }

    public enum Status {
        DEPENDENCY_REGISTERED("green", true,1),
        DEPENDENCY_REGISTRED_WITH_NO_MATCH("orange", true, 2),
        NO_DEPENDENCY_REGISTERED("red", false, 3);

        private final String color;
        protected final int priority;
        private final boolean isDependencyImplemented;

        Status(String color, boolean isDependencyImplemented, int priority) {
            this.color = color;
            this.isDependencyImplemented = isDependencyImplemented;
            this.priority = priority;
        }

        Status(boolean isDependencyImplemented, int priority) {
            this(null, isDependencyImplemented, priority);
        }

        public Optional<String> getColor() {
            return Optional.ofNullable(color);
        }

        public boolean isDependencyImplemented() {
            return isDependencyImplemented;
        }
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
