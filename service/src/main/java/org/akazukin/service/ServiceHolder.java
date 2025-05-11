package org.akazukin.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a container for associating a service interface type with its corresponding implementation.
 * This class is intended to be used for managing and organizing services in a type-safe manner.
 *
 * @param <T> the type of the service, which must extend {@link IService}
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class ServiceHolder<T extends IService> {
    /**
     * Represents the interface class associated with the service type managed by this holder.
     * This field may be {@code null} if the service implementation is not explicitly associated
     * with an interface class.
     * Typically used to identify or retrieve services by their interface
     * type within a service management system.
     */
    @Nullable Class<T> interfaceClass;

    /**
     * Represents the concrete implementation of the service managed by this holder.
     * This field is guaranteed to be non-null and contains the actual instance of the service.
     * It is used to access the functionality of the service, allowing operations suitable
     * for its specific type.
     */
    @NotNull T implementation;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.interfaceClass);
    }
}
