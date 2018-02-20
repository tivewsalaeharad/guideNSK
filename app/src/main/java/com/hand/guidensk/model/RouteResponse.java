
package com.hand.guidensk.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteResponse {

    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
