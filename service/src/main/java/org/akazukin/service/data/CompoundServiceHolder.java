package org.akazukin.service.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A concrete implementation of the {@link IServiceHolder} interface.
 * This class serves as a type-safe holder for managing both the interface
 * class and the implementation of a specific service.
 *
 * @param <T> the type of the service
 */
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class CompoundServiceHolder<T, U> implements ICompoundServiceHolder<T, U> {
    @Nullable
    final Class<T> interfaceClass;
    @NotNull
    final T implementation;
    @Setter
    @Nullable U data;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.interfaceClass);
    }
}
