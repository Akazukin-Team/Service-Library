package org.akazukin.service.manager;

import org.akazukin.service.data.ICompoundServiceHolder;
import org.akazukin.util.object.Pair;

public interface ICompoundServiceManager<T extends ICompoundServiceHolder<? extends U, V>, U, V> extends IServiceManager<T, U> {

    <U2 extends U> V getDataByImplementation(Class<U> service);

    <U2 extends U> V getDataByInterface(Class<U> service);

    V getDataByService(U service);

    <U2 extends U> Pair<U2, V> getServiceAndDataByImplementation(Class<U2> service);

    <U2 extends U> Pair<U2, V> getServiceAndDataByInterface(Class<U2> service);

    Pair<U, V>[] getServicesAndData();

    V[] getData();
}
