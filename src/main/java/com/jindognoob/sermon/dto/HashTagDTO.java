package com.jindognoob.sermon.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HashTagDTO {
    List<String> hashTags = new ArrayList<String>();
    
    public void addHashTag(String hashTag){
        this.hashTags.add(hashTag);
    }
}
