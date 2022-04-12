package emented.lab7.server.util;

import emented.lab7.common.util.DeSerializer;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class ServerSocketWorker {

    private final int defaultPort = 228;
    private final int selectorDelay = 100;
    private Selector selector;
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private int port = defaultPort;

    public ServerSocketWorker(int aPort) throws IOException {
        initialization(aPort);
    }

    public ServerSocketWorker() throws IOException {
        initialization(this.defaultPort);
    }

    private void initialization(int aPort) throws IOException {
        datagramChannel = DatagramChannel.open();
        selector = Selector.open();
        datagramChannel.socket().bind(new InetSocketAddress(aPort));
        datagramChannel.configureBlocking(false);
        datagramChannel.register(selector, SelectionKey.OP_READ);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void stopServer() throws IOException {
        selector.close();
        datagramChannel.close();
    }

    public Request listenForRequest() throws IOException, ClassNotFoundException {
        if (selector.select(selectorDelay) == 0) {
            return null;
        }
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isReadable()) {
                int arraySize = datagramChannel.socket().getReceiveBufferSize();
                ByteBuffer packet = ByteBuffer.allocate(arraySize);
                socketAddress = datagramChannel.receive(packet);
                ((Buffer) packet).flip();
                byte[] bytes = new byte[packet.remaining()];
                packet.get(bytes);
                return DeSerializer.deSerializeRequest(bytes);
            }
        }
        return null;
    }

    public void sendResponse(Response response) throws IOException {
        ByteBuffer bufferToSend = Serializer.serializeResponse(response);
        datagramChannel.send(bufferToSend, socketAddress);
    }
}
