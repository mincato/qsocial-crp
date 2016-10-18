package com.qsocialnow.handler;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.GenericInitiator;

public class AjaxAccessDeniedHandler extends GenericInitiator {

	
    public void doInit(Page page, Map<String, Object> args) throws Exception {
        // when this initiator has been executed that means users encounter access denied problem.
    	
    	System.out.println("Te dejo pasaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar???????????????????");
    	
        Execution exec = Executions.getCurrent();
        HttpServletResponse response = (HttpServletResponse) exec.getNativeResponse();
        response.sendRedirect(response.encodeRedirectURL("http://www.volkno.com")); //assume there is /login
        exec.setVoided(true); //no need to create UI since redirect will take place    	
         
        /*Execution exec = Executions.getCurrent();
        
         
        if (null == SecurityUtil.getUser()){ //unauthenticated user
            exec.sendRedirect("/login.zul");
        }else{
            //display error's detail
            Executions.createComponents("/WEB-INF/errors/displayAccessDeniedException.zul", null, args);
        }*/
    }
    
}
