package beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;
import pojos.Notes;
import pojos.Users;
import util.HibernateUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stupid
 */
@ManagedBean
@SessionScoped
public class NoteBean implements Serializable{
     private String search_text;
     private HibernateUtil hibernate_util;
     private Session hibernate_session;
     private List<Notes> notes;
     private String[] note_count;

    public NoteBean() {
        this.hibernate_session=hibernate_util.getSessionFactory().openSession();

    }
     
     
     public String search(){
        
        return "admin.xhtml?faces-redirect=true";
    }

    public String getSearch_text() {
        return search_text;
    }
    

    

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public List<Notes> getNotes() {
        hibernate_session.beginTransaction();
        notes = (List<Notes>) hibernate_session.createQuery("from Notes").list();
        hibernate_session.getTransaction().commit();
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }
    
    public String[] getNote_count() {
        hibernate_session.beginTransaction();
        //user_list =  hibernate_session.createQuery("from Users").list();
        hibernate_session.getTransaction().commit();
        return note_count;
    }

    public void setNote_count(String[] note_count) {
        this.note_count = note_count;
    }
}
