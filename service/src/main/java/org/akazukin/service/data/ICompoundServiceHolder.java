package org.akazukin.service.data;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a holder interface for managing a compound service that includes both
 * a service type and an additional data type.
 * <p>
 * This interface extends {@link IServiceHolder}, enabling type-safe access to
 * the interface and implementation of the service as well as providing
 * methods for getting and setting supplementary data.
 *
 * @param <T> the type of the service
 * @param <U> the type of the additional data
 */
public interface ICompoundServiceHolder<T, U> extends IServiceHolder<T> {
    /**
     * Retrieves the additional data associated with the compound service holder.
     * The returned data may be {@code null} if it has not been set.
     *
     * @return the associated data of type {@code U}, or {@code null} if no data is set.
     * @see #setData(Object)
     */
    @Nullable
    U getData();

    /**
     * Sets the additional data associated with the compound service holder.
     * The provided data can be {@code null} to clear any previously set data.
     *
     * @param data the associated data of type {@code U} to set, or {@code null} to clear the data.
     * @see #getData()
     */
    void setData(@Nullable U data);
}
