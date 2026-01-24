package org.akazukin.service.manager.single;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface defining management operations for compound service holders and associated data.
 *
 * @param <U> the type of the service managed by the service holder.
 * @param <V> the type of data associated with the service holder.
 */
public interface ICompoundSingleServiceManager<U, V> extends ISingleServiceManager<U> {
    /**
     * Retrieves data associated with the specified service.
     *
     * @param serviceImpl the service whose associated data is to be retrieved.
     *                    Must not be {@code null}.
     * @return the data array associated with the given service, or an empty array if no data is associated with the service.
     *         Must not be {@code null}.
     */
    @NotNull
    V[] getDataByService(@NotNull U serviceImpl);

    /**
     * Retrieves data associated with the given service implementation class.
     *
     * @param serviceImpl the class of the service implementation for which associated data is to be retrieved.
     *                    Must not be {@code null}.
     * @return the data array associated with the given service implementation class, or an empty array if no data is associated.
     *         Must not be {@code null}.
     */
    @NotNull
    V[] getDataByClass(@NotNull Class<? extends U> serviceImpl);

    /**
     * Retrieves data associated with the specified service interface.
     *
     * @param service the service interface class whose associated data is to be retrieved.
     *                Must not be {@code null}.
     * @return the data associated with the given service interface, or {@code null} if no data is associated.
     */
    @Nullable
    V getDataByInterfaceClass(@NotNull Class<? extends U> service);

    /**
     * Retrieves an array of all data associated with the registered services.
     *
     * @return an array of all data associated with services currently registered, or an empty array if no services are registered.
     *         Must not be {@code null}.
     */
    @NotNull
    V[] getAllData();
}
