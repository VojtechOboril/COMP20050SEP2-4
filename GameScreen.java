import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameScreen extends JPanel{
    public GameScreen(CardLayout cardLayout, JPanel cardPanel) {
        JButton endGameButton = new JButton("End Game");
        endGameButton.setBackground(Color.red);

        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "EndScreen");
            }
        });

        this.setBackground(Color.BLACK);
        this.add(endGameButton);

        this.add(new Board());
    }
}
