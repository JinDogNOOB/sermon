package com.jindognoob.sermon.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jindognoob.sermon.domain.Point;

import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {
    @PersistenceContext EntityManager em;

    public void save(Point point){
        if(point.getId() == null) em.persist(point);
        else em.merge(point);
    }
}
