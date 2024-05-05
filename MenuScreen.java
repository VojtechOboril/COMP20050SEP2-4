import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuScreen extends JPanel {
    public MenuScreen(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(null);

        JButton newGameButton = new JButton("New Game");
        JButton quitButton = new JButton("Quit");

        newGameButton.setBackground(Color.green);
        quitButton.setBackground(Color.red);

        JLabel title = new JLabel("Welcome to our black box game", JLabel.CENTER);
        title.setForeground(Color.white);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "GameScreen");
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // s
        newGameButton.setBounds(125, 200, 100, 50);
        quitButton.setBounds(275, 200, 100, 50);
        title.setBounds(150, 100, 200, 15);

        this.setSize(300, 400);
        this.add(title);
        this.setBackground(Color.BLACK);
        this.add(newGameButton);
        this.add(quitButton);

    }

}
