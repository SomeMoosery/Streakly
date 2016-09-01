package carter.streakly;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by Carter Klein on 6/26/2016.
 */
public class AddActivityRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2:8080/streakly/addBasicActivity.php";
    private Map<String, String> params;

    public AddActivityRequest(String user, String activityName, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user", user);
        params.put("activityName", activityName);
        checkParams(params);
    }


    private Map<String, String> checkParams(Map<String, String> map){
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
