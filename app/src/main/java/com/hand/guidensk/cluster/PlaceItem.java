package com.hand.guidensk.cluster;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.hand.guidensk.constant.Markers;

public class PlaceItem implements ClusterItem {
    private int index;
    private MarkerOptions options;

    public PlaceItem(LatLng point, int category, int tag) {
        options = new MarkerOptions()
            .position(point)
            .icon(BitmapDescriptorFactory.fromResource(Markers.ARRAY[category]));
        index = tag;
    }

    @Override
    public LatLng getPosition() {
        return options.getPosition();
    }

    MarkerOptions getMarker() {
        return options;
    }

    public int getIndex() {
        return index;
    }
}
