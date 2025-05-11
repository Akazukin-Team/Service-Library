package org.akazukin.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines the contract for managing a collection of services that implement the {@link IService} interface.
 * This interface provides methods for registering, retrieving, and interacting with services by their types,
 * implementations, or unique identifiers.
 *
 * @param <T> the type of services managed, which must extend {@link IService}
 */
public interface IServiceManager<T extends IService> {
    /**
     * Retrieves a service instance based on its interface type.
     *
     * @param <T2>    the type of the service to be retrieved, which must extend {@link T}
     * @param service the class object representing the interface of the service to be retrieved
     * @return the instance of the service matching the specified interface type, or {@code null} if no service is found
     */
    @Nullable
    <T2 extends T> T2 getServiceByInterface(Class<T2> service);

    /**
     * Retrieves a registered service by its specific implementation class.
     *
     * @param <T2>    the type of the service being retrieved, which must extend {@link T}
     * @param service the class object representing the implementation of the service to be retrieved
     * @return the instance of the service matching the specified implementation class, or {@code null} if no service is found
     */
    @Nullable
    <T2 extends T> T2 getServiceByImplementation(Class<T2> service);

    /**
     * Registers a service implementation with its corresponding service interface.
     * This method allows associating a service interface with a specific implementation.
     *
     * @param <T2>        the type of the service to register, extending the base type {@link T}.
     * @param service     the class object representing the service interface, used as the key for management.
     *                    Must be {@code null} if the service is not associated with a specific interface.
     * @param serviceImpl the implementation instance of the service to register.
     *                    Must not be {@code null}.
     */
    <T2 extends T> void registerService(@Nullable Class<T2> service, @NotNull T2 serviceImpl);

    /**
     * Registers a service implementation without associating it with a specific service interface.
     *
     * @param <T2>        the type of the service implementation being registered, extending the base type {@link T}
     * @param serviceImpl the implementation instance of the service to be registered.
     *                    Must not be {@code null}.
     */
    <T2 extends T> void registerService(@NotNull T2 serviceImpl);

    /**
     * Retrieves an array of all the registered service instances.
     *
     * @return an array of all services currently registered, or an empty array if no services are registered.
     * Must not be {@code null}.
     */
    @NotNull
    T[] getAllServices();

    /**
     * Retrieves a registered service by its unique identifier.
     * <p>
     * This method searches through the registered services and retrieves the service instance
     * whose {@link IService#getServiceId()} matches the specified {@code serviceId}.
     * If no service with the given identifier is found, {@code null} is returned.
     *
     * @param serviceId the unique identifier of the service to be retrieved
     * @return the service instance whose {@link IService#getServiceId()} matches the specified {@code serviceId},
     * or {@code null} if no service is found
     */
    @Nullable
    T getServiceById(long serviceId);
}
