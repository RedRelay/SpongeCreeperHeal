package fr.redrelay.spongecreeperheal.data.explosion;

import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
import fr.redrelay.spongecreeperheal.healable.explosion.ChunkedHealableExplosion;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.*;

public class HealableExplosionData implements DataSerializable {

    private static class Keys {
        final static DataQuery HEALABLES = DataQuery.of("healableAtoms");
    }

    public static class EmptyHealableExplosionDataException extends RuntimeException {

        private final Collection<? extends HealableAtom> healableAtoms;

        private EmptyHealableExplosionDataException(Collection<? extends HealableAtom> healableAtoms) {
            super("Unable to create an "+ HealableExplosionData.class.getSimpleName()+" without at least one "+HealableAtom.class.getSimpleName());
            this.healableAtoms = healableAtoms;
        }

        public Collection<? extends HealableAtom> getEntries() {
            return Collections.unmodifiableCollection(healableAtoms);
        }
    }

    private final LinkedList<HealableAtom> healableAtoms = new LinkedList<>();

    public HealableExplosionData(Collection<? extends HealableAtom> healableAtoms) throws EmptyHealableExplosionDataException {
        if(healableAtoms.isEmpty()) {
            throw new EmptyHealableExplosionDataException(healableAtoms);
        }
        this.healableAtoms.addAll(healableAtoms);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(Keys.HEALABLES, healableAtoms);
        return data;
    }

    public LinkedList<HealableAtom> getHealableAtoms() {
        return healableAtoms;
    }

    public static class DataBuilder extends AbstractDataBuilder<HealableExplosionData> {

        protected DataBuilder(int supportedVersion) {
            super(HealableExplosionData.class, supportedVersion);
        }

        @Override
        protected Optional<HealableExplosionData> buildContent(DataView data) throws InvalidDataException {
            final List<? extends HealableAtom> healableAtoms = data.getSerializableList(Keys.HEALABLES, HealableAtom.class)
                    .orElseThrow(() -> new InvalidDataException(ChunkedHealableExplosion.class.getName()+" : Missing \""+Keys.HEALABLES.toString()+"\" data"));
            return Optional.of(new HealableExplosionData(healableAtoms));
        }
    }
}
