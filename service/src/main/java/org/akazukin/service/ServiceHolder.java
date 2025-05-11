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
    @Nullable Class<T> interfaceClass;
    @NotNull T implementation;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.interfaceClass);
    }
}
