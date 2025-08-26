package org.akazukin.service.manager;

import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.BlueprintedServiceHolder;
import org.akazukin.service.data.IBlueprintedServiceHolder;
import org.akazukin.service.data.IServiceHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * A base abstract class that provides the implementation of a service management system.
 * It enables registering, unregistering, and retrieving services by their interface,
 * implementation, or holders.
 * <p>
 * The service manager is thread-safe and can be used in multithreaded environments.
 *
 * @param <U> The type of the service object managed by this service manager.
 */
@ThreadSafe
public abstract class ABlueprintedServiceManager<U> extends ASingleServiceManager<U> implements IBlueprintedServiceManager<U> {
    public static final String EXCE_INTERFACE_REGISTERED = "An service that associated is already registered; Interface:";

    /**
     * Constructs an instance of AServiceManager with the specified service holder type and service type.
     *
     * @param serviceType the class object representing the type of the service.
     *                    Must not be null.
     */
    protected ABlueprintedServiceManager(@NotNull final Class<U> serviceType) {
        super(serviceType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByInterface(@NotNull final Class<U2> service) {
        final Optional<U2> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .filter(s -> s instanceof IBlueprintedServiceHolder
                            && Objects.equals(((IBlueprintedServiceHolder<? extends U>) s).getInterfaceClass(), service))
                    .findFirst()
                    .map(IServiceHolder::getImplementation)
                    .map(s -> (U2) s);
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                if (subManager instanceof IBlueprintedServiceManager) {
                    final U2 subService = ((IBlueprintedServiceManager<U>) subManager).getServiceByInterface(service);
                    if (subService != null) {
                        return subService;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public <U2 extends U> void registerService(@NotNull final Class<U2> service, @NotNull final U2 serviceImpl) {
        if (this.services.stream()
                .anyMatch(s -> s instanceof IBlueprintedServiceHolder
                        && Objects.equals(((IBlueprintedServiceHolder<? extends U>) s).getInterfaceClass(), service))) {
            throw new IllegalStateException(EXCE_INTERFACE_REGISTERED + service.getName());
        }
        if (this.services.stream()
                .anyMatch(s -> Objects.equals(s.getImplementation().getClass(), serviceImpl.getClass()))) {
            throw new IllegalStateException(ASingleServiceManager.EXCE_IMPL_REGISTERED + serviceImpl.getClass().getName());
        }
        this.services.add(this.createServiceHolder(service, serviceImpl));
    }

    /**
     * Creates a service holder for the specified service interface or implementation.
     *
     * @param <U2>        The type of the service, which must extend {@link U}.
     * @param service     The class object representing the service's interface or implementation.
     *                    Can be {@code null} if there is no associated interface.
     * @param serviceImpl The instance of the service implementation.
     *                    Must not be {@code null}.
     * @return A newly created service holder.
     * Must not be {@code null}.
     */
    @NotNull
    protected <U2 extends U> IBlueprintedServiceHolder<U2> createServiceHolder(final @NotNull Class<U2> service, final @NotNull U2 serviceImpl) {
        return new BlueprintedServiceHolder<>(service, serviceImpl);
    }

    @Override
    public void unregisterServiceByInterface(@NotNull final Class<? extends U> service) {
        this.services.removeIf(h -> h instanceof IBlueprintedServiceHolder
                && Objects.equals(((IBlueprintedServiceHolder<? extends U>) h).getInterfaceClass(), service));
    }

    @Override
    public IBlueprintedServiceHolder<? extends U> getServiceHolderByInterface(@NotNull final Class<? extends U> service) {
        final Optional<IBlueprintedServiceHolder<? extends U>> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .filter(s -> s instanceof IBlueprintedServiceHolder
                            && Objects.equals(((IBlueprintedServiceHolder<? extends U>) s).getInterfaceClass(), service))
                    .findFirst()
                    .map(s -> (IBlueprintedServiceHolder<? extends U>) s);
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                if (subManager instanceof IBlueprintedServiceManager) {
                    final IBlueprintedServiceHolder<? extends U> subService = ((IBlueprintedServiceManager<U>) subManager).getServiceHolderByInterface(service);
                    if (subService != null) {
                        return subService;
                    }
                }
            }
        }
        return null;
    }
}
