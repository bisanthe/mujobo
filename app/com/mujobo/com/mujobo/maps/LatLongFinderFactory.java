package com.mujobo.com.mujobo.maps;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 2/6/13
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class LatLongFinderFactory {
    public static LatLongFinder create(){
        return new GMapLatLongFinder();
    }
}
