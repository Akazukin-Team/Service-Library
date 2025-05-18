package org.akazukin.service.manager;

import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IBlueprintedServiceHolder;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.BlueprintedServiceHolder;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of the {@link ASingleServiceManager} class for managing services
 * with {@link org.akazukin.service.data.IServiceHolder} as the holder type.
 * This class simplifies the process of creating service holders by using a specified type.
 *
 * @param <T> The type of the service being managed by this ServiceManager.
 */
@ThreadSafe
public class ServiceManager<T> extends ASingleServiceManager<IServiceHolder<? extends T>, T> {
    /**
     * Constructs a {@link ServiceManager} instance for managing services of the specified type.
     * This constructor leverages the {@link org.akazukin.service.data.IServiceHolder} class for service holder management.
     *
     * @param serviceType The class object representing the type of the service to be managed.
     *                    Must not be {@code null}.
     */
    @SuppressWarnings("unchecked")
    public ServiceManager(final @NotNull Class<T> serviceType) {
        super((Class<IServiceHolder<? extends T>>) (Object) IServiceHolder.class, serviceType);
    }

    @Override
    protected <T2 extends T> @NotNull IBlueprintedServiceHolder<? extends T> createServiceHolder(@NotNull final T2 serviceImpl) {
        return new BlueprintedServiceHolder<>(null, serviceImpl);
    }
}
