package org.akazukin.service.manager.multi;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.ICompoundServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A base abstract class that provides the implementation of a service management system.
 * <p>
 * Extends the {@link AMultiServiceManager} with additional functionalities
 * for handling data linked with service holders.
 * <p>
 * The service manager is thread-safe and can be used in multithreaded environments.
 *
 * @param <U> The type of the service object managed by this service manager.
 * @param <V> the type of data associated with the services.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@ThreadSafe
public abstract class ACompoundMultiServiceManager<U, V>
        extends AMultiServiceManager<U> implements ICompoundMultiServiceManager<U, V> {
    Class<V> dataType;

    /**
     * Constructs an instance of ACompoundSingleServiceManager, which manages compound services
     * and their associated data types.
     * This manager extends the capabilities of a basic service manager by allowing
     * management of data linked with compound service holders.
     *
     * @param serviceType The class object representing the type of the service.
     *                    Must not be {@code null}.
     * @param dataType    The class object representing the type of data associated with the services.
     *                    Must not be {@code null}.
     */
    protected ACompoundMultiServiceManager(final @NotNull Class<U> serviceType, @NotNull final Class<V> dataType) {
        super(serviceType);
        this.dataType = dataType;
    }

    @Override
    public V[] getDataByService(final @NotNull U serviceImpl) {
        synchronized (this.services) {
            return this.services.stream()
                    .filter(h -> h instanceof ICompoundServiceHolder
                            && h.getImplementation() == serviceImpl)
                    .map(h -> ((ICompoundServiceHolder<? extends U, V>) h).getData())
                    .toArray(ArrayUtils.collectToArray(this.dataType));
        }
    }

    @Override
    public V[] getDataByClass(final @NotNull Class<? extends U> serviceImpl) {
        synchronized (this.services) {
            return this.services.stream()
                    .filter(s -> s instanceof ICompoundServiceHolder
                            && Objects.equals(s.getImplementation().getClass(), serviceImpl))
                    .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData())
                    .toArray(ArrayUtils.collectToArray(this.dataType));
        }
    }

    @Override
    @NotNull
    public V[] getDataByInterfaceClass(@NotNull final Class<? extends U> service) {
        synchronized (this.services) {
            return this.services.stream()
                    .filter(h ->
                            Objects.equals(h.getInterfaceClass(), service))
                    .map(h -> ((ICompoundServiceHolder<? extends U, V>) h).getData())
                    .toArray(ArrayUtils.collectToArray(this.dataType));
        }
    }

    @Override
    public V[] getAllData() {
        synchronized (this.services) {
            return this.services.stream()
                    .filter(s -> s instanceof ICompoundServiceHolder)
                    .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData())
                    .toArray(ArrayUtils.collectToArray(this.dataType));
        }
    }
}
