package pong;

import javax.swing.JFrame;

public class ClientTest {
    public static void main(String[] args) {
        Client application;

        if (args.length == 0)
            application = new Client("192.168.43.1");
        else
            application = new Client(args[0]);

        application.startButton.setVisible(false);
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.runClient();
    }
}