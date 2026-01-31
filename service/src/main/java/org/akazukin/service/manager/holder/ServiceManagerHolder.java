package org.akazukin.service.manager.holder;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.service.data.IServiceHolder;
import org.akazukin.service.manager.IServiceManager;
import org.akazukin.service.manager.IServiceStore;
import org.akazukin.service.manager.multi.IMultiServiceManager;
import org.akazukin.service.manager.single.ISingleServiceManager;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceManagerHolder<U> implements IServiceManagerHolder<U> {
    public static final IServiceStore<?>[] EMPTY_STORES = new IServiceStore[0];
    Collection<IServiceStore<U>> stores = new HashSet<>();
    Class<U> serviceType;

    public ServiceManagerHolder(final Class<U> serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public synchronized void registerStore(final @NotNull IServiceStore<U> store) {
        this.stores.add(store);
    }

    @Override
    public synchronized void unregisterStore(final @NotNull IServiceStore<U> store) {
        this.stores.remove(store);
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized IServiceStore<U>[] getAllStores() {
        return (IServiceStore<U>[]) this.stores.toArray(EMPTY_STORES);
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2[] getServicesByClass(@NotNull final Class<U2> serviceImpl) {
        final Collection<U2> services = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof ISingleServiceManager) {
                    services.addAll(Arrays.asList(((ISingleServiceManager<U>) store).getServicesByClass(serviceImpl)));
                } else if (store instanceof IMultiServiceManager) {
                    services.addAll(Arrays.asList(((IMultiServiceManager<U>) store).getServicesByClass(serviceImpl)));
                } else {
                    services.addAll(Arrays.stream(store.getAllServices())
                            .filter(s -> Objects.equals(s.getClass(), serviceImpl))
                            .map(s -> (U2) s)
                            .collect(Collectors.toSet()));
                }
            }
        }
        return services.toArray(ArrayUtils.getNewArray(serviceImpl, 0));
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2[] getServicesByStructClass(@NotNull final Class<U2> service, @NotNull final Class<U2> serviceImpl) {
        final Collection<U2> services = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof ISingleServiceManager) {
                    services.add(((ISingleServiceManager<U>) store).getServiceByStructClass(service, serviceImpl));
                } else if (store instanceof IMultiServiceManager) {
                    services.addAll(Arrays.asList(((IMultiServiceManager<U>) store).getServicesByStructClass(service, serviceImpl)));
                } else {
                    services.addAll(Arrays.stream(store.getAllHolders())
                            .filter(h -> Objects.equals(h.getInterfaceClass(), service)
                                    && Objects.equals(h.getImplementation(), serviceImpl))
                            .map(h -> (U2) h.getImplementation())
                            .collect(Collectors.toSet()));
                }
            }
        }
        return services.toArray(ArrayUtils.getNewArray(serviceImpl, 0));
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <U2 extends U> U2[] getServicesByInterfaceClass(@NotNull final Class<U2> service) {
        final Collection<U2> services = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof ISingleServiceManager) {
                    services.add(((ISingleServiceManager<U>) store).getServiceByInterfaceClass(service));
                } else if (store instanceof IMultiServiceManager) {
                    services.addAll(Arrays.asList(((IMultiServiceManager<U>) store).getServicesByInterfaceClass(service)));
                } else {
                    services.addAll(Arrays.stream(store.getAllHolders())
                            .filter(h -> Objects.equals(h.getInterfaceClass(), service))
                            .map(h -> (U2) h.getImplementation())
                            .collect(Collectors.toSet()));
                }
            }
        }
        return services.toArray(ArrayUtils.getNewArray(service, 0));
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public IServiceHolder<? extends U>[] getHoldersByService(@NotNull final U serviceImpl) {
        final Collection<IServiceHolder<? extends U>> holders = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof ISingleServiceManager) {
                    holders.addAll(Arrays.asList(((ISingleServiceManager<U>) store).getHoldersByService(serviceImpl)));
                } else if (store instanceof IMultiServiceManager) {
                    holders.addAll(Arrays.asList(((IMultiServiceManager<U>) store).getHoldersByService(serviceImpl)));
                } else {
                    holders.addAll(Arrays.stream(store.getAllHolders())
                            .filter(h -> h.getImplementation() == serviceImpl)
                            .map(h -> (IServiceHolder<? extends U>) h)
                            .collect(Collectors.toSet()));
                }
            }
        }
        return holders.toArray(ArrayUtils.getNewArray(IServiceHolder.class, 0));
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2>[] getHoldersByClass(@NotNull final Class<? extends U2> serviceImpl) {
        final Collection<IServiceHolder<U2>> holders = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof ISingleServiceManager) {
                    holders.addAll(Arrays.asList(((ISingleServiceManager<U2>) store).getHoldersByClass(serviceImpl)));
                } else if (store instanceof IMultiServiceManager) {
                    holders.addAll(Arrays.asList(((IMultiServiceManager<U2>) store).getHoldersByClass(serviceImpl)));
                } else {
                    holders.addAll(Arrays.stream(store.getAllHolders())
                            .filter(h -> Objects.equals(h.getImplementation().getClass(), serviceImpl))
                            .map(h -> (IServiceHolder<U2>) h)
                            .collect(Collectors.toSet()));
                }
            }
        }
        return holders.toArray(ArrayUtils.getNewArray(IServiceHolder.class, 0));
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <U2 extends U> IServiceHolder<U2>[] getHoldersByInterfaceClass(@NotNull final Class<U2> service) {
        final Collection<IServiceHolder<U2>> holders = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof ISingleServiceManager) {
                    holders.add(((ISingleServiceManager<U2>) store).getHolderByInterfaceClass(service));
                } else if (store instanceof IMultiServiceManager) {
                    holders.addAll(Arrays.asList(((IMultiServiceManager<U2>) store).getHoldersByInterfaceClass(service)));
                } else {
                    holders.addAll(Arrays.stream(store.getAllHolders())
                            .filter(h -> Objects.equals(h.getInterfaceClass(), service))
                            .map(h -> (IServiceHolder<U2>) h)
                            .collect(Collectors.toSet()));
                }
            }
        }
        return holders.toArray(ArrayUtils.getNewArray(IServiceHolder.class, 0));
    }

    @Override
    @NotNull
    public U[] getAllServices() {
        final Collection<U> services = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof IServiceManager) {
                    Collections.addAll(services, ((IServiceManager<U>) store).getRegistry().getAllServices());
                } else {
                    Collections.addAll(services, store.getAllServices());
                }
            }
        }
        return services.toArray(ArrayUtils.getNewArray(this.serviceType, 0));
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public IServiceHolder<? extends U>[] getAllHolders() {
        final Collection<IServiceHolder<U>> holders = new HashSet<>();
        synchronized (this) {
            for (final IServiceStore<U> store : this.stores) {
                if (store instanceof IServiceManager) {
                    Collections.addAll(holders, (IServiceHolder<U>[]) ((IServiceManager<U>) store).getRegistry().getAllHolders());
                } else {
                    Collections.addAll(holders, (IServiceHolder<U>[]) store.getAllHolders());
                }
            }
        }
        return holders.toArray(ArrayUtils.getNewArray(IServiceHolder.class, 0));
    }

    @Override
    public synchronized boolean containsService(@NotNull final U service) {
        return this.stores.stream()
                .anyMatch(s -> s.containsService(service));
    }

    @Override
    public synchronized boolean containsServiceByClass(@NotNull final Class<? extends U> serviceImpl) {
        return this.stores.stream()
                .anyMatch(s -> s.containsServiceByClass(serviceImpl));
    }

    @Override
    public synchronized boolean containsServiceByInterface(@NotNull final Class<? extends U> service) {
        return this.stores.stream()
                .anyMatch(s -> s.containsServiceByInterface(service));
    }

    @Override
    public synchronized <U2 extends U> boolean containsServiceByStructClass(@NotNull final Class<U2> service, @NotNull final Class<? extends U2> serviceImpl) {
        return this.stores.stream()
                .anyMatch(s -> s.containsServiceByStructClass(service, serviceImpl));
    }

    @Override
    public synchronized <U2 extends U> boolean containsServiceByStruct(@NotNull final Class<? super U2> service, @NotNull final U2 serviceImpl) {
        return this.stores.stream()
                .anyMatch(s -> s.containsServiceByStruct(service, serviceImpl));
    }
}
