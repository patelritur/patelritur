package com.demo.home.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class DirectionResults {
    @SerializedName("routes")
    private List<Route> routes;

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    @Keep
    public class Route {
        @SerializedName("overview_polyline")
        private OverviewPolyLine overviewPolyLine;

        private List<Legs> legs;

        public OverviewPolyLine getOverviewPolyLine() {
            return overviewPolyLine;
        }

        public void setOverviewPolyLine(OverviewPolyLine overviewPolyLine) {
            this.overviewPolyLine = overviewPolyLine;
        }

        public void setLegs(List<Legs> legs) {
            this.legs = legs;
        }

        public List<Legs> getLegs() {
            return legs;
        }
    }

    @Keep
    public class Legs {
        public String getStart_address() {
            return start_address;
        }

        public void setStart_address(String start_address) {
            this.start_address = start_address;
        }

        private String start_address;

        private List<Steps> steps;

        public Duration getDistance() {
            return distance;
        }

        public void setDistance(Duration distance) {
            this.distance = distance;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        private Duration distance;
        private Duration duration;
        @SerializedName("start_location")
        private Location startLocation;
        @SerializedName("end_location")
        private Location endLocation;

        public Location getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(Location startLocation) {
            this.startLocation = startLocation;
        }

        public Location getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(Location endLocation) {
            this.endLocation = endLocation;
        }

        public void setSteps(List<Steps> steps) {
            this.steps = steps;
        }

        public List<Steps> getSteps() {
            return steps;
        }
    }

    @Keep
    public class Steps {
        private Location start_location;


        private Location end_location;
        private OverviewPolyLine polyline;

        public Location getStart_location() {
            return start_location;
        }

        public Location getEnd_location() {
            return end_location;
        }

        public OverviewPolyLine getPolyline() {
            return polyline;
        }

        public void setStart_location(Location start_location) {
            this.start_location = start_location;
        }

        public void setEnd_location(Location end_location) {
            this.end_location = end_location;
        }

        public void setPolyline(OverviewPolyLine polyline) {
            this.polyline = polyline;
        }
    }

    @Keep
    public class OverviewPolyLine {

        @SerializedName("points")
        public String points;

        public void setPoints(String points) {
            this.points = points;
        }

        public String getPoints() {
            return points;
        }
    }

    @Keep
    public class Duration {
        private String  text;
        private Long value;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }
    }
    @Keep
    public class Location {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}