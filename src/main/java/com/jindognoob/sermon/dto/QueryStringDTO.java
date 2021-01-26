package com.jindognoob.sermon.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


@Getter
public class QueryStringDTO {
    List<String> hashTags = new ArrayList<String>();
    List<String> querys = new ArrayList<String>();
    
    public void addHashTag(String hashTag){
        this.hashTags.add(hashTag);
    }
    public void addQuery(String query){
        this.querys.add(query);
    }
}
