package org.akazukin.service.manager;

import org.akazukin.service.data.ISingleServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISingleServiceManager<T extends ISingleServiceHolder<? extends U>, U> {
    /**
     * Retrieves a registered service by its specific implementation class.
     *
     * @param <U2>    the type of the service being retrieved, which must extend {@link U}
     * @param service the class object representing the implementation of the service to be retrieved
     * @return the instance of the service matching the specified implementation class, or {@code null} if no service is found
     */
    @Nullable
    <U2 extends U> U2 getServiceByImplementation(@NotNull Class<U2> service);

    /**
     * Registers a service implementation.
     *
     * @param serviceImpl the implementation instance of the service to be registered.
     *                    Must not be {@code null}.
     * @throws IllegalStateException if the class of service implementation is already registered.
     */
    void registerService(@NotNull U serviceImpl);

    /**
     * Retrieves an array of all the registered service instances.
     *
     * @return an array of all services currently registered, or an empty array if no services are registered.
     * Must not be {@code null}.
     */
    @NotNull
    U[] getAllServices();

    /**
     * Unregisters a service implementation from the service manager.
     * This method removes the specified service implementation from the managed collection of services.
     * If the provided implementation is not currently registered, no action is taken.
     *
     * @param serviceImpl the instance of the service implementation to be unregistered;
     *                    must not be null.
     */
    void unregisterService(@NotNull U serviceImpl);

    /**
     * Unregisters a service implementation from the service manager based on its implementation class.
     * This method removes all instances of a registered service that match the provided implementation type.
     * If no matching implementation is registered, no action is taken.
     *
     * @param serviceImpl the class object representing the implementation type of the service to be unregistered;
     *                    must not be null.
     */
    void unregisterServiceByImplementation(@NotNull Class<? extends U> serviceImpl);

    /**
     * Retrieves an array of all service holders that the registered service.
     *
     * @return an array of all services currently registered, or an empty array if no services are registered.
     * Must not be {@code null}.
     */
    @NotNull
    T[] getAllServiceHolders();

    /**
     * Retrieves the service holder associated with the given service implementation class.
     *
     * @param service the class object representing the implementation type of the service.
     *                Must not be null.
     * @return the service holder matching the specified implementation class, or null if no service holder is found.
     */
    @Nullable
    T getServiceHolderByImplementation(@NotNull Class<? extends U> service);

    /**
     * Retrieves the service holder associated with the given service instance.
     *
     * @param service the instance of the service for which the service holder is to be retrieved.
     *                Must not be null.
     * @return the service holder matching the specified service instance, or null if no service holder is found.
     */
    @Nullable
    T getServiceHolderByService(@NotNull U service);
}
