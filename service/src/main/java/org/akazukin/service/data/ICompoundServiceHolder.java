package org.akazukin.service.data;

public interface ICompoundServiceHolder<T, U> extends IServiceHolder<T> {
    U getData();

    void setData(U data);
}
