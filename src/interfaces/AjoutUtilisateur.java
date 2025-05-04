/*package interfaces;

import DBO.DBconnect;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AjoutUtilisateur extends JFrame {

    private JTextField loginField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton btnAjouter;

    public AjoutUtilisateur() {
        setTitle("Ajouter un utilisateur");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Login :"));
        loginField = new JTextField();
        add(loginField);

        add(new JLabel("Mot de passe :"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Rôle :"));
        roleCombo = new JComboBox<>(new String[]{"enseignant", "responsable"});
        add(roleCombo);

        btnAjouter = new JButton("Ajouter");
        add(btnAjouter);

        JButton btnAnnuler = new JButton("Annuler");
        add(btnAnnuler);

        btnAjouter.addActionListener(e -> ajouterUtilisateur());
        btnAnnuler.addActionListener(e -> dispose());
    }

    private void ajouterUtilisateur() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleCombo.getSelectedItem();

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        try (Connection conn = DBconnect.getconnection()) {
            String sql = "INSERT INTO utilisateurs (login, mot_de_passe, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, password); // Non hashé ici
            ps.setString(3, role);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès !");
                loginField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'ajout.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

 */

/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Map;

public class AjoutUtilisateur extends JFrame {

    private JComboBox<String> typeCombo;
    private JTextField prenomField, nomField, loginField;
    private JPasswordField passwordField;

    public AjoutUtilisateur(Map<String, Object> appState) {
        setTitle("Ajouter un utilisateur");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));
        setResizable(false);

        // Champs

        add(new JLabel("Nom :"));
        nomField = new JTextField();
        add(nomField);

        add(new JLabel("Login :"));
        loginField = new JTextField();
        add(loginField);

        add(new JLabel("Mot de passe :"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Type d'utilisateur :"));
        typeCombo = new JComboBox<>(new String[]{"enseignant", "responsable"});
        add(typeCombo);

        JButton btnAjouter = new JButton("Ajouter");
        add(btnAjouter);

        JButton btnAnnuler = new JButton("Annuler");
        add(btnAnnuler);

        // Action bouton Ajouter
        btnAjouter.addActionListener(e -> ajouterUtilisateur());

        // Action bouton Annuler
        btnAnnuler.addActionListener(e -> dispose());
    }

    private void ajouterUtilisateur() {
        String prenom = prenomField.getText().trim();
        String nom = nomField.getText().trim();
        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String role = (String) typeCombo.getSelectedItem();

        if (prenom.isEmpty() || nom.isEmpty() || login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        String hashedPassword = hashPassword(password);

        String sql = "SELECT * FROM enseignants_cours WHERE Enseignant_id = ? AND cours_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(2, nom);
            pst.setString(3, login);
            pst.setString(4, hashedPassword);
            pst.setString(5, role);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'ajout.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage", e);
        }
    }
}

 */

/*
package interfaces;


import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class AjoutUtilisateur extends JPanel {
    private JComboBox<String> typeCombo;
    private JTextField prenomField, nomField, loginField;
    private JPasswordField passwordField;

    public AjoutUtilisateur() {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Champs
        add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        add(prenomField);

        add(new JLabel("Nom :"));
        nomField = new JTextField();
        add(nomField);

        add(new JLabel("Login :"));
        loginField = new JTextField();
        add(loginField);

        add(new JLabel("Mot de passe :"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Type d'utilisateur :"));
        typeCombo = new JComboBox<>(new String[]{"enseignant", "responsable"});
        add(typeCombo);

        JButton btnAjouter = new JButton("Ajouter");
        add(btnAjouter);

        JButton btnAnnuler = new JButton("Annuler");
        add(btnAnnuler);

        // Style des boutons
        btnAjouter.setBackground(new Color(50, 150, 50));
        btnAjouter.setForeground(Color.WHITE);
        btnAnnuler.setBackground(new Color(200, 50, 50));
        btnAnnuler.setForeground(Color.WHITE);

        // Action bouton Ajouter
        btnAjouter.addActionListener(e -> ajouterUtilisateur());

        // Action bouton Annuler
        btnAnnuler.addActionListener(e -> {
            Container parent = getParent();
            if (parent != null) {
                CardLayout layout = (CardLayout) parent.getLayout();
                layout.show(parent, "default");
            }
        });
    }

    private void ajouterUtilisateur() {
        String prenom = prenomField.getText().trim();
        String nom = nomField.getText().trim();
        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String role = (String) typeCombo.getSelectedItem();

        if (prenom.isEmpty() || nom.isEmpty() || login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedPassword = hashPassword(password);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "")) {

            // Vérifie si le login existe déjà
            try (PreparedStatement check = conn.prepareStatement("SELECT id FROM utilisateurs WHERE login = ?")) {
                check.setString(1, login);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Ce login est déjà utilisé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Insertion
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO utilisateurs (prenom, nom, login, password, role) VALUES (?, ?, ?, ?, ?)")) {

                pst.setString(1, prenom);
                pst.setString(2, nom);
                pst.setString(3, login);
                pst.setString(4, hashedPassword);
                pst.setString(5, role);

                int result = pst.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur '" + login + "' ajouté avec succès avec le rôle '" + role + "'.",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    resetFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de l'ajout.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'insertion : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFields() {
        prenomField.setText("");
        nomField.setText("");
        loginField.setText("");
        passwordField.setText("");
        typeCombo.setSelectedIndex(0);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage", e);
        }
    }
}

 */
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
    private String motdepasse;

    public AjoutUtilisateur() {
        // Configuration du layout principal
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