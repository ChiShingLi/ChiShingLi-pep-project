package Controller;

import java.util.List;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getAllMessage);
        app.get("/messages/{message_id}", this::getMessageById);
        return app;
    }

    private void registerUser(Context ctx) {
        Account newAccount = ctx.bodyAsClass(Account.class);
        Account res = socialMediaService.registerUser(newAccount);

        if (res == null) {
            ctx.status(400);
        } else {
            ctx.json(res).status(200);
        }
    }

    private void loginUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account existingAccount = socialMediaService.loginUser(account);

        if (existingAccount == null) {
            // user not found
            ctx.status(401);
        } else {
            // user found
            ctx.json(existingAccount).status(200);
        }
    }

    private void postMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message newMessage = socialMediaService.postMessage(message);

        if (newMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(newMessage).status(200);
        }
    }

    private void getAllMessage(Context ctx) {
        List<Message> messageList = socialMediaService.getAllMessage();
        ctx.json(messageList).status(200);
    }

    private void getMessageById(Context ctx){
        Message message = socialMediaService.getMessageById(ctx.pathParam("message_id"));
        //message_id
        if(message == null){
            ctx.result("").status(200);
        }else{
            ctx.json(message).status(200);
        }
    }
}