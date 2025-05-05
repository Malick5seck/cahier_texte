

package interfaces;

import DBO.DBconnect;
import models.Utilisateur;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
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
        setSize(900, 655);
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

        JLabel loginLabel = new JLabel("     Login:");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 12));
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
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
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
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
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

        try (Connection connection = DBconnect.getconnection()) {
            statusLabel.setText("Connexion à la base");

            String sqlUser = "SELECT id, password, role FROM utilisateur WHERE login=?";
            PreparedStatement statement = connection.prepareStatement(sqlUser);
            statement.setString(1, login);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                String storedPassword = resultat.getString("password");
                String role = resultat.getString("role");

                if (storedPassword.equals(password)) {
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

    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}

