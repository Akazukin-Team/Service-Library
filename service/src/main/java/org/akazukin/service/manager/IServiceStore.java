package org.akazukin.service.manager;

import org.akazukin.service.data.IServiceHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Interface defining basic storage operations for service holders.
 *
 * @param <U> the type of the service managed by the service holder.
 */
public interface IServiceStore<U> {
    /**
     * Retrieves an array of all registered service instances.
     *
     * @return an array of all services currently registered, or an empty array if no services are registered.
     * Must not be {@code null}.
     */
    @NotNull
    U[] getAllServices();

    /**
     * Retrieves an array of all service holders that contain the registered services.
     *
     * @return an array of all service holders currently registered, or an empty array if no services are registered.
     * Must not be {@code null}.
     */
    @NotNull
    IServiceHolder<? extends U>[] getAllHolders();

    /**
     * Checks if a service is registered for the specified service instance.
     *
     * @param service the service instance to check.
     *                Must not be {@code null}.
     * @return {@code true} if a service implementation of the specified type is registered.
     * {@code false} otherwise.
     */
    boolean containsService(@NotNull U service);

    /**
     * Checks if a service is registered for the specified implementation class.
     *
     * @param serviceImpl the class object representing the implementation type of the service.
     *                    Must not be {@code null}.
     * @return {@code true} if a service implementation of the specified type is registered.
     * {@code false} otherwise.
     */
    boolean containsServiceByClass(@NotNull Class<? extends U> serviceImpl);

    /**
     * Checks if a service is registered for the specified interface class.
     *
     * @param service the class object representing the interface type of the service.
     *                Must not be {@code null}.
     * @return {@code true} if a service implementation of the specified type is registered.
     * {@code false} otherwise.
     */
    boolean containsServiceByInterface(@NotNull Class<? extends U> service);

    /**
     * Checks if a service is registered for the specified interface and implementation classes.
     *
     * @param <U2>        the type of the service, extending the base type {@link U}.
     * @param service     the class object representing the interface type of the service.
     *                    Must not be {@code null}.
     * @param serviceImpl the class object representing the implementation type of the service.
     *                    Must not be {@code null}.
     * @return {@code true} if a service implementation of the specified type is registered.
     * {@code false} otherwise.
     */
    <U2 extends U> boolean containsServiceByStructClass(@NotNull Class<U2> service, @NotNull Class<? extends U2> serviceImpl);

    /**
     * Checks if a service is registered for the specified interface and service instance.
     *
     * @param <U2>        the type of the service, extending the base type {@link U}.
     * @param service     the class object representing the interface type of the service.
     *                    Must not be {@code null}.
     * @param serviceImpl the service instance to check.
     *                    Must not be {@code null}.
     * @return {@code true} if a service implementation of the specified type is registered.
     * {@code false} otherwise.
     */
    <U2 extends U> boolean containsServiceByStruct(@NotNull Class<? super U2> service, @NotNull U2 serviceImpl);
}
