package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.service.data.CompoundServiceHolder;
import org.akazukin.service.data.ICompoundServiceHolder;
import org.akazukin.util.object.Pair;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class ACompoundServiceManager<U, V>
        extends AServiceManager<ICompoundServiceHolder<? extends U, V>, U> implements ICompoundServiceManager<ICompoundServiceHolder<? extends U, V>, U, V> {
    Class<V> dataType;

    @SuppressWarnings("unchecked")
    public ACompoundServiceManager(final @NotNull Class<U> serviceType, final Class<V> dataType) {
        super((Class<ICompoundServiceHolder<? extends U, V>>) (Object) CompoundServiceHolder.class, serviceType);
        this.dataType = dataType;
    }

    @Override
    public <U2 extends U> V getDataByImplementation(final Class<U> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getImplementation().getClass(), service))
                .findFirst()
                .map(ICompoundServiceHolder::getData)
                .orElse(null);
    }

    @Override
    public <U2 extends U> V getDataByInterface(final Class<U> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getInterfaceClass(), service))
                .findFirst()
                .map(ICompoundServiceHolder::getData)
                .orElse(null);
    }

    @Override
    public V getDataByService(final U service) {
        return this.services.stream()
                .filter(s -> s.getImplementation() == service)
                .findFirst()
                .map(ICompoundServiceHolder::getData)
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> Pair<U2, V> getServiceAndDataByImplementation(final Class<U2> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getImplementation().getClass(), service))
                .findFirst()
                .map(s -> new Pair<>((U2) s.getImplementation(), s.getData()))
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> Pair<U2, V> getServiceAndDataByInterface(final Class<U2> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getInterfaceClass(), service))
                .findFirst()
                .map(s -> new Pair<>((U2) s.getImplementation(), s.getData()))
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Pair<U, V>[] getServicesAndData() {
        return this.services.stream()
                .map(s -> new Pair<>(s.getImplementation(), s.getData()))
                .toArray(Pair[]::new);
    }

    @Override
    public V[] getData() {
        return this.services.stream()
                .map(ICompoundServiceHolder::getData)
                .toArray(ArrayUtils.collectToArray(this.dataType));
    }
}
