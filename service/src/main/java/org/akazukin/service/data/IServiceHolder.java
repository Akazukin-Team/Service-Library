package org.akazukin.service.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a holder interface for managing a service of a specific type.
 * This interface is designed to provide type-safe access to both the interface
 * class and the implementation of a service.
 *
 * @param <T> the type of the service, which must extend {@link IService}
 */
public interface IServiceHolder<T extends IService> {
    /**
     * Retrieves the interface class associated with the service type managed by the holder.
     * This method returns the class object representing the service interface,
     * which may be useful for identifying or retrieving services
     * based on their interface types.
     *
     * @return the class object of the service interface,
     * or {@code null} if the service is not explicitly associated with an interface class
     */
    @Nullable
    Class<T> getInterfaceClass();

    /**
     * Retrieves the concrete implementation of the service managed by the holder.
     * This method provides access to the actual service instance,
     * which is guaranteed to be non-null
     * and can be used to perform operations specific to the service type.
     *
     * @return the non-null implementation of the service managed by the holder
     */
    @NotNull
    T getImplementation();
}
