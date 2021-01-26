package com.jindognoob.sermon.utils;

import java.util.ArrayList;
import java.util.List;




public class HashTagParser {
    
    
    public static List<String> parseHashTagsString(String queryString){
        List<String> hashTags = new ArrayList<String>();
        String queryStrings[] = queryString.split(" ");

        for(String q : queryStrings){
            if(q.length() < 2) continue;
            if(q.charAt(0)=='#')
                hashTags.add(q.substring(1));
        }
        return hashTags;
    }
}
