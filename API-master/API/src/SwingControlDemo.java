import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingControlDemo implements ActionListener {
    public static void main(String[] args) {
        new SwingControlDemo();
    }

    private JFrame mainFrame;
    private JLabel statusLabel;
    private JPanel searchPanel;
    private JPanel controlPanel;
    private JMenuBar mb;
    private JMenu file, edit, help;
    private JMenuItem cut, copy, paste, selectAll;
    private JTextField nameField;
    private JButton searchButton;
    private JTextArea ta;

    private int currentPokemonId = 1;

    public SwingControlDemo() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Java SWING Examples");
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new BorderLayout(5, 5));

        cut = new JMenuItem("cut");
        copy = new JMenuItem("copy");
        paste = new JMenuItem("paste");
        selectAll = new JMenuItem("selectAll");
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);

        mb = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        help = new JMenu("Help");
        edit.add(cut); edit.add(copy); edit.add(paste); edit.add(selectAll);
        mb.add(file); mb.add(edit); mb.add(help);
        mainFrame.setJMenuBar(mb);

        searchPanel = new JPanel(new BorderLayout(5, 5));
        nameField = new JTextField(String.valueOf(currentPokemonId));
        searchButton = new JButton("Search");

        searchButton.addActionListener(e -> searchPokemon());
        nameField.addActionListener(e -> searchPokemon());

        searchPanel.add(new JLabel("Pokemon (Name or ID):"), BorderLayout.WEST);
        searchPanel.add(nameField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        mainFrame.add(searchPanel, BorderLayout.NORTH);

        ta = new JTextArea();
        mainFrame.add(new JScrollPane(ta), BorderLayout.CENTER);

        statusLabel = new JLabel("Ready", JLabel.CENTER);
        controlPanel = new JPanel(new GridLayout(1, 5, 5, 5));

        JButton okButton = new JButton("OK");
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        JButton nextButton = new JButton("Next");
        JButton backButton = new JButton("Back");

        nextButton.setActionCommand("Next");
        backButton.setActionCommand("Back");

        ButtonClickListener btnListener = new ButtonClickListener();
        okButton.addActionListener(btnListener);
        submitButton.addActionListener(btnListener);
        cancelButton.addActionListener(btnListener);
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

        if (query.matches("\\d+")) {
            currentPokemonId = Integer.parseInt(query);
        }

        try {
            ReadJson api = new ReadJson();
            PokemonData data = api.fetchPokemon(query);
            displayPokemonData(data);
        } catch (Exception ex) {
            ta.setText("Could not find data for query: " + query);
        }
    }

    private void displayPokemonData(PokemonData data) {
        String displayAbilities = (data.abilities != null) ? String.join(", ", data.abilities) : "None";

        String text = "Name: " + data.name + "\n"
                + "Height: " + data.height + "\n"
                + "Weight: " + data.weight + "\n"
                + "Abilities: " + displayAbilities;

        ta.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cut) ta.cut();
        if (e.getSource() == paste) ta.paste();
        if (e.getSource() == copy) ta.copy();
        if (e.getSource() == selectAll) ta.selectAll();
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Next")) {
                currentPokemonId++;
                nameField.setText(String.valueOf(currentPokemonId));
                searchPokemon();
            } else if (command.equals("Back")) {
                if (currentPokemonId > 1) {
                    currentPokemonId--;
                    nameField.setText(String.valueOf(currentPokemonId));
                    searchPokemon();
                } else {
                    statusLabel.setText("Already at the first Pokémon!");
                }
            } else {
                statusLabel.setText(command + " Button clicked.");
            }
        }
    }
}
