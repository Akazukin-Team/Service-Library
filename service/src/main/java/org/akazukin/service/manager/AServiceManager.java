package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.util.annotation.ThreadSafe;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A base abstract class that provides the implementation of a service management system.
 * It enables registering, unregistering, and retrieving services by their interface,
 * implementation, or holders.
 *
 * @param <T> The type of the service holder, which extends {@link IServiceHolder}.
 * @param <U> The type of the service object managed by this service manager.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@ThreadSafe
public abstract class AServiceManager<T extends IServiceHolder<? extends U>, U> implements IServiceManager<T, U> {
    public static final String EXCE_IMPL_REGISTERED = "The service is already registered; Implementation:";
    public static final String EXCE_INTERFACE_REGISTERED = "An service that associated is already registered; Interface:";

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
    public AServiceManager(@NotNull final Class<T> serviceHolderType, @NotNull final Class<U> serviceType) {
        this.serviceHolderType = serviceHolderType;
        this.serviceType = serviceType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByInterface(@NotNull final Class<U2> service) {
        return (U2) this.services.stream()
                .filter(s -> Objects.equals(s.getInterfaceClass(), service))
                .findFirst()
                .map(IServiceHolder::getImplementation)
                .orElse(null);
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
    public <U2 extends U> void registerService(@Nullable final Class<U2> service, @NotNull final U2 serviceImpl) {
        if (service != null) {
            if (this.services.stream()
                    .anyMatch(s -> Objects.equals(s.getInterfaceClass(), service))) {
                throw new IllegalStateException(EXCE_INTERFACE_REGISTERED + service.getName());
            }
        }
        if (this.services.stream()
                .anyMatch(s -> Objects.equals(s.getImplementation().getClass(), serviceImpl.getClass()))) {
            throw new IllegalStateException(EXCE_IMPL_REGISTERED + serviceImpl.getClass().getName());
        }
        this.services.add(this.createServiceHolder(service, serviceImpl));
    }

    @Override
    public void registerService(final @NotNull U serviceImpl) {
        this.registerService(null, serviceImpl);
    }

    @Override
    @SuppressWarnings("unused")
    public U[] getAllServices() {
        return this.services.stream()
                .map(IServiceHolder::getImplementation)
                .toArray(ArrayUtils.collectToArray(this.serviceType));
    }

    @Override
    public <U2 extends U> void unregisterService(@NotNull final U2 serviceImpl) {
        this.services.removeIf(h -> h.getImplementation() == serviceImpl);
    }

    @Override
    public <U2 extends U> void unregisterServiceByImplementation(@NotNull final Class<U2> serviceImpl) {
        this.services.removeIf(h -> Objects.equals(h.getImplementation().getClass(), serviceImpl));
    }

    @Override
    public <U2 extends U> void unregisterServiceByInterface(@NotNull final Class<U2> service) {
        this.services.removeIf(h -> Objects.equals(h.getInterfaceClass(), service));
    }

    @Override
    public T[] getAllServiceHolders() {
        return this.services.toArray(ArrayUtils.getNewArray(this.serviceHolderType, 0));
    }

    @Override
    public T getServiceHolderByInterface(@NotNull final Class<? extends U> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getInterfaceClass(), service))
                .findFirst()
                .orElse(null);
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
     * @param service     The class object representing the service's interface or implementation.
     *                    Can be {@code null} if there is no associated interface.
     * @param serviceImpl The instance of the service implementation.
     *                    Must not be {@code null}.
     * @return A newly created service holder of type {@link T}.
     * Must not be {@code null}.
     */
    @NotNull
    protected abstract <U2 extends U> T createServiceHolder(final @Nullable Class<U2> service, final @NotNull U2 serviceImpl);
}
