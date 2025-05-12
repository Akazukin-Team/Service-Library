package org.akazukin.service.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A concrete implementation of the {@link IServiceHolder} interface.
 * This class serves as a type-safe holder for managing both the interface
 * class and the implementation of a specific service.
 *
 * @param <T> the type of the service, which must extend {@link IService}
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class ServiceHolder<T extends IService> implements IServiceHolder<T> {
    @Nullable Class<T> interfaceClass;
    @NotNull T implementation;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.interfaceClass);
    }
}
