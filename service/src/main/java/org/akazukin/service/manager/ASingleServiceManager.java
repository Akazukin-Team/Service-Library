package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A base abstract class that provides the implementation of a service management system.
 * It enables registering, unregistering, and retrieving services by their interface,
 * implementation, or holders.
 * <p>
 * The service manager is thread-safe and can be used in multithreaded environments.
 *
 * @param <T> The type of the service holder, which extends {@link org.akazukin.service.data.IBlueprintedServiceHolder}.
 * @param <U> The type of the service object managed by this service manager.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@ThreadSafe
public abstract class ASingleServiceManager<T extends IServiceHolder<? extends U>, U> implements IServiceManager<T, U> {
    public static final String EXCE_IMPL_REGISTERED = "The service is already registered; Implementation:";

    Set<T> services = new HashSet<>();
    Class<T> serviceHolderType;
    Class<U> serviceType;

    /**
     * Constructs an instance of AServiceManager with the specified service holder type and service type.
     *
     * @param serviceHolderType the class object representing the type of the service holder.
     *                          Must not be null.
     * @param serviceType       the class object representing the type of the service.
     *                          Must not be null.
     */
    public ASingleServiceManager(@NotNull final Class<T> serviceHolderType, @NotNull final Class<U> serviceType) {
        this.serviceHolderType = serviceHolderType;
        this.serviceType = serviceType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByImplementation(@NotNull final Class<U2> service) {
        return (U2) this.services.stream()
                .map(IServiceHolder::getImplementation)
                .filter(s -> Objects.equals(s.getClass(), service))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized void registerService(final @NotNull U serviceImpl) {
        if (this.services.stream()
                .anyMatch(s -> Objects.equals(s.getImplementation().getClass(), serviceImpl.getClass()))) {
            throw new IllegalStateException(EXCE_IMPL_REGISTERED + serviceImpl.getClass().getName());
        }
        this.services.add(this.createServiceHolder(serviceImpl));
    }

    @Override
    @SuppressWarnings("unused")
    public U[] getAllServices() {
        return this.services.stream()
                .map(IServiceHolder::getImplementation)
                .toArray(ArrayUtils.collectToArray(this.serviceType));
    }

    @Override
    public synchronized void unregisterService(@NotNull final U serviceImpl) {
        this.services.removeIf(h -> h.getImplementation() == serviceImpl);
    }

    @Override
    public synchronized void unregisterServiceByImplementation(@NotNull final Class<? extends U> serviceImpl) {
        this.services.removeIf(h -> Objects.equals(h.getImplementation().getClass(), serviceImpl));
    }

    @Override
    public T[] getAllServiceHolders() {
        return this.services.toArray(ArrayUtils.getNewArray(this.serviceHolderType, 0));
    }

    @Override
    public T getServiceHolderByImplementation(@NotNull final Class<? extends U> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getImplementation().getClass(), service))
                .findFirst()
                .orElse(null);
    }

    @Override
    public T getServiceHolderByService(@NotNull final U service) {
        return this.services.stream()
                .filter(s -> s.getImplementation() == service)
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a service holder for the specified service interface or implementation.
     *
     * @param <U2>        The type of the service, which must extend {@link U}.
     * @param serviceImpl The instance of the service implementation.
     *                    Must not be {@code null}.
     * @return A newly created service holder of type {@link T}.
     * Must not be {@code null}.
     */
    @NotNull
    protected abstract <U2 extends U> T createServiceHolder(final @NotNull U2 serviceImpl);
}
