package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

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

    public Message postMessage(Message message){
        return socialMediaDAO.postMesesge(message);
    }

    public List<Message> getAllMessage(){
        return socialMediaDAO.getAllMessage();
    }
}
