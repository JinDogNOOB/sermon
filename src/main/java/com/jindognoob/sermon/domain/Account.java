package com.jindognoob.sermon.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jindognoob.sermon.domain.etypes.AccountRoleType;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNTS")
public class Account {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(name ="EMAIL")
    private String email;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name="PASSWORD")
    private String password;

    @Column(name = "SIGNUP_TYPE")
    @Enumerated(EnumType.STRING)
    private AccountSignupType signupType;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private AccountRoleType roleType;


    // ### mappedby
    @OneToOne(mappedBy = "account")
    private Point point;

    @OneToMany(mappedBy = "account")
    private List<Question> questions = new ArrayList<Question>();

    @OneToMany(mappedBy = "account")
    private List<Answer> answers = new ArrayList<Answer>();
    
}
