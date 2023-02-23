package com.example.lab_4_rest.models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.lab_4_rest.entities.TableUser;
import com.example.lab_4_rest.utils.ExceptionCodes;
import com.example.lab_4_rest.utils.HibernateUtil;
import jakarta.ejb.Singleton;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.lab_4_rest.utils.ExceptionCodes.*;

@Singleton
public class UserBean {
    private List<Integer> tokens = new ArrayList<>();

    public ExceptionCodes addUser(String userName, String password){
       Session session = HibernateUtil.getSessionFactory().openSession();
       if(Objects.equals(userName, "")){
           session.close();
           HibernateUtil.shutdown();
            return ExceptionCodes.NULL_USERNAME;
       }
       if(Objects.equals(password, "")){
           session.close();
           HibernateUtil.shutdown();
           return ExceptionCodes.NULL_PASSWORD;
       }
        TableUser user = getUser(userName, session);
        if(user!=null){
            session.close();
            HibernateUtil.shutdown();
            return  ExceptionCodes.TAKEN_USERNAME;
        }
        session.getTransaction().begin();
        TableUser newElement = new TableUser();
        newElement.setPoint_user(userName);
        newElement.setPassword(hashCode(password));
        session.persist(newElement);
        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
        return OK;
    }

    public Integer login(String userName, String password){
        Session session = HibernateUtil.getSessionFactory().openSession();
        TableUser data = getUser(userName, session);
        if(data==null){
            session.close();
            HibernateUtil.shutdown();
            return null;
        }
        boolean pass = validate(data.getPassword(), password);
        if(pass){
            Integer token = tokenGenerate(userName);
            if(!tokens.contains(token)) tokens.add(token);
            return token;
        }
        session.close();
        HibernateUtil.shutdown();
        return 0;
    }

    public boolean checkToken(Integer token){
        return tokens.contains(token);
    }

    public void exit(Integer token){
        tokens.remove(token);
    }

    private TableUser getUser(String userName, Session session){
        System.out.println(userName);
        List<TableUser> users = loadAllData(TableUser.class, session);
        System.out.println(users.toString());
        if(!users.isEmpty() && users!=null) {
            for (TableUser user : users) {
                if (user.getPoint_user().equals(userName)) return user;
            }
        }
        return null;
    }


    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }

    private Integer tokenGenerate(String user){
            int a = (int)(Math.random()*100000);
            a += user.hashCode();
            return a;
        }



    private String hashCode(String code){
        return BCrypt.withDefaults().hashToString(12, code.toCharArray());
    }
    private boolean validate(String hash, String code){
        BCrypt.Result result = BCrypt.verifyer().verify(code.toCharArray(), hash);
        System.out.println(result.verified);
        return result.verified;
    }
}
