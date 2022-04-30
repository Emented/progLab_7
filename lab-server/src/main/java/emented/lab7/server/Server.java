package emented.lab7.server;

public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        ServerWorker serverWorker = new ServerWorker();
        serverWorker.startServerWorker();
    }
}
