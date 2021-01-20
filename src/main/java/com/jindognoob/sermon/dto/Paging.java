package com.jindognoob.sermon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    int pageNumber;
    int pageSize;
    public Paging(int pageNumber, int pageSize){
        if(pageSize > 100) pageSize = 100;
        this.pageNumber = pageNumber-1;
        this.pageSize = pageSize;
    }

}
