package com.jindognoob.sermon.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jindognoob.sermon.domain.HashTag;
import com.jindognoob.sermon.domain.QHashTag;

import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Repository;



@Repository
public class HashTagRepositoty {
    @PersistenceContext EntityManager em;

    public void save(HashTag hashTag){
        if(hashTag.getId() == null){
            em.persist(hashTag);
        }else{
            em.merge(hashTag);
        }
    }

    public HashTag findOne(Long id){
        return em.find(HashTag.class, id);
    }

    public List<HashTag> findByTag(String tag){
        return em.createQuery("select h from HashTag h where h.tag = :tag", HashTag.class)
        .setParameter("tag", tag).getResultList();
    }
    
    public List<HashTag> findCandidateTags(String letter){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QHashTag h = QHashTag.hashTag;

        // .like 는 완전일치만 반환 여기다가 앞뒤로 "%" 붙이는 등으로 부분검색이 가능
        // .contains 는 like 앞뒤로 %붙인거랑 동 
        // 여기서는 앞글자는 똑같아야하니까 전자 사용
        List<Long> ids = queryFactory
            .select(h.id)
            .from(h)
            .where(h.tag.like(letter + "%"))
            .orderBy(h.tag.asc())
            .limit(10)
            .fetch();
        
        return queryFactory.select(h).from(h).where(h.id.in(ids)).orderBy(h.tag.asc()).fetch();
    }


}
