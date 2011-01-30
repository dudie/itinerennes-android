package fr.itinerennes.business.event;

/**
 * Implementing this interface allow a class to listen to bookmarks additions and removals.
 * 
 * @author Jérémie Huchet
 */
public interface IBookmarkModificationListener {

    /**
     * Called when a bookmark is removed.
     * 
     * @param type
     *            the type of the bookmark
     * @param id
     *            the identifier of the bookmark
     */
    void onBookmarkRemoval(String type, String id);

    /**
     * Called when a bookmark is added.
     * 
     * @param type
     *            the type of the bookmark
     * @param id
     *            the identifier of the bookmark
     * @param label
     *            the label of the bookmark
     */
    void onBookmarkAddition(String type, String id, String label);
}
