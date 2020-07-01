package uj.java.pwj2019.w3;

import java.util.List;
import java.util.Map;

public class JsonCreator implements JsonMapper {
    private String jsonSwitch(Object value){
        String json="";
        if(value instanceof Map)
            json+=toJsonComposite((Map<String, ?>)value);
        else if(value instanceof List)
            json+=toJsonTable((List)value);
        else if(value instanceof String)
            json+="\""+((String) value).replace("\"", "\\\"")+"\",";
        else if(value instanceof Boolean)
            json+=((Boolean) value).toString()+",";
        else
            json+=value+",";
        return json;
    }

    private String toJsonTable(List list){
        String json="[";
        for(Object listElement: list)
            json+=jsonSwitch(listElement);
        if(json.length()>1)
            json=json.substring(0, json.length()-1);
        json+="],";
        return json;
    }

    private String toJsonComposite(Map<String, ?> map){
        String json="{";
        for(Map.Entry<String, ?>mapElement: map.entrySet()){
            json+="\""+mapElement.getKey()+"\": ";
            json+=jsonSwitch(mapElement.getValue());
        }
        if(json.length()>1)
            json=json.substring(0, json.length()-1);
        json+="},";
        return json;
    }

    @Override
    public String toJson(Map<String, ?> map) {
        if(map!=null){
            String json=toJsonComposite(map);
            if(json.length()>1)
                json=json.substring(0, json.length()-1);
            return json;
        }
        return "{}";
    }
}
