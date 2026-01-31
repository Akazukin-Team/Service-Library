package org.akazukin.service.manager;

import org.akazukin.service.data.IServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public abstract class ServiceManager<U> implements IServiceManager<U> {
    @Override
    public @NotNull U[] getAllServices() {
        return this.getRegistry().getAllServices();
    }

    @Override
    public @NotNull IServiceHolder<? extends U>[] getAllHolders() {
        return this.getRegistry().getAllHolders();
    }

    @Override
    public boolean containsService(@NotNull final U service) {
        return this.getRegistry().containsService(service);
    }

    @Override
    public boolean containsServiceByClass(@NotNull final Class<? extends U> serviceImpl) {
        return this.getRegistry().containsServiceByClass(serviceImpl);
    }

    @Override
    public boolean containsServiceByInterface(@NotNull final Class<? extends U> service) {
        return this.getRegistry().containsServiceByInterface(service);
    }

    @Override
    public <U2 extends U> boolean containsServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        return this.getRegistry().containsServiceByStructClass(service, serviceImpl);
    }

    @Override
    public <U2 extends U> boolean containsServiceByStruct(@NotNull final Class<? super U2> service, @NotNull final U2 serviceImpl) {
        return this.getRegistry().containsServiceByStruct(service, serviceImpl);
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2> getHolderByStruct(@NotNull final Class<U2> service, @NotNull final U2 serviceImpl) {
        return (IServiceHolder<U2>) Arrays.stream(this.getRegistry().getAllHolders())
                .filter(h ->
                        h.getImplementation() == serviceImpl
                                && Objects.equals(h.getInterfaceClass(), service))
                .findFirst()
                .orElse(null);
    }

    @Override
    public <U2 extends U> U2[] getServicesByClass(@NotNull final Class<U2> serviceImpl) {
        return Arrays.stream(this.getRegistry().getAllHolders())
                .filter(s -> Objects.equals(s.getImplementation().getClass(), serviceImpl))
                .map(IServiceHolder::getImplementation)
                .distinct()
                .toArray(ArrayUtils.collectToArray(serviceImpl));
    }

    @Override
    @SuppressWarnings("unchecked")
    public IServiceHolder<? extends U>[] getHoldersByService(@NotNull final U serviceImpl) {
        return Arrays.stream(this.getRegistry().getAllHolders())
                .filter(h -> h.getImplementation() == serviceImpl)
                .toArray(ArrayUtils.collectToArray(IServiceHolder.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2>[] getHoldersByClass(@NotNull final Class<? extends U2> serviceImpl) {
        return Arrays.stream(this.getRegistry().getAllHolders())
                .filter(h -> Objects.equals(h.getImplementation().getClass(), serviceImpl))
                .toArray(ArrayUtils.collectToArray(IServiceHolder.class));
    }
}
