/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojos.Lectures;
import pojos.Notes;
import pojos.Users;
import util.HibernateUtil;

/**
 *
 * @author stupid
 */
@ManagedBean
@SessionScoped
public class LectureBean implements Serializable{
    private List<Lectures> lecture_list1;
    private List<Lectures> lecture_list2;
    private List<Lectures> lecture_list3;
    private List<Lectures> lecture_list4;
    private List<Lectures> lecture_list_all;
    private HibernateUtil hibernate_util;
    private Session hibernate_session;
    private Transaction transaction;
    public LectureBean() {
        hibernate_session=hibernate_util.getSessionFactory().openSession();
    }

    public List<Lectures> getLecture_list1() {
        return getLectureByYear("1");
    } 
    private List<Lectures> getLectureByYear(String year){
        List<Lectures> l;
        hibernate_session.beginTransaction();
        Query q = hibernate_session.createQuery("from Lectures as l where l.year=:year");
        q.setString("year", year);
        hibernate_session.getTransaction().commit();
        return (List<Lectures>)q.list();
        
    }
    public void setLecture_list1(List<Lectures> lecture_list1) {
        this.lecture_list1 = lecture_list1;
    }

    public List<Lectures> getLecture_list2() {
        return getLectureByYear("2");
    }

    public void setLecture_list2(List<Lectures> lecture_list2) {
        this.lecture_list2 = lecture_list2;
    }

    public List<Lectures> getLecture_list3() {
        return getLectureByYear("3");
    }

    public void setLecture_list3(List<Lectures> lecture_list3) {
        this.lecture_list3 = lecture_list3;
    }

    public List<Lectures> getLecture_list4() {
        return getLectureByYear("4");
    }

    public void setLecture_list4(List<Lectures> lecture_list4) {
        this.lecture_list4 = lecture_list4;
    }

    public List<Lectures> getLecture_list_all() {
        hibernate_session.beginTransaction();
        lecture_list_all = hibernate_session.createQuery("from Lectures").list();
        hibernate_session.getTransaction().commit();
        return lecture_list_all;
    }
    public Lectures getLectureById(Integer id) {
      Lectures l;
      try{
        hibernate_session.beginTransaction();
        Query q = hibernate_session.createQuery("from Lectures as l where l.lectureId=:id");
        q.setInteger("id", id);
        l = (Lectures) q.list().get(0);
        hibernate_session.getTransaction().commit();
      }catch(RuntimeException e){
        transaction.rollback();
        throw e;
      }
        
        return l;
    }

    public void setLecture_list_all(List<Lectures> lecture_list_all) {
        this.lecture_list_all = lecture_list_all;
    }

    

    

    

    
    
}
