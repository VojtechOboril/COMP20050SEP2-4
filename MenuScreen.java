import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MenuScreen extends JPanel {
    public MenuScreen(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(null);

        cardPanel.setBackground(Color.PINK);
        JButton newGameButton = new JButton("New Game");
        JButton quitButton = new JButton("Quit");

        //newGameButton.setLayout(null);
        newGameButton.setBackground(Color.green);
        quitButton.setBackground(Color.red);

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
        newGameButton.setBounds(150,120,100,50);
        quitButton.setBounds(250,120,100,50);

        this.setBackground(Color.PINK);
        this.add(newGameButton);
        this.add(quitButton);

    }
    
}
