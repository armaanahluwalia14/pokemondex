import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class SwingControlDemo {
    public static void main(String[] args) {
        new SwingControlDemo();
    }

    private JFrame mainFrame;
    private JLabel statusLabel;
    private JLabel imageLabel;
    private JPanel searchPanel;
    private JPanel infoPanel;
    private JPanel controlPanel;
    private JTextField nameField;
    private JButton searchButton;

    // One text area for each piece of info
    private JTextArea nameArea;
    private JTextArea abilityArea;
    private JTextArea typeArea;
    private JTextArea expArea;

    private int currentPokemonId = 1;

    public SwingControlDemo() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Pokedex");
        mainFrame.setSize(700, 400);
        mainFrame.setLayout(new BorderLayout(5, 5));

        // TOP: search bar
        searchPanel = new JPanel(new BorderLayout(5, 5));
        nameField = new JTextField(String.valueOf(currentPokemonId));
        searchButton = new JButton("Search");

        searchButton.addActionListener(e -> searchPokemon());
        nameField.addActionListener(e -> searchPokemon());

        searchPanel.add(new JLabel("Type Name or ID:"), BorderLayout.WEST);
        searchPanel.add(nameField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        mainFrame.add(searchPanel, BorderLayout.NORTH);

        // MIDDLE: picture on the left, info on the right
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        imageLabel = new JLabel("", JLabel.CENTER);
        centerPanel.add(imageLabel);

        // Info panel: 4 rows, each row has a label and a text area
        infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        nameArea = new JTextArea();
        abilityArea = new JTextArea();
        typeArea = new JTextArea();
        expArea = new JTextArea();

        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(nameArea);
        infoPanel.add(new JLabel("Ability:"));
        infoPanel.add(abilityArea);
        infoPanel.add(new JLabel("Type:"));
        infoPanel.add(typeArea);
        infoPanel.add(new JLabel("Base Experience:"));
        infoPanel.add(expArea);

        centerPanel.add(infoPanel);
        mainFrame.add(centerPanel, BorderLayout.CENTER);

        // BOTTOM: Back and Next buttons plus a status message
        statusLabel = new JLabel("Ready", JLabel.CENTER);
        controlPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        JButton nextButton = new JButton("Next");
        JButton backButton = new JButton("Back");

        nextButton.setActionCommand("Next");
        backButton.setActionCommand("Back");

        ButtonClickListener btnListener = new ButtonClickListener();
        nextButton.addActionListener(btnListener);
        backButton.addActionListener(btnListener);

        controlPanel.add(backButton);
        controlPanel.add(nextButton);

        JPanel southPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        southPanel.add(controlPanel);
        southPanel.add(statusLabel);
        mainFrame.add(southPanel, BorderLayout.SOUTH);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        searchPokemon();
        mainFrame.setVisible(true);
    }

    private void searchPokemon() {
        String query = nameField.getText().trim();
        statusLabel.setText("Searching for: " + query);

        // Do nothing if the box is empty
        if (query.equals("")) {
            statusLabel.setText("Type smth");
            return;
        }

        // try/catch so the program keeps running if the Pokemon is not found
        try {
            ReadJson api = new ReadJson();
            PokemonData data = api.fetchPokemon(query);

            // remember which Pokemon we are on so the +1 -1 thing works
            currentPokemonId = (int) data.id;
            displayPokemonData(data);
            statusLabel.setText("Showing Pokemon #" + data.id);
        } catch (Exception ex) {
            statusLabel.setText("Could not find data for query: " + query);
        }
    }

    private void displayPokemonData(PokemonData data) {
        nameArea.setText(data.name);
        abilityArea.setText(data.abilities);
        typeArea.setText(data.types);
        expArea.setText(String.valueOf(data.baseExperience));

        // it shows the image
        try {
            imageLabel.setText("");
            imageLabel.setIcon(new ImageIcon(new URL(data.imageUrl)));
        } catch (Exception ex) {
            imageLabel.setIcon(null);
            imageLabel.setText("No picture");
        }
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Next")) {
                nameField.setText(String.valueOf(currentPokemonId + 1));
                searchPokemon();
            } else if (command.equals("Back")) {
                if (currentPokemonId > 1) {
                    nameField.setText(String.valueOf(currentPokemonId - 1));
                    searchPokemon();
                } else {
                    statusLabel.setText("No Pokemon selected");
                }
            }
        }
    }
}