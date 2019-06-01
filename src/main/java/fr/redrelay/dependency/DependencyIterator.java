package fr.redrelay.dependency;


import java.util.*;
import java.util.stream.Collectors;

public class DependencyIterator<T> implements Iterator<T> {

    private class Tuple {
        final T key;
        final DependencyNode<T> node;

        public Tuple(T key, DependencyNode<T> node) {
            this.key = key;
            this.node = node;
        }
    }

    private static final Random rdn = new Random();
    private final List<DependencyNode<T>> availables;
    private final Set<DependencyNode<T>> unavailables;
    private final Map<T, Set<DependencyNode<T>>> updateDependents;

    public DependencyIterator(Collection<DependencyNode<T>> nodes) {
        final Map<Boolean, Set<DependencyNode<T>>> havingDependency = nodes.parallelStream().collect(Collectors.groupingBy(node -> node.getDependencyModel().hasDependency(), Collectors.toSet()));
        availables = new ArrayList<>(havingDependency.getOrDefault(false, Collections.emptySet()));
        unavailables = havingDependency.getOrDefault(true, Collections.emptySet());

        updateDependents = unavailables.parallelStream()
                .flatMap(node -> node.getDependencyModel().getDependencies().parallelStream()
                        .map(dependency ->new Tuple(dependency, node)))
                .collect(Collectors.groupingBy(tuple -> tuple.key, Collectors.mapping(tuple -> tuple.node, Collectors.toSet())));
    }

    @Override
    public boolean hasNext() {
        return !availables.isEmpty();
    }

    @Override
    public T next() {
        final DependencyNode<T> next = availables.remove(rdn.nextInt(availables.size()));
        final Set<DependencyNode<T>> dependents = updateDependents.remove(next.getEntry());
        if(dependents != null)  {
            final Set<DependencyNode<T>> unlockedNodes = dependents.parallelStream().filter(dependencyNode -> {
                dependencyNode.getDependencyModel().removeDependency(next.getEntry());
                return !dependencyNode.getDependencyModel().hasDependency();
            }).collect(Collectors.toSet());

            unavailables.removeAll(unlockedNodes);
            availables.addAll(unlockedNodes);
        }
        return next.getEntry();
    }
}
