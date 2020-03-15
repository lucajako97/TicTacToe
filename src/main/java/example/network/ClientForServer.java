package example.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientForServer {
    private Socket socket;
    private String nickname;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientForServer (Socket socket) {

        this.socket = socket;
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Something is gone wrong with the initialization...");
        }

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public Socket getSocket() {
        return socket;
    }

    public void closeConnections() {

        try {
            this.oos.close();

        } catch (IOException e) {
            //e.printStackTrace();
        }
        try {
            this.ois.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
