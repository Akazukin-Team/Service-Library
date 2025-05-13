package org.akazukin.service.manager;

import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.ServiceHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServiceManager<T> extends AServiceManager<IServiceHolder<? extends T>, T> {
    @SuppressWarnings("unchecked")
    public ServiceManager(final @NotNull Class<T> serviceType) {
        super((Class<IServiceHolder<? extends T>>) (Object) ServiceHolder.class, serviceType);
    }

    @Override
    protected <T2 extends T> @NotNull IServiceHolder<? extends T> createServiceHolder(final @Nullable Class<T2> service, @NotNull final T2 serviceImpl) {
        return new ServiceHolder<>(service, serviceImpl);
    }
}
