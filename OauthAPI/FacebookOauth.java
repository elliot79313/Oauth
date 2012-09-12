package OauthAPI;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

class FacebookOauth implements OauthUser{
	
	@SuppressWarnings("unused")
	private String accestoken;
	@SuppressWarnings("unused")
	private String Name;
	@SuppressWarnings("unused")
	private String uid;
	private static String[] scope={"publish_stream", "read_stream"};
	@SuppressWarnings("unused")
	private String imgUrl;

	final private static String appId="appid";
	final private static String secret ="secret key";
	final private static String redirect_uri = "redirect_url"; 
	
	
	private HttpSession session;
	
	protected FacebookOauth(HttpServletRequest req){
		session = req.getSession(true);
		session.setMaxInactiveInterval(60*60);
	}
	@Override
	public String getUID() {
		// TODO Auto-generated method stub
		if(session.getAttribute("uid")==null)
			return null;
		return session.getAttribute("uid").toString();
	}
	
	@Override
	public String getaccess_token() {
		// TODO Auto-generated method stub
		if(session.getAttribute("ac_token")==null)
			return null;
		return session.getAttribute("ac_token").toString();
	}	
	@Override
	public String getuserName() {
		// TODO Auto-generated method stub
		if(session.getAttribute("Name")==null)
		return null;
		return session.getAttribute("Name").toString();
	}
	
	@Override
	public String getImgURL() {
		// TODO Auto-generated method stub
		if(session.getAttribute("imgUrl")==null)
			return null;
		return session.getAttribute("imgUrl").toString();
	}
	public String getProvider(){
	   return Oauth.PROVIDER.Facebook.toString();
	}
	
	public void setUID (String uid){
		this.uid = uid;
		session.setAttribute("uid", uid);	
	}
	
	public void setaccess_token(String ac_token) {
		// TODO Auto-generated method stub
		accestoken = ac_token;
		session.setAttribute("ac_token", ac_token);		
	}
	
	public void setuserName(String name) {
		// TODO Auto-generated method stub
		Name=name;
		session.setAttribute("Name", name);		
	}

	public void setImgURL(String url) {
		// TODO Auto-generated method stub
		imgUrl =url;
		session.setAttribute("imgUrl", url);
	}
	public Boolean getloginstatus() {
		// TODO Auto-generated method stub
		if(session.getAttribute("Oauth")==null)
			return false;
		return (session.getAttribute("Oauth").toString().equals("Facebook")?true:false);
	}
	
	public static String  connectOauthProvider(){
		//"&display=touch"
		String url="https://www.facebook.com/dialog/oauth?client_id="+FacebookOauth.appId +"&redirect_uri="+redirect_uri+""+"&scope=";
		StringBuffer scopeurl= new StringBuffer("");		
		
			
		for (String temp: scope)
			scopeurl.append(","+temp);
		return url=url+scopeurl.toString().replaceFirst(",", "");   
		
		
	}
	public static Oauth parseCode(String code){
		try {
			String url= "https://graph.facebook.com/oauth/access_token?client_id="+FacebookOauth.appId+ "&"+
				"redirect_uri=" + redirect_uri +
				"client_secret="+FacebookOauth.secret +"&"+
				"code="+ URLEncoder.encode(code, "utf-8");	
			URLFetchService service = URLFetchServiceFactory.getURLFetchService();    
			HTTPResponse response = service.fetch(new HTTPRequest(new URL(url), HTTPMethod.GET));
			if(response.getResponseCode()==200){
				String url_getuser = "https://graph.facebook.com/me?fields=name&" + new String(response.getContent())
				+ "&format=json";
				HTTPResponse result = service.fetch(new URL(url_getuser));
				
				/*-- parse facebook response --*/
				
    			JSONObject obj = new JSONObject(new String(result.getContent(),"UTF-8"));
    			
    			Oauth oauth = new Oauth(Oauth.PROVIDER.Facebook);    			
    			oauth.setAccesstoken(new String(response.getContent()));
    			oauth.setUserName(obj.get("name").toString());
    			oauth.setUid(obj.get("id").toString());
    			oauth.setimgUrl("http://graph.facebook.com/"+ obj.get("id").toString() +"/picture");	
    			
    			return oauth;
    			
			}
			return null;
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}
	public void refresh_token(){
		
	}
	public static FacebookOauth getService(HttpServletRequest req){
		return new FacebookOauth(req);
	}
	

	
	
	
}
