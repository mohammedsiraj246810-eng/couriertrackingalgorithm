/**
 * Persistence abstraction for courier records.
 */
package com.courier.persistence;

import com.courier.model.Courier;
import java.io.IOException;
import java.util.List;

/**
 * Repository interface for loading and saving courier records.
 * Demonstrates abstraction by separating persistence from business logic.
 */
public interface CourierRepository {

    /**
     * Loads all courier records from persistent storage.
     *
     * @return list of couriers
     * @throws IOException when storage cannot be read
     */
    List<Courier> loadAll() throws IOException;

    /**
     * Saves courier records to persistent storage.
     *
     * @param couriers list of couriers to save
     * @throws IOException when storage cannot be written
     */
    void saveAll(List<Courier> couriers) throws IOException;
}
