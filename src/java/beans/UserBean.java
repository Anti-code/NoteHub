package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import pojos.Document;
import pojos.Users;
import util.HibernateUtil;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

  private HibernateUtil hibernate_util;
  private Session hibernate_session;
  private List<Users> user_list;
  private List<Users> selected_users;
  private Users new_user = new Users();
  private Users login_user = new Users();
  private String password_verify, email;
  private String password_new, password_old;
  private java.util.Date timestamp = new java.util.Date();
  private Boolean isLoggedIn = true;

  @EJB
  private CounterBean counterBean;

  private int hitCount;

  public UserBean() {
    this.hibernate_session = hibernate_util.getSessionFactory().openSession();
    this.hitCount = 0;

  }

  public List<Users> getUser_list() {
    hibernate_session.beginTransaction();
    user_list = (List<Users>) hibernate_session.createQuery("from Users").list();
    hibernate_session.getTransaction().commit();
    return user_list;
  }
  
  public String deleteSelectedUsers(){
     hibernate_session.beginTransaction();
      for(Users d: selected_users){
        hibernate_session.delete(d);
      }
      hibernate_session.getTransaction().commit();
      return "user_charts.xhtml?faces-redirect=true";
  }

  public String register() {
    try {
      hibernate_session = hibernate_util.getSessionFactory().openSession();

      hibernate_session.beginTransaction();
      new_user.setAuthority("User");
      new_user.setRegisterDate(new Date(timestamp.getTime()));
      hibernate_session.save(new_user);

      hibernate_session.getTransaction().commit();
      return "login.xhtml?faces-redirect=true";
    } catch (HibernateException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Users> dailyUsers() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    hibernate_session.beginTransaction();
    Query q = hibernate_session.createQuery("from Users as u where u.registerDate > :today");

    q.setParameter("today", cal.getTime());
    hibernate_session.getTransaction().commit();
    return (List<Users>) q.list();

  }
  public List<Users> monthlyUsers() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    hibernate_session.beginTransaction();
    Query q = hibernate_session.createQuery("from Users as u where u.registerDate > :today");

    q.setParameter("today", cal.getTime());
    hibernate_session.getTransaction().commit();
    return (List<Users>) q.list();

  }
  public String profile(){
    return "customer-account.xhtml  ";
  }
  public void updateUser(){
    hibernate_session.beginTransaction();
    hibernate_session.update(login_user);
    hibernate_session.getTransaction().commit();
    System.out.println("update succesfull");
  }
  public void updatePassword(){
    if((password_old == null ? login_user.getPassword() == null : password_old.equals(login_user.getPassword())) && (password_new == null ? password_verify == null : password_new.equals(password_verify)) && (password_new == null ? password_old != null : !password_new.equals(password_old)) ){
    login_user.setPassword(password_new);
      hibernate_session.beginTransaction();
    hibernate_session.update(login_user);
    hibernate_session.getTransaction().commit();
    System.out.println("pw update succesfull");
    }
  }
  public String login() {
    try {

      hibernate_session = hibernate_util.getSessionFactory().openSession();
      hibernate_session.beginTransaction();
      Query q = hibernate_session.createQuery("from Users as u where u.EMail =:EMail");
      q.setString("EMail", email);
      hibernate_session.getTransaction().commit();
      setLogin_user((Users) q.list().get(0));
      if (login_user.getPassword().equals(password_verify)) {
        isLoggedIn = true;
        return "/user/index.xhtml?faces-redirect=true";
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String logout() {
    isLoggedIn=false;
    return getRequest().getContextPath()+"/login.xhtml?faces-redirect=true";
  }
public String adminLogout() {
    isLoggedIn=false;
    return "login.xhtml?faces-redirect=true";
  }
  protected HttpServletRequest getRequest() {
    return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
  }

  protected FacesContext getFacesContext() {
    return FacesContext.getCurrentInstance();
  }

  public String admin() {
    return "admin.xhtml?faces-redirect=true";
  }

  public String getPassword_verify() {
    return password_verify;
  }

  public void setPassword_verify(String password_verify) {
    this.password_verify = password_verify;
  }

  public Users getNew_user() {
    return new_user;
  }

  public void setNew_user(Users new_user) {
    this.new_user = new_user;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Users getLogin_user() {
    return login_user;
  }

  public void setLogin_user(Users login_user) {
    this.login_user = login_user;
  }

  public int getHitCount() {
    hitCount = counterBean.getHits();
    return hitCount;
  }

  public void setHitCount(int newHits) {
    this.hitCount = newHits;
  }

  public Boolean getIsLoggedIn() {
    return isLoggedIn;
  }

  public void setIsLoggedIn(Boolean isLoggedIn) {
    this.isLoggedIn = isLoggedIn;
  }

  public String getPassword_new() {
    return password_new;
  }

  public void setPassword_new(String password_new) {
    this.password_new = password_new;
  }

  public String getPassword_old() {
    return password_old;
  }

  public void setPassword_old(String password_old) {
    this.password_old = password_old;
  }

  public List<Users> getSelected_users() {
    return selected_users;
  }

  public void setSelected_users(List<Users> selected_users) {
    this.selected_users = selected_users;
  }
}
