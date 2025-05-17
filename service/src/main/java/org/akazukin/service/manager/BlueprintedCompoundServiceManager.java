package org.akazukin.service.manager;

import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IBlueprintedCompoundServiceHolder;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of the {@link ABlueprintedCompoundServiceManager} class for managing services
 * with {@link org.akazukin.service.data.IBlueprintedCompoundServiceHolder} as the holder type.
 * This class simplifies the process of creating service holders by using a specified type.
 *
 * @param <T> The type of the service being managed by this ServiceManager.
 * @param <U> The type of the data associated with the service being managed by this ServiceManager.
 */
@ThreadSafe
public abstract class BlueprintedCompoundServiceManager<T, U> extends ABlueprintedCompoundServiceManager<IBlueprintedCompoundServiceHolder<? extends T, U>, T, U> {
    /**
     * Constructs a {@link BlueprintedCompoundServiceManager} instance for managing services of the specified type.
     * This constructor leverages the {@link org.akazukin.service.data.IBlueprintedCompoundServiceHolder} class for service holder management.
     *
     * @param serviceType The class object representing the type of the service to be managed.
     *                    Must not be {@code null}.
     * @param dataType    The class object representing the type of the data associated with the service.
     *                    Must not be {@code null}.
     */
    @SuppressWarnings("unchecked")
    public BlueprintedCompoundServiceManager(final @NotNull Class<T> serviceType, @NotNull final Class<U> dataType) {
        super((Class<IBlueprintedCompoundServiceHolder<? extends T, U>>) (Object) IBlueprintedCompoundServiceHolder.class, serviceType, dataType);
    }
}
