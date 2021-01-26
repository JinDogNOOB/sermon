package com.jindognoob.sermon.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.Answer;
import com.jindognoob.sermon.domain.QHashTag;
import com.jindognoob.sermon.domain.QQuestion;
import com.jindognoob.sermon.domain.QQuestionHashTag;
import com.jindognoob.sermon.domain.Question;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.dto.HashTagDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public List<Question> findPage(Paging paging, HashTagDTO hashTagDTO){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QQuestion q = QQuestion.question;
        QQuestionHashTag qht = QQuestionHashTag.questionHashTag;
        QHashTag ht = QHashTag.hashTag;

        if(hashTagDTO.getHashTags().size() > 0){
            List<Long> ids = queryFactory
            .select(q.id)
            .from(q)
            .where(ht.tag.in(hashTagDTO.getHashTags()))
            .orderBy(q.id.desc())
            .offset(paging.getPageSize() * paging.getPageNumber())
            .limit(paging.getPageSize())
            .innerJoin(q.questionHashTags, qht)
            .innerJoin(qht.hashTag, ht)
            .fetch();
            return queryFactory.select(q).from(q).where(q.id.in(ids)).orderBy(q.id.desc()).fetch();
        }

        TypedQuery<Question> query = em.createQuery("select q from Question q order by q.id DESC", Question.class);
        query.setFirstResult((paging.getPageNumber()) * paging.getPageSize());
        query.setMaxResults(paging.getPageSize());
        return query.getResultList();
    }
    public List<Question> findPage(Paging paging, long lastIndex, HashTagDTO hashTagDTO){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QQuestion q = QQuestion.question;

        QQuestionHashTag qht = QQuestionHashTag.questionHashTag;
        QHashTag ht = QHashTag.hashTag;

        if(hashTagDTO.getHashTags().size() > 0){
            List<Long> ids = queryFactory
            .select(q.id)
            .from(q)
            .where(q.id.lt(lastIndex).and(ht.tag.in(hashTagDTO.getHashTags())))
            .orderBy(q.id.desc())
            .limit(paging.getPageSize())
            .innerJoin(q.questionHashTags, qht)
            .innerJoin(qht.hashTag, ht)
            .fetch();
            return queryFactory.select(q).from(q).where(q.id.in(ids)).orderBy(q.id.desc()).fetch();
        }


        List<Long> ids = queryFactory
            .select(q.id)
            .from(q)
            .where(q.id.lt(lastIndex))
            .orderBy(q.id.desc())
            .limit(paging.getPageSize())
            .fetch();

            if(CollectionUtils.isEmpty(ids)){
                return new ArrayList<Question>();
            }
        return queryFactory.select(q).from(q).where(q.id.in(ids)).orderBy(q.id.desc()).fetch();

    }
    public List<Question> findPage(Paging paging, QuestionStatusType type){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QQuestion q = QQuestion.question;
        
        // 1) 커버링 인덱스로 대상 조회 -> https://jojoldu.tistory.com/529?category=637935 이분 진짜 주석 멋지게 다신다!! 배움
        List<Long> ids = queryFactory
            .select(q.id)
            .from(q)
            .where(q.status.eq(type))
            .orderBy(q.id.desc())
            .limit(paging.getPageSize())
            .offset(paging.getPageNumber() * paging.getPageSize())
            .fetch();
        
        // 1-1) 대상이 없을 경우
        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<Question>();
        }
        log.info("QuestionRepository 커버링인덱스 : " + ids.size());

        // 2) 1에서 조회한 id로 실제 값 조회
        // select 안에 Projections.fields(DTOclass.class, q.id.as(bookId), q.name ...)등으로 dto 변환 가능
        return queryFactory.select(q).from(q).where(q.id.in(ids)).orderBy(q.id.desc()).fetch();

    }


    public List<Question> findMyQuestionAsPage(Account account, Paging paging){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QQuestion question = QQuestion.question;

        List<Long> ids = queryFactory
            .select(question.id)
            .from(question)
            .where(question.account.eq(account))
            .orderBy(question.id.desc())
            .limit(paging.getPageSize())
            .offset((paging.getPageNumber()) * paging.getPageSize())
            .fetch();

        if(CollectionUtils.isEmpty(ids)) return new ArrayList<Question>();

        return queryFactory
            .select(question)
            .from(question)
            .where(question.id.in(ids))
            .orderBy(question.id.desc()).fetch();
    }

    public List<Question> findMyQuestionAsPage(Account account, Paging paging, QuestionStatusType type){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QQuestion question = QQuestion.question;

        List<Long> ids = queryFactory
            .select(question.id)
            .from(question)
            .where(question.account.eq(account).and(question.status.eq(type)))
            .orderBy(question.id.desc())
            .limit(paging.getPageSize())
            .offset((paging.getPageNumber()) * paging.getPageSize())
            .fetch();

        if(CollectionUtils.isEmpty(ids)) return new ArrayList<Question>();

        return queryFactory
            .select(question)
            .from(question)
            .where(question.id.in(ids))
            .orderBy(question.id.desc()).fetch();
    }



}
