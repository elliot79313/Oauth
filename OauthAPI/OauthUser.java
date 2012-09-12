package OauthAPI;


public interface OauthUser {
	
	
    String getaccess_token();

    String getuserName();

    String getUID();    

    String getImgURL();
    String getProvider();
    void logout();
    
}
