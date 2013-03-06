package com.mujobo.util;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/26/13
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListUtil {
    public static <T> T first(List<T> list){
        if(list != null && list.size() > 0 ){
          return list.get(0);
        }
        return null;
    }
}
