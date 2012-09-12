package OauthAPI;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mgr.TagFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class checkUser extends HttpServlet{
	private static final Logger log = Logger.getLogger(checkUser.class.getName());
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/plain;charset=utf-8");
		try {
			if(req.getParameter("uid")==null || req.getParameter("name")==null || req.getParameter("img")==null ||req.getParameter("oauth")==null){
				log.severe("user commit error");
				throw new Exception();
			}
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q= new Query("user");
			q.addFilter("uid", FilterOperator.EQUAL, req.getParameter("uid").toString());
			PreparedQuery pq = datastore.prepare(q);
			
			if(pq.countEntities()>0){
				Entity e=null;
				for (Entity result : pq.asIterable()) {
					e=result;
					break;
				}
				if(e!=null){
					Calendar n_time = Calendar.getInstance();
					e.setProperty("name", req.getParameter("name").toString());
					e.setProperty("uid", req.getParameter("uid").toString());
					e.setProperty("photo", req.getParameter("img").toString());					
					e.setProperty("Oauth", req.getParameter("oauth").toString());
					e.setProperty("status", "1");
					e.setProperty("last_time", n_time.getTimeInMillis());
					datastore.put(e);
					return;
				}
			}else{
				Entity e= new Entity("user");
				Calendar n_time = Calendar.getInstance();
				e.setProperty("name", req.getParameter("name").toString());
				e.setProperty("uid", req.getParameter("uid").toString());
				e.setProperty("photo", req.getParameter("img").toString());					
				e.setProperty("Oauth", req.getParameter("oauth").toString());
				e.setProperty("status", "1");
				e.setProperty("last_time", n_time.getTimeInMillis());
				String RootTid = TagFactory.createTag(req.getParameter("uid").toString()+"", null, "Home");
				if(RootTid!=null){	
					e.setProperty("RootTid", RootTid);
				}
				
				datastore.put(e);
				
				return;
			}
				
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
