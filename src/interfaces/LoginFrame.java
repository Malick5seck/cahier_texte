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


package interfaces;
import DBO.DBconnect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

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
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/img1.jpg")));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image de fond non trouvée", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

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

        // Ajout du formulaire au panel principal
        mainPanel.add(formPanel);

        // Configuration finale du JFrame
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



