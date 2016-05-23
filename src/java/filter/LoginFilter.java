/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import beans.UserBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author stupid
 */


/*  Filter settings will be added to web.xml  */
public class LoginFilter implements Filter{
  
  
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    UserBean loginBean = (UserBean)((HttpServletRequest)request).getSession().getAttribute("loginBean");
		
		// For the first application request there is no loginBean in the session so user needs to log in
		// For other requests loginBean is present but we need to check if user has logged in successfully
		if (loginBean == null || !loginBean.getIsLoggedIn()) {
			String contextPath = ((HttpServletRequest)request).getContextPath();
			System.out.println(contextPath);
			((HttpServletResponse)response).sendRedirect(contextPath + "/login.xhtml");
		}
		
		chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
