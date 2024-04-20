package org.example.database;

public interface Identifiable<T> {
    /*
     * Returns the id of the entity.
     */
    T getId();

    /*
     * Sets the id of the entity.
     */
    void setId(T id);

}
