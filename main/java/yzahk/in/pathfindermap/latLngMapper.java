/**
 * Created by Ákos Nikházy 2019
 *
 * Map image pixels to latlng
 *
 * We use this to get coordinates on the
 * map overlay based on the images pixel
 * coordinates. The image top left
 * coordinate is 0x0 and in this demo
 * the right bottom coordinate is
 * 1024x1024. On the map these must be
 * map coordinates.
 *
 * It is much easier to save image
 * pixel coordinates in the nodes.csv
 * file than map coordinates, as every
 * use case will be somewhere else on the
 * map, but the pixels of the image is
 * always the same.
 */

package yzahk.in.pathfindermap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class latLngMapper {

    private static float width;
    private static float height;
    private static LatLngBounds bounds;

    public latLngMapper(float width, float height, LatLngBounds bounds) {
        latLngMapper.width = width;
        latLngMapper.height = height;
        latLngMapper.bounds = bounds;
    }

    /**
     * Returns and LatLng object with the map coordinates
     * at the overlay's image's pixes coordinates
     * @param x
     * @param y
     * @return LatLng
     */
    public static LatLng calculateLatLng(int x, int y) {
        return new LatLng((bounds.northeast.latitude + ((y / (height / 100)) / 100) * (bounds.southwest.latitude - bounds.northeast.latitude)),
                (bounds.southwest.longitude + ((x / (width / 100)) / 100) * (bounds.northeast.longitude - bounds.southwest.longitude)));
    }
}