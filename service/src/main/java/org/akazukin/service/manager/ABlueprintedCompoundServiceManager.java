package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.BlueprintedCompoundServiceHolder;
import org.akazukin.service.data.IBlueprintedCompoundServiceHolder;
import org.akazukin.service.data.ICompoundServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An abstract implementation of a compound service manager that manages services and their associated data.
 * Extends the {@link ABlueprintedServiceManager} with additional functionalities for handling data linked with service holders.
 * <p>
 * The service manager is thread-safe and can be used in multithreaded environments.
 *
 * @param <U> the type of service managed by this manager.
 * @param <V> the type of data associated with the services.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@ThreadSafe
public abstract class ABlueprintedCompoundServiceManager<U, V>
        extends ABlueprintedServiceManager<U> implements IBlueprintedCompoundServiceManager<U, V> {
    Class<V> dataType;

    /**
     * Constructs an instance of ACompoundServiceManager, which manages compound services
     * and their associated data types.
     * This manager extends the capabilities of a basic service manager by allowing
     * management of data linked with compound service holders.
     *
     * @param serviceType the class object representing the type of the service.
     *                    Must not be null.
     * @param dataType    the class object representing the type of data associated with the services.
     */
    protected ABlueprintedCompoundServiceManager(final @NotNull Class<U> serviceType, final Class<V> dataType) {
        super(serviceType);
        this.dataType = dataType;
    }

    @Override
    public V getDataByImplementation(final Class<? extends U> service) {
        return this.services.stream()
                .filter(s ->
                        s instanceof ICompoundServiceHolder
                                && Objects.equals(s.getImplementation().getClass(), service))
                .findFirst()
                .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData())
                .orElse(null);
    }

    @Override
    public V getDataByService(final @NotNull U service) {
        return this.services.stream()
                .filter(s ->
                        s instanceof ICompoundServiceHolder
                                && s.getImplementation() == service)
                .findFirst()
                .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData())
                .orElse(null);
    }

    @Override
    public V[] getAllData() {
        return this.services.stream()
                .filter(s -> s instanceof ICompoundServiceHolder)
                .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData())
                .toArray(ArrayUtils.collectToArray(this.dataType));
    }

    @Override
    public ICompoundServiceHolder<? extends U, V>[] getServiceHolderByData(@Nullable final V data) {
        return this.services.stream()
                .filter(s ->
                        s instanceof ICompoundServiceHolder
                                && Objects.equals(((ICompoundServiceHolder<? extends U, V>) s).getData(), data))
                .toArray(ArrayUtils.collectToArray((Class<ICompoundServiceHolder<? extends U, V>>) (Object) ICompoundServiceHolder.class));
    }

    @Override
    public V getDataByInterface(final Class<? extends U> service) {
        return this.services.stream()
                .filter(s -> s instanceof IBlueprintedCompoundServiceHolder
                        && Objects.equals(((IBlueprintedCompoundServiceHolder<? extends U, V>) s).getInterfaceClass(), service))
                .findFirst()
                .map(s -> ((IBlueprintedCompoundServiceHolder<? extends U, V>) s).getData())
                .orElse(null);
    }

    @Override
    protected @NotNull <U2 extends U> IBlueprintedCompoundServiceHolder<U2, V> createServiceHolder(final @NotNull Class<U2> service, @NotNull final U2 serviceImpl) {
        return new BlueprintedCompoundServiceHolder<>(service, serviceImpl);
    }
}
