
package com.hand.guidensk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    @SerializedName("legs")
    @Expose
    private List<Leg> legs = null;

    @SerializedName("overview_polyline")
    @Expose
    private OverviewPolyline overviewPolyline;

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public List<Leg> getLegs() {
        return legs;
    }
}
