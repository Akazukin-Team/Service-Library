package org.akazukin.service.registry;

import org.akazukin.service.data.IServiceHolder;
import org.jetbrains.annotations.NotNull;

public class SingleServiceRegistry<U> extends MultiServiceRegistry<U> {
    protected SingleServiceRegistry(final @NotNull Class<U> serviceType) {
        super(serviceType);
    }

    @Override
    public synchronized void registerService(final @NotNull IServiceHolder<? extends U> holder) {
        if (this.isExistsServiceByInterface((Class<? extends U>) holder.getInterfaceClass())) {
            throw new IllegalStateException(String.format(MultiServiceRegistry.EX_EXISTS + "; interface: %s",
                    holder.getInterfaceClass().getName()));
        }
        super.registerService(holder);
    }
}
