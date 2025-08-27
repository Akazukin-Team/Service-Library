package org.akazukin.service.manager.single;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.BlueprintedServiceHolder;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.ServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A base abstract class that provides the implementation of a service management system.
 * It enables registering, unregistering, and retrieving services
 * by their interface, implementation, or holders.
 * <p>
 * The service manager is thread-safe and can be used in multithreaded environments.
 *
 * @param <U> The type of the service object managed by this service manager.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@ThreadSafe
public abstract class ASingleServiceManager<U> implements ISingleServiceManager<U> {
    public static final String EXCE_REGISTERED = "An service that associated is already registered; Interface:";

    Set<IServiceHolder<? extends U>> services = new HashSet<>();
    Class<U> serviceType;

    Set<ISingleServiceManager<U>> subManagers = new HashSet<>();

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
            for (final ISingleServiceManager<U> subManager : this.subManagers) {
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
            if (this.isExistsService((Class<? extends U>) serviceImpl.getClass())) {
                throw new IllegalStateException(EXCE_REGISTERED + serviceImpl.getClass().getName());
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
                    .map(ISingleServiceManager::getAllHolders)
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
                    .map(ISingleServiceManager::getAllHolders)
                    .flatMap(Arrays::stream)
                    .toArray(ArrayUtils.collectToArray((Class<IServiceHolder<? extends U>>) (Object) IServiceHolder.class));
        }

        synchronized (this.services) {
            return ArrayUtils.concat(services,
                    this.services.toArray(ArrayUtils.getNewArray((Class<IServiceHolder<? extends U>>) (Object) IServiceHolder.class, 0)));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2> getHolderByClass(@NotNull final Class<U2> service) {
        final Optional<IServiceHolder<U2>> opt;
        synchronized (service) {
            opt = this.services.stream()
                    .filter(s -> Objects.equals(s.getImplementation().getClass(), service))
                    .findFirst()
                    .map(s -> (IServiceHolder<U2>) s);
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final ISingleServiceManager<U> subManager : this.subManagers) {
                final IServiceHolder<U2> subService = subManager.getHolderByClass(service);
                if (subService != null) {
                    return subService;
                }
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2> getHolderByService(@NotNull final U2 service) {
        return (IServiceHolder<U2>) this.services.stream()
                .filter(s -> s.getImplementation() == service)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void registerSubManager(final ISingleServiceManager<U> subManager) {
        synchronized (this.subManagers) {
            this.subManagers.add(subManager);
        }
    }

    @Override
    public void unregisterSubManager(final ISingleServiceManager<U> subManager) {
        synchronized (this.subManagers) {
            this.subManagers.remove(subManager);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByInterfaceClass(@NotNull final Class<U2> service) {
        final Optional<U2> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .filter(s -> s instanceof IServiceHolder
                            && Objects.equals(((IServiceHolder<? extends U>) s).getInterfaceClass(), service))
                    .findFirst()
                    .map(IServiceHolder::getImplementation)
                    .map(s -> (U2) s);
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final ISingleServiceManager<U> subManager : this.subManagers) {
                if (subManager instanceof ISingleServiceManager) {
                    final U2 subService = subManager.getServiceByInterfaceClass(service);
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
        synchronized (this.services) {
            if (this.isExistsService(service)) {
                throw new IllegalStateException(EXCE_REGISTERED + serviceImpl.getClass().getName());
            }
            this.services.add(this.createHolder(service, serviceImpl));
        }
    }

    @Override
    public void unregisterServiceByInterfaceClass(@NotNull final Class<? extends U> service) {
        this.services.removeIf(h -> h instanceof IServiceHolder
                && Objects.equals(((IServiceHolder<? extends U>) h).getInterfaceClass(), service));
    }

    @Override
    public <U2 extends U> IServiceHolder<U2> getHolderByInterfaceClass(@NotNull final Class<U2> service) {
        final Optional<IServiceHolder<U2>> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .filter(s -> s instanceof IServiceHolder)
                    .map(s -> (IServiceHolder<U2>) s)
                    .filter(s -> Objects.equals(s.getInterfaceClass(), service))
                    .findFirst();
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final ISingleServiceManager<U> subManager : this.subManagers) {
                if (subManager instanceof ISingleServiceManager) {
                    final IServiceHolder<? extends U> subService = subManager.getHolderByInterfaceClass(service);
                    if (subService != null) {
                        return (IServiceHolder<U2>) subService;
                    }
                }
            }
        }
        return null;
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
    protected <U2 extends U> IServiceHolder<U2> createHolder(final @NotNull Class<U2> service, final @NotNull U2 serviceImpl) {
        return new BlueprintedServiceHolder<>(service, serviceImpl);
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

    public boolean isExistsService(final @NotNull Class<? extends U> service) {
        return this.services.stream()
                .anyMatch(s -> Objects.equals(s.getInterfaceClass(), service));
    }
}
