package com.example.lab_4_rest.models;


import com.example.lab_4_rest.entities.TableElement;
import com.example.lab_4_rest.utils.HibernateUtil;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;

import java.util.List;

@Stateless
public class PointBean {

    public boolean check(int x, int y, int r) {
        boolean result;
        if (x <= 0 && y >= 0 && x >= -r && y <= r) result = true;
        else if (x <= 0 && y <= 0 && y > (-1 * x)- r) result = true;
        else result = x >= 0 && y >= 0 && (x * x + y * y) <= (r/2) * (r/2);
        return result;
    }

    public void addPoint(int x, int y, int r, boolean result, String username){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        TableElement newElement = new TableElement();
        newElement.setX(x);
        newElement.setY(y);
        newElement.setR(r);
        newElement.setResult(result);
        newElement.setUsername(username);
        session.persist(newElement);
        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }

    public void updatePoint(int x, int y, int r, long id, boolean result, String username){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("update TableElement set username = :nameParam, x = :xParam" +
                ", y = :yParam"+
                ", r = :rParam"+
                ", result = :resParam"+
                " where id = :idCode");
        query.setParameter("idCode", id);
        query.setParameter("nameParam", username);
        query.setParameter("xParam", x);
        query.setParameter("yParam", y);
        query.setParameter("rParam", r);
        query.setParameter("resParam", check(x, y, r));
        int res = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public List<TableElement> getElements() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return loadAllData(TableElement.class, session);
    }

    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }

}
