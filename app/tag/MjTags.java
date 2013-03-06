package tag;

import com.mujobo.util.HeaderTabs;
import groovy.lang.Closure;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.templates.JavaExtensions;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/12/13
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */

@FastTags.Namespace("mj")
public class MjTags extends FastTags{
    public static void _searchLink(Map<?, ?> args, Closure body, PrintWriter out,
                             GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Object keywordObj = args.get("keyword");
        Object locationObject = args.get("location");
        String keyword = keywordObj == null ? null : keywordObj.toString();
        String location = locationObject == null ? null : locationObject.toString();
        if(keyword == null && location == null){
            out.println("");
        }
        else if(keyword != null && location == null){
             out.println("search?keyword="+keyword);
        }
        else if(keyword == null && location != null){
            out.println("search?location="+location);
        }
        else{
            out.println("search?keyword="+keyword+"&location="+location);
        }
    }

    public static void _liTab(Map<?, ?> args, Closure body, PrintWriter out,
    GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Object activeTabObj = args.get("activeTab");
        Object idObj = args.get("id");
        out.println("<li");
        if(activeTabObj != null && idObj != null){
            if(idObj.toString().equals(activeTabObj.toString())){
                out.println(" class=\"active\"");
            }
        }
        out.println(">");
        out.println(JavaExtensions.toString(body));
        out.println("</li>");
    }

    public static void _first(Map<?, ?> args, Closure body, PrintWriter out,
                                  GroovyTemplate.ExecutableTemplate template, int fromLine){
        Object listObj = args.get("list");
        if(listObj instanceof List){
            List list = (List)listObj;
            if(list.size() > 0){
                out.print(list.get(0));
            }
        }
    }
}
