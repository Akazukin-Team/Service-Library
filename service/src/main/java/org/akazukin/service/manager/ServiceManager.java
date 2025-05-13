package org.akazukin.service.manager;

import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.ServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of the {@link AServiceManager} class for managing services
 * with {@link IServiceHolder} as the holder type.
 * This class simplifies the process of creating service holders by using a specified type.
 *
 * @param <T> The type of the service being managed by this ServiceManager.
 */
public class ServiceManager<T> extends AServiceManager<IServiceHolder<? extends T>, T> {
    /**
     * Constructs a {@link ServiceManager} instance for managing services of the specified type.
     * This constructor leverages the {@link ServiceHolder} class for service holder management.
     *
     * @param serviceType The class object representing the type of the service to be managed.
     *                    Must not be {@code null}.
     */
    @SuppressWarnings("unchecked")
    public ServiceManager(final @NotNull Class<T> serviceType) {
        super((Class<IServiceHolder<? extends T>>) (Object) ServiceHolder.class, serviceType);
    }

    @Override
    protected <T2 extends T> @NotNull IServiceHolder<? extends T> createServiceHolder(final @Nullable Class<T2> service, @NotNull final T2 serviceImpl) {
        return new ServiceHolder<>(service, serviceImpl);
    }
}
