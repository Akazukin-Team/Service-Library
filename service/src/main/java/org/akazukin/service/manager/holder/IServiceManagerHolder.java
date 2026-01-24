package org.akazukin.service.manager.holder;

import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.manager.IServiceStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface is responsible for managing service stores,
 * allowing the registration, unregistration, and retrieval of services and their holders by various criteria.
 *
 * @param <U> the type of service that the service manager holder will manage
 */
public interface IServiceManagerHolder<U> extends IServiceStore<U> {
    /**
     * Registers a service store.
     *
     * @param store the service store to be registered.
     *              Must not be {@code null}.
     */
    void registerStore(@NotNull IServiceStore<U> store);

    /**
     * Unregisters a service store from the service manager holder.
     * This method removes the specified service store from the managed collection of stores.
     * If the provided store is not currently registered, no action is taken.
     *
     * @param store the service store to be unregistered.
     *              Must not be {@code null}.
     */
    void unregisterStore(@NotNull IServiceStore<U> store);

    /**
     * Retrieves all registered service stores managed by this service manager holder.
     * Each service store represents a collection of services or service holders.
     *
     * @return an array containing all instances of {@link IServiceStore}.
     *         If no service stores are registered, an empty array is returned.
     */
    @NotNull
    IServiceStore<? extends U>[] getAllStores();

    /**
     * Retrieves registered services by their specific implementation class.
     *
     * @param <U2>        the type of the service being retrieved, which must extend {@link U}
     * @param serviceImpl the class object representing the implementation of the service to be retrieved
     * @return an array of service instances matching the specified implementation class, or {@code null} if no services are found
     */
    @Nullable
    <U2 extends U> U2[] getServicesByClass(@NotNull Class<U2> serviceImpl);

    /**
     * Retrieves registered services by their specific service and implementation classes.
     *
     * @param <U2>        the type of the service being retrieved, which must extend {@link U}
     * @param service     the class object representing the service interface to be retrieved
     * @param serviceImpl the class object representing the implementation of the service to be retrieved
     * @return an array of service instances matching the specified service and implementation classes, or {@code null} if no services are found
     */
    @Nullable
    <U2 extends U> U2[] getServicesByStructClass(@NotNull Class<U2> service, @NotNull Class<U2> serviceImpl);

    /**
     * Retrieves service instances based on their interface type.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the class object representing the interface of the service to be retrieved
     * @return an array of service instances matching the specified interface type, or {@code null} if no services are found
     */
    @Nullable
    <U2 extends U> U2[] getServicesByInterfaceClass(@NotNull Class<U2> service);

    /**
     * Retrieves the service holders associated with the given service instance.
     *
     * @param serviceImpl the instance of the service for which the service holders are to be retrieved.
     *                    Must not be {@code null}.
     * @return an array of service holders matching the specified service instance, or {@code null} if no service holders are found.
     */
    @Nullable
    IServiceHolder<? extends U>[] getHoldersByService(@NotNull U serviceImpl);

    /**
     * Retrieves the service holders associated with the given service implementation class.
     *
     * @param <U2>        the type of the service to be retrieved, which must extend {@link U}
     * @param serviceImpl the class object representing the implementation type of the service.
     *                    Must not be {@code null}.
     * @return an array of service holders matching the specified implementation class, or {@code null} if no service holders are found.
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2>[] getHoldersByClass(@NotNull final Class<? extends U2> serviceImpl);

    /**
     * Retrieves the service holders associated with the given service interface type.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the class object representing the interface type of the service.
     *                Must not be {@code null}.
     * @return an array of service holders matching the specified interface type, or {@code null} if no service holders are found.
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2>[] getHoldersByInterfaceClass(@NotNull Class<U2> service);
}
