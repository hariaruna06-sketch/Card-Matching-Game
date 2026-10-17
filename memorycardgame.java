import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
class memorycardGame extends JFrame {

    private final JButton[] cards = new JButton[16];

    private final String[] symbols = {
            "🍎", "🍎",
            "🍌", "🍌",
            "🍒", "🍒",
            "⭐", "⭐",
            "❤️", "❤️",
            "⚽", "⚽",
            "🎁", "🎁",
            "😘", "😘"
    };

    private final boolean[] matched = new boolean[16];

    private int firstCard = -1;
    private int secondCard = -1;
    private int moves = 0;
    private int matchedPairs = 0;
    private int seconds = 0;

    private boolean checking = false;
    private boolean gameStarted = false;

    private JLabel movesLabel;
    private JLabel timerLabel;
    private JLabel messageLabel;

    private javax.swing.Timer gameTimer;
    private javax.swing.Timer flipTimer;

    public memorycardGame() {

        setTitle("Team 3 - Memory Card Matching Game");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // Title and game information
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 5, 10)
        );

        JLabel titleLabel = new JLabel(
                "Memory Card Matching Game",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(45, 55, 120));

        JPanel infoPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 35, 5)
        );
        infoPanel.setBackground(Color.WHITE);

        movesLabel = new JLabel("Moves: 0");
        timerLabel = new JLabel("Time: 0 seconds");

        movesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        infoPanel.add(movesLabel);
        infoPanel.add(timerLabel);

        topPanel.add(titleLabel);
        topPanel.add(infoPanel);

        add(topPanel, BorderLayout.NORTH);

        // 4 x 4 card grid
        JPanel gridPanel = new JPanel(
                new GridLayout(4, 4, 8, 8)
        );

        gridPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        );
        gridPanel.setBackground(Color.WHITE);

        createCards(gridPanel);

        add(gridPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 15, 15)
        );

        messageLabel = new JLabel(
                "Find all 8 matching pairs!",
                SwingConstants.CENTER
        );

        messageLabel.setFont(new Font("Arial", Font.BOLD, 15));

        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setBackground(new Color(45, 150, 90));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);

        restartButton.addActionListener(e -> restartGame());

        bottomPanel.add(messageLabel);
        bottomPanel.add(restartButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Game timer
        gameTimer = new javax.swing.Timer(1000, e -> {
            seconds++;
            timerLabel.setText("Time: " + seconds + " seconds");
        });

        setVisible(true);
    }

    private void createCards(JPanel gridPanel) {

        List<String> shuffledSymbols =
                new ArrayList<>(Arrays.asList(symbols));

        Collections.shuffle(shuffledSymbols);

        for (int i = 0; i < 16; i++) {

            final int index = i;

            cards[i] = new JButton("?");
            cards[i].setFont(
                    new Font("Segoe UI Emoji", Font.BOLD, 30)
            );

            cards[i].setBackground(new Color(80, 110, 220));
            cards[i].setForeground(Color.WHITE);
            cards[i].setFocusPainted(false);
            cards[i].setOpaque(true);

            cards[i].putClientProperty(
                    "symbol", shuffledSymbols.get(i)
            );

            cards[i].addActionListener(
                    e -> flipCard(index)
            );

            gridPanel.add(cards[i]);
        }
    }

    private void flipCard(int index) {

        if (checking || matched[index]
                || !cards[index].getText().equals("?")) {
            return;
        }

        if (!gameStarted) {
            gameStarted = true;
            gameTimer.start();
        }

        String symbol = (String)
                cards[index].getClientProperty("symbol");

        cards[index].setText(symbol);
        cards[index].setBackground(Color.WHITE);
        cards[index].setForeground(Color.BLACK);

        if (firstCard == -1) {

            firstCard = index;

        } else {

            secondCard = index;
            moves++;

            movesLabel.setText("Moves: " + moves);

            checkMatch();
        }
    }

    private void checkMatch() {

        String firstSymbol = (String)
                cards[firstCard].getClientProperty("symbol");

        String secondSymbol = (String)
                cards[secondCard].getClientProperty("symbol");

        if (firstSymbol.equals(secondSymbol)) {

            matched[firstCard] = true;
            matched[secondCard] = true;

            cards[firstCard].setBackground(
                    new Color(130, 220, 150)
            );

            cards[secondCard].setBackground(
                    new Color(130, 220, 150)
            );

            matchedPairs++;

            messageLabel.setText(
                    "Matched! Pairs: " + matchedPairs + "/8"
            );

            firstCard = -1;
            secondCard = -1;

            if (matchedPairs == 8) {
                finishGame();
            }

        } else {

            checking = true;

            messageLabel.setText("Not a match! Try again.");

            flipTimer = new javax.swing.Timer(1000, e -> {

                cards[firstCard].setText("?");
                cards[secondCard].setText("?");

                cards[firstCard].setBackground(
                        new Color(80, 110, 220)
                );

                cards[secondCard].setBackground(
                        new Color(80, 110, 220)
                );

                cards[firstCard].setForeground(Color.WHITE);
                cards[secondCard].setForeground(Color.WHITE);

                firstCard = -1;
                secondCard = -1;

                checking = false;

                messageLabel.setText(
                        "Find all 8 matching pairs!"
                );
            });

            flipTimer.setRepeats(false);
            flipTimer.start();
        }
    }

    private void finishGame() {

        gameTimer.stop();

        int score = Math.max(
                0, 100 - (moves - 8) * 5
        );

        messageLabel.setText(
                "You Won! Final Score: " + score
        );

        JOptionPane.showMessageDialog(
                this,
                "Congratulations! You Won!\n\n"
                        + "Total Moves: " + moves + "\n"
                        + "Time Taken: " + seconds + " seconds\n"
                        + "Final Score: " + score,
                "Game Completed",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void restartGame() {

        gameTimer.stop();

        if (flipTimer != null) {
            flipTimer.stop();
        }

        firstCard = -1;
        secondCard = -1;
        moves = 0;
        matchedPairs = 0;
        seconds = 0;

        checking = false;
        gameStarted = false;

        movesLabel.setText("Moves: 0");
        timerLabel.setText("Time: 0 seconds");

        messageLabel.setText(
                "Find all 8 matching pairs!"
        );

        List<String> shuffledSymbols =
                new ArrayList<>(Arrays.asList(symbols));

        Collections.shuffle(shuffledSymbols);

        for (int i = 0; i < 16; i++) {

            matched[i] = false;

            cards[i].setText("?");
            cards[i].putClientProperty(
                    "symbol", shuffledSymbols.get(i)
            );

            cards[i].setBackground(
                    new Color(80, 110, 220)
            );

            cards[i].setForeground(Color.WHITE);
            cards[i].setEnabled(true);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new memorycardGame()
        );
    }
}