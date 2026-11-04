import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingControlDemo implements ActionListener {
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

    public SwingControlDemo() {
        prepareGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingControlDemo swingControlDemo = new SwingControlDemo();
            swingControlDemo.showEventDemo();
        });
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Java SWING Examples");
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new BorderLayout(5, 5));

        //menu at top
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
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(selectAll);
        mb.add(file);
        mb.add(edit);
        mb.add(help);
        mainFrame.setJMenuBar(mb);
        //end menu at top


        searchPanel = new JPanel(new BorderLayout(5, 5));
        nameField = new JTextField("ditto");
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchPokemon());
        nameField.addActionListener(e -> searchPokemon());
        searchPanel.add(new JLabel("Pokemon:"), BorderLayout.WEST);
        searchPanel.add(nameField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        mainFrame.add(searchPanel, BorderLayout.NORTH);

        ta = new JTextArea();
        mainFrame.add(new JScrollPane(ta), BorderLayout.CENTER);

        statusLabel = new JLabel("", JLabel.CENTER);
        controlPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        southPanel.add(controlPanel);
        southPanel.add(statusLabel);
        mainFrame.add(southPanel, BorderLayout.SOUTH);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        mainFrame.setVisible(true);
    }

    private void searchPokemon() {

        statusLabel.setText("Search clicked: " + nameField.getText().trim());
    }

    private void showEventDemo() {

        JButton okButton = new JButton("OK");
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        okButton.setActionCommand("OK");
        submitButton.setActionCommand("Submit");
        cancelButton.setActionCommand("Cancel");

        okButton.addActionListener(new ButtonClickListener());
        submitButton.addActionListener(new ButtonClickListener());
        cancelButton.addActionListener(new ButtonClickListener());

        controlPanel.add(okButton);
        controlPanel.add(submitButton);
        controlPanel.add(cancelButton);

        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cut)
            ta.cut();
        if (e.getSource() == paste)
            ta.paste();
        if (e.getSource() == copy)
            ta.copy();
        if (e.getSource() == selectAll)
            ta.selectAll();
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("OK")) {
                statusLabel.setText("Ok Button clicked.");
            } else if (command.equals("Submit")) {
                statusLabel.setText("Submit Button clicked.");
            } else {
                statusLabel.setText("Cancel Button clicked.");
            }
        }
    }
}
