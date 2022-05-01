package emented.lab7.client;

import emented.lab7.client.util.ClientSocketWorker;
import emented.lab7.client.util.Session;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.RequestType;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientWorker {
    private final Scanner scanner = new Scanner(System.in);
    private final int maxPort = 65535;
    private final int logAndPasMinLen = 5;
    private ClientSocketWorker clientSocketWorker;

    public void startClientWorker() {
        ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Welcome to the program! To see the list of commands type HELP"));
        inputAddress();
        inputPort();
        List<String> user;
        if (askForRegistration()) {
            user = registerUser();
        } else {
            user = loginUser();
        }
        Session session = new Session(user, clientSocketWorker);
        session.startSession();
    }

    private boolean askForRegistration() {
        ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Do you have an account? [y/n]"));
        while (true) {
            try {
                String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                if ("y".equals(s)) {
                    return false;
                } else if ("n".equals(s)) {
                    return true;
                } else {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("You entered not valid symbol, try again"));
                }
            } catch (NoSuchElementException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
                System.exit(1);
            }
        }
    }

    private List<String> registerUser() {
        ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Welcome to the registration tab!"));
        String login;
        String password;
        while (true) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Enter the username that you will"
                    + " use to work with the application (it should contain at least 5 symbols)"));
            while (true) {
                login = scanner.nextLine().trim();
                if (login.length() < logAndPasMinLen) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Login is too small, try again"));
                    continue;
                }
                break;
            }
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Enter the password that you will"
                    + " use to work with the application (it should contain at least 5 symbols)"));
            while (true) {
                password = scanner.nextLine().trim();
                if (password.length() < logAndPasMinLen) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Password is too small, try again"));
                    continue;
                }
                break;
            }
            try {
                clientSocketWorker.sendRequest(new Request(login, password, RequestType.REGISTER));
                List<String> listToReturn = receiveUsersResponse(login, password);
                if (listToReturn != null) {
                    return listToReturn;
                }
            } catch (SocketTimeoutException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The waiting time for a response from the server has been exceeded, try again later"));
            } catch (IOException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while serializing the request, try again"));
            } catch (ClassNotFoundException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The response came damaged"));
            }
        }
    }

    private List<String> loginUser() {
        ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Welcome to the login tab!"));
        String login;
        String password;
        while (true) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Enter your login"
                    + " (it should contain at least 5 symbols)"));
            while (true) {
                login = scanner.nextLine().trim();
                if (login.length() < logAndPasMinLen) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Login is too small, try again"));
                    continue;
                }
                break;
            }
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Enter your password"
                    + " (it should contain at least 5 symbols)"));
            while (true) {
                password = scanner.nextLine().trim();
                if (password.length() < logAndPasMinLen) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Password is too small, try again"));
                    continue;
                }
                break;
            }
            try {
                clientSocketWorker.sendRequest(new Request(login, password, RequestType.LOGIN));
                List<String> listToReturn = receiveUsersResponse(login, password);
                if (listToReturn != null) {
                    return listToReturn;
                }
            } catch (SocketTimeoutException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The waiting time for a response from the server has been exceeded, try again later"));
            } catch (IOException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while serializing the request, try again"));
            } catch (ClassNotFoundException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The response came damaged"));
            }
        }
    }

    private List<String> receiveUsersResponse(String login, String password) throws ClassNotFoundException, IOException {
        Response response = clientSocketWorker.receiveResponse();
        ClientConfig.getConsoleTextPrinter().printlnText(response.getMessageToResponse());
        if (response.isSuccess()) {
            List<String> listToReturn = new ArrayList<>();
            listToReturn.add(login);
            listToReturn.add(password);
            return listToReturn;
        }
        return null;
    }

    private void inputAddress() {
        ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Do you want to use a default server address? [y/n]"));
        while (true) {
            try {
                String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                if ("y".equals(s)) {
                    clientSocketWorker = new ClientSocketWorker();
                    break;
                } else if ("n".equals(s)) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Please enter the server's internet address"));
                    String address = scanner.nextLine();
                    clientSocketWorker = new ClientSocketWorker();
                    clientSocketWorker.setAddress(address);
                    break;
                } else {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("You entered not valid symbol, try again"));
                }
            } catch (UnknownHostException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Unknown address, try again"));
            } catch (SocketException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Troubles, while opening server port, try again"));
            } catch (NoSuchElementException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
                System.exit(1);
            }
        }
    }

    private void inputPort() {
        ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Do you want to use a default port? [y/n]"));
        try {
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Please enter the remote host port (1-65535)"));
                String port = scanner.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= maxPort) {
                        clientSocketWorker.setPort(portInt);
                    } else {
                        ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The number did not fall within the limits, repeat the input"));
                        inputPort();
                    }
                } catch (IllegalArgumentException e) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Error processing the number, repeat the input"));
                    inputPort();
                }
            } else if (!"y".equals(s)) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("You entered not valid symbol, try again"));
                inputPort();
            }
        } catch (NoSuchElementException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
            System.exit(1);
        }
    }
}
