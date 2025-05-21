package Service;

import DAO.SocialMediaDAO;
import Model.Account;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    public boolean isUniqueUsername(String username) {
        return socialMediaDAO.isUniqueUsername(username);
    }

    public Account registerUser(Account account){
        return socialMediaDAO.registerUser(account);
    }

    public Account loginUser(Account account){
        return socialMediaDAO.loginUser(account);
    }
}
