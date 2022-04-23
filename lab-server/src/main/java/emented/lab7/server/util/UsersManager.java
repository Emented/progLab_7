package emented.lab7.server.util;

import emented.lab7.common.exceptions.DatabaseException;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.db.DBManager;

public class UsersManager {

    private final DBManager dbManager;

    public UsersManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Response registerNewUser(Request request) {
        try {
            if (!dbManager.checkUsersExistence(request.getUsername())) {
                dbManager.addUser(request.getUsername(), request.getPassword());
                return new Response(TextColoring.getGreenText("Registration was completed successfully!"));
            } else {
                return new Response(TextColoring.getRedText("This username already exists!"), false);
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()), false);
        }
    }

    public Response loginUser(Request request) {
        try {
            boolean check = dbManager.validateUser(request.getUsername(), request.getPassword());
            if (check) {
                return new Response(TextColoring.getGreenText("Login successful!"));
            } else {
                return new Response(TextColoring.getRedText("Wrong login or password!"), false);
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()), false);
        }
    }
}
