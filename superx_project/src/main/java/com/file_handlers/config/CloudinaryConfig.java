package com.file_handlers.config;

import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {

    public static Cloudinary cloudinary;
    public static Cloudinary getCloudinary(){

    if(cloudinary == null){

        Map <String, Object> config = new HashMap<>();

        config.put("cloud_name","rzxsdf2b");
        config.put("api_key","783414616781416");
        config.put("api_secret","9X5v6lo--CtXVJ-h0mhayiUJi8Y");
        config.put("secure",true);

        cloudinary = new Cloudinary(config);
    }
    return cloudinary;
    }

    
}

