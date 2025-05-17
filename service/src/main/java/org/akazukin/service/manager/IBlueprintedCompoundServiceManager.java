package org.akazukin.service.manager;

import org.akazukin.service.data.IBlueprintedCompoundServiceHolder;

/**
 * Interface defining management operations for compound service holders and associated data.
 *
 * @param <T> the type of service holder being managed, which must extend {@link IBlueprintedCompoundServiceHolder}.
 * @param <U> the type of the service managed by the service holder.
 * @param <V> the type of data associated with the service holder.
 */
public interface IBlueprintedCompoundServiceManager<T extends IBlueprintedCompoundServiceHolder<? extends U, V>, U, V> extends IBlueprintedServiceManager<T, U>, ICompoundServiceManager<T, U, V> {
    /**
     * Retrieves data associated with the specified service interface.
     *
     * @param service the service interface class whose associated data is to be retrieved.
     *                Must not be {@code null}.
     * @return the data associated with the given service interface, or {@code null} if no data is associated.
     */
    V getDataByInterface(Class<? extends U> service);
}
