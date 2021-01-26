package com.jindognoob.sermon.utils;

import com.jindognoob.sermon.dto.QueryStringDTO;

public class QueryStringParser {
    

    public static QueryStringDTO parseQueryString(String queryString){
        QueryStringDTO result = new QueryStringDTO();
        String queryStrings[] = queryString.split(" ");
        for(String q : queryStrings){
            if(q.length() < 2) continue;
            if(q.charAt(0)=='#')
                result.addHashTag(q.substring(1));
            else
                result.addQuery(q);
        }
        return result;
    }
}
