package com.jindognoob.sermon.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.Answer;
import com.jindognoob.sermon.domain.Question;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.Paging;

import org.springframework.stereotype.Repository;

@Repository
public class QuestionRepository {
    @PersistenceContext EntityManager em;

    public void save(Question question){
        if(question.getId() == null){
            em.persist(question);
        }else{
            em.merge(question);
        }
    }
    public void delete(Question question){
        List<Answer> answers = question.getAnswers();
        for(Answer answer : answers)
            em.remove(answer);
        em.remove(question);
    }

    public Question findOne(Long id){
        return em.find(Question.class, id);
    }

    /**
     * 테스트할때만 사용하시오
     * @return
     */
    public List<Question> findAll(){
        return em.createQuery("select q from Question q", Question.class).getResultList();
    }

    public List<Question> findPage(Paging paging){
        TypedQuery<Question> query = em.createQuery("select q from Question q order by q.id DESC", Question.class);
        query.setFirstResult((paging.getPageNumber()) * paging.getPageSize());
        query.setMaxResults(paging.getPageSize());
        return query.getResultList();
    }
    public List<Question> findPage(Paging paging, QuestionStatusType type){
        TypedQuery<Question> query = em.createQuery("select q from Question q where q.status = :status order by q.id DESC", Question.class);
        query.setFirstResult((paging.getPageNumber()) * paging.getPageSize());
        query.setMaxResults(paging.getPageSize());
        query.setParameter("status", type);
        return query.getResultList();
    }


    public List<Question> findMyQuestionAsPage(Account account, Paging paging){
        TypedQuery<Question> query = em.createQuery("select q from Question q where q.account = :account order by q.id DESC ", Question.class);
        query.setFirstResult((paging.getPageNumber()) * paging.getPageSize());
        query.setMaxResults(paging.getPageSize());
        query.setParameter("account", account);
        return query.getResultList();
    }

    public List<Question> findMyQuestionAsPage(Account account, Paging paging, QuestionStatusType type){
        TypedQuery<Question> query = em.createQuery("select q from Question q where q.account = :account q.status = :status order by q.id DESC ", Question.class);
        query.setFirstResult((paging.getPageNumber()) * paging.getPageSize());
        query.setMaxResults(paging.getPageSize());
        query.setParameter("account", account);
        query.setParameter("status", type);
        return query.getResultList();
    }

}
