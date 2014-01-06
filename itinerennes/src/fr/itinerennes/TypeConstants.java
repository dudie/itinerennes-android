package fr.itinerennes;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

/**
 * Defines the types of entities used in the application.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public final class TypeConstants {

    /**
     * Private constructor to avoid instantiation.
     */
    private TypeConstants() {

    }

    /** Bus stop. */
    public static final String TYPE_BUS = "BUS";

    // /** Point of sale. */
    // public static final String TYPE_POS = "POS";

    /** Bike station. */
    public static final String TYPE_BIKE = "BIKE";

    /** Subway station. */
    public static final String TYPE_SUBWAY = "SUBWAY";

    /** Relay park. */
    public static final String TYPE_CAR_PARK = "CAR_PARK";

    /** Bus route. */
    public static final String TYPE_BUS_ROUTE = "BUS_ROUTE";

}
