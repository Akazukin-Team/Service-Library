package org.akazukin.service.manager;

import org.akazukin.service.data.ICompoundServiceHolder;

/**
 * Interface defining management operations for compound service holders and associated data.
 *
 * @param <T> the type of service holder being managed, which must extend {@link ICompoundServiceHolder}.
 * @param <U> the type of the service managed by the service holder.
 * @param <V> the type of data associated with the service holder.
 */
public interface ICompoundServiceManager<T extends ICompoundServiceHolder<? extends U, V>, U, V> extends IServiceManager<T, U>, ICompoundSingleServiceManager<T, U, V> {
    /**
     * Retrieves data associated with the specified service interface.
     *
     * @param service the service interface class whose associated data is to be retrieved.
     *                Must not be {@code null}.
     * @return the data associated with the given service interface, or {@code null} if no data is associated.
     */
    V getDataByInterface(Class<? extends U> service);
}
