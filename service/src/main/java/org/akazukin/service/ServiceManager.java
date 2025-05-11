package org.akazukin.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.akazukin.util.annotation.ThreadSafe;
import org.akazukin.util.utils.ArrayUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages a collection of services that implement the {@link IService} interface.
 * This class provides functionality for registering services, retrieving them by their interface type,
 * implementation class, or unique identifier, and retrieving all registered services.
 * The class is thread-safe, ensuring safe concurrent access in a multithreaded environment.
 *
 * @param <T> the type of services managed, which must extend {@link IService}
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ThreadSafe
public class ServiceManager<T extends IService> implements IServiceManager<T> {
    Set<ServiceHolder<? extends T>> services = new HashSet<>();
    Class<T> type;

    /**
     * Constructs a new instance of the ServiceManager for managing services of the specified type.
     *
     * @param type the class object representing the type of services to be managed;
     *             must not be null
     */
    public ServiceManager(@NonNull final Class<T> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T2 extends T> T2 getServiceByInterface(final Class<T2> service) {
        return (T2) this.services.stream()
                .filter(s -> s.getInterfaceClass().equals(service))
                .findFirst()
                .map(ServiceHolder::getImplementation)
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T2 extends T> T2 getServiceByImplementation(final Class<T2> service) {
        return (T2) this.services.stream()
                .map(ServiceHolder::getImplementation)
                .filter(s -> s.getClass().equals(service))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized <T2 extends T> void registerService(final Class<T2> service, final T2 serviceImpl) {
        this.services.add(new ServiceHolder<>(service, serviceImpl));
    }

    @Override
    public synchronized <T2 extends T> void registerService(final T2 serviceImpl) {
        this.services.add(new ServiceHolder<>(null, serviceImpl));
    }

    @Override
    public T[] getAllServices() {
        return this.services.stream()
                .map(ServiceHolder::getImplementation)
                .toArray(size -> ArrayUtils.getNewArray(this.type, size));
    }

    @Override
    public T getServiceById(final long serviceId) {
        return this.services.stream()
                .filter(s -> s.getImplementation().getServiceId() == serviceId)
                .findFirst()
                .map(ServiceHolder::getImplementation)
                .orElse(null);
    }
}
