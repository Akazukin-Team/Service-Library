package org.akazukin.service.manager;

import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.BlueprintedServiceHolder;
import org.akazukin.service.data.IBlueprintedServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of the {@link ABlueprintedServiceManager} class for managing services
 * with {@link IBlueprintedServiceHolder} as the holder type.
 * This class simplifies the process of creating service holders by using a specified type.
 *
 * @param <T> The type of the service being managed by this ServiceManager.
 */
@ThreadSafe
public class BlueprintedServiceManager<T> extends ABlueprintedServiceManager<IBlueprintedServiceHolder<? extends T>, T> {
    /**
     * Constructs a {@link BlueprintedServiceManager} instance for managing services of the specified type.
     * This constructor leverages the {@link IBlueprintedServiceHolder} class for service holder management.
     *
     * @param serviceType The class object representing the type of the service to be managed.
     *                    Must not be {@code null}.
     */
    @SuppressWarnings("unchecked")
    public BlueprintedServiceManager(final @NotNull Class<T> serviceType) {
        super((Class<IBlueprintedServiceHolder<? extends T>>) (Object) IBlueprintedServiceHolder.class, serviceType);
    }

    @Override
    protected <T2 extends T> @NotNull IBlueprintedServiceHolder<? extends T> createServiceHolder(final @Nullable Class<T2> service, @NotNull final T2 serviceImpl) {
        return new BlueprintedServiceHolder<>(service, serviceImpl);
    }
}
