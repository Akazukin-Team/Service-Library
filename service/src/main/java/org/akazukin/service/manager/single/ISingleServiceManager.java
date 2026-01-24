package org.akazukin.service.manager.single;

import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.manager.IServiceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface defining management operations for service holders.
 *
 * @param <U> the type of the service managed by the service holder.
 */
public interface ISingleServiceManager<U> extends IServiceManager<U> {
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
     * Retrieves a registered service by its specific implementation class.
     *
     * @param <U2>        the type of the service being retrieved, which must extend {@link U}
     * @param service     the class object representing the interface of the service to be retrieved
     * @param serviceImpl the class object representing the implementation of the service to be retrieved
     * @return the instance of the service matching the specified implementation class, or {@code null} if no service is found
     */
    @Nullable
    <U2 extends U> U2 getServiceByStructClass(@NotNull Class<U2> service, @NotNull Class<? extends U2> serviceImpl);

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

    /**
     * Retrieves the service holder associated with the given service interface type.
     *
     * @param <U2>    the type of the service to be retrieved, which must extend {@link U}
     * @param service the class object representing the interface type of the service.
     *                Must not be {@code null}.
     * @return the service holder matching the specified interface type, or {@code null} if no service holder is found
     */
    @Nullable
    <U2 extends U> IServiceHolder<U2> getHolderByInterfaceClass(@NotNull Class<U2> service);
}
