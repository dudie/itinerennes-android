package fr.itinerennes.model;

import java.util.Date;
import java.util.List;

/**
 * Represents an alert.
 * 
 * @author Jérémie Huchet
 */
public class Alert {

    /** The title of the alert. */
    private String title;

    /** The start date of the alert. */
    private Date start;

    /** The end date of the alert. */
    private Date end;

    /** The lines affected by the alert. */
    private List<String> lines;

    /** The major disturbance. */
    private String majorDisturbance;

    /** The details of the alert. */
    private String detail;

    /** A link related to the alert. */
    private String link;

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public final String getTitle() {

        return title;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            the title to set
     */
    public final void setTitle(final String title) {

        this.title = title;
    }

    /**
     * Gets the start.
     * 
     * @return the start
     */
    public final Date getStart() {

        return start;
    }

    /**
     * Sets the start.
     * 
     * @param start
     *            the start to set
     */
    public final void setStart(final Date start) {

        this.start = start;
    }

    /**
     * Gets the end.
     * 
     * @return the end
     */
    public final Date getEnd() {

        return end;
    }

    /**
     * Sets the end.
     * 
     * @param end
     *            the end to set
     */
    public final void setEnd(final Date end) {

        this.end = end;
    }

    /**
     * Gets the lines.
     * 
     * @return the lines
     */
    public final List<String> getLines() {

        return lines;
    }

    /**
     * Sets the lines.
     * 
     * @param lines
     *            the lines to set
     */
    public final void setLines(final List<String> lines) {

        this.lines = lines;
    }

    /**
     * Gets the majorDisturbance.
     * 
     * @return the majorDisturbance
     */
    public final String getMajorDisturbance() {

        return majorDisturbance;
    }

    /**
     * Sets the majorDisturbance.
     * 
     * @param majorDisturbance
     *            the majorDisturbance to set
     */
    public final void setMajorDisturbance(final String majorDisturbance) {

        this.majorDisturbance = majorDisturbance;
    }

    /**
     * Gets the detail.
     * 
     * @return the detail
     */
    public final String getDetail() {

        return detail;
    }

    /**
     * Sets the detail.
     * 
     * @param detail
     *            the detail to set
     */
    public final void setDetail(final String detail) {

        this.detail = detail;
    }

    /**
     * Gets the link.
     * 
     * @return the link
     */
    public final String getLink() {

        return link;
    }

    /**
     * Sets the link.
     * 
     * @param link
     *            the link to set
     */
    public final void setLink(final String link) {

        this.link = link;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final int maxLen = 10;
        final StringBuilder builder = new StringBuilder();
        builder.append("Alert [title=");
        builder.append(title);
        builder.append(", start=");
        builder.append(start);
        builder.append(", end=");
        builder.append(end);
        builder.append(", lines=");
        builder.append(lines != null ? lines.subList(0, Math.min(lines.size(), maxLen)) : null);
        builder.append(", majorDisturbance=");
        builder.append(majorDisturbance);
        builder.append(", detail=");
        builder.append(detail);
        builder.append(", link=");
        builder.append(link);
        builder.append("]");
        return builder.toString();
    }

}
