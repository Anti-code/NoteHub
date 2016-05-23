package pojos;
// Generated May 16, 2016 10:12:23 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Document generated by hbm2java
 */
public class Document  implements java.io.Serializable {


     private Integer iddocument;
     private Lectures lectures  ;
     private Users users;
     private String documentInfo;
     private String documentLink;
     private String documentFormat;
     private Date postDate;

    public Document() {
    }

    public Document(Lectures lectures, Users users, String documentInfo, String documentLink, String documentFormat, Date postDate) {
       this.lectures = lectures;
       this.users = users;
       this.documentInfo = documentInfo;
       this.documentLink = documentLink;
       this.documentFormat = documentFormat;
       this.postDate = postDate;
    }
   
    public Integer getIddocument() {
        return this.iddocument;
    }
    
    public void setIddocument(Integer iddocument) {
        this.iddocument = iddocument;
    }
    public Lectures getLectures() {
        return this.lectures;
    }
    
    public void setLectures(Lectures lectures) {
        this.lectures = lectures;
    }
    public Users getUsers() {
        return this.users;
    }
    
    public void setUsers(Users users) {
        this.users = users;
    }
    public String getDocumentInfo() {
        return this.documentInfo;
    }
    
    public void setDocumentInfo(String documentInfo) {
        this.documentInfo = documentInfo;
    }
    public String getDocumentLink() {
        
        return this.documentLink;
    }
    
    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }
    public String getDocumentFormat() {
        return this.documentFormat;
    }
    
    public void setDocumentFormat(String documentFormat) {
        this.documentFormat = documentFormat;
    }
    public Date getPostDate() {
        return this.postDate;
    }
    
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }




}

