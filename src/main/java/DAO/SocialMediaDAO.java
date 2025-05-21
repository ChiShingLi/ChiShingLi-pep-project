package DAO;

import java.sql.*;

import Model.Account;
import Model.Message;

import java.util.ArrayList;
import java.util.List;
import Util.ConnectionUtil;

public class SocialMediaDAO {

    // get user by id
    public Account getUserById(int id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            // return back account obj, if user exists
            if (rs.next()) {
                return new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // get user by username
    public boolean isUniqueUsername(String username) {

        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            // if username found
            if (rs.next())
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    // username is not empty & is unique
    // password length of 4
    // return JSON of the Account, account_id, status code 200
    // save to db
    // else, status code 400
    public Account registerUser(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {

            // if valid username
            if (!account.getUsername().isBlank() && isUniqueUsername(account.getUsername())) {
                if (account.getPassword().length() >= 4) {
                    String sql = "INSERT INTO account(username, password) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, account.getUsername());
                    preparedStatement.setString(2, account.getPassword());
                    int rowAffected = preparedStatement.executeUpdate();

                    // if insert is successful, get back the primary key
                    if (rowAffected > 0) {
                        ResultSet rs = preparedStatement.getGeneratedKeys();

                        if (rs.next()) {
                            int generatedId = rs.getInt(1);
                            Account newAccount = new Account(generatedId, account.getUsername(),
                                    account.getPassword());

                            return newAccount;
                        }
                    }
                }
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // registration not successful
        return null;
    }

    // return existing account obj as json, 200 status
    // 400 staus if failed
    public Account loginUser(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Account existingAccount = new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
                return existingAccount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // return newly created message, 200 status
    // if failed, 400 status
    public Message postMesesge(Message message) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            if (!message.getMessage_text().isBlank() && message.getMessage_text().length() <= 255) {
                // check if user is real
                if (getUserById(message.getPosted_by()) != null) {
                    String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?);";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setInt(1, message.getPosted_by());
                    preparedStatement.setString(2, message.getMessage_text());
                    preparedStatement.setLong(3, message.getTime_posted_epoch());

                    // insert data into the db and get the primary key back
                    int rowAffected = preparedStatement.executeUpdate();
                    if (rowAffected > 0) {
                        ResultSet rs = preparedStatement.getGeneratedKeys();

                        // get the primary back from the inserted statement
                        if (rs.next()) {
                            int generatedId = rs.getInt(1);
                            return new Message(generatedId, message.getPosted_by(), message.getMessage_text(),
                                    message.getTime_posted_epoch());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // return all messages from the db
    // if failed, still return empty list
    public List<Message> getAllMessage() {
        List<Message> messageList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                messageList.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messageList;
    }

    //get a message by message_id
    //if failed, still return status code 200
    public Message getMessageById(String id) {
        Message message = new Message();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.valueOf(id));

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1));
                message.setPosted_by(rs.getInt(2));
                message.setMessage_text(rs.getString(3));
                message.setTime_posted_epoch(rs.getLong(4));
                return message;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
