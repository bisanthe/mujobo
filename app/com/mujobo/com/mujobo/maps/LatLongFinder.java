package com.mujobo.com.mujobo.maps;

import models.Location;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 2/6/13
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LatLongFinder {
    public Location findCoordinate(String address);
}
