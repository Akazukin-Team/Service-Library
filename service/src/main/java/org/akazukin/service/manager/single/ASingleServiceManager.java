package org.akazukin.service.manager.single;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.BlueprintedServiceHolder;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.ServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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

    Collection<IServiceHolder<? extends U>> services = new HashSet<>();
    @Getter
    Class<U> serviceType;

    /**
     * Constructs an instance of ASingleServiceManager with the specified service type.
     *
     * @param serviceType The class object representing the type of the service.
     *                    Must not be {@code null}.
     */
    protected ASingleServiceManager(@NotNull final Class<U> serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public <U2 extends U> U2[] getServicesByClass(@NotNull final Class<U2> serviceImpl) {
        synchronized (this.services) {
            return this.services.stream()
                    .filter(s -> Objects.equals(s.getImplementation().getClass(), serviceImpl))
                    .map(IServiceHolder::getImplementation)
                    .toArray(ArrayUtils.collectToArray(serviceImpl));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> @Nullable U2 getServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        synchronized (this.services) {
            return (U2) this.services.stream()
                    .filter(h ->
                            Objects.equals(h.getInterfaceClass(), serviceImpl)
                                    && Objects.equals(h.getImplementation().getClass(), service))
                    .findFirst()
                    .map(IServiceHolder::getImplementation)
                    .orElse(null);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByInterfaceClass(@NotNull final Class<U2> service) {
        synchronized (this.services) {
            return (U2) this.services.stream()
                    .filter(h ->
                            Objects.equals(h.getInterfaceClass(), service))
                    .findFirst()
                    .map(IServiceHolder::getImplementation)
                    .orElse(null);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public IServiceHolder<? extends U>[] getHoldersByService(@NotNull final U serviceImpl) {
        return this.services.stream()
                .filter(h -> h.getImplementation() == serviceImpl)
                .toArray(ArrayUtils.collectToArray(IServiceHolder.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2>[] getHoldersByClass(@NotNull final Class<? extends U2> serviceImpl) {
        synchronized (this.services) {
            return this.services.stream()
                    .filter(h -> Objects.equals(h.getImplementation().getClass(), serviceImpl))
                    .toArray(ArrayUtils.collectToArray(IServiceHolder.class));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2> getHolderByInterfaceClass(@NotNull final Class<U2> service) {
        synchronized (this.services) {
            return (IServiceHolder<U2>) this.services.stream()
                    .filter(h -> Objects.equals(h.getInterfaceClass(), service))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    @SuppressWarnings("unused")
    public U[] getAllServices() {
        synchronized (this.services) {
            return this.services.stream()
                    .map(IServiceHolder::getImplementation)
                    .toArray(ArrayUtils.collectToArray(this.serviceType));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public IServiceHolder<? extends U>[] getAllHolders() {
        synchronized (this.services) {
            return this.services.toArray(
                    ArrayUtils.getNewArray(
                            (Class<IServiceHolder<? extends U>>) (Object) IServiceHolder.class, 0));
        }
    }

    @Override
    public boolean isExistsService(@NotNull final U service) {
        synchronized (this.services) {
            return this.services.stream()
                    .anyMatch(h -> h.getImplementation() == service);
        }
    }

    @Override
    public boolean isExistsServiceByClass(final @NotNull Class<? extends U> serviceImpl) {
        synchronized (this.services) {
            return this.services.stream()
                    .anyMatch(h -> Objects.equals(h.getInterfaceClass(), serviceImpl));
        }
    }

    @Override
    public boolean isExistsServiceByInterface(@NotNull final Class<? extends U> service) {
        synchronized (this.services) {
            return this.services.stream()
                    .anyMatch(h -> Objects.equals(h.getInterfaceClass(), service));
        }
    }

    @Override
    public <U2 extends U> boolean isExistsServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        synchronized (this.services) {
            return this.services.stream()
                    .anyMatch(h ->
                            Objects.equals(h.getImplementation().getClass(), serviceImpl)
                                    && Objects.equals(h.getInterfaceClass(), service));
        }
    }

    @Override
    public <U2 extends U> boolean isExistsServiceByStruct(@NotNull final Class<? super U2> service, @NotNull final U2 serviceImpl) {
        synchronized (this.services) {
            return this.services.stream()
                    .anyMatch(h ->
                            h.getImplementation() == serviceImpl
                                    && Objects.equals(h.getInterfaceClass(), service));
        }
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2> getHolderByStruct(@NotNull final Class<U2> service, @NotNull final U2 serviceImpl) {
        synchronized (this.services) {
            return (IServiceHolder<U2>) this.services.stream()
                    .filter(h ->
                            h.getImplementation() == serviceImpl
                                    && Objects.equals(h.getInterfaceClass(), service))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void registerService(final @NotNull U serviceImpl) {
        synchronized (this.services) {
            if (this.isExistsServiceByClass((Class<? extends U>) serviceImpl.getClass())) {
                throw new IllegalStateException(EXCE_REGISTERED + serviceImpl.getClass().getName());
            }
            this.services.add(this.createHolder(serviceImpl));
        }
    }

    @Override
    public <U2 extends U> void registerService(@NotNull final Class<U2> service, @NotNull final U2 serviceImpl) {
        synchronized (this.services) {
            if (this.isExistsServiceByClass(service)) {
                throw new IllegalStateException(EXCE_REGISTERED + serviceImpl.getClass().getName());
            }
            this.services.add(this.createHolder(service, serviceImpl));
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
    public void unregisterServiceByInterfaceClass(@NotNull final Class<? extends U> service) {
        this.services.removeIf(h -> Objects.equals(h.getInterfaceClass(), service));
    }

    @Override
    public <U2 extends U> void unregisterServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
    }

    @Override
    public <U2 extends U> void unregisterServiceByStruct(@NotNull final Class<? super U2> service, @NotNull final U2 serviceImpl) {
    }

    /**
     * Creates a service holder for the specified service interface or implementation.
     *
     * @param <U2>        The type of the service, which must extend {@link U}.
     * @param service     The class object representing the service's interface or implementation.
     *                    Can be {@code null} if there is no associated interface.
     * @param serviceImpl The instance of the service implementation.
     *                    Must not be {@code null}.
     * @return a newly created service holder.
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
}
