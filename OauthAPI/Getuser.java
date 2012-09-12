package OauthAPI;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class Getuser extends HttpServlet {
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/plain;charset=utf-8");
		OauthUser user = OauthFactory.getService(req);
		
			if(user!=null){
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				Query q= new Query("user");
				q.addFilter("uid",FilterOperator.EQUAL, user.getUID());
				PreparedQuery pq = datastore.prepare(q);
				String tid = "";
				for(Entity result: pq.asIterable()){
					tid = result.getProperty("RootTid").toString();
					break;
				}
				
				resp.getWriter().print("{\"id\":\""+ user.getUID() +"\",\"name\":\"" + 
									user.getuserName() + "\",\"provider\":\"" +
									user.getProvider() + "\",\"img\":\"" +
									user.getImgURL() + "\",\"tid\":\"" +
									tid + "\",\"access_token\":\"" +
									user.getaccess_token()+"\"}");
				return;
			}
			resp.getWriter().print("{\"error\":\"Nofound\"}");
			return;
		
	}
}
