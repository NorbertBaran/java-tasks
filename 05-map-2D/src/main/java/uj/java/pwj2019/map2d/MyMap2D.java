package uj.java.pwj2019.map2d;

import java.util.*;
import java.util.function.Function;

public class MyMap2D<R, C, V> implements Map2D<R, C, V> {
    Map<R, Map<C, V>> map2D=new LinkedHashMap<>();

    @Override
    public V put(R rowKey, C columnKey, V value) {
        if(rowKey==null || columnKey==null)
            throw new NullPointerException();
        if(!map2D.containsKey(rowKey)) {
            map2D.put(rowKey, new HashMap<>());
        }
        V preValue=map2D.get(rowKey).get(columnKey);
        map2D.get(rowKey).put(columnKey, value);
        return preValue;
    }

    @Override
    public V get(R rowKey, C columnKey) {
        if(map2D.get(rowKey)!=null)
            return map2D.get(rowKey).get(columnKey);
        return null;
    }

    @Override
    public V getOrDefault(R rowKey, C columnKey, V defaultValue) {
        return map2D.getOrDefault(rowKey, Collections.emptyMap()).getOrDefault(columnKey, defaultValue);
    }

    @Override
    public V remove(R rowKey, C columnKey) {
        return map2D.getOrDefault(rowKey, Collections.emptyMap()).remove(columnKey);
    }

    @Override
    public boolean isEmpty() {
        return map2D.isEmpty();
    }

    @Override
    public boolean nonEmpty() {
        return !isEmpty();
    }

    @Override
    public int size() {
        int size=0;
        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> map: map2dSet){
            size+=map.getValue().size();
        }
        return size;
    }

    @Override
    public void clear() {
        map2D.clear();
    }

    @Override
    public Map<C, V> rowView(R rowKey) {
        return map2D.get(rowKey);
    }

    @Override
    public Map<R, V> columnView(C columnKey) {
        Map<R, V> map=new HashMap<R, V>();
        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> iMap: map2dSet)
            if(iMap.getValue().get(columnKey)!=null)
                map.put(iMap.getKey(), iMap.getValue().get(columnKey));
        return map;
    }

    @Override
    public boolean hasValue(V value) {
        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> iMap: map2dSet)
            if(iMap.getValue().containsValue(value))
                return true;
        return false;
    }

    @Override
    public boolean hasKey(R rowKey, C columnKey) {
        if(map2D.get(rowKey)!=null)
            return map2D.get(rowKey).get(columnKey)!=null;
        return false;
    }

    @Override
    public boolean hasRow(R rowKey) {
        return map2D.containsKey(rowKey);
    }

    @Override
    public boolean hasColumn(C columnKey) {
        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> iMap: map2dSet)
            if(iMap.getValue().containsKey(columnKey))
                return true;
        return false;
    }

    @Override
    public Map<R, Map<C,V>> rowMapView() {
        Map<R, Map<C, V>> result=new LinkedHashMap<>();
        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> iMap2D: map2dSet)
            result.put(iMap2D.getKey(), Collections.unmodifiableMap(Map.copyOf(iMap2D.getValue())));
        return Collections.unmodifiableMap(Map.copyOf(result));
    }

    @Override
    public Map<C, Map<R,V>> columnMapView() {
        Map2D columnMap=Map2D.createInstance();
        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> iMap: map2dSet){
            R rowKey=iMap.getKey();
            Set<Map.Entry<C, V>> mapSet=iMap.getValue().entrySet();
            for(Map.Entry<C, V> element: mapSet){
                C columnKey=element.getKey();
                V value=element.getValue();

                columnMap.put(columnKey, rowKey, value);
            }
        }
        return columnMap.rowMapView();
    }

    @Override
    public Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey) {
        if(map2D.containsKey(rowKey)) {
            target.putAll(map2D.get(rowKey));
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey) {
        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> iMap: map2dSet){
            if(iMap.getValue().get(columnKey)!=null)
                target.put(iMap.getKey(), iMap.getValue().get(columnKey));
        }
        return this;
    }

    @Override
    public Map2D<R, C, V>  putAll(Map2D<? extends R, ? extends C, ? extends V> source) {
        Map2D map2D=source;
        this.map2D=map2D.rowMapView();
        return this;
    }

    @Override
    public Map2D<R, C, V>  putAllToRow(Map<? extends C, ? extends V> source, R rowKey) {
        Set<? extends Map.Entry<? extends C, ? extends V>> mapSet=source.entrySet();
        for(Map.Entry<? extends C, ? extends V> element: mapSet){
            this.put(rowKey, element.getKey(), element.getValue());
        }
        return this;
    }

    @Override
    public Map2D<R, C, V>  putAllToColumn(Map<? extends R, ? extends V> source, C columnKey) {
        Set<? extends Map.Entry<? extends R, ? extends V>> mapSet=source.entrySet();
        for(Map.Entry<? extends R, ? extends V> element: mapSet){
            this.put(element.getKey(), columnKey, element.getValue());
        }
        return this;
    }

    @Override
    public <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(
            Function<? super R, ? extends R2> rowFunction,
            Function<? super C, ? extends C2> columnFunction,
            Function<? super V, ? extends V2> valueFunction) {
        Map2D convertedMap2D=Map2D.createInstance();

        Set<Map.Entry<R, Map<C, V>>> map2dSet=map2D.entrySet();
        for(Map.Entry<R, Map<C, V>> iMap: map2dSet){
            R rowKey=iMap.getKey();
            R2 convertedRowKey=rowFunction.apply(rowKey);

            Set<Map.Entry<C, V>> mapSet=iMap.getValue().entrySet();
            for(Map.Entry<C, V> element: mapSet){
                C columnKey=element.getKey();
                C2 convertedColumnKey=columnFunction.apply(columnKey);

                V value=element.getValue();
                V2 convertedValue=valueFunction.apply(value);

                convertedMap2D.put(convertedRowKey, convertedColumnKey, convertedValue);
            }
        }

        return convertedMap2D;
    }
}
