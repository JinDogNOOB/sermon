package com.jindognoob.sermon.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@MappedSuperclass
public class Post {
    
    @Column(name="TITLE")
    private String title;

    @Column(name="CONTENT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;


}
