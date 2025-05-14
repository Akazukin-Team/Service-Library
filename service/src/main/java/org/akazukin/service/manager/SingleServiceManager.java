package org.akazukin.service.manager;

import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.ISingleServiceHolder;
import org.akazukin.service.data.ServiceHolder;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of the {@link ASingleServiceManager} class for managing services
 * with {@link ISingleServiceHolder} as the holder type.
 * This class simplifies the process of creating service holders by using a specified type.
 *
 * @param <T> The type of the service being managed by this ServiceManager.
 */
@ThreadSafe
public class SingleServiceManager<T> extends ASingleServiceManager<ISingleServiceHolder<? extends T>, T> {
    /**
     * Constructs a {@link SingleServiceManager} instance for managing services of the specified type.
     * This constructor leverages the {@link ISingleServiceHolder} class for service holder management.
     *
     * @param serviceType The class object representing the type of the service to be managed.
     *                    Must not be {@code null}.
     */
    @SuppressWarnings("unchecked")
    public SingleServiceManager(final @NotNull Class<T> serviceType) {
        super((Class<ISingleServiceHolder<? extends T>>) (Object) ISingleServiceHolder.class, serviceType);
    }

    @Override
    protected <T2 extends T> @NotNull IServiceHolder<? extends T> createServiceHolder(@NotNull final T2 serviceImpl) {
        return new ServiceHolder<>(null, serviceImpl);
    }
}
