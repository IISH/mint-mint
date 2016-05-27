package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.util.Config;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Properties;

/**
 * Overrides the default login procedure to implement LDAP authentication.
 */
public class LdapLogin extends Login {
    @Override
    @Action(value = "Login", interceptorRefs = @InterceptorRef("defaultStack"))
    public String execute() throws Exception {
        if (getUsername() == null || getUsername().length() == 0) {
            addFieldError("username", "Username is required");
        }
        if (getPassword() == null || getPassword().length() == 0) {
            addFieldError("password", "Password is required");
        }
        if (!getFieldErrors().isEmpty()) {
            return ERROR;
        }

        User user = DB.getUserDAO().getByLogin(getUsername());
        if (user != null) {
            if (!user.isAccountActive()) {
                addActionError("account is no longer active");
                return ERROR;
            }
            else if (!ldapAuthentication()) {
                addActionError("wrong login/password combination");
                return ERROR;
            }
            else {
                log.debug("Login successful for user:" + user.getLogin());
                getSession().put("user", user);
            }
        }
        else {
            addActionError("wrong login/password combination");
            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Authenticates the user through LDAP.
     *
     * @return Whether authentication was successful.
     */
    private boolean ldapAuthentication() {
        try {
            Properties props = new Properties();

            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props.put("com.sun.jndi.ldap.connect.timeout", "5000");
            props.put("com.sun.jndi.ldap.read.timeout", "5000");

            props.put(Context.PROVIDER_URL, Config.get("ldap.url"));
            props.put(Context.SECURITY_PRINCIPAL, Config.get("ldap.usernameField") + "=" + escape(getUsername())
                    + "," + Config.get("ldap.searchBase"));
            props.put(Context.SECURITY_CREDENTIALS, getPassword());

            DirContext ctx = new InitialDirContext(props);
            ctx.close();
        }
        catch (NamingException ne) {
            if (!(ne instanceof AuthenticationException)) {
                log.warn("Failing LDAP connection!", ne);
            }
            return false;
        }
        return true;
    }

    /**
     * Escapes the user provided LDAP input.
     * Code snippet from: https://www.owasp.org/index.php/Preventing_LDAP_Injection_in_Java
     *
     * @param input The input string.
     * @return The escaped input string.
     */
    private static String escape(String input) {
        StringBuilder sb = new StringBuilder();
        if ((input.length() > 0) && ((input.charAt(0) == ' ') || (input.charAt(0) == '#'))) {
            sb.append('\\');
        }
        for (int i = 0; i < input.length(); i++) {
            char curChar = input.charAt(i);
            switch (curChar) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case ',':
                    sb.append("\\,");
                    break;
                case '+':
                    sb.append("\\+");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '<':
                    sb.append("\\<");
                    break;
                case '>':
                    sb.append("\\>");
                    break;
                case ';':
                    sb.append("\\;");
                    break;
                default:
                    sb.append(curChar);
            }
        }
        if ((input.length() > 1) && (input.charAt(input.length() - 1) == ' ')) {
            sb.insert(sb.length() - 1, '\\');
        }
        return sb.toString();
    }
}
