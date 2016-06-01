/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author stupid
 */

import java.io.Serializable;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

import javax.faces.context.FacesContext;


public class SessionCounter implements HttpSessionListener {

  private static int totalActiveSessions;
	
  public static int getTotalActiveSession(){
	return totalActiveSessions;
  }
	
  @Override
  public void sessionCreated(HttpSessionEvent arg0) {
	totalActiveSessions++;
	System.out.println("sessionCreated - add one session into counter");
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent arg0) {
	totalActiveSessions--;
	System.out.println("sessionDestroyed - deduct one session from counter");
  }	
}