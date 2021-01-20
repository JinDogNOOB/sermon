package com.jindognoob.sermon.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jindognoob.sermon.domain.QuestionHashTag;

import org.springframework.stereotype.Repository;

@Repository
public class QuestionHashTagRepository {
    @PersistenceContext EntityManager em;

    public void save(QuestionHashTag questionHashTag){
        if(questionHashTag.getId() ==null ){
            em.persist(questionHashTag);
        }else{
            em.merge(questionHashTag);
        }
    }

    public void delete(QuestionHashTag questionHashTag){
        em.remove(questionHashTag);
    }
    
}
