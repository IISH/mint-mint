
package gr.ntua.ivml.mint.actions;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

@Results({
        @Result(name="error", location="register.jsp"),
        @Result(location="Home.action", type="redirectAction" )
})


/**
 * We disable registering online, hence this empty class.
 */
public class Register extends GeneralAction implements SessionAware{

	protected final Logger log = Logger.getLogger(getClass());

       
    @Action(value="Register",interceptorRefs=@InterceptorRef("defaultStack"))
    public String execute() throws Exception {
        log.info("Registration disabled.");
		return SUCCESS;
      }

    @Override
    public void setSession(Map session) {

    }

}