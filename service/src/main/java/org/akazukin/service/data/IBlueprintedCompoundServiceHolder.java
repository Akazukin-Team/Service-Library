package org.akazukin.service.data;

/**
 * Represents a holder interface for managing a compound service that includes both
 * a service type and an additional data type.
 * <p>
 * This interface extends {@link IBlueprintedServiceHolder}, enabling type-safe access to
 * the interface and implementation of the service as well as providing
 * methods for getting and setting supplementary data.
 *
 * @param <T> the type of the service
 * @param <U> the type of the additional data
 */
public interface IBlueprintedCompoundServiceHolder<T, U> extends ICompoundServiceHolder<T, U>, IBlueprintedServiceHolder<T> {
}
