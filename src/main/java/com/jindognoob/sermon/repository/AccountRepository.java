package com.jindognoob.sermon.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jindognoob.sermon.domain.Account;

import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    @PersistenceContext
    EntityManager em;

    public void save(Account account) {
        if (account.getId() == null) {
            em.persist(account);
        } else {
            em.merge(account);
        }
    }

    public Account findOne(Long id) {
        return em.find(Account.class, id);
    }

    public List<Account> findAll() {
        return em.createQuery("select a from Account a", Account.class).getResultList();
    }

    public List<Account> findByEmail(String email) {
        return em.createQuery("select a from Account a where a.email = :email", Account.class)
                .setParameter("email", email).getResultList();
    }

    public Account findOneByEmail(String email) {
        return em.createQuery("select a from Account a where a.email = :email", Account.class)
                .setParameter("email", email).getSingleResult();
    }

    public Optional<Account> findOneByEmailAsOptional(String email) {
        return Optional.of(em.createQuery("select a from Account a where a.email = :email", Account.class)
                .setParameter("email", email).getSingleResult());
    }
}
