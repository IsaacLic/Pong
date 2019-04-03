package pong;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.Timer;

public class Client extends Pong {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket client;

    public Client(String host) {
        

        super("Client");

        setLocation(0, 0);

        chatServer = host;
    }

    public void runClient() {
        try {
            connectToServer();
            getStreams();
            processConnection();
        } catch (EOFException eofException) {
            eofException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException {
        client = new Socket(InetAddress.getByName(chatServer), 12345);
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();

        input = new ObjectInputStream(client.getInputStream());
    }

    private void processConnection() throws IOException {

        System.out.println("success!");
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

                    if(data.startButtonPressed && !hasStarted){
                        super.panel.startGame();
                    }
                    if (!data.startButtonPressed && hasStarted){
                        super.panel.endGame();
                    }
                    if (!data.successfulHit) {
                        this.panel.endGame();
                    }
                }

            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }

        } while (!message.equals("SERVER>>> TERMINATE"));
    }

    private void closeConnection() {

        try {
            output.close();
            input.close();
            client.close();
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