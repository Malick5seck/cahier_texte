
package interfaces;

import DBO.DBconnect;
import models.Utilisateur;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, Object> appState;
    private Utilisateur currentUser;

    // Références aux panels
    private VoirSeancesTousEnseignants seancesPanel;
    private AjoutUtilisateur ajoutUtilisateurPanel;
    private AssignerCours assignerCoursPanel;
    private AjoutCoursPanel ajoutCoursPanel;
    private JPanel gestionUtilisateursPanel;
    private DefaultTableModel usersTableModel;
    private JPanel statsPanel;

    public InterfaceChefDepartement() {
//        this.currentUser = user;
        this.appState = new HashMap<>();
        appState.put("currentUser", currentUser);
        initializeUI();
        initComponents();
    }

    private void initializeUI() {
        setTitle("Tableau de bord - Chef de Département");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Navigation
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initContentPanels();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("Espace Chef de Département", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        headerPanel.add(title, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(70, 130, 180));
        navPanel.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(70, 130, 180));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(200, 100));

        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(
                    getClass().getResource("/logo.png")));
            Image scaledImage = originalIcon.getImage().getScaledInstance(140, 130, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(logoPanel);
        navPanel.add(Box.createVerticalStrut(20));

        // Boutons de navigation
        JButton btnDashboard = createNavButton("Tableau de bord", "dashboard");
        JButton btnSeances = createNavButton("Gestion des séances", "seances");
        JButton btnUtilisateurs = createNavButton("Gestion des utilisateurs", "utilisateurs");
        JButton btnCours = createNavButton("Assignation des cours", "assignation");
        JButton btnAjouterCours = createNavButton("Ajouter des Cours", "ajoutcours", new Color(50, 100, 150));
        JButton btnGenererPDF = createNavButton("Générer PDF", "genererpdf", new Color(50, 100, 150));
        JButton btnDeconnexion = createNavButton("Déconnexion", "logout", new Color(220, 80, 60));

        navPanel.add(btnDashboard);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnSeances);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnUtilisateurs);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnCours);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnAjouterCours);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnGenererPDF);
        navPanel.add(Box.createVerticalGlue());
        navPanel.add(btnDeconnexion);

        return navPanel;
    }

    private JButton createNavButton(String text, String panelName) {
        return createNavButton(text, panelName, new Color(50, 100, 150));
    }

    private JButton createNavButton(String text, String panelName, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(230, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            if ("logout".equals(panelName)) {
                confirmAndExit();
            } else {
                showPanel(panelName);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    private void initContentPanels() {
        // Dashboard
        contentPanel.add(createDashboardPanel(), "dashboard");

        // Séances
        seancesPanel = new VoirSeancesTousEnseignants(appState, true);
        contentPanel.add(seancesPanel, "seances");

        // Utilisateurs

        gestionUtilisateursPanel = createGestionUtilisateursPanel();
        contentPanel.add(gestionUtilisateursPanel, "utilisateurs");

        // Assignation cours
        assignerCoursPanel = new AssignerCours(appState);
        contentPanel.add(assignerCoursPanel, "assignation");

        // Ajout cours
        ajoutCoursPanel = new AjoutCoursPanel(assignerCoursPanel);
        contentPanel.add(ajoutCoursPanel, "ajoutcours");

        ajoutUtilisateurPanel = new AjoutUtilisateur();
        contentPanel.add(ajoutUtilisateurPanel,"ajout");

        // Génération PDF
        contentPanel.add(createGenererPDFPanel(), "genererpdf");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        refreshDashboardStats();
        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshDashboardStats() {
        SwingUtilities.invokeLater(() -> {
            statsPanel.removeAll();
            statsPanel.add(createStatCard("Séances cette semaine", getSeancesCount()));
            statsPanel.add(createStatCard("Enseignants", getEnseignantsCount()));
            statsPanel.add(createStatCard("Cours non assignés", getCoursNonAssignesCount()));
            statsPanel.revalidate();
            statsPanel.repaint();
        });
    }

    private String getSeancesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM seance " +
                             "WHERE dateseance BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY) " +
                             "AND statut = 'validee'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "NULL";
        }
    }

    private String getEnseignantsCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateur WHERE role = 'Enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "NULL";
        }
    }

    private String getCoursNonAssignesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM cours " +
                             "WHERE id NOT IN (SELECT cours_id FROM enseignants_cours)")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "NULL";
        }
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);

        return card;
    }

    private JPanel createGestionUtilisateursPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        usersTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usersTableModel.setColumnIdentifiers(new String[]{"ID", "Nom", "Prénom", "Login", "Rôle"});

        JTable usersTable = new JTable(usersTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(usersTable);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton refreshBtn = new JButton("Actualiser");

        styleHoverButton(addBtn, new Color(50, 150, 50));
        styleHoverButton(editBtn, new Color(70, 130, 180));
        styleHoverButton(deleteBtn, new Color(220, 80, 60));
        styleHoverButton(refreshBtn, new Color(100, 100, 100));

        addBtn.addActionListener(e -> showPanel("ajout"));
        editBtn.addActionListener(e -> editSelectedUser(usersTable));
        deleteBtn.addActionListener(e -> deleteSelectedUser(usersTable));
        refreshBtn.addActionListener(e -> loadUsersData());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadUsersData();

        return panel;
    }

    private void loadUsersData() {
        usersTableModel.setRowCount(0);

        try (Connection conn = DBconnect.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id, nom, prenom, login, role FROM utilisateur ORDER BY id")) {

            while (rs.next()) {
                usersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("role"),
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur de chargement des utilisateurs: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedUser(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un utilisateur à modifier",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int userId = (int) usersTableModel.getValueAt(selectedRow, 0);
        String nom = (String) usersTableModel.getValueAt(selectedRow, 1);
        String prenom = (String) usersTableModel.getValueAt(selectedRow, 2);
        String login = (String) usersTableModel.getValueAt(selectedRow, 3);
        String role = (String) usersTableModel.getValueAt(selectedRow, 4);

        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField nomField = new JTextField(nom);
        JTextField prenomField = new JTextField(prenom);
        JTextField loginField = new JTextField(login);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Enseignant", "Responsable"});
        roleCombo.setSelectedItem(role);
        JPasswordField passwordField = new JPasswordField();

        editPanel.add(new JLabel("Nom:"));
        editPanel.add(nomField);
        editPanel.add(new JLabel("Prénom:"));
        editPanel.add(prenomField);
        editPanel.add(new JLabel("Login:"));
        editPanel.add(loginField);
        editPanel.add(new JLabel("Rôle:"));
        editPanel.add(roleCombo);
        editPanel.add(new JLabel("Nouveau mot de passe:"));
        editPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                this, editPanel, "Modifier l'utilisateur",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            updateUserInDatabase(
                    userId,
                    nomField.getText().trim(),
                    prenomField.getText().trim(),
                    loginField.getText().trim(),
                    (String) roleCombo.getSelectedItem(),
                    passwordField.getPassword()
            );
            loadUsersData();
            refreshDashboardStats();
        }
    }

    private void updateUserInDatabase(int userId, String nom, String prenom, String login,
                                      String role, char[] password) {
        String sql = "UPDATE utilisateur SET nom=?, prenom=?, login=?, role=?";
        boolean changePassword = password.length > 0;

        if (changePassword) {
            sql += ", password=?";
        }
        sql += " WHERE id=?";

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, login);
            pstmt.setString(4, role);

            int paramIndex = 5;
            if (changePassword) {
                pstmt.setString(paramIndex++, new String(password));
            }

            pstmt.setInt(paramIndex, userId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Utilisateur mis à jour avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la mise à jour: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedUser(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un utilisateur à supprimer",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int userId = (int) usersTableModel.getValueAt(selectedRow, 0);
        String nomComplet = usersTableModel.getValueAt(selectedRow, 2) + " " +
                usersTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer définitivement l'utilisateur " + nomComplet + "?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBconnect.getconnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "DELETE FROM utilisateur WHERE id = ?")) {

                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur supprimé avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadUsersData();
                    refreshDashboardStats();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createGenererPDFPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Génération de PDF", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnGenererEmploi = new JButton("Générer Emploi du Temps");
        styleHoverButton(btnGenererEmploi, new Color(70, 130, 180));
        btnGenererEmploi.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenererEmploi.addActionListener(e -> genererEmploiDuTempsPDF());

        JButton btnGenererListe = new JButton("Générer Liste Enseignants");
        styleHoverButton(btnGenererListe, new Color(70, 130, 180));
        btnGenererListe.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenererListe.addActionListener(e -> genererListeEnseignantsPDF());

        content.add(Box.createVerticalStrut(50));
        content.add(title);
        content.add(Box.createVerticalStrut(30));
        content.add(btnGenererEmploi);
        content.add(Box.createVerticalStrut(15));
        content.add(btnGenererListe);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void styleHoverButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Arial", Font.BOLD, 14));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void genererEmploiDuTempsPDF() {
        GenererPDF.genererEmploiDuTempsPDF(this);
    }

    private void genererListeEnseignantsPDF() {
        GenererPDF.genererListeEnseignantsPDF(this);
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        switch (panelName) {
            case "dashboard":
                refreshDashboardStats();
                break;
            case "seances":
                seancesPanel.chargerDonnees();
                break;
            case "utilisateurs":
                loadUsersData();
                break;
            case "assignation":
                assignerCoursPanel.rafraichirDonnees();
                break;
        }
    }

    private void confirmAndExit() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir vous déconnecter?",
                "Confirmation de déconnexion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceChefDepartement().setVisible(true);
        });
    }
}