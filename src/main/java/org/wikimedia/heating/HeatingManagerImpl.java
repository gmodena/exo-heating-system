package org.wikimedia.heating;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


public class HeatingManagerImpl {
    public class HeatingClient {
        private final String host;
        private final Integer port;

        HeatingClient(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public void send(byte[] message) throws IOException {
            Socket socket = new Socket(host, port);
            OutputStream os = socket.getOutputStream();
            os.write(message);
            os.flush();
            os.close();
            socket.close();
        }
    }

    private final String OFF = "off";
    private final String ON = "on";

    private final String HEATER_HOST = "heater.home";
    private final Integer HEATER_PORT = 9999;

    HeatingClient client = new HeatingClient(HEATER_HOST, HEATER_PORT);

    public void adjust(Boolean toggle) {
        if (toggle) {
            changeStatus(OFF);
        } else {
            changeStatus(ON);
        }
    }

    private void changeStatus(String statusCode) {
        try {
            client.send(statusCode.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
