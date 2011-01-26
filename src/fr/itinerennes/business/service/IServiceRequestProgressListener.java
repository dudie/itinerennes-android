package fr.itinerennes.business.service;


/**
 * Implementing this interface allows you to be notified of a service request progress.
 * Notifications does not use a concrete unit (bit, byte...) but <i>units of work</i>.
 * 
 * @author Jérémie Huchet
 */
public interface IServiceRequestProgressListener {

    /**
     * Method called before the service request begins its work, but after the service was able to
     * determine an approximative cost for the request.
     * 
     * @param cost
     *            an evaluation of the cost of the request
     */
    void onRequestStart(int cost);

    /**
     * Method called during the progress of the request.
     * 
     * @param lenght
     *            the amount of units of work the request has progressed since the last call to this
     *            method
     */
    void onRequestProgress(int lenght);

    /**
     * Method called when the request is terminated.
     * 
     * @param result
     *            the result returned by the service
     */
    void onRequestFinish(Object result);
}
