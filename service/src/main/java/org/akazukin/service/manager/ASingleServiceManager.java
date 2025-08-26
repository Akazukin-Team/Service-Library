package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.ServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A base abstract class that provides the implementation of a service management system.
 * It enables registering, unregistering, and retrieving services by their interface,
 * implementation, or holders.
 * <p>
 * The service manager is thread-safe and can be used in multithreaded environments.
 *
 * @param <U> The type of the service object managed by this service manager.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@ThreadSafe
public abstract class ASingleServiceManager<U> implements IServiceManager<U> {
    public static final String EXCE_IMPL_REGISTERED = "The service is already registered; Implementation:";

    Set<IServiceHolder<? extends U>> services = new HashSet<>();
    Class<U> serviceType;
    List<IServiceManager<U>> subManagers = new ArrayList<>();

    /**
     * Constructs an instance of AServiceManager with the specified service holder type and service type.
     *
     * @param serviceType the class object representing the type of the service.
     *                    Must not be null.
     */
    protected ASingleServiceManager(@NotNull final Class<U> serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByClass(@NotNull final Class<U2> service) {
        final Optional<U2> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .map(IServiceHolder::getImplementation)
                    .filter(s -> Objects.equals(s.getClass(), service))
                    .findFirst()
                    .map(s -> (U2) s);
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                final U2 subService = subManager.getServiceByClass(service);
                if (subService != null) {
                    return subService;
                }
            }
        }
        return null;
    }

    @Override
    public synchronized void registerService(final @NotNull U serviceImpl) {
        synchronized (this.services) {
            if (this.services.stream()
                    .anyMatch(s -> Objects.equals(s.getImplementation().getClass(), serviceImpl.getClass()))) {
                throw new IllegalStateException(EXCE_IMPL_REGISTERED + serviceImpl.getClass().getName());
            }
            this.services.add(this.createHolder(serviceImpl));
        }
    }

    @Override
    @SuppressWarnings("unused")
    public U[] getAllServices() {
        final U[] services;
        synchronized (this.subManagers) {
            services = this.subManagers.stream()
                    .map(IServiceManager::getAllHolders)
                    .flatMap(Arrays::stream)
                    .toArray(ArrayUtils.collectToArray(this.serviceType));
        }

        synchronized (this.services) {
            return ArrayUtils.concat(services,
                    this.services.stream()
                            .map(IServiceHolder::getImplementation)
                            .toArray(ArrayUtils.collectToArray(this.serviceType)));
        }
    }

    @Override
    public synchronized void unregisterService(@NotNull final U serviceImpl) {
        synchronized (this.services) {
            this.services.removeIf(h -> h.getImplementation() == serviceImpl);
        }
    }

    @Override
    public synchronized void unregisterServiceByClass(@NotNull final Class<? extends U> serviceImpl) {
        synchronized (this.services) {
            this.services.removeIf(h -> Objects.equals(h.getImplementation().getClass(), serviceImpl));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public IServiceHolder<? extends U>[] getAllHolders() {
        final IServiceHolder<? extends U>[] services;
        synchronized (this.subManagers) {
            services = this.subManagers.stream()
                    .map(IServiceManager::getAllHolders)
                    .flatMap(Arrays::stream)
                    .toArray(ArrayUtils.collectToArray((Class<IServiceHolder<? extends U>>) (Object) IServiceHolder.class));
        }

        synchronized (this.services) {
            return ArrayUtils.concat(services,
                    this.services.toArray(ArrayUtils.getNewArray((Class<IServiceHolder<? extends U>>) (Object) IServiceHolder.class, 0)));
        }
    }

    @Override
    public IServiceHolder<? extends U> getHolderByClass(@NotNull final Class<? extends U> service) {
        final Optional<IServiceHolder<? extends U>> opt;
        synchronized (service) {
            opt = this.services.stream()
                    .filter(s -> Objects.equals(s.getImplementation().getClass(), service))
                    .findFirst();
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                final IServiceHolder<? extends U> subService = subManager.getHolderByClass(service);
                if (subService != null) {
                    return subService;
                }
            }
        }
        return null;
    }

    @Override
    public IServiceHolder<? extends U> getHolderByService(@NotNull final U service) {
        return this.services.stream()
                .filter(s -> s.getImplementation() == service)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void registerSubServiceManager(final IServiceManager<U> subManager) {
        synchronized (this.subManagers) {
            this.subManagers.add(subManager);
        }
    }

    @Override
    public void unregisterSubServiceManager(final IServiceManager<U> subManager) {
        synchronized (this.subManagers) {
            this.subManagers.remove(subManager);
        }
    }

    /**
     * Creates a service holder for the specified service interface or implementation.
     *
     * @param serviceImpl The instance of the service implementation.
     *                    Must not be {@code null}.
     * @return A newly created service holder.
     * Must not be {@code null}.
     */
    @NotNull
    protected IServiceHolder<? extends U> createHolder(final @NotNull U serviceImpl) {
        return new ServiceHolder<>(serviceImpl);
    }
}
