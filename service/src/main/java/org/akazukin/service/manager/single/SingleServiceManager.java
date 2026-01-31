package org.akazukin.service.manager.single;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.manager.ServiceManager;
import org.akazukin.service.registry.IServiceRegistry;
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
public class SingleServiceManager<U> extends ServiceManager<U> implements ISingleServiceManager<U> {
    @Getter
    IServiceRegistry<U> registry;

    /**
     * Constructs an instance of ASingleServiceManager with the specified service type.
     *
     * @param registry The service registry to be used by this manager.
     *                 Must not be {@code null}.
     */
    public SingleServiceManager(@NotNull final IServiceRegistry<U> registry) {
        this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> @Nullable U2 getServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        return (U2) Arrays.stream(this.registry.getAllHolders())
                .filter(h ->
                        Objects.equals(h.getInterfaceClass(), serviceImpl)
                                && Objects.equals(h.getImplementation().getClass(), service))
                .findFirst()
                .map(IServiceHolder::getImplementation)
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2 getServiceByInterfaceClass(@NotNull final Class<U2> service) {
        return (U2) Arrays.stream(this.registry.getAllHolders())
                .filter(h ->
                        Objects.equals(h.getInterfaceClass(), service))
                .findFirst()
                .map(IServiceHolder::getImplementation)
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2> getHolderByInterfaceClass(@NotNull final Class<U2> service) {
        return (IServiceHolder<U2>) Arrays.stream(this.registry.getAllHolders())
                .filter(h -> Objects.equals(h.getInterfaceClass(), service))
                .findFirst()
                .orElse(null);
    }
}
