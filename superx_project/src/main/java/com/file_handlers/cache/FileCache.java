package com.file_handlers.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.file_handlers.model.FileData;

public final class FileCache {
    private static final Map<String,List<FileData>> CACHE=new ConcurrentHashMap<>();

    private FileCache(){}

    public static List<FileData> get(String uid){
        List<FileData> files=CACHE.get(uid);
        return files==null?null:new ArrayList<>(files);
    }

    public static void put(String uid,List<FileData> files){
        if(uid==null||uid.isBlank()||files==null)return;
        CACHE.put(uid,Collections.unmodifiableList(new ArrayList<>(files)));
    }

    public static void invalidate(String uid){
        if(uid!=null&&!uid.isBlank())CACHE.remove(uid);
    }

    public static void clear(){
        CACHE.clear();
    }

    public static boolean contains(String uid){
        return uid!=null&&CACHE.containsKey(uid);
    }
}
