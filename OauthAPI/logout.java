package OauthAPI;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@SuppressWarnings("serial")
public class logout extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setCharacterEncoding("utf-8");
	    resp.setContentType("text/plain;charset=utf-8");
	    OauthUser user = OauthFactory.getService(req);
	    HttpSession session =req.getSession(true);
	    if (user!=null){
	    	
	    	session.invalidate();
	    	resp.getWriter().print("{\"status\":\"logout\"}");
	    	return;
	    }
	    session.invalidate();
	    resp.getWriter().print("{\"status\":\"null\"}");
	}
}
