package fr.itinerennes.database;

import java.util.Iterator;

/**
 * An interface providing a way to iterate over data to import into a DB table.
 * 
 * @author Jérémie Huchet
 */
public interface IDataReader extends Iterator<String[]> {

    /**
     * Gets the total amount of rows.
     * 
     * @return the row count
     */
    int getRowCount();

    /**
     * Gets the name of the table to which the read data should be imported.
     * 
     * @return the table name
     */
    String getTable();

    /**
     * Gets the name of the columns of the table.
     * 
     * @return the columns names
     */
    String[] getColumns();
}
