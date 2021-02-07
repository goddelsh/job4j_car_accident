package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AccidentHibernate {
    private final SessionFactory sf;

    public AccidentHibernate(SessionFactory sf) {
        this.sf = sf;
    }

    public Accident save(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(accident);
            session.getTransaction().commit();
            return accident;
        }
    }

    public List<Accident> getAll() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Accident a left join fetch a.type left join fetch a.rules")
                    .list();
        }
    }

    public List<AccidentType> getAccidentTypes() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from AccidentType ")
                    .list();
        }
    }

    public List<Rule> getRules() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Rule ")
                    .list();
        }
    }

    public Set<Rule> getRulesByIds(String[] rIds) {
        if(rIds != null) {
            try (Session session = sf.openSession()) {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
                Root rule = criteriaQuery.from(Rule.class);
                criteriaQuery.select(rule);
                Arrays.asList(rIds).forEach(id -> criteriaQuery.where(criteriaBuilder.equal(rule.get("id"), id)));
                return (Set<Rule>) session.createQuery(criteriaQuery).list().stream().collect(Collectors.toSet());
            }
        } else {
            return new HashSet<>();
        }
    }


    public Accident findById(int id) {
        try (Session session = sf.openSession()) {
            return (Accident) session
                    .createQuery("from Accident a left join fetch a.type left join fetch a.rules where a.id = :id")
                    .setParameter("id", id)
                    .list().stream().findFirst().orElse(null);
        }
    }

    public void edit(Accident accident) {
        try (Session session = sf.openSession()) {
            Transaction tr= session.beginTransaction();
            session.createQuery("update Accident set name = :name where id = :id")
                    .setParameter("name", accident.getName())
                    .setParameter("id", accident.getId())
                    .executeUpdate();
            tr.commit();
        }
    }
}
