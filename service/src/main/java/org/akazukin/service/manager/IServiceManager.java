package org.akazukin.service.manager;

import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.registry.IServiceRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface defining management operations for services.
 *
 * @param <U> the type of the service managed by the service manager.
 */
public interface IServiceManager<U> extends IServiceStore<U> {
    /**
     * Retrieves a service holder by its specific implementation class and service instance.
     *
     * @param <U2>        the type of the service being retrieved, which must extend {@link U}
     * @param serviceImpl the class object representing the implementation of the service to be retrieved
     * @param service     the service instance to be retrieved
     * @return the service holder matching the specified implementation class and service, or {@code null} if no service is found
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2> getHolderByStruct(@NotNull Class<U2> service, @NotNull U2 serviceImpl);

    /**
     * Retrieves registered services by their specific implementation class.
     *
     * @param <U2>        the type of the service being retrieved, which must extend {@link U}
     * @param serviceImpl the class object representing the implementation of the service to be retrieved
     * @return the instances of the services matching the specified implementation class, or {@code null} if no services are found
     */
    @Nullable
    <U2 extends U> U2[] getServicesByClass(@NotNull Class<U2> serviceImpl);

    /**
     * Retrieves the service holders associated with the given service instance.
     *
     * @param serviceImpl the instance of the service for which the service holders are to be retrieved.
     *                    Must not be {@code null}.
     * @return the service holders matching the specified service instance, or {@code null} if no service holders are found.
     */
    @Nullable
    IServiceHolder<? extends U>[] getHoldersByService(@NotNull U serviceImpl);

    /**
     * Retrieves the service holders associated with the given service implementation class.
     *
     * @param <U2>        the type of the service to be retrieved, which must extend {@link U}
     * @param serviceImpl the class object representing the implementation type of the service.
     *                    Must not be {@code null}.
     * @return the service holders matching the specified implementation class, or {@code null} if no service holders are found
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2>[] getHoldersByClass(@NotNull Class<? extends U2> serviceImpl);

    IServiceRegistry<U> getRegistry();
}
