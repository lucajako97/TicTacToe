package example.network;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class HandlerConnection {

    public void checkTheConnections (List<ClientForServer> list) {

        ListIterator<ClientForServer> listIterator = list.listIterator();
        int i = 0;

        while (listIterator.hasNext()) {

            ClientForServer clientForServer = listIterator.next();

            try {
                if ( i != list.size()-1 ) {
                    clientForServer.getOos().writeObject("keep alive");
                    clientForServer.getOis().readObject();
                }

            } catch (IOException | ClassNotFoundException e) {

                try {
                    clientForServer.getSocket().close();

                } catch (IOException e1) {
                    e.printStackTrace();
                }
                list.remove(i);
                i--;
            }

            i++;
        }

    }

    public boolean checkNickname (String nick, List<ClientForServer> list) {

        for (ClientForServer clientForServer : list) {
            if ( nick.equals(clientForServer.getNickname()) ) return false;
        }

        return true;
    }

    public int whichIsNotDisconnected (List<ClientForServer> list) {

        ListIterator<ClientForServer> listIterator = list.listIterator();
        int i = 0;

        while (listIterator.hasNext()) {

            ClientForServer clientForServer = listIterator.next();

            try {
                clientForServer.getOos().writeObject("keep alive");

            } catch (IOException e) {
                if ( i == 0) return 1;
                else
                    return 0;
            }

            i++;
        }

        return -1;
    }

}
