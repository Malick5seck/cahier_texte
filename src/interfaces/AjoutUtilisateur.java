
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.*;

public class AjoutUtilisateur extends JPanel {
    private JComboBox<String> typeCombo;
    private JTextField prenomField, nomField, loginField;
    private JPasswordField passwordField;

    // Configuration du layout principal

    public AjoutUtilisateur() {

        setLayout(new GridLayout(6, 2, 5, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Police Arial pour l'interface

        Font labelFont = new Font("Arial", Font.BOLD, 20);
        Font fieldFont = new Font("Arial", Font.BOLD, 20);

        // Champ Prénom

        add(createLabel("Prénom :", labelFont));
        prenomField = createTextField();
        prenomField.setFont(fieldFont);
        add(prenomField);

        // Champ Nom
        add(createLabel("Nom :", labelFont));
        nomField = createTextField();
        nomField.setFont(fieldFont);
        add(nomField);

        // Champ Login

        add(createLabel("Login :", labelFont));
        loginField = createTextField();
        loginField.setFont(fieldFont);
        add(loginField);

        // Champ Mot de passe

        add(createLabel("Mot de passe :", labelFont));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(getWidth() , 45));
        passwordField.setFont(fieldFont);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(passwordField);

        // Champ Type d'utilisateur

        add(createLabel("Type d'utilisateur :", labelFont));
        typeCombo = new JComboBox<>(new String[]{"enseignant", "responsable"});
        typeCombo.setFont(fieldFont);
        add(typeCombo);

        // Panel pour les boutons

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Bouton Ajouter

        JButton btnAjouter = createButton("Ajouter", new Color(50, 150, 50));
        btnAjouter.addActionListener(e -> ajouterUtilisateur());
        addHoverEffect(btnAjouter, new Color(40, 130, 40), new Color(50, 150, 50));

        // Bouton Annuler

        JButton btnAnnuler = createButton("Annuler", new Color(200, 50, 50));
        btnAnnuler.addActionListener(e -> resetFields());
        addHoverEffect(btnAnnuler, new Color(180, 40, 40), new Color(200, 50, 50));

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnAnnuler);

        add(new JLabel());
        add(buttonPanel);


        SwingUtilities.invokeLater(() -> prenomField.requestFocusInWindow());
    }

    // effet de survol

    private void addHoverEffect(JButton button, Color hoverColor, Color normalColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    // Méthode creation des  labels

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        return label;
    }

    // Méthode creation des champs texte

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    // Méthode creation des boutons

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(110, 32));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        return button;
    }

    private void ajouterUtilisateur() {
        String prenom = prenomField.getText().trim();
        String nom = nomField.getText().trim();
        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String role = (String) typeCombo.getSelectedItem();

        // Validation des champs
        if (prenom.isEmpty() || nom.isEmpty() || login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }




        // Connexion à la base de données
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "")) {

            // Vérification de l'existence du login
            try (PreparedStatement check = conn.prepareStatement("SELECT id FROM utilisateur WHERE login = ?")) {
                check.setString(1, login);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this,
                            "Ce login est déjà utilisé.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Insertion du nouvel utilisateur
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO utilisateur (prenom, nom, login, `password`, role) VALUES (?, ?, ?, ?, ?)"
            )) {

                pst.setString(1, prenom);
                pst.setString(2, nom);
                pst.setString(3, login);
                pst.setString(4,password);
                pst.setString(5, role);

                int result = pst.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur '" + login + "' ajouté avec succès avec le rôle '" + role + "'.",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    resetFields();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'insertion : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFields() {
        prenomField.setText("");
        nomField.setText("");
        loginField.setText("");
        passwordField.setText("");
        typeCombo.setSelectedIndex(0);
        prenomField.requestFocusInWindow();
    }



}