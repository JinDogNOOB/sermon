package com.jindognoob.sermon.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "POINTS")
public class Point {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POINT_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "AMOUNT")
    private Long amount;


    
    // Getter
    public void setAccount(Account account){
        this.account = account;
        account.setPoint(this);
    }

    // ## BM ##
    public void addPoint(Long amount){
        this.amount += amount;
    }
    public void subPoint(Long amount) throws Exception{
        Long restAmount = this.amount - amount;
        if(restAmount < 0) throw new Exception("not enough point");
        this.amount = restAmount;
    }
    
}
