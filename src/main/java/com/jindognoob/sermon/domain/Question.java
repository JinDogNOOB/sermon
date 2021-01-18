package com.jindognoob.sermon.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.jindognoob.sermon.domain.etypes.QuestionStatusType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "QUESTIONS")
@Entity
public class Question extends Post{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private QuestionStatusType status;



    // Getter
    public void setAccount(Account account){
        this.account = account;
        account.getQuestions().add(this);
    }

    // mappedby
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<Answer>();
}
