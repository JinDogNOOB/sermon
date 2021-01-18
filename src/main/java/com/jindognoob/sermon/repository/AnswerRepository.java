package com.jindognoob.sermon.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jindognoob.sermon.domain.Answer;
import com.jindognoob.sermon.domain.Question;

import org.springframework.stereotype.Repository;

@Repository
public class AnswerRepository {
    @PersistenceContext EntityManager em;

    public void save(Answer answer){
        if(answer.getId() == null){
            em.persist(answer);
        }else{
            em.merge(answer);
        }
    }

    public void delete(Answer answer){
        em.remove(answer);
    }

    public Answer findOne(Long id){
        return em.find(Answer.class, id);
    }
    /**
     * 테스트할대만 사용하세요
     * @return
     */
    public List<Answer> findAll(){
        return em.createQuery("select a from Answer a", Answer.class).getResultList();
    }

    public List<Answer> findAllByQuestion(Question question){
        return em.createQuery("select a from Answer a where a.question = :question", Answer.class)
            .setParameter("question", question)
            .getResultList();
    }
    
}
