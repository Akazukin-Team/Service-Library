package org.akazukin.service.manager;

import org.akazukin.service.data.IBlueprintedServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines the contract for managing a collection of services.
 * This interface provides methods for registering, retrieving, and interacting with services by their types,
 * implementations, or unique identifiers.
 *
 * @param <T> the type of service holder being managed, which must extend {@link IBlueprintedServiceHolder}.
 * @param <U> the type of services managed
 */
public interface IBlueprintedServiceManager<T extends IBlueprintedServiceHolder<? extends U>, U> extends IServiceManager<T, U> {
    /**
     * Retrieves a service instance based on its interface type.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the class object representing the interface of the service to be retrieved
     * @return the instance of the service matching the specified interface type, or {@code null} if no service is found
     */
    @Nullable
    <U2 extends U> U2 getServiceByInterface(@NotNull Class<U2> service);

    /**
     * Registers a service implementation with its corresponding service interface.
     * This method allows associating a service interface with a specific implementation.
     *
     * @param <U2>        the type of the service to register, extending the base type {@link U}.
     * @param service     the class object representing the service interface, used as the key for management.
     *                    Must be {@code null} if the service is not associated with a specific interface.
     * @param serviceImpl the implementation instance of the service to register.
     *                    Must not be {@code null}.
     * @throws IllegalStateException if the service is not null and already registered
     *                               or
     */
    <U2 extends U> void registerService(@Nullable Class<U2> service, @NotNull U2 serviceImpl);

    /**
     * Unregisters a service implementation using its interface type.
     * This method removes all instances of services associated with the specified service interface class.
     * If no matching implementation is registered, no action is taken.
     *
     * @param service the class object representing the interface of the service to be unregistered;
     *                must not be null
     */
    void unregisterServiceByInterface(@NotNull Class<? extends U> service);

    /**
     * Retrieves the service holder associated with the given service interface type.
     *
     * @param service the class object representing the interface type of the service.
     *                Must not be null.
     * @return the service holder matching the specified interface type, or null if no service holder is found.
     */
    @Nullable
    T getServiceHolderByInterface(@NotNull Class<? extends U> service);
}
