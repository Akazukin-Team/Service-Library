package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
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
public abstract class ACompoundServiceManager<T extends ICompoundServiceHolder<? extends U, V>, U, V>
        extends ASingleServiceManager<T, U> implements ICompoundServiceManager<T, U, V> {
    Class<V> dataType;

    /**
     * Constructs an instance of ACompoundServiceManager, which manages compound services
     * and their associated data types.
     * This manager extends the capabilities of a basic service manager by allowing
     * management of data linked with compound service holders.
     *
     * @param serviceHolderType the class object representing the type of the service holder.
     *                          Must not be null.
     * @param serviceType       the class object representing the type of the service.
     *                          Must not be null.
     * @param dataType          the class object representing the type of data associated with the services.
     */
    public ACompoundServiceManager(@NotNull final Class<T> serviceHolderType, final @NotNull Class<U> serviceType, final Class<V> dataType) {
        super(serviceHolderType, serviceType);
        this.dataType = dataType;
    }

    @Override
    public V getDataByImplementation(final Class<? extends U> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getImplementation().getClass(), service))
                .findFirst()
                .map(ICompoundServiceHolder::getData)
                .orElse(null);
    }

    @Override
    public V getDataByService(final @NotNull U service) {
        return this.services.stream()
                .filter(s -> s.getImplementation() == service)
                .findFirst()
                .map(ICompoundServiceHolder::getData)
                .orElse(null);
    }

    @Override
    public V[] getAllData() {
        return this.services.stream()
                .map(ICompoundServiceHolder::getData)
                .toArray(ArrayUtils.collectToArray(this.dataType));
    }

    @Override
    public T[] getServiceHolderByData(@Nullable final V data) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getData(), data))
                .toArray(ArrayUtils.collectToArray(this.serviceHolderType));
    }
}
