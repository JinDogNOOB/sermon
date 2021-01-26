package com.jindognoob.sermon.utils;

import com.jindognoob.sermon.dto.HashTagDTO;


public class HashTagParser {
    
    
    public static HashTagDTO parseQueryString(String queryString){
        HashTagDTO result = new HashTagDTO();
        String queryStrings[] = queryString.split(" ");
        for(String q : queryStrings){
            if(q.length() < 2) continue;
            if(q.charAt(0)=='#')
                result.addHashTag(q.substring(1));
        }
        return result;
    }
}
