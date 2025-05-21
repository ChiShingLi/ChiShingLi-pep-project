package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class SocialMediaDAO {

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

}
