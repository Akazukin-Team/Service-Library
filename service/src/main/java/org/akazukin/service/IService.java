package org.akazukin.service;

/**
 * Represents a generic service with functionality to retrieve its unique identifier.
 * This interface serves as a base contract for services that can be registered,
 * managed, and retrieved based on their implementation or interface classes.
 */
public interface IService {
    /**
     * Retrieves the unique identifier for the service.
     *
     * @return the unique ID of the service
     */
    long getServiceId();
}
