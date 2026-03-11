package org.akazukin.service.registry;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.service.data.BlueprintedServiceHolder;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.data.ServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MultiServiceRegistry<U> implements IServiceRegistry<U> {
    public static final String EX_EXISTS = "The service already registered";
    Set<IServiceHolder<? extends U>> holders = new HashSet<>();
    Class<U> serviceType;

    public MultiServiceRegistry(@NotNull final Class<U> serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public void registerService(@NotNull final U serviceImpl) {
        this.registerService(new ServiceHolder<>(serviceImpl));
    }

    @Override
    public <U2 extends U> void registerService(@NotNull final Class<U2> service, @NotNull final U2 serviceImpl) {
        this.registerService(new BlueprintedServiceHolder<>(service, serviceImpl));
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void registerService(@NotNull final IServiceHolder<? extends U> holder) {
        if (this.containsServiceByStruct((Class<U>) holder.getInterfaceClass(), holder.getImplementation())) {
            throw new IllegalStateException(String.format(EX_EXISTS + "; impl: %s, interface: %s",
                    holder.getImplementation().getClass().getName(), holder.getInterfaceClass().getName()));
        }
        this.holders.add(holder);
    }

    @Override
    public synchronized void unregisterService(@NotNull final U serviceImpl) {
        this.holders.removeIf(h -> h.getImplementation() == serviceImpl);
    }

    @Override
    public synchronized void unregisterServiceByClass(@NotNull final Class<? extends U> serviceImpl) {
        this.holders.removeIf(h -> Objects.equals(h.getImplementation().getClass(), serviceImpl));
    }

    @Override
    public synchronized void unregisterServiceByInterfaceClass(@NotNull final Class<? extends U> service) {
        this.holders.removeIf(h -> Objects.equals(h.getInterfaceClass(), service));
    }

    @Override
    public synchronized <U2 extends U> void unregisterServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        this.holders.removeIf(h ->
                Objects.equals(h.getInterfaceClass(), service) &&
                        Objects.equals(h.getImplementation().getClass(), serviceImpl));
    }

    @Override
    public synchronized <U2 extends U> void unregisterServiceByStruct(@NotNull final Class<? super U2> service, @NotNull final U2 serviceImpl) {
        this.holders.removeIf(h ->
                Objects.equals(h.getInterfaceClass(), service) &&
                        h.getImplementation() == serviceImpl);
    }

    @Override
    public synchronized void unregisterService(@NotNull final IServiceHolder<? extends U> holder) {
        this.holders.remove(holder);
    }

    @Override
    public synchronized @NotNull U[] getAllServices() {
        return this.holders.stream()
                .map(IServiceHolder::getImplementation)
                .distinct()
                .toArray(ArrayUtils.collectToArray(this.serviceType));
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized @NotNull IServiceHolder<? extends U>[] getAllHolders() {
        return this.holders.toArray(new IServiceHolder[0]);
    }

    @Override
    public synchronized boolean containsService(@NotNull final U service) {
        return this.holders.stream()
                .anyMatch(h -> h.getImplementation() == service);
    }

    @Override
    public synchronized boolean containsServiceByClass(final @NotNull Class<? extends U> serviceImpl) {
        return this.holders.stream()
                .anyMatch(h -> Objects.equals(h.getInterfaceClass(), serviceImpl));
    }

    @Override
    public synchronized boolean containsServiceByInterface(@NotNull final Class<? extends U> service) {
        return this.holders.stream()
                .anyMatch(h -> Objects.equals(h.getInterfaceClass(), service));
    }

    @Override
    public synchronized <U2 extends U> boolean containsServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        return this.holders.stream()
                .anyMatch(h ->
                        Objects.equals(h.getImplementation().getClass(), serviceImpl)
                                && Objects.equals(h.getInterfaceClass(), service));
    }

    @Override
    public synchronized <U2 extends U> boolean containsServiceByStruct(@NotNull final Class<? super U2> service, @NotNull final U2 serviceImpl) {
        return this.holders.stream()
                .anyMatch(h ->
                        h.getImplementation() == serviceImpl
                                && Objects.equals(h.getInterfaceClass(), service));
    }
}
