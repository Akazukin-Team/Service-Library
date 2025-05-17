package org.akazukin.service.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A concrete implementation of the {@link IBlueprintedServiceHolder} interface.
 * This class serves as a type-safe holder for managing both the interface
 * class and the implementation of a specific service.
 *
 * @param <T> the type of the service
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class ServiceHolder<T> implements IServiceHolder<T> {
    @NotNull T implementation;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.implementation.getClass());
    }
}
