package com.ordermanager.utils;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T> {
    @PersistenceContext
    protected EntityManager em;
    private final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.em=JPAUtil.getEntityManager();
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            T savedEntity;
            if (isNew(entity)) {
                em.persist(entity);
                savedEntity = entity;
            } else {
                savedEntity = em.merge(entity);
            }

            tx.commit();
            return savedEntity;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PersistenceException("Failed to save entity of type " + entityClass.getSimpleName(), e);
        }
    }

    public void delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            T managedEntity = em.contains(entity) ? entity : em.merge(entity);
            em.remove(managedEntity);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PersistenceException("Failed to delete entity of type " + entityClass.getSimpleName(), e);
        }
    }

    public Optional<T> findById(Object id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public List<T> findAll() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        TypedQuery<T> query = em.createQuery(jpql, entityClass);
        return query.getResultList();
    }

    private boolean isNew(T entity) {
        try {
            Object id = em.getEntityManagerFactory()
                    .getPersistenceUnitUtil()
                    .getIdentifier(entity);
            return id == null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Entity of type " + entityClass.getSimpleName() +
                    " has no valid ID field", e);
        }
    }
}