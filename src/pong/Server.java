package pong;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.Timer;

public class Server extends Pong {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server() {
        super("Server");

        setLocation(800, 0);

    }

    public void runServer() {
        try {
            server = new ServerSocket(12345, 100);
            waitForConnection();
            getStreams();
            processConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void waitForConnection() throws IOException {
        connection = server.accept();
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());
    }

    private void processConnection() throws IOException {

        Timer t = new Timer(10, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                sendData(new Pong.DataToCommunicate(panel.didHitBall, panel.yPositionPlayerOne, hasStarted));
            }

        });
        t.start();

        do {
            try {
                Object o = input.readObject();
                if (o instanceof Pong.DataToCommunicate) {
                    Pong.DataToCommunicate data = (Pong.DataToCommunicate) o;
                    this.panel.yPositionPlayerTwo = data.paddlePosition;
                    if (!data.successfulHit) {
                        this.panel.endGame();
                    }

                }
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }

        } while (true);
    }

    private void closeConnection() {

        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendData(Object o) {
        try {
            output.writeObject(o);
            output.flush();
        } catch (IOException ioException) {

        }
    }

}