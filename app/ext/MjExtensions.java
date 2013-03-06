package ext;

import play.templates.JavaExtensions;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/20/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MjExtensions extends JavaExtensions{
    public static String first(List<?> items) {
        return items == null || items.size() == 0 ? "" : items.get(0).toString();
    }


    public static String shorten(String str, Integer length){
        if(str == null){return "";}
        if(str.length() < length){
            return str;
        }
        else{
            return str.substring(0,length);
        }
    }
}
