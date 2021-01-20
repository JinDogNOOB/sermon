package com.jindognoob.sermon.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="HASHTAGS")
@Entity
public class HashTag {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "HASHTAG_ID")
    private Long id;

    @Column(name = "HASHTAG")
    private String hashTag;
}
