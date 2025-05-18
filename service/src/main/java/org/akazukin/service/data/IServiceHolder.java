package org.akazukin.service.data;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a holder interface for managing a service of a specific type.
 * This interface is designed to provide type-safe access to the implementation of a service.
 *
 * @param <T> the type of the service
 */
public interface IServiceHolder<T> {
    /**
     * Retrieves the concrete implementation of the service managed by the holder.
     * This method provides access to the actual service instance,
     * which is guaranteed to be not {@code null}
     * and can be used to perform operations specific to the service type.
     *
     * @return the implementation of the service managed by the holder
     * Must not be {@code null}.
     */
    @NotNull
    T getImplementation();
}
