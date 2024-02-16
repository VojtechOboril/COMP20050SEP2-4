import javax.swing.*;
import java.awt.*;
import java.awt.CardLayout;

public class Main {

    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public Main() {
        frame = new JFrame("Bkack Box game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        cardPanel.add(new MenuScreen(cardLayout, cardPanel), "MenuScreen");
        cardPanel.add(new GameScreen(cardLayout, cardPanel), "GameScreen");
        cardPanel.add(new EndScreen(cardLayout, cardPanel), "EndScreen");

        cardLayout.show(cardPanel, "MenuScreen");
        frame.add(cardPanel, BorderLayout.CENTER);

        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);   
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}