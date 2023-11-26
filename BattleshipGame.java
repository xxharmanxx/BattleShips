import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class BattleshipGame extends JFrame {

    private final int numRows = 10;
    private final int numCols = 10;

    private final char[][] playerGrid;
    private JButton[][] gridButtons; // For the game grid
    private JLabel statusLabel; // To display status messages

    private char[][] computerGrid; // For tracking computer's ships
    private final Random random;
    private int playerShips;
    private int computerShips;
    // Game state variables


    public BattleshipGame() {
        computerGrid = new char[numRows][numCols];
        random = new Random();
        initializeComponents();
        initializeGameState();
        playerGrid = new char[numRows][numCols];
        computerGrid = new char[numRows][numCols];
    }


    private void initializeComponents() {
        // Main window setup
        for (int i = 0; i < numRows; i++) {
            Arrays.fill(computerGrid[i], ' '); // Set all to empty
        }
        setTitle("Battleship Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize panels
        JPanel gridPanel = new JPanel();
        JPanel menuPanel = new JPanel();
        JPanel statusPanel = new JPanel();

        initializeGridPanel(gridPanel);
        initializeMenuPanel(menuPanel);
        initializeStatusPanel(statusPanel);

        // Adding panels to the main frame
        add(gridPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);

        // Finalizing the main frame
        pack();
        setVisible(true);
    }

    private void initializeGameState() {
        // Clearing the grid for a new game
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                computerGrid[i][j] = ' ';
            }
        }

        // Deploy computer's fleet
        deployShip(4, 1); // 1 Battleship
        deployShip(3, 2); // 2 Cruisers
        deployShip(2, 3); // 3 Destroyers
        deployShip(1, 3); // 3 Submarines
        playerShips = 10; // Total number of player's ships
        computerShips = 10; // Total number of computer's ships
    }

    private void deployShip(int size, int count) {
        for (int i = 0; i < count; i++) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(numRows);
                int y = random.nextInt(numCols);
                boolean horizontal = random.nextBoolean();

                placed = canPlaceShip(x, y, size, horizontal);
                if (placed) {
                    placeShip(x, y, size, horizontal);
                }
            }
        }
    }

    private boolean canPlaceShip(int x, int y, int size, boolean horizontal) {
        // Check if the ship can be placed at the specified location
        for (int i = 0; i < size; i++) {
            int dx = x + (horizontal ? i : 0);
            int dy = y + (horizontal ? 0 : i);

            if (dx >= numRows || dy >= numCols || computerGrid[dx][dy] != ' ') {
                return false; // Ship can't be placed here
            }
        }
        return true; // Ship can be placed
    }

    private void placeShip(int x, int y, int size, boolean horizontal) {
        // Place the ship on the grid
        for (int i = 0; i < size; i++) {
            int dx = x + (horizontal ? i : 0);
            int dy = y + (horizontal ? 0 : i);

            computerGrid[dx][dy] = 'S'; // Mark the ship's position
        }
    }

    private void initializeGridPanel(JPanel gridPanel) {
        gridPanel.setLayout(new GridLayout(numRows, numCols));
        gridButtons = new JButton[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                JButton button = new JButton();
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> handleGridButtonAction(finalI, finalJ));
                gridButtons[i][j] = button;
                gridPanel.add(button);
            }
        }
    }

    private void handleGridButtonAction(int x, int y) {
        if (computerGrid[x][y] == 'S') {
            computerGrid[x][y] = 'H';
            gridButtons[x][y].setBackground(Color.RED);
            gridButtons[x][y].setEnabled(false);
            updateStatusLabel("Hit at " + x + ", " + y);
            computerShips--;

            if (computerShips == 0) {
                gameOver(true);
            }
        } else if (computerGrid[x][y] == ' ') {
            computerGrid[x][y] = 'M';
            gridButtons[x][y].setBackground(Color.BLUE);
            gridButtons[x][y].setEnabled(false);
            updateStatusLabel("Missed at " + x + ", " + y);
        } else {
            updateStatusLabel("Already targeted at " + x + ", " + y);
        }
    }

    private void updateStatusLabel(String text) {
        statusLabel.setText("Status: " + text);
    }

    private void gameOver(boolean playerWon) {
        if (playerWon) {
            JOptionPane.showMessageDialog(this, "Congratulations, you won the game!");
        } else {
            JOptionPane.showMessageDialog(this, "Game Over, you lost.");
        }
        // Disable all buttons or reset the game
    }
    private void initializeMenuPanel(JPanel menuPanel) {
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());

        JButton saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener(e -> saveGame());

        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.addActionListener(e -> loadGame());

        JButton viewGridButton = new JButton("View Grid");
        viewGridButton.addActionListener(e -> viewGrid());

        // Add buttons to menu panel
        menuPanel.add(newGameButton);
        menuPanel.add(saveGameButton);
        menuPanel.add(loadGameButton);
        menuPanel.add(viewGridButton);
    }

    private void initializeStatusPanel(JPanel statusPanel) {
        statusLabel = new JLabel("Status: ");
        statusPanel.add(statusLabel);
    }

    private void startNewGame() {
        // Reset the game state
        resetGameState();

        // Reset the GUI for a new game
        resetGridButtons();
        updateStatusLabel("New game started. Good luck!");

        // Place player and computer ships
        placePlayerShips(); // This method needs to be implemented
        placeComputerShips(); // This method is similar to initializeGameState()
    }
    private void resetGameState() {
        // Reset player and computer grids
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                playerGrid[i][j] = ' ';
                computerGrid[i][j] = ' ';
            }
        }
    }

    private void resetGridButtons() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                gridButtons[i][j].setEnabled(true);
                gridButtons[i][j].setBackground(null);
                gridButtons[i][j].setText(""); // Clear any text if used
            }
        }
    }
    private void placePlayerShips() {
        // Randomly place player's ships
        deployShipOnGrid(playerGrid, 4, 1); // 1 Battleship
        deployShipOnGrid(playerGrid, 3, 2); // 2 Cruisers
        deployShipOnGrid(playerGrid, 2, 3); // 3 Destroyers
        deployShipOnGrid(playerGrid, 1, 3); // 3 Submarines
    }

    private void placeComputerShips() {
        // Randomly place computer's ships
        deployShipOnGrid(computerGrid, 4, 1); // 1 Battleship
        deployShipOnGrid(computerGrid, 3, 2); // 2 Cruisers
        deployShipOnGrid(computerGrid, 2, 3); // 3 Destroyers
        deployShipOnGrid(computerGrid, 1, 3); // 3 Submarines
    }

    private void deployShipOnGrid(char[][] grid, int size, int count) {
        for (int i = 0; i < count; i++) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(numRows);
                int y = random.nextInt(numCols);
                boolean horizontal = random.nextBoolean();

                placed = canPlaceShipOnGrid(grid, x, y, size, horizontal);
                if (placed) {
                    placeShipOnGrid(grid, x, y, size, horizontal);
                }
            }
        }
    }

    private boolean canPlaceShipOnGrid(char[][] grid, int x, int y, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            int dx = x + (horizontal ? i : 0);
            int dy = y + (horizontal ? 0 : i);

            if (dx >= numRows || dy >= numCols || grid[dx][dy] != ' ') {
                return false;
            }
        }
        return true;
    }

    private void placeShipOnGrid(char[][] grid, int x, int y, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            int dx = x + (horizontal ? i : 0);
            int dy = y + (horizontal ? 0 : i);

            grid[dx][dy] = 'S'; // Mark the ship's position
        }
    }

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (PrintWriter out = new PrintWriter(fileToSave)) {
                // Write the state of the game to the file
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        out.print(playerGrid[i][j] + " ");
                    }
                    out.println();
                }

                out.println(); // Separate player grid from computer grid

                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        out.print(computerGrid[i][j] + " ");
                    }
                    out.println();
                }
                out.flush();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error saving the game: " + e.getMessage());
            }
        }
    }
    private void updateGridButtonsFromState() {
        // Assuming the computer's grid is what the player interacts with
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                JButton button = gridButtons[i][j];
                switch (computerGrid[i][j]) {
                    case 'H': // Hit
                        button.setBackground(Color.RED);
                        button.setEnabled(false);
                        break;
                    case 'M': // Miss
                        button.setBackground(Color.BLUE);
                        button.setEnabled(false);
                        break;
                    case 'S': // Ship (unhit)
                    case ' ': // Empty space
                        button.setBackground(null); // Default background
                        button.setEnabled(true);
                        break;
                }
            }
        }
    }
    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file to load");

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();

            try (Scanner scanner = new Scanner(fileToLoad)) {
                for (int i = 0; i < numRows; i++) {
                    if (scanner.hasNextLine()) {
                        String[] row = scanner.nextLine().trim().split(" ");
                        for (int j = 0; j < row.length && j < numCols; j++) {
                            playerGrid[i][j] = row[j].charAt(0);
                        }
                    }
                }

                if (scanner.hasNextLine()) { // Skip the empty line
                    scanner.nextLine();
                }

                for (int i = 0; i < numRows; i++) {
                    if (scanner.hasNextLine()) {
                        String[] row = scanner.nextLine().trim().split(" ");
                        for (int j = 0; j < row.length && j < numCols; j++) {
                            computerGrid[i][j] = row[j].charAt(0);
                        }
                    }
                }

                // Update the GUI based on the loaded game state
                updateGridButtonsFromState();
                updateStatusLabel("Game loaded successfully.");

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error loading the game: " + e.getMessage());
            }
        }
    }

    private void viewGrid() {
        JTextArea gridDisplay = new JTextArea(numRows, numCols);
        gridDisplay.setEditable(false);

        StringBuilder gridBuilder = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                char cell = computerGrid[i][j];
                gridBuilder.append(cell == ' ' ? '.' : cell); // Replace empty space with dot for visibility
                gridBuilder.append(" "); // Add space between columns
            }
            gridBuilder.append("\n"); // New line after each row
        }

        gridDisplay.setText(gridBuilder.toString());

        // Display in a dialog
        JOptionPane.showMessageDialog(this, new JScrollPane(gridDisplay), "Game Grid", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleshipGame());
    }
}
