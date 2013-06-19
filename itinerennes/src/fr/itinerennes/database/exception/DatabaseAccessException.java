package fr.itinerennes.database.exception;

public class DatabaseAccessException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8713032310284026028L;

    public DatabaseAccessException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

    public DatabaseAccessException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

}
