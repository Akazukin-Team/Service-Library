package org.akazukin.service.manager.single;

import org.akazukin.service.data.IServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface defining management operations for service holders.
 *
 * @param <U> the type of the service managed by the service holder.
 */
public interface ISingleServiceManager<U> {
    /**
     * Retrieves a registered service by its specific implementation class.
     *
     * @param <U2>    the type of the service being retrieved, which must extend {@link U}
     * @param service the class object representing the implementation of the service to be retrieved
     * @return the instance of the service matching the specified implementation class, or {@code null} if no service is found
     */
    @Nullable
    <U2 extends U> U2 getServiceByClass(@NotNull Class<U2> service);

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
    void unregisterServiceByClass(@NotNull Class<? extends U> serviceImpl);

    /**
     * Retrieves an array of all service holders that the registered service.
     *
     * @return an array of all services currently registered, or an empty array if no services are registered.
     * Must not be {@code null}.
     */
    @NotNull
    IServiceHolder<? extends U>[] getAllHolders();

    /**
     * Retrieves the service holder associated with the given service implementation class.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the class object representing the implementation type of the service.
     *                Must not be null.
     * @return the service holder matching the specified implementation class, or null if no service holder is found.
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2> getHolderByClass(@NotNull Class<U2> service);

    /**
     * Retrieves the service holder associated with the given service instance.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the instance of the service for which the service holder is to be retrieved.
     *                Must not be null.
     * @return the service holder matching the specified service instance, or null if no service holder is found.
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2> getHolderByService(@NotNull U2 service);

    /**
     * Registers a submanager that will manage a subset of services within the current service hierarchy.
     * This allows hierarchical service management where submanagers can oversee specific services independently.
     *
     * @param subMgr the submanager to be registered.
     *               Must be an instance of {@link ISingleServiceManager}, managing the same type parameter {@link U}.
     *               Must not be {@code null}.
     */
    void registerSubManager(ISingleServiceManager<U> subMgr);

    /**
     * Unregisters a submanager from the current service manager.
     * This method allows the removal of a submanager that was previously registered and managing a subset of services.
     *
     * @param subMgr the submanager to be unregistered.
     *               Must be an instance of {@link ISingleServiceManager}, managing the same type parameter {@link U}.
     *               Should not be {@code null}.
     */
    void unregisterSubManager(ISingleServiceManager<U> subMgr);

    /**
     * Registers the specified parent manager for the current service manager.
     * The parent manager is responsible for overseeing and managing the services
     * provided by this service manager, forming a hierarchical relationship.
     *
     * @param parentMgr the parent manager to be registered.
     *                  Must be an instance of {@link ISingleServiceManager} managing the same type parameter {@link U}.
     *                  Must not be {@code null}.
     */
    void registerParentManager(ISingleServiceManager<U> parentMgr);

    /**
     * Unregisters the parent manager associated with the current service manager.
     * The parent manager oversees and manages the services provided by this service manager,
     * and unregistering it will remove this hierarchical link.
     *
     * @param parentMgr the parent manager to be unregistered.
     *                  Must be an instance of {@link ISingleServiceManager} managing the same type parameter {@link U}.
     *                  Must not be {@code null}.
     */
    void unregisterParentManager(ISingleServiceManager<U> parentMgr);

    /**
     * Retrieves a service instance based on its interface type.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the class object representing the interface of the service to be retrieved
     * @return the instance of the service matching the specified interface type, or {@code null} if no service is found
     */
    @Nullable
    <U2 extends U> U2 getServiceByInterfaceClass(@NotNull Class<U2> service);

    /**
     * Registers a service implementation with its corresponding service interface.
     * This method allows associating a service interface with a specific implementation.
     *
     * @param <U2>        the type of the service to register, extending the base type {@link U}.
     * @param service     the class object representing the service interface, used as the key for management.
     *                    Must not be {@code null}.
     * @param serviceImpl the implementation instance of the service to register.
     *                    Must not be {@code null}.
     * @throws IllegalStateException if the service is not null and already registered
     *                               or
     */
    <U2 extends U> void registerService(@NotNull Class<U2> service, @NotNull U2 serviceImpl);

    /**
     * Unregisters a service implementation using its interface type.
     * This method removes all instances of services associated with the specified service interface class.
     * If no matching implementation is registered, no action is taken.
     *
     * @param service the class object representing the interface of the service to be unregistered;
     *                must not be null
     */
    void unregisterServiceByInterfaceClass(@NotNull Class<? extends U> service);

    /**
     * Retrieves the service holder associated with the given service interface type.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the class object representing the interface type of the service.
     *                Must not be {@code null}.
     * @return the service holder matching the specified interface type, or null if no service holder is found.
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2> getHolderByInterfaceClass(@NotNull Class<U2> service);

    /**
     * Checks if a service is registered for the specified implementation class.
     *
     * @param service the class object representing the implementation type of the service.
     *                Must not be {@code null}.
     * @return {@code true} if a service implementation of the specified type is registered;
     * {@code false} otherwise.
     */
    boolean isExistsService(@NotNull Class<? extends U> service);

    /**
     * Checks if a service of the specified implementation type exists within
     * the currently managed service collection or sub service collections.
     *
     * @param service the class object representing the implementation type of the service.
     *                Must not be {@code null}.
     * @return {@code true} if a service of the specified implementation type is found
     * within the service hierarchy; {@code false} otherwise.
     */
    boolean isExistsServiceDeeply(@NotNull Class<? extends U> service);

    /**
     * Checks if a service of the specified implementation type exists within
     * the currently managed service collection, submanagers or parent managers.
     *
     * @param service the class object representing the implementation type of the service.
     *                Must not be {@code null}.
     * @return {@code true} if a service of the specified implementation type is found
     * within the service hierarchy; {@code false} otherwise.
     */
    boolean isExistsServiceDeeplyWithParent(@NotNull Class<? extends U> service);

    /**
     * Checks if a service of the specified implementation type exists within
     * the currently managed service collection, submanagers, or parent managers,
     * using an optional exclusion for a specific service manager.
     *
     * @param service     the class object representing the implementation type of the service.
     *                    Must not be {@code null}.
     * @param executedMgr the service manager to exclude during the search for the service.
     *                    Can be {@code null} if no exclusion is needed.
     * @return {@code true} if a service of the specified implementation type is found
     * within the service hierarchy; {@code false} otherwise.
     */
    boolean isExistsServiceDeeplyWithParent(@NotNull Class<? extends U> service, ISingleServiceManager<U> executedMgr);

    /**
     * Checks if a service of the specified implementation type exists within
     * the currently managed service collection or submanagers,
     * using an optional exclusion for a specific service manager.
     *
     * @param service     The class type of the service to be checked.
     *                    Must not be null.
     *                    Use {@link Class} to refer to the type of the service.
     * @param executedMgr The manager context where the service presence is checked.
     *                    May be null if no specific manager is provided.
     *                    Use {@link ISingleServiceManager} to define the manager context.
     * @return True if the service exists deeply in the context of the provided manager.
     * False otherwise.
     */
    boolean isExistsServiceDeeply(@NotNull Class<? extends U> service, @Nullable ISingleServiceManager<U> executedMgr);
}
