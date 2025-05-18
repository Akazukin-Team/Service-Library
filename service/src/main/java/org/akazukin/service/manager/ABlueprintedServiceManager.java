package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IBlueprintedServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
public abstract class ABlueprintedServiceManager<T extends IBlueprintedServiceHolder<? extends U>, U> extends ASingleServiceManager<T, U> implements IBlueprintedServiceManager<T, U> {
    public static final String EXCE_INTERFACE_REGISTERED = "An service that associated is already registered; Interface:";

    /**
     * Constructs an instance of AServiceManager with the specified service holder type and service type.
     *
     * @param serviceHolderType the class object representing the type of the service holder.
     *                          Must not be null.
     * @param serviceType       the class object representing the type of the service.
     *                          Must not be null.
     */
    public ABlueprintedServiceManager(@NotNull final Class<T> serviceHolderType, @NotNull final Class<U> serviceType) {
        super(serviceHolderType, serviceType);
    }

    @Override
    @NotNull
    protected <U2 extends U> T createServiceHolder(@NotNull final U2 serviceImpl) {
        return this.createServiceHolder(null, serviceImpl);
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

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByInterface(@NotNull final Class<U2> service) {
        return (U2) this.services.stream()
                .filter(s -> Objects.equals(s.getInterfaceClass(), service))
                .findFirst()
                .map(IBlueprintedServiceHolder::getImplementation)
                .orElse(null);
    }

    @Override
    public <U2 extends U> void registerService(@Nullable final Class<U2> service, @NotNull final U2 serviceImpl) {
        if (service == null) {
            this.registerService(serviceImpl);
        } else {
            if (this.services.stream()
                    .anyMatch(s -> Objects.equals(s.getInterfaceClass(), service))) {
                throw new IllegalStateException(EXCE_INTERFACE_REGISTERED + service.getName());
            }
            if (this.services.stream()
                    .anyMatch(s -> Objects.equals(s.getImplementation().getClass(), serviceImpl.getClass()))) {
                throw new IllegalStateException(ASingleServiceManager.EXCE_IMPL_REGISTERED + serviceImpl.getClass().getName());
            }
            this.services.add(this.createServiceHolder(service, serviceImpl));
        }
    }

    @Override
    public void unregisterServiceByInterface(@NotNull final Class<? extends U> service) {
        this.services.removeIf(h -> Objects.equals(h.getInterfaceClass(), service));
    }

    @Override
    public T getServiceHolderByInterface(@NotNull final Class<? extends U> service) {
        return this.services.stream()
                .filter(s -> Objects.equals(s.getInterfaceClass(), service))
                .findFirst()
                .orElse(null);
    }
}
