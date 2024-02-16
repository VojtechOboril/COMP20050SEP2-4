import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuScreen extends JPanel {
    public MenuScreen(CardLayout cardLayout, JPanel cardPanel) {
        JButton newGameButton = new JButton("New Game");
        JButton quitButton = new JButton("Quit");

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

        this.add(newGameButton);
        this.add(quitButton);
    }
    
}
