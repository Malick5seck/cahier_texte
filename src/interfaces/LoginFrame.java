/*package interfaces ;
import DBO.DBconnect;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    //private JLabel titrelabel = new JLabel("CAHIER_DE_TEXTE");

    public LoginFrame() {
        setTitle("Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label et champ UserID
     //   titrelabel.setFont(new Font("" , Font.BOLD , 20));
       // add(titrelabel);



        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("login:"), gbc);
        loginField = new JTextField(15);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(loginField, gbc);

        // Label et champ Password

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Boutons

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Se connecter");
        JButton resetButton = new JButton("Reinitialiser");
        Dimension buttonSize = new Dimension(120, 30); // même taille pour les deux
        loginButton.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Label de statut

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.GRAY);
        gbc.gridy = 3;
        add(statusLabel, gbc);

        // Écouteurs

         loginButton.addActionListener(e -> Connecter());
         resetButton.addActionListener(e -> resetChamps());
    }

    // --- Réinitialisation des champs ---

   private void resetChamps() {
        loginField.setText("");
        passwordField.setText("");
        statusLabel.setText("Champs réinitialisés.");
    }



    // --- Validation des champs ---

    private boolean champsValid() {
        if (loginField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Tous les champs sont obligatoires.");
            return false;
        }
        return true;
    }

    // --- Connexion + hachage sécurisé ---

    private void Connecter() {
        if (!champsValid()) return;

        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        //hashPassword
        String hashedPassword = password ;

        try (Connection connection = DBconnect.getconnection()) {
            statusLabel.setText("Connexion à la base...");

            String sqlUser = "SELECT password, role FROM utilisateur WHERE login=?";
            PreparedStatement statement = connection.prepareStatement(sqlUser);
            statement.setString(1, login);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                String storedHashedPassword = resultat.getString("password");
                String role = resultat.getString("role");

                if (storedHashedPassword.equals(hashedPassword)) {
                    // Connexion réussie
                    statusLabel.setForeground(Color.GREEN);
                    statusLabel.setText("Connexion réussie (" + role + ")");
                    dispose();
                    switch (role) {
                        case "Chefdepartement": new InterfaceChefDepartement().setVisible(true); break;
                        case "Enseignant": new InterfaceEnseignant( ).setVisible(true); break;
                        case "Responsable": new InterfaceResponsable().setVisible(true); break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + role);
                    }
                } else {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Mot de passe incorrect.");
                }
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Identifiant inexistant.");
            }

        } catch (SQLException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de connexion SQL.");
            ex.printStackTrace();
        }
    }

    //  Hachage SHA-256

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
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de hachage.");
            return null;
        }
    }
}


 */

/*
package interfaces;
import DBO.DBconnect;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginFrame extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private Image backgroundImage;
    private int enseignantID;

    public LoginFrame() {
        setTitle("CAHIER DE TEXTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); // Taille augmentée pour mieux voir l'image
        setLocationRelativeTo(null);

        // IMAGE POUR LE FOND
//        try {
//            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/img1.jpg")));
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Image de fond non trouvée", "Erreur", JOptionPane.ERROR_MESSAGE);
//        }

        // Création du panel principal avec image de fond
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setBackground(new Color(255, 255, 255, 200)); // Blanc semi-transparent
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel pour le formulaire (pour mieux organiser les composants)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent i.e enlever la couleur beige de defaut qu'a frame

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel titleLabel = new JLabel("CAHIER DE TEXTE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);

        // Champ Login
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Login:"), gbc);

        loginField = new JTextField(20);
        loginField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(loginField, gbc);

        // Champ Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mot de passe:"), gbc);

        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Se connecter");
        styleButton(loginButton, new Color(0, 120, 215), Color.WHITE);
        loginButton.addActionListener(e -> Connecter());

        JButton resetButton = new JButton("Réinitialiser");
        styleButton(resetButton, new Color(200, 200, 200), Color.BLACK);
        resetButton.addActionListener(e -> resetChamps());

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Label de statut
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridy = 4;
        formPanel.add(statusLabel, gbc);

        mainPanel.add(formPanel);

        setContentPane(mainPanel);
    }

    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void resetChamps() {
        loginField.setText("");
        passwordField.setText("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setText("Champs réinitialisés.");
    }

    private boolean champsValid() {
        if (loginField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Tous les champs sont obligatoires.      ");
            return false;
        }
        return true;
    }

    private void Connecter() {
        if (!champsValid()) return;

        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String hashedPassword = hashPassword(password);

        try (Connection connection = DBconnect.getconnection()) {
            statusLabel.setText("Connexion à la base...");

            String sqlUser = "SELECT password, role FROM utilisateur WHERE login=?";
            PreparedStatement statement = connection.prepareStatement(sqlUser);
            statement.setString(1, login);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                String storedHashedPassword = resultat.getString("password");
                String role = resultat.getString("role");

                if (storedHashedPassword.equals(hashedPassword)) {
                    statusLabel.setForeground(new Color(0, 150, 0));
                    statusLabel.setText("Connexion réussie (" + role + ")");
                    dispose();

                    switch (role) {
                        case "Chefdepartement":
                            new InterfaceChefDepartement().setVisible(true);
                            break;
                        case "Enseignant":
                            new InterfaceEnseignant(enseignantID).setVisible(true);
                            break;
                        case "Responsable":
                            new InterfaceResponsable().setVisible(true);
                            break;
                        default:
                            throw new IllegalStateException("Rôle inconnu: " + role);
                    }
                } else {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Mot de passe incorrect.");
                }
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Identifiant inexistant.");
            }

        } catch (SQLException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de connexion à la base de données.");
            ex.printStackTrace();
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
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de hachage");
            return null;
        }
    }
    public static void main(String[] args) {

        new LoginFrame().setVisible(true);

    }

}




 */

/*
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private int enseignantID;

    public LoginFrame() {
        setTitle("CAHIER DE TEXTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        // 1. Panel pour les logos (en haut)
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        try {
            // Logo gauche (150x80 pixels)
            ImageIcon leftLogo = new ImageIcon(getClass().getResource("/logo.png"));
            leftLogo = resizeIcon(leftLogo, 150, 80);
            JLabel leftLogoLabel = new JLabel(leftLogo);
            logoPanel.add(leftLogoLabel, BorderLayout.WEST);

            // Logo droit (150x80 pixels)
            ImageIcon rightLogo = new ImageIcon(getClass().getResource("/SET.jpg"));
            rightLogo = resizeIcon(rightLogo, 150, 80);
            JLabel rightLogoLabel = new JLabel(rightLogo);
            logoPanel.add(rightLogoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Erreur de chargement des logos: " + e.getMessage());
        }

        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // 2. Panel central pour le contenu
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        // Panel pour le formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Texte institutionnel
        JLabel uniLabel = new JLabel("UNIVERSITE IBA DER THIAM DE THIES");
        uniLabel.setFont(new Font("Arial", Font.BOLD, 24));
        uniLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uniLabel.setForeground(new Color(0, 0, 0));

        JLabel ufrLabel = new JLabel("U.F.R Sciences Et Technologies");
        ufrLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        ufrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel deptLabel = new JLabel("Département: Informatique");
        deptLabel.setFont(new Font("Arial", Font.PLAIN, 19));
        deptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel filiereLabel = new JLabel("Filière: Licence Informatique");
        filiereLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        filiereLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Titre principal
        JLabel titleLabel = new JLabel("CAHIER DE TEXTE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 0,0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 0));

        // Ajout des composants textuels
        formPanel.add(uniLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(ufrLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(deptLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(filiereLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(titleLabel);

        // Champ Login
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(new JLabel("Login:"));
        loginField = new JTextField(20);
        loginField.setPreferredSize(new Dimension(200, 30));
        loginPanel.add(loginField);
        formPanel.add(loginPanel);
        formPanel.add(Box.createVerticalStrut(30));

        // Champ Mot de passe
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordPanel.add(passwordField);
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(25));

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = new JButton("Se connecter");
        styleButton(loginButton, new Color(0, 120, 215), Color.WHITE);
        loginButton.addActionListener(e -> Connecter());

        JButton resetButton = new JButton("Réinitialiser");
        styleButton(resetButton, new Color(200, 200, 200), Color.BLACK);
        resetButton.addActionListener(e -> resetChamps());

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Label de statut
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(statusLabel);

        centerPanel.add(formPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        try {
            Image img = icon.getImage();
            Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            return icon;
        }
    }

    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(7, 20, 7, 20)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void resetChamps() {
        loginField.setText("");
        passwordField.setText("");
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText("Champs réinitialisés.");
    }

    private boolean champsValid() {
        if (loginField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Tous les champs sont obligatoires.");
            return false;
        }
        return true;
    }

    private void Connecter() {
        if (!champsValid()) return;

        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String hashedPassword = hashPassword(password);

        try (Connection connection = DBconnect.getconnection()) {
            statusLabel.setText("Connexion à la base...");

            String sqlUser = "SELECT id, password, role FROM utilisateur WHERE login=?";
            PreparedStatement statement = connection.prepareStatement(sqlUser);
            statement.setString(1, login);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                String storedHashedPassword = resultat.getString("password");
                String role = resultat.getString("role");

                if (storedHashedPassword.equals(hashedPassword)) {
                    statusLabel.setForeground(new Color(0, 150, 0));
                    statusLabel.setText("Connexion réussie (" + role + ")");
                    dispose();

                    if (role.equals("Enseignant")) {
                        enseignantID = resultat.getInt("id");
                    }

                    switch (role) {
                        case "Chefdepartement":
                            new InterfaceChefDepartement().setVisible(true);
                            break;
                        case "Enseignant":
                            new InterfaceEnseignant(enseignantID).setVisible(true);
                            break;
                        case "Responsable":
                            new InterfaceResponsable().setVisible(true);
                            break;
                        default:
                            throw new IllegalStateException("Rôle inconnu: " + role);
                    }
                } else {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Mot de passe incorrect.");
                }
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Identifiant inexistant.");
            }
        } catch (SQLException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de connexion à la base de données.");
            ex.printStackTrace();
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
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de hachage");
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}

 */

/*
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private int enseignantID;

    public LoginFrame() {
        setTitle("CAHIER DE TEXTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        // Panel logos
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        try {
            ImageIcon leftLogo = new ImageIcon(getClass().getResource("/logo.png"));
            leftLogo = resizeIcon(leftLogo, 150, 80);
            JLabel leftLogoLabel = new JLabel(leftLogo);
            logoPanel.add(leftLogoLabel, BorderLayout.WEST);

            ImageIcon rightLogo = new ImageIcon(getClass().getResource("/SET.jpg"));
            rightLogo = resizeIcon(rightLogo, 150, 80);
            JLabel rightLogoLabel = new JLabel(rightLogo);
            logoPanel.add(rightLogoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Erreur de chargement des logos: " + e.getMessage());
        }

        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Textes institutionnels
        JLabel uniLabel = new JLabel("UNIVERSITE IBA DER THIAM DE THIES");
        uniLabel.setFont(new Font("Arial", Font.BOLD, 24));
        uniLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uniLabel.setForeground(Color.BLACK);

        JLabel ufrLabel = new JLabel("U.F.R Sciences Et Technologies");
        ufrLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        ufrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel deptLabel = new JLabel("Département: Informatique");
        deptLabel.setFont(new Font("Arial", Font.PLAIN, 19));
        deptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel filiereLabel = new JLabel("Filière: Licence Informatique");
        filiereLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        filiereLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("CAHIER DE TEXTE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 0));

        formPanel.add(uniLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(ufrLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(deptLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(filiereLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(titleLabel);

        // Champs de formulaire avec tailles fixes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        // Login
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);

        JLabel loginLabel = new JLabel("      Login:");
        loginField = new JTextField(20);
        loginField.setPreferredSize(new Dimension(200, 30));
        loginField.setMaximumSize(new Dimension(200, 30));
        loginField.setMinimumSize(new Dimension(200, 30));
        loginField.setHorizontalAlignment(JTextField.LEFT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(loginLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(loginField, gbc);

        formPanel.add(loginPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Mot de passe
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setMinimumSize(new Dimension(200, 30));
        passwordField.setHorizontalAlignment(JTextField.LEFT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        passwordPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordPanel.add(passwordField, gbc);

        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(25));

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = new JButton("Se connecter");
        styleButton(loginButton, new Color(0, 102, 204), Color.WHITE);
        loginButton.addActionListener(e -> Connecter());

        JButton resetButton = new JButton("Réinitialiser");
        styleButton(resetButton, new Color(204, 0, 0), Color.WHITE);
        resetButton.addActionListener(e -> resetChamps());

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Status
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(statusLabel);

        centerPanel.add(formPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        try {
            Image img = icon.getImage();
            Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            return icon;
        }
    }

    private void styleButton(JButton button, Color bgColor, Color textColor) {
        Dimension buttonSize = new Dimension(150, 40);

        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);

        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    private void resetChamps() {
        loginField.setText("");
        passwordField.setText("");
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText("Champs réinitialisés.");
    }

    private boolean champsValid() {
        if (loginField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Tous les champs sont obligatoires.");
            return false;
        }
        return true;
    }

    private void Connecter() {
        if (!champsValid()) return;

        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String hashedPassword = hashPassword(password);

        try (Connection connection = DBconnect.getconnection()) {
            statusLabel.setText("Connexion à la base...");

            String sqlUser = "SELECT id, password, role FROM utilisateur WHERE login=?";
            PreparedStatement statement = connection.prepareStatement(sqlUser);
            statement.setString(1, login);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                String storedHashedPassword = resultat.getString("password");
                String role = resultat.getString("role");

                if (storedHashedPassword.equals(hashedPassword)) {
                    statusLabel.setForeground(new Color(0, 150, 0));
                    statusLabel.setText("Connexion réussie (" + role + ")");
                    dispose();

                    if (role.equals("Enseignant")) {
                        enseignantID = resultat.getInt("id");
                    }

                    switch (role) {
                        case "Chefdepartement":
                            new InterfaceChefDepartement().setVisible(true);
                            break;
                        case "Enseignant":
                            new InterfaceEnseignant(enseignantID).setVisible(true);
                            break;
                        case "Responsable":
                            new InterfaceResponsable().setVisible(true);
                            break;
                        default:
                            throw new IllegalStateException("Rôle inconnu: " + role);
                    }
                } else {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Mot de passe incorrect.");
                }
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Identifiant inexistant.");
            }
        } catch (SQLException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de connexion à la base de données.");
            ex.printStackTrace();
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
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de hachage");
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}


 */
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private int enseignantID;

    public LoginFrame() {
        setTitle("CAHIER DE TEXTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        // Panel logos
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        try {
            ImageIcon leftLogo = new ImageIcon(getClass().getResource("/logo.png"));
            leftLogo = resizeIcon(leftLogo, 150, 100);
            JLabel leftLogoLabel = new JLabel(leftLogo);
            logoPanel.add(leftLogoLabel, BorderLayout.WEST);

            ImageIcon rightLogo = new ImageIcon(getClass().getResource("/SET.jpg"));
            rightLogo = resizeIcon(rightLogo, 150, 100);
            JLabel rightLogoLabel = new JLabel(rightLogo);
            logoPanel.add(rightLogoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Erreur de chargement des logos: " + e.getMessage());
        }

        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Textes institutionnels
        JLabel uniLabel = new JLabel("UNIVERSITE IBA DER THIAM DE THIES");
        uniLabel.setFont(new Font("Arial", Font.BOLD, 24));
        uniLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uniLabel.setForeground(Color.BLACK);

        JLabel ufrLabel = new JLabel("U.F.R Sciences Et Technologies");
        ufrLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        ufrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel deptLabel = new JLabel("Département: Informatique");
        deptLabel.setFont(new Font("Arial", Font.PLAIN, 19));
        deptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel filiereLabel = new JLabel("Filière: Licence Informatique");
        filiereLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        filiereLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("CAHIER DE TEXTE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 0));

        formPanel.add(uniLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(ufrLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(deptLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(filiereLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(titleLabel);

        // Champs de formulaire avec tailles fixes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        // Login
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);

        JLabel loginLabel = new JLabel("   Login:");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 12)); // taille augmentée
        loginField = new JTextField(20);
        loginField.setPreferredSize(new Dimension(200, 30));
        loginField.setMaximumSize(new Dimension(200, 30));
        loginField.setMinimumSize(new Dimension(200, 30));
        loginField.setHorizontalAlignment(JTextField.LEFT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(loginLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(loginField, gbc);

        formPanel.add(loginPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Mot de passe
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12)); // taille augmentée
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setMinimumSize(new Dimension(200, 30));
        passwordField.setHorizontalAlignment(JTextField.LEFT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        passwordPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordPanel.add(passwordField, gbc);

        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(25));

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14)); // taille augmentée
        styleButton(loginButton, new Color(0, 102, 204), Color.WHITE);
        loginButton.addActionListener(e -> Connecter());

        JButton resetButton = new JButton("Réinitialiser");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        styleButton(resetButton, new Color(204, 0, 0), Color.WHITE);
        resetButton.addActionListener(e -> resetChamps());

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Status
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(statusLabel);

        centerPanel.add(formPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        try {
            Image img = icon.getImage();
            Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            return icon;
        }
    }

    private void styleButton(JButton button, Color bgColor, Color textColor) {
        Dimension buttonSize = new Dimension(150, 40);

        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);

        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

       // button.setFont(new Font("Arial", Font.BOLD, 14));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void resetChamps() {
        loginField.setText("");
        passwordField.setText("");
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText("Champs réinitialisés.");
    }

    private boolean champsValid() {
        if (loginField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Tous les champs sont obligatoires.");
            return false;
        }
        return true;
    }

    private void Connecter() {
        if (!champsValid()) return;

        String login = loginField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String hashedPassword = hashPassword(password);

        try (Connection connection = DBconnect.getconnection()) {
            statusLabel.setText("Connexion à la base");

            String sqlUser = "SELECT id, password, role FROM utilisateur WHERE login=?";
            PreparedStatement statement = connection.prepareStatement(sqlUser);
            statement.setString(1, login);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                String storedHashedPassword = resultat.getString("password");
                String role = resultat.getString("role");

                if (storedHashedPassword.equals(hashedPassword)) {
                    statusLabel.setForeground(new Color(0, 150, 0));
                    statusLabel.setText("Connexion réussie (" + role + ")");
                    dispose();

                    if (role.equals("Enseignant")) {
                        enseignantID = resultat.getInt("id");
                    }

                    switch (role) {
                        case "Chefdepartement":
                            new InterfaceChefDepartement().setVisible(true);
                            break;
                        case "Enseignant":
                            new InterfaceEnseignant(enseignantID).setVisible(true);
                            break;
                        case "Responsable":
                            new InterfaceResponsable().setVisible(true);
                            break;
                        default:
                            throw new IllegalStateException("Rôle inconnu: " + role);
                    }
                } else {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Mot de passe incorrect.");
                }
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Identifiant inexistant.");
            }
        } catch (SQLException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de connexion à la base de données.");
            ex.printStackTrace();
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
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Erreur de hachage");
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}

