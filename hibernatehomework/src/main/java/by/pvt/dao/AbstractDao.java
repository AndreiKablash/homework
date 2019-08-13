package by.pvt.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDao<T> implements GenericDao<T>{
    protected Logger log = Logger.getLogger(String.valueOf(this.getClass()));
    private  Session session;
    private  Class<T> persistentClass;

    public AbstractDao(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    protected Session getSession() {
        if (session == null)
            throw new IllegalStateException("Session has not been set");
        return session;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public T getById(Long id) {
        Transaction tx = null;
        T entity;
        try {
            tx = getSession().beginTransaction();
            entity = getSession().get(getPersistentClass(), id);
            tx.commit();
        } catch (HibernateException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            if(tx!=null) {
                tx.rollback();
            }
            throw e;
        }
        return entity;
    }

    @Override
    public List<T> getList() {
        List<T> studentsList;
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            //create criteria
            CriteriaBuilder cb = getSession().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(getPersistentClass());
            Root<T> rootEntry = cq.from(persistentClass);
            CriteriaQuery<T> all = cq.select(rootEntry);
            //get list of nodes
            studentsList = getSession().createQuery(all).list();
        } catch (HibernateException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            if(tx!=null) {
                tx.rollback();
            }
            throw  e;
        }
        return studentsList;
    }

    @Override
    public boolean save(T entity) {
        boolean flag = false;
        if(entity!=null) {
            Transaction tx = null;
            try {
                tx = getSession().beginTransaction();
                getSession().save(entity);
                tx.commit();
                flag = true;
                log.info("Course saved");
            } catch (HibernateException e) {
                log.log(Level.SEVERE, e.getMessage(), e);
                if(tx!=null) {
                    tx.rollback();
                }
                throw e;
            }
        }else{
            log.info("The input of Object is null");
        }
        return flag;
    }

    @Override
    public void delete(Long id) {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            T entity = getSession().get(getPersistentClass(), id);
            if(entity==null) {
                log.info("There is no a row with id="+ id + " in DB");
                return;
            }
            getSession().delete(entity);
            tx.commit();
            log.info("Row deleted");
        } catch (HibernateException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            if(tx!=null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(Long id, T updateInfo) {
        Transaction tx = null;
        if(updateInfo==null){
            log.info("Node with new information is empty");
            return;
        }
        try {
            tx = getSession().beginTransaction();
            T node = getSession().get(getPersistentClass(), id);
            if(node==null) {
                log.info("There is no a row with id="+ id + " in DB");
                return;
            }
            getSession().update(fillEntity(node,updateInfo));
            tx.commit();
            log.info("Row updated");
        } catch (HibernateException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            if(tx!=null) {
                tx.rollback();
            }
            throw e;
        }
    }

    /**
     * Method to fill T object from database with new information
     * @param node - existing node from database
     * @param updateInfo - contains new information to update existing node
     * @return updated T object
     */
    protected abstract T fillEntity(T node, T updateInfo);
}
