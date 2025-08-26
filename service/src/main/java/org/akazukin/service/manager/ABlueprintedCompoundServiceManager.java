package org.akazukin.service.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.service.data.BlueprintedCompoundServiceHolder;
import org.akazukin.service.data.IBlueprintedCompoundServiceHolder;
import org.akazukin.service.data.ICompoundServiceHolder;
import org.akazukin.util.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An abstract implementation of a compound service manager that manages services and their associated data.
 * Extends the {@link ABlueprintedServiceManager} with additional functionalities for handling data linked with service holders.
 * <p>
 * The service manager is thread-safe and can be used in multithreaded environments.
 *
 * @param <U> the type of service managed by this manager.
 * @param <V> the type of data associated with the services.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@ThreadSafe
public abstract class ABlueprintedCompoundServiceManager<U, V>
        extends ABlueprintedServiceManager<U> implements IBlueprintedCompoundServiceManager<U, V> {
    Class<V> dataType;

    /**
     * Constructs an instance of ACompoundServiceManager, which manages compound services
     * and their associated data types.
     * This manager extends the capabilities of a basic service manager by allowing
     * management of data linked with compound service holders.
     *
     * @param serviceType the class object representing the type of the service.
     *                    Must not be null.
     * @param dataType    the class object representing the type of data associated with the services.
     */
    protected ABlueprintedCompoundServiceManager(final @NotNull Class<U> serviceType, final Class<V> dataType) {
        super(serviceType);
        this.dataType = dataType;
    }

    @Override
    public V getDataByClass(final Class<? extends U> service) {
        final Optional<V> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .filter(s ->
                            s instanceof ICompoundServiceHolder
                                    && Objects.equals(s.getImplementation().getClass(), service))
                    .findFirst()
                    .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData());
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                if (!(subManager instanceof ICompoundServiceManager)) {
                    continue;
                }
                final V data = ((ICompoundServiceManager<U, V>) subManager).getDataByClass(service);
                if (data != null) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    public V getDataByService(final @NotNull U service) {
        final Optional<V> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .filter(s ->
                            s instanceof ICompoundServiceHolder
                                    && s.getImplementation() == service)
                    .findFirst()
                    .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData());
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                if (!(subManager instanceof ICompoundServiceManager)) {
                    continue;
                }
                final V data = ((ICompoundServiceManager<U, V>) subManager).getDataByService(service);
                if (data != null) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    public V[] getAllData() {
        final Set<V> data = new HashSet<>();
        synchronized (this.services) {
            data.addAll(this.services.stream()
                    .filter(s -> s instanceof ICompoundServiceHolder)
                    .map(s -> ((ICompoundServiceHolder<? extends U, V>) s).getData())
                    .collect(Collectors.toSet()));
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                if (!(subManager instanceof ICompoundServiceManager)) {
                    continue;
                }
                data.addAll(Arrays.asList(((ICompoundServiceManager<U, V>) subManager).getAllData()));
            }
        }
        return data.toArray(ArrayUtils.getNewArray(this.dataType, 0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ICompoundServiceHolder<? extends U, V>[] getHolderByData(@Nullable final V data) {
        final Set<ICompoundServiceHolder<? extends U, V>> holders = new HashSet<>();
        synchronized (this.services) {
            holders.addAll(this.services.stream()
                    .filter(s ->
                            s instanceof ICompoundServiceHolder)
                    .map(s -> (ICompoundServiceHolder<? extends U, V>) s)
                    .filter(s -> Objects.equals(s.getData(), data))
                    .collect(Collectors.toSet()));
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                if (!(subManager instanceof ICompoundServiceManager)) {
                    continue;
                }
                holders.addAll(Arrays.asList(((ICompoundServiceManager<U, V>) subManager).getHolderByData(data)));
            }
        }
        return holders.toArray(ArrayUtils.getNewArray(ICompoundServiceHolder.class, 0));
    }

    @Override
    public V getDataByInterfaceClass(final Class<? extends U> service) {
        final Optional<V> opt;
        synchronized (this.services) {
            opt = this.services.stream()
                    .filter(s -> s instanceof IBlueprintedCompoundServiceHolder
                            && Objects.equals(((IBlueprintedCompoundServiceHolder<? extends U, V>) s).getInterfaceClass(), service))
                    .findFirst()
                    .map(s -> ((IBlueprintedCompoundServiceHolder<? extends U, V>) s).getData());
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        synchronized (this.subManagers) {
            for (final IServiceManager<U> subManager : this.subManagers) {
                if (!(subManager instanceof IBlueprintedCompoundServiceManager)) {
                    continue;
                }
                final V data = ((IBlueprintedCompoundServiceManager<U, V>) subManager).getDataByInterfaceClass(service);
                if (data != null) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    protected @NotNull <U2 extends U> IBlueprintedCompoundServiceHolder<U2, V> createHolder(final @NotNull Class<U2> service, @NotNull final U2 serviceImpl) {
        return new BlueprintedCompoundServiceHolder<>(service, serviceImpl);
    }
}
