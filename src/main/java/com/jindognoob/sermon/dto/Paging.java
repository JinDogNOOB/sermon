package com.jindognoob.sermon.dto;

import lombok.Getter;

@Getter
public class Paging {
    int pageNumber;
    int pageSize;
    public Paging(int pageNumber, int pageSize){
        if(pageSize > 100) pageSize = 100;
        this.pageNumber = pageNumber-1;
        this.pageSize = pageSize;
    }

}
