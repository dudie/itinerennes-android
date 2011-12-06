package fr.itinerennes.business.event;

/**
 * Implementing this interface allow a class to listen to bookmarks additions and removals.
 * 
 * @author Jérémie Huchet
 */
public interface IBookmarkModificationListener {

    /**
     * Called when a bookmark is removed or added. E.g. called when the bookmarked state of an
     * element is modified.
     * 
     * @param type
     *            the type of the bookmark
     * @param id
     *            the identifier of the bookmark
     * @param bookmarked
     *            true if the element pointed out by parameters <code>type</code> and
     *            <code>id</code> is bookmarked, else false
     */
    void onBookmarkStateChanged(String type, String id, boolean bookmarked);
}
