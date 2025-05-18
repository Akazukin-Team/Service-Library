package org.akazukin.service.manager;

import org.akazukin.service.data.ICompoundServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface defining management operations for compound service holders and associated data.
 *
 * @param <T> the type of service holder being managed, which must extend {@link ICompoundServiceHolder}.
 * @param <U> the type of the service managed by the service holder.
 * @param <V> the type of data associated with the service holder.
 */
public interface ICompoundServiceManager<T extends ICompoundServiceHolder<? extends U, V>, U, V> extends IServiceManager<T, U> {
    /**
     * Retrieves data associated with the given service implementation class.
     *
     * @param service the class of the service implementation for which associated data is to be retrieved.
     *                Must not be null.
     * @return the data associated with the given service implementation class, or null if no data is associated.
     */
    V getDataByImplementation(Class<? extends U> service);

    /**
     * Retrieves data associated with the specified service.
     *
     * @param service the service whose associated data is to be retrieved.
     *                Must not be {@code null}.
     * @return the data associated with the given service, or {@code null} if no data is associated with the service.
     */
    @Nullable
    V getDataByService(@NotNull U service);

    /**
     * Retrieves an array of all data associated with the registered services.
     *
     * @return an array of all data associated with services currently registered, or an empty array if no services are registered.
     * Must not be {@code null}.
     */
    V[] getAllData();

    /**
     * Retrieves an array of service holders associated with the given data.
     *
     * @param data the data to find associated service holders
     * @return an array of service holders that are associated with the specified data.
     * The returned array is never {@code null} but may be empty if no matching service holders are found.
     */
    @NotNull
    T[] getServiceHolderByData(@Nullable V data);
}
