package org.akazukin.service.manager.multi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.manager.ServiceManager;
import org.akazukin.service.registry.IServiceRegistry;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
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
public class MultiServiceManager<U> extends ServiceManager<U> implements IMultiServiceManager<U> {
    Class<U> serviceType;
    @Getter
    IServiceRegistry<U> registry;

    /**
     * Constructs an instance of ASingleServiceManager with the specified service type.
     *
     * @param serviceType The class object representing the type of the service.
     *                    Must not be {@code null}.
     * @param registry    The service registry to be used by this manager.
     *                    Must not be {@code null}.
     */
    public MultiServiceManager(@NotNull final Class<U> serviceType, @NotNull final IServiceRegistry<U> registry) {
        this.serviceType = serviceType;
        this.registry = registry;
    }

    @Override
    @Nullable
    public <U2 extends U> U2[] getServicesByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        return Arrays.stream(this.registry.getAllHolders())
                .filter(h ->
                        Objects.equals(h.getInterfaceClass(), serviceImpl)
                                && Objects.equals(h.getImplementation().getClass(), service))
                .map(IServiceHolder::getImplementation)
                .distinct()
                .toArray(ArrayUtils.collectToArray(service));
    }

    @Override
    public <U2 extends U> U2[] getServicesByInterfaceClass(@NotNull final Class<U2> service) {
        return Arrays.stream(this.registry.getAllHolders())
                .filter(h ->
                        Objects.equals(h.getInterfaceClass(), service))
                .map(IServiceHolder::getImplementation)
                .distinct()
                .toArray(ArrayUtils.collectToArray(service));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2>[] getHoldersByInterfaceClass(@NotNull final Class<U2> service) {
        return Arrays.stream(this.registry.getAllHolders())
                .filter(h -> Objects.equals(h.getInterfaceClass(), service))
                .toArray(IServiceHolder[]::new);
    }
}
