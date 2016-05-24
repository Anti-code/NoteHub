/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.TemporalType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.weld.util.collections.ArraySet;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;
import pojos.Document;
import pojos.Lectures;
import util.HibernateUtil;


/**
 *
 * @author stupid
 */
@ManagedBean
@SessionScoped
public class DocumentBean implements Serializable{
    private List<Document> document_list;
    private HibernateUtil hibernate_util;
    private Document new_document;
    private Session session;
    
    private UploadedFile document_file;
    private File local_file;
    private String doc_info;
    private String file_name;
    private List<String> doc_formats;
    
    private Integer secilen;
    private List<Document> edit_list;
    private Lectures doc_cat;
    private static final long serialVersionUID = 1L;
    
    private List<Document> selected_documents;
    
    private int doc_index;
    private String message = "ready";
    Transaction transaction = null; 
    
    @ManagedProperty(value = "#{userBean}")
    private UserBean userBean;

  public DocumentBean() {
      doc_cat=new Lectures(); 
        session=hibernate_util.getSessionFactory().openSession();
        
     this.doc_index=6;
     loadMore();
        
  }
  public String loadLatest(){
    session.beginTransaction();
    Query q=session.createQuery("from Document as d  ORDER BY d.postDate DESC");
    q.setMaxResults(10);
    document_list=q.list();
    session.getTransaction().commit();
    return "index.xhtml?faces-redirect=true";
  }
  public List<String> listToSet(){
    Set<String> s =new ArraySet();
    List<String> format_list = new ArrayList<>();
    for(Document d:getDocument_list_all()){
      s.add(d.getDocumentFormat());
    }
    format_list.addAll(s);
    return format_list;
  }
    
    
    
    public List<Document> getDocument_list() {
        
        
        return document_list;
    }
    public void paginate(){
    }
    
    
    public List<Document> getDocument_list_all() {
      if(session.getTransaction().isActive()){
        transaction.commit();
      }
        session.beginTransaction();
        List<Document> docs = session.createQuery("from Document as d ORDER BY d.postDate DESC").list();
        session.getTransaction().commit();
        return docs;
    }
    
    public void loadMore(){
        doc_index+=3;
        session.beginTransaction();
        Query q = session.createQuery("from Document as d ORDER BY d.postDate DESC");
        
        session.getTransaction().commit();
        List<Document> tl;
        if(doc_index<=q.list().size()){
          
        tl = (List<Document>) q.list().subList(0, doc_index);
        }
        else{
          tl = (List<Document>) q.list();
        }
        this.document_list=tl;
       
    }
    public void documentByLecture(Integer id){
        if(session.getTransaction().isActive()){
      transaction.commit();
    }
        session.beginTransaction();
       
        Query q = session.createQuery("from Document as d where d.lectures.lectureId=:id ORDER BY d.postDate DESC");
        q.setInteger("id", id);
        session.getTransaction().commit();
       this.document_list = (List<Document>) q.list();
    }
    
    public void documentByFormat(){
        if(session.getTransaction().isActive()){
      transaction.commit();
    }
        if(doc_formats.isEmpty()){
          loadLatest();
        }
        else{
          
        
        session.beginTransaction();
        this.document_list.clear();
        for(Document d: (List<Document>)session.createQuery("from Document as d order by d.postDate DESC").list()){
          if(doc_formats.contains(d.getDocumentFormat())){
          this.document_list.add(d);
          }
        }
        session.getTransaction().commit();
        }
    }
    
    public List<Document> documentByYear(Integer year){
      
        session.beginTransaction();
        Query q = session.createQuery("from Document as d where d.lectures.year=:year ORDER BY d.postDate DESC");
        q.setInteger("year", year);
        session.getTransaction().commit();
       return (List<Document>) q.list();
    }
    public String[] montlyDocumentVariance(Integer day){
      String[] values = null;
      Integer temp;
      session.beginTransaction();
      Calendar c = Calendar.getInstance();
      c.add(Calendar.DATE, -7);
      c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
      Query q = session.createQuery("from Document as d where dayofweek(d.postDate)=:day and d.postDate >:sun");
      q.setMaxResults(7);
      q.setInteger("day", day);
      q.setParameter("sun", c.getTime());
      
      session.getTransaction().commit();
        return values;
    }
    
    public Integer weekly(Integer year, Integer day){
      session.beginTransaction();
      Calendar c = Calendar.getInstance();
      c.add(Calendar.DATE, -7);
      c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
      Query q = session.createQuery("from Document as d where dayofweek(d.postDate)=:day and d.lectures.year=:year and d.postDate >:sun");
      
      q.setMaxResults(7);
      q.setInteger("day", day);
      q.setInteger("year", year);
      q.setParameter("sun", c.getTime());
      List l2=q.list();
      
        session.getTransaction().commit();
     return  l2.size();
    }
    public String upload() {
        if(document_file != null) {
            try (InputStream input = document_file.getInputstream()){
                
                new_document=new Document();
                
                local_file = new File("/home/stupid/NoteHubResources/", document_file.getFileName());
                
                Files.copy(input, local_file.toPath());
                
                new_document.setDocumentFormat(document_file.getContentType());
                new_document.setDocumentInfo(doc_info);
                new_document.setDocumentLink(local_file.toPath().toString());
                new_document.setPostDate(new Date(new java.util.Date().getTime()));
                new_document.setUsers(userBean.getLogin_user());
                
                new_document.setLectures(new LectureBean().getLectureById(secilen));
                
                try{
                  
                transaction = session.beginTransaction();
                session.save(new_document);
                session.getTransaction().commit();
                }catch(RuntimeException e){
                  transaction.rollback();
                  throw e;
                }
                
                
                
                FacesMessage message = new FacesMessage("Dosya başarıyla eklendi");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "index.xhtml?faces-redirect=true";
            } catch (IOException e) {
              if(e instanceof  FileAlreadyExistsException){
                FacesMessage message = new FacesMessage("Bu döküman daha önce eklenmiş.");
                FacesContext.getCurrentInstance().addMessage(null, message);
              }
                
            }
            
        }
        return null;
    }
    public void export(String path) throws IOException {
        System.out.println(path);
    File file = new File(path);
    Faces.sendFile(file, true); 
    }
    public String delete(Integer iddoc) {
      if(session.getTransaction().isActive()){
        transaction.commit();
      }
        session.beginTransaction();
        Query q =session.createQuery("delete from Document as d where d.iddocument:iddoc");
        q.setInteger("iddoc", iddoc);
        q.executeUpdate();
        session.getTransaction().commit();
        return "document_charts.xhtml";

      }
    public String deleteSelectedDocuments(){
      session.beginTransaction();
      for(Document d: selected_documents){
        session.delete(d);
      }
      session.getTransaction().commit();
      return "charts.xhtml?faces-redirect=true";
    }
   

    public Document getNew_document() {
        return new_document;
    }

    public void setNew_document(Document new_document) {
        this.new_document = new_document;
    }

    public UploadedFile getDocument_file() {
        return document_file;
    }

    public void setDocument_file(UploadedFile document_file) {
        this.document_file = document_file;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getDoc_info() {
        return doc_info;
    }

    public void setDoc_info(String doc_info) {
        this.doc_info = doc_info;
    }

    public Lectures getDoc_cat() {
        return doc_cat;
    }

    public void setDoc_cat(Lectures doc_cat) {
        this.doc_cat = doc_cat;
    }


    public Integer getSecilen() {
        return secilen;
    }

    public void setSecilen(Integer secilen) {
        this.secilen = secilen;
    }
    
    public void fileSelected(FileUploadEvent event) {
      
      
      file_name=event.getFile().getFileName();
      
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

  public void setDocument_list(List<Document> document_list) {
    this.document_list = document_list;
  }

  public List<Document> getSelected_documents() {
    return selected_documents;
  }

  public void setSelected_documents(List<Document> selected_documents) {
    this.selected_documents = selected_documents;
  }

  public int getDoc_index() {
    return doc_index;
  }

  public void setDoc_index(int doc_index) {
    this.doc_index = doc_index;
  }

  public List<String> getDoc_formats() {
    return doc_formats;
  }

  public void setDoc_formats(List<String> doc_formats) {
    this.doc_formats = doc_formats;
  }

  public List<Document> getEdit_list() {
    return edit_list;
  }

  public void setEdit_list(List<Document> edit_list) {
    this.edit_list = edit_list;
  }

     public void onRowEdit(RowEditEvent event) {
       System.out.println("Hi");
     }
     
    public void onRowCancel(RowEditEvent event) {
    }
}
