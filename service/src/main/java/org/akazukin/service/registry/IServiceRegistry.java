package org.akazukin.service.registry;

import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.manager.IServiceStore;
import org.jetbrains.annotations.NotNull;

public interface IServiceRegistry<U> extends IServiceStore<U> {

    /**
     * Registers a service implementation.
     *
     * @param serviceImpl the implementation instance of the service to be registered.
     *                    Must not be {@code null}.
     * @throws IllegalStateException if the class of service implementation is already registered.
     */
    void registerService(@NotNull U serviceImpl);

    /**
     * Registers a service implementation with its corresponding service interface.
     * This method allows associating a service interface with a specific implementation.
     *
     * @param <U2>        the type of the service to register, extending the base type {@link U}.
     * @param service     the class object representing the service interface, used as the key for management.
     *                    Must not be {@code null}.
     * @param serviceImpl the implementation instance of the service to register.
     *                    Must not be {@code null}.
     * @throws IllegalStateException if the service is already registered.
     */
    <U2 extends U> void registerService(@NotNull Class<U2> service, @NotNull U2 serviceImpl);

    void registerService(@NotNull IServiceHolder<? extends U> holder);

    /**
     * Unregisters a service implementation from the service manager.
     * This method removes the specified service implementation from the managed collection of services.
     * If the provided implementation is not currently registered, no action is taken.
     *
     * @param serviceImpl the instance of the service implementation to be unregistered.
     *                    Must not be {@code null}.
     */
    void unregisterService(@NotNull U serviceImpl);

    /**
     * Unregisters a service implementation from the service manager based on its implementation class.
     * This method removes all instances of a registered service that match the provided implementation type.
     * If no matching implementation is registered, no action is taken.
     *
     * @param serviceImpl the class object representing the implementation type of the service to be unregistered.
     *                    Must not be {@code null}.
     */
    void unregisterServiceByClass(@NotNull Class<? extends U> serviceImpl);

    /**
     * Unregisters a service implementation using its interface type.
     * This method removes all instances of services associated with the specified service interface class.
     * If no matching implementation is registered, no action is taken.
     *
     * @param service the class object representing the interface of the service to be unregistered.
     *                Must not be {@code null}.
     */
    void unregisterServiceByInterfaceClass(@NotNull Class<? extends U> service);

    /**
     * Unregisters a service implementation using its interface type and implementation class.
     * This method removes services associated with the specified service interface class and implementation class.
     * If no matching implementation is registered, no action is taken.
     *
     * @param <U2>        the type of the service, extending the base type {@link U}.
     * @param service     the class object representing the interface of the service to be unregistered.
     *                    Must not be {@code null}.
     * @param serviceImpl the class object representing the implementation type of the service to be unregistered.
     *                    Must not be {@code null}.
     */
    <U2 extends U> void unregisterServiceByStructClass(@NotNull Class<U2> service, @NotNull Class<? extends U2> serviceImpl);

    /**
     * Unregisters a service implementation using its interface type and service instance.
     * This method removes services associated with the specified service interface class and service instance.
     * If no matching implementation is registered, no action is taken.
     *
     * @param <U2>        the type of the service, extending the base type {@link U}.
     * @param service     the class object representing the interface of the service to be unregistered.
     *                    Must not be {@code null}.
     * @param serviceImpl the service instance to be unregistered.
     *                    Must not be {@code null}.
     */
    <U2 extends U> void unregisterServiceByStruct(@NotNull Class<? super U2> service, @NotNull U2 serviceImpl);

    void unregisterService(@NotNull IServiceHolder<? extends U> holder);
}
