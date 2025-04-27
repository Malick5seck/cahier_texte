/*package interfaces;

import javax.swing.*;
import java.awt.*;

public class InterfaceChefDepartement extends JFrame {
    public InterfaceChefDepartement() {


        setTitle("Interface Chef de Département");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("Tableau de bord - Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        JPanel panelBoutons = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnVoirSeances = new JButton("Voir toutes les séances");
        JButton btnAjoutUtilisateur = new JButton("Ajouter un enseignant ou responsable");
        JButton btnAssignerCours = new JButton("Assigner un cours à un enseignant");

        panelBoutons.add(btnVoirSeances);
        panelBoutons.add(btnAjoutUtilisateur);
        panelBoutons.add(btnAssignerCours);
// button deconnection
        add(panelBoutons, BorderLayout.CENTER);
        JButton btnDeconnexion = new JButton("Deconnexion");
        btnDeconnexion.setForeground(Color.RED);
        btnDeconnexion.setFont(new Font("Arial",Font.PLAIN,14));
        add(btnDeconnexion,BorderLayout.SOUTH);

        // Listeners à ajouter ici plus tard pour afficher les écrans secondaires

        btnVoirSeances.addActionListener(e -> {
            // À implémenter : ouvrir une interface pour voir toutes les séances
            new VoirSeancesTousEnseignants().setVisible(true);
            new AjoutUtilisateur().setVisible(true);
            new AssignerCours().setVisible(true);
            JOptionPane.showMessageDialog(this, "Affichage des séances à venir.");
        });

        btnAjoutUtilisateur.addActionListener(e -> {
         new AjoutUtilisateur().setVisible(true);
        });
        btnDeconnexion.addActionListener(e -> {
           dispose();
            new LoginFrame().setVisible(true);
        });


        btnAssignerCours.addActionListener(e -> {
           new AssignerCours().setVisible(true);
        });

       // this.pack();
    }
    public static void main(String[] args) {
      new InterfaceChefDepartement().setVisible(true);
    }




}

 */

/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public InterfaceChefDepartement() {
        setTitle("Tableau de bord - Chef de Département");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Titre en haut
        JLabel titre = new JLabel("Tableau de bord - Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Panel des boutons à droite
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBoutons.setBackground(new Color(70, 130, 180));

        // Création des boutons avec style
        JButton btnVoirSeances = createStyledButton("Voir toutes les séances");
        JButton btnAjoutUtilisateur = createStyledButton("Ajouter un enseignant ou responsable");
        JButton btnAssignerCours = createStyledButton("Assigner un cours à un enseignant");
        JButton btnDeconnexion = createStyledButton("Déconnexion", new Color(220, 80, 60));

        // Ajout des boutons avec espacement POUR AMELIORER LA VISIBILITE
        panelBoutons.add(btnVoirSeances);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 15)));
        panelBoutons.add(btnAjoutUtilisateur);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 15)));
        panelBoutons.add(btnAssignerCours);
        panelBoutons.add(Box.createVerticalGlue());
        panelBoutons.add(btnDeconnexion);

        mainPanel.add(panelBoutons, BorderLayout.EAST);

        // Panel de contenu au centre
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Ajout des différents panels
        contentPanel.add(createDefaultPanel(), "default");
        contentPanel.add(new VoirSeancesTousEnseignants(), "seances");
        contentPanel.add(new AjoutUtilisateur(), "ajout");
        contentPanel.add(new AssignerCours(), "assignation");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Gestion des événements
        btnVoirSeances.addActionListener(e -> cardLayout.show(contentPanel, "seances"));
        btnAjoutUtilisateur.addActionListener(e -> cardLayout.show(contentPanel, "ajout user"));
        btnAssignerCours.addActionListener(e -> cardLayout.show(contentPanel, "assignation"));
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(50, 100, 150));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JPanel createDefaultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            new InterfaceChefDepartement().setVisible(true);
        });
    }

}

 */
/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public InterfaceChefDepartement() {
        setTitle("Tableau de bord - Chef de Département");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel titre = new JLabel("Espace Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titre, BorderLayout.NORTH);

        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBoutons.setBackground(new Color(70, 130, 180));

        JButton btnVoirSeances = createStyledButton("Voir toutes les séances");
        JButton btnAjoutUtilisateur = createStyledButton("Ajouter un enseignant ou responsable");
        JButton btnAssignerCours = createStyledButton("Assigner un cours à un enseignant");
        JButton btnDeconnexion = createStyledButton("Déconnexion", new Color(220, 80, 60));

        panelBoutons.add(btnVoirSeances);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBoutons.add(btnAjoutUtilisateur);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBoutons.add(btnAssignerCours);
        panelBoutons.add(Box.createVerticalGlue());
        panelBoutons.add(btnDeconnexion);

        mainPanel.add(panelBoutons, BorderLayout.EAST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(createDefaultPanel(), "default");
        contentPanel.add(new VoirSeancesTousEnseignantsPanel(), "seances");
        contentPanel.add(new AjoutUtilisateur(), "ajout");
        contentPanel.add(new AssignerCours(), "assignation");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        btnVoirSeances.addActionListener(e -> cardLayout.show(contentPanel, "seances"));
        btnAjoutUtilisateur.addActionListener(e -> cardLayout.show(contentPanel, "ajout"));
        btnAssignerCours.addActionListener(e -> cardLayout.show(contentPanel, "assignation"));
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(50, 100, 150));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JPanel createDefaultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }
    class VoirSeancesTousEnseignantsPanel extends JPanel {
        public VoirSeancesTousEnseignantsPanel() {
            setLayout(new BorderLayout());
            VoirSeancesTousEnseignants voirSeances = new VoirSeancesTousEnseignants();
            voirSeances.setVisible(false); // éviter d'afficher la JFrame
            this.add(voirSeances.getRootPane(), BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceChefDepartement frame = new InterfaceChefDepartement();
            frame.setVisible(true);
        });
    }
}





 */


/*
package interfaces;

import DBO.DBconnect;
import models.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, Object> appState;
    private Utilisateur currentUser;
    private JLabel statusLabel;

    // Références aux panels
    private VoirSeancesTousEnseignants seancesPanel;
    private AjoutUtilisateur ajoutUtilisateurPanel;
    private AssignerCours assignerCoursPanel;
    private JPanel gestionUtilisateursPanel;
    private DefaultTableModel usersTableModel;

    public InterfaceChefDepartement(Utilisateur user) {
        this.currentUser = user;
        this.appState = new HashMap<>();
        appState.put("currentUser", currentUser);

        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    public InterfaceChefDepartement() {
        // Constructeur par défaut
    }

    private void initializeUI() {
        setTitle("Tableau de bord - Chef de Département");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void initComponents() {
        // Main panel structure
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Navigation panel
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initContentPanels();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Status bar
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel titre = new JLabel("Espace Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        headerPanel.add(titre, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("Connecté: " + currentUser.getNomComplet(), SwingConstants.RIGHT);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setBackground(new Color(70, 130, 180));

        // Boutons de navigation
        JButton btnDashboard = createNavButton("Tableau de bord", "default");
        JButton btnSeances = createNavButton("Gestion des séances", "seances");
        JButton btnUtilisateurs = createNavButton("Gestion des utilisateurs", "gestionUtilisateurs");
        JButton btnCours = createNavButton("Assignation des cours", "assignation");
        JButton btnGenererPDF = createNavButton("Générer PDF", "genererPDF",new Color(50, 100, 150));
        JButton btnDeconnexion = createNavButton("Déconnexion", "logout", new Color(220, 80, 60));

        // Ajout des boutons au panel
        navPanel.add(btnDashboard);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnSeances);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnUtilisateurs);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnCours);
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
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            if ("logout".equals(panelName)) {
               confirmAndExit();
            } else if ("genererPDF".equals(panelName)) {
                genererPDF();
            } else {
                showPanel(panelName);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void initContentPanels() {
        // Panel Dashboard
        contentPanel.add(createDashboardPanel(), "default");

        // Panel Séances
        seancesPanel = new VoirSeancesTousEnseignants(appState);
        contentPanel.add(seancesPanel, "seances");

        // Panel Ajout Utilisateur
        ajoutUtilisateurPanel = new AjoutUtilisateur();
        contentPanel.add(ajoutUtilisateurPanel, "ajout");

        // Panel Assignation Cours
        assignerCoursPanel = new AssignerCours(appState);
        contentPanel.add(assignerCoursPanel, "assignation");

        // Panel Gestion Utilisateurs
        gestionUtilisateursPanel = createGestionUtilisateursPanel();
        contentPanel.add(gestionUtilisateursPanel, "gestionUtilisateurs");

        // Panel Génération PDF
        contentPanel.add(createGenererPDFPanel(), "genererPDF");
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
        btnGenererEmploi.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenererEmploi.addActionListener(e -> genererEmploiDuTempsPDF());

        JButton btnGenererListe = new JButton("Générer Liste Enseignants");
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

    private void genererPDF() {
        cardLayout.show(contentPanel, "genererPDF");
        updateStatus("Prêt à générer des PDF");
    }

    private void genererEmploiDuTempsPDF() {
        // Implémentation réelle à ajouter ici
        JOptionPane.showMessageDialog(this,
                "Génération de l'emploi du temps PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void genererListeEnseignantsPDF() {
        // Implémentation réelle à ajouter ici
        JOptionPane.showMessageDialog(this,
                "Génération de la liste des enseignants PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        // Statistiques
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statsPanel.add(createStatCard("Séances cette semaine", getSeancesCount()));
        statsPanel.add(createStatCard("Enseignants", getEnseignantsCount()));
        statsPanel.add(createStatCard("Cours assignés", getCoursAssignesCount()));

        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String getSeancesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM seance WHERE dateseance BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "null";
        }
    }

    private String getEnseignantsCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateurs WHERE role = 'enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "null";
        }
    }

    private String getCoursAssignesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM enseignants_cours")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "null";
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

        // Modèle de tableau
        usersTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Seule la colonne Actions est éditable
            }
        };
        usersTableModel.setColumnIdentifiers(new String[]{"ID", "Nom", "Prénom", "Login", "Rôle", "Actions"});

        // Tableau
        JTable usersTable = new JTable(usersTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(usersTable);

        // Panel boutons
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton refreshBtn = new JButton("Actualiser");

        // Styles boutons
        addBtn.setBackground(new Color(50, 150, 50));
        editBtn.setBackground(new Color(70, 130, 180));
        deleteBtn.setBackground(new Color(220, 80, 60));
        refreshBtn.setBackground(new Color(100, 100, 100));

        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, refreshBtn}) {
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        // Actions boutons
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

        // Chargement initial
        loadUsersData();

        return panel;
    }

    private void loadUsersData() {
        usersTableModel.setRowCount(0); // Vider le tableau

        try (Connection conn = DBconnect.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id, nom, prenom, login, role FROM utilisateurs ORDER BY nom, prenom")) {

            while (rs.next()) {
                usersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("role"),
                        "Actions"
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

        // Formulaire d'édition
        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField nomField = new JTextField(nom);
        JTextField prenomField = new JTextField(prenom);
        JTextField loginField = new JTextField(login);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"enseignant", "responsable", "chef"});
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
        editPanel.add(new JLabel("Nouveau mot de passe (laisser vide pour ne pas changer):"));
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
            loadUsersData(); // Rafraîchir les données
        }
    }

    private void updateUserInDatabase(int userId, String nom, String prenom, String login,
                                      String role, char[] password) {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, login=?, role=?";
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

            if (changePassword) {
                pstmt.setString(5, hashPassword(new String(password)));
                pstmt.setInt(6, userId);
            } else {
                pstmt.setInt(5, userId);
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Utilisateur mis à jour avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(this,
                        "Ce login est déjà utilisé par un autre utilisateur",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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
                         "DELETE FROM utilisateurs WHERE id = ?")) {

                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur supprimé avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadUsersData(); // Rafraîchir les données
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setBackground(new Color(240, 240, 240));

        statusLabel = new JLabel("Prêt");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);

        return statusPanel;
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
             //   confirmAndExit();
            }
        });
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        updateStatus("Affichage: " + getPanelTitle(panelName));

        // Rafraîchir les données si nécessaire
        if ("seances".equals(panelName)) {
            seancesPanel.chargerDonnees();
        } else if ("gestionUtilisateurs".equals(panelName)) {
            loadUsersData();
        } else if ("assignation".equals(panelName)) {
            assignerCoursPanel.rafraichirDonnees();
        }
    }

    private String getPanelTitle(String panelName) {
        switch (panelName) {
            case "default": return "Tableau de bord";
            case "seances": return "Gestion des séances";
            case "ajout": return "Ajout d'utilisateur";
            case "gestionUtilisateurs": return "Gestion des utilisateurs";
            case "assignation": return "Assignation des cours";
            case "genererPDF": return "Génération de PDF";
            default: return "NULL";
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Pour tester l'interface sans connexion réelle
            Utilisateur testUser = new Utilisateur();
            testUser.setNomComplet("Chef Département ");
            new InterfaceChefDepartement(testUser).setVisible(true);
        });
    }
}



 */


/*
package interfaces;

import DBO.DBconnect;
import models.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, Object> appState;
    private Utilisateur currentUser;
    private JLabel statusLabel;
    private VoirSeancesTousEnseignants seancesPanel;

    public InterfaceChefDepartement(Utilisateur user) {
        this.currentUser = user;
        this.appState = new HashMap<>();
        appState.put("currentUser", currentUser);

        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    public InterfaceChefDepartement() {
        
    }

    private void initializeUI() {
        setTitle("Tableau de bord - Chef de Département");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void initComponents() {
        // Main panel structure
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Navigation panel
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initContentPanels();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Status bar
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel titre = new JLabel("Espace Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        headerPanel.add(titre, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("Connecté: " + currentUser.getNomComplet(), SwingConstants.RIGHT);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setBackground(new Color(70, 130, 180));

        JButton btnDashboard = createNavButton("Tableau de bord", "default");
        JButton btnSeances = createNavButton("Gestion des séances", "seances");
        JButton btnUtilisateurs = createNavButton("Gestion des utilisateurs", "gestionUtilisateurs");
        JButton btnCours = createNavButton("Assignation des cours", "assignation");
        JButton btnDeconnexion = createNavButton("Déconnexion", "logout", new Color(220, 80, 60));

        navPanel.add(btnDashboard);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnSeances);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnUtilisateurs);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnCours);
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
        button.setMaximumSize(new Dimension(250, 45));
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
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void initContentPanels() {
        // Panel Dashboard
        contentPanel.add(createDashboardPanel(), "default");

        // Panel Séances (version sans validation)
        seancesPanel = new VoirSeancesTousEnseignants(appState, false);
        contentPanel.add(seancesPanel, "seances");

        // Panel Gestion Utilisateurs
        contentPanel.add(createGestionUtilisateursPanel(), "gestionUtilisateurs");
        

        // Panel Assignation Cours
        contentPanel.add(new AssignerCours(appState), "assignation");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        // Statistiques
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statsPanel.add(createStatCard("Séances cette semaine", getSeancesCount()));
        statsPanel.add(createStatCard("Enseignants", getEnseignantsCount()));
        statsPanel.add(createStatCard("Cours assignés", getCoursAssignesCount()));

        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String getSeancesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM seance WHERE dateseance BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (Exception e) {
            return "Err";
        }
    }

    private String getEnseignantsCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateurs WHERE role = 'enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (Exception e) {
            return "Err";
        }
    }

    private String getCoursAssignesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM enseignants_cours")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (Exception e) {
            return "Err";
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

        // Modèle de tableau
        DefaultTableModel usersTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Seule la colonne Actions est éditable
            }
        };
        usersTableModel.setColumnIdentifiers(new String[]{"ID", "Nom", "Prénom", "Login", "Rôle", "Actions"});

        // Tableau
        JTable usersTable = new JTable(usersTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(usersTable);

        // Panel boutons
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton refreshBtn = new JButton("Actualiser");

        // Styles boutons
        addBtn.setBackground(new Color(50, 150, 50));
        editBtn.setBackground(new Color(70, 130, 180));
        deleteBtn.setBackground(new Color(220, 80, 60));
        refreshBtn.setBackground(new Color(100, 100, 100));

        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, refreshBtn}) {
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        // Actions boutons
        addBtn.addActionListener(e -> showPanel("ajout"));
        editBtn.addActionListener(e -> editSelectedUser(usersTable));
        deleteBtn.addActionListener(e -> deleteSelectedUser(usersTable));
        refreshBtn.addActionListener(e -> setState());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Chargement initial
        setState();

        return panel;
    }

    private void deleteSelectedUser(JTable usersTable) {
    }

    private void editSelectedUser(JTable usersTable) {
    }

    private void setState() {
    }


    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setBackground(new Color(240, 240, 240));

        statusLabel = new JLabel("Prêt");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);

        return statusPanel;
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Fermeture directe par la croix
            }
        });
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        if ("seances".equals(panelName)) {
            seancesPanel.chargerDonnees();
        }
    }

    private void confirmAndExit() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            dispose();
            // new LoginFrame().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Utilisateur testUser = new Utilisateur();
            testUser.setNomComplet("Chef Département Test");
            testUser.setRole("chef");
            new InterfaceChefDepartement(testUser).setVisible(true);
        });
    }
}

 */


//code correcte
/*
package interfaces;

import DBO.DBconnect;
import models.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, Object> appState;
    private Utilisateur currentUser;
    private JLabel statusLabel;

    // Références aux panels
    private VoirSeancesTousEnseignants seancesPanel;
    private AjoutUtilisateur ajoutUtilisateurPanel;
    private AssignerCours assignerCoursPanel;
    private JPanel gestionUtilisateursPanel;
    private DefaultTableModel usersTableModel;

    public InterfaceChefDepartement(Utilisateur user) {
        this.currentUser = user;
        this.appState = new HashMap<>();
        appState.put("currentUser", currentUser);

        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    public InterfaceChefDepartement() {
        // Constructeur par défaut
    }

    private void initializeUI() {
        setTitle("Tableau de bord - Chef de Département");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void initComponents() {
        // Main panel structure
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Navigation panel
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initContentPanels();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Status bar
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel titre = new JLabel("Espace Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        headerPanel.add(titre, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("Connecté: " + currentUser.getNomComplet(), SwingConstants.RIGHT);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setBackground(new Color(70, 130, 180));

        // Boutons de navigation
        JButton btnDashboard = createNavButton("Tableau de bord", "default");
        JButton btnSeances = createNavButton("Gestion des séances", "seances");
        JButton btnUtilisateurs = createNavButton("Gestion des utilisateurs", "gestionUtilisateurs");
        JButton btnCours = createNavButton("Assignation des cours", "assignation");
        JButton btnGenererPDF = createNavButton("Générer PDF", "genererPDF", new Color(100, 70, 150));
        JButton btnDeconnexion = createNavButton("Déconnexion", "logout", new Color(220, 80, 60));

        // Ajout des boutons au panel
        navPanel.add(btnDashboard);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnSeances);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnUtilisateurs);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnCours);
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
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            if ("logout".equals(panelName)) {
                confirmAndExit();
            } else if ("genererPDF".equals(panelName)) {
                genererPDF();
            } else {
                showPanel(panelName);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void initContentPanels() {
        seancesPanel = new VoirSeancesTousEnseignants(appState ,true) ;
        // Panel Dashboard
        contentPanel.add(createDashboardPanel(), "default");

        // Panel Séances
        seancesPanel = new VoirSeancesTousEnseignants(appState, true);
        contentPanel.add(seancesPanel, "seances");

        // Panel Ajout Utilisateur
        ajoutUtilisateurPanel = new AjoutUtilisateur();
        contentPanel.add(ajoutUtilisateurPanel, "ajout");

        // Panel Assignation Cours
        assignerCoursPanel = new AssignerCours(appState);
        contentPanel.add(assignerCoursPanel, "assignation");

        // Panel Gestion Utilisateurs
        gestionUtilisateursPanel = createGestionUtilisateursPanel();
        contentPanel.add(gestionUtilisateursPanel, "gestionUtilisateurs");

        // Panel Génération PDF
        contentPanel.add(createGenererPDFPanel(), "genererPDF");
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
        btnGenererEmploi.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenererEmploi.addActionListener(e -> genererEmploiDuTempsPDF());

        JButton btnGenererListe = new JButton("Générer Liste Enseignants");
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

    private void genererPDF() {
        cardLayout.show(contentPanel, "genererPDF");
        updateStatus("Prêt à générer des PDF");
    }

    private void genererEmploiDuTempsPDF() {
        // Implémentation réelle à ajouter ici
        JOptionPane.showMessageDialog(this,
                "Génération de l'emploi du temps PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void genererListeEnseignantsPDF() {
        // Implémentation réelle à ajouter ici
        JOptionPane.showMessageDialog(this,
                "Génération de la liste des enseignants PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        // Statistiques
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statsPanel.add(createStatCard("Séances cette semaine", getSeancesCount()));
        statsPanel.add(createStatCard("Enseignants", getEnseignantsCount()));
        statsPanel.add(createStatCard("Cours assignés", getCoursAssignesCount()));

        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String getSeancesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM seance WHERE dateseance BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "Err";
        }
    }

    private String getEnseignantsCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateurs WHERE role = 'enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "Err";
        }
    }

    private String getCoursAssignesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM enseignants_cours")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "Err";
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

        // Modèle de tableau
        usersTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Seule la colonne Actions est éditable
            }
        };
        usersTableModel.setColumnIdentifiers(new String[]{"ID", "Nom", "Prénom", "Login", "Rôle", "Actions"});

        // Tableau
        JTable usersTable = new JTable(usersTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(usersTable);

        // Panel boutons
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton refreshBtn = new JButton("Actualiser");

        // Styles boutons
        addBtn.setBackground(new Color(50, 150, 50));
        editBtn.setBackground(new Color(70, 130, 180));
        deleteBtn.setBackground(new Color(220, 80, 60));
        refreshBtn.setBackground(new Color(100, 100, 100));

        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, refreshBtn}) {
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        // Actions boutons
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

        // Chargement initial
        loadUsersData();

        return panel;
    }

    private void loadUsersData() {
        usersTableModel.setRowCount(0); // Vider le tableau

        try (Connection conn = DBconnect.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id, nom, prenom, login, role FROM utilisateurs ORDER BY nom, prenom")) {

            while (rs.next()) {
                usersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("role"),
                        "Actions"
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

        // Formulaire d'édition
        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField nomField = new JTextField(nom);
        JTextField prenomField = new JTextField(prenom);
        JTextField loginField = new JTextField(login);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"enseignant", "responsable", "chef"});
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
        editPanel.add(new JLabel("Nouveau mot de passe (laisser vide pour ne pas changer):"));
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
            // Rafraîchir les données
        }
    }

    private void updateUserInDatabase(int userId, String nom, String prenom, String login,
                                      String role, char[] password) {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, login=?, role=?";
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

            if (changePassword) {
                pstmt.setString(5, hashPassword(new String(password)));
                pstmt.setInt(6, userId);
            } else {
                pstmt.setInt(5, userId);
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Utilisateur mis à jour avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(this,
                        "Ce login est déjà utilisé par un autre utilisateur",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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
                         "DELETE FROM utilisateurs WHERE id = ?")) {

                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur supprimé avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadUsersData(); // Rafraîchir les données
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setBackground(new Color(240, 240, 240));

        statusLabel = new JLabel("Prêt");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);

        return statusPanel;
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

            }
        });
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        updateStatus("Affichage: " + getPanelTitle(panelName));

        // Rafraîchir les données si nécessaire
        if ("seances".equals(panelName)) {
            seancesPanel.chargerDonnees();
        } else if ("gestionUtilisateurs".equals(panelName)) {
            loadUsersData();
        } else if ("assignation".equals(panelName)) {
            assignerCoursPanel.rafraichirDonnees();
        }
    }

    private String getPanelTitle(String panelName) {
        switch (panelName) {
            case "default": return "Tableau de bord";
            case "seances": return "Gestion des séances";
            case "ajout": return "Ajout d'utilisateur";
            case "gestionUtilisateurs": return "Gestion des utilisateurs";
            case "assignation": return "Assignation des cours";
            case "genererPDF": return "Génération de PDF";
            default: return "";
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Pour tester l'interface sans connexion réelle
            Utilisateur testUser = new Utilisateur();
            testUser.setNomComplet("Chef Département Test");
            new InterfaceChefDepartement(testUser).setVisible(true);
        });
    }
}

 */

/*
package interfaces;

import DBO.DBconnect;
import models.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, Object> appState;
    private Utilisateur currentUser;

    // Références aux panels
    private VoirSeancesTousEnseignants seancesPanel;
    private AjoutUtilisateur ajoutUtilisateurPanel;
    private AssignerCours assignerCoursPanel;
    private JPanel gestionUtilisateursPanel;
    private DefaultTableModel usersTableModel;

    public InterfaceChefDepartement(Utilisateur user) {
        this.currentUser = user;
        this.appState = new HashMap<>();
        appState.put("currentUser", currentUser);
        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    public InterfaceChefDepartement() {
        this.currentUser = new Utilisateur();
        this.appState = new HashMap<>();
        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    private void initializeUI() {
        setTitle("Tableau de bord - Chef de Département");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header (sans mention d'utilisateur)
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Navigation panel avec logo
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initContentPanels();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 20));

        JLabel titre = new JLabel("Espace Chef de Département");
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.BLACK);
        headerPanel.add(titre);

        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setBackground(new Color(70, 130, 180));
        navPanel.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));

        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(70, 130, 180));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(200, 100));

        // Placeholder d'image (à remplacer par votre logo)
        BufferedImage dummyImage = new BufferedImage(150, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dummyImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.drawString("LOGO", 50, 40);
        g2d.dispose();
        JLabel logoLabel = new JLabel(new ImageIcon(dummyImage));
        logoPanel.add(logoLabel);

        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(logoPanel);
        navPanel.add(Box.createVerticalStrut(20));

        // Boutons de navigation
        JButton btnDashboard = createNavButton("Tableau de bord", "default");
        JButton btnSeances = createNavButton("Gestion des séances", "seances");
        JButton btnUtilisateurs = createNavButton("Gestion des utilisateurs", "gestionUtilisateurs");
        JButton btnCours = createNavButton("Assignation des cours", "assignation");
        JButton btnGenererPDF = createNavButton("Générer PDF", "genererPDF", new Color(100, 70, 150));
        JButton btnDeconnexion = createNavButton("Déconnexion", "logout", new Color(220, 80, 60));

        navPanel.add(btnDashboard);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnSeances);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnUtilisateurs);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnCours);
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
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            if ("logout".equals(panelName)) {
                confirmAndExit();
            } else if ("genererPDF".equals(panelName)) {
                genererPDF();
            } else {
                showPanel(panelName);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void initContentPanels() {
        // Panel Dashboard
        contentPanel.add(createDashboardPanel(), "default");

        // Panel Séances
        seancesPanel = new VoirSeancesTousEnseignants(appState, true);
        contentPanel.add(seancesPanel, "seances");

        // Panel Ajout Utilisateur
        ajoutUtilisateurPanel = new AjoutUtilisateur();
        contentPanel.add(ajoutUtilisateurPanel, "ajout");

        // Panel Assignation Cours
        assignerCoursPanel = new AssignerCours(appState);
        contentPanel.add(assignerCoursPanel, "assignation");

        // Panel Gestion Utilisateurs
        gestionUtilisateursPanel = createGestionUtilisateursPanel();
        contentPanel.add(gestionUtilisateursPanel, "gestionUtilisateurs");

        // Panel Génération PDF
        contentPanel.add(createGenererPDFPanel(), "genererPDF");
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
        btnGenererEmploi.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenererEmploi.addActionListener(e -> genererEmploiDuTempsPDF());

        JButton btnGenererListe = new JButton("Générer Liste Enseignants");
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

    private void genererPDF() {
        cardLayout.show(contentPanel, "genererPDF");
    }

    private void genererEmploiDuTempsPDF() {
        JOptionPane.showMessageDialog(this,
                "Génération de l'emploi du temps PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void genererListeEnseignantsPDF() {
        JOptionPane.showMessageDialog(this,
                "Génération de la liste des enseignants PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statsPanel.add(createStatCard("Séances cette semaine", getSeancesCount()));
        statsPanel.add(createStatCard("Enseignants", getEnseignantsCount()));
        statsPanel.add(createStatCard("Cours assignés", getCoursAssignesCount()));

        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String getSeancesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM seance WHERE dateseance BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "Err";
        }
    }

    private String getEnseignantsCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateurs WHERE role = 'enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "Err";
        }
    }

    private String getCoursAssignesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM enseignants_cours")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "Err";
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
                return column == 5;
            }
        };
        usersTableModel.setColumnIdentifiers(new String[]{"ID", "Nom", "Prénom", "Login", "Rôle", "Actions"});

        JTable usersTable = new JTable(usersTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(usersTable);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton refreshBtn = new JButton("Actualiser");

        addBtn.setBackground(new Color(50, 150, 50));
        editBtn.setBackground(new Color(70, 130, 180));
        deleteBtn.setBackground(new Color(220, 80, 60));
        refreshBtn.setBackground(new Color(100, 100, 100));

        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, refreshBtn}) {
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

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
                     "SELECT id, nom, prenom, login, role FROM utilisateurs ORDER BY nom, prenom")) {

            while (rs.next()) {
                usersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("role"),
                        "Actions"
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
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"enseignant", "responsable", "chef"});
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
        editPanel.add(new JLabel("Nouveau mot de passe (laisser vide pour ne pas changer):"));
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
        }
    }

    private void updateUserInDatabase(int userId, String nom, String prenom, String login,
                                      String role, char[] password) {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, login=?, role=?";
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

            if (changePassword) {
                pstmt.setString(5, hashPassword(new String(password)));
                pstmt.setInt(6, userId);
            } else {
                pstmt.setInt(5, userId);
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Utilisateur mis à jour avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(this,
                        "Ce login est déjà utilisé par un autre utilisateur",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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
                         "DELETE FROM utilisateurs WHERE id = ?")) {

                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur supprimé avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadUsersData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Gestion facultative de la fermeture
            }
        });
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        if ("seances".equals(panelName)) {
            seancesPanel.chargerDonnees();
        } else if ("gestionUtilisateurs".equals(panelName)) {
            loadUsersData();
        } else if ("assignation".equals(panelName)) {
            assignerCoursPanel.rafraichirDonnees();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceChefDepartement().setVisible(true);
        });
    }
}

 */


/*
package interfaces;

import DBO.DBconnect;
import models.Utilisateur;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, Object> appState;
    private Utilisateur currentUser;

    // Références aux panels
    private VoirSeancesTousEnseignants seancesPanel;
    private AjoutUtilisateur ajoutUtilisateurPanel;
    private AssignerCours assignerCoursPanel;
    private JPanel gestionUtilisateursPanel;
    private DefaultTableModel usersTableModel;

    public InterfaceChefDepartement(Utilisateur user) {
        this.currentUser = user;
        this.appState = new HashMap<>();
        appState.put("currentUser", currentUser);
        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    public InterfaceChefDepartement() {
        this(new Utilisateur());
    }

    private void initializeUI() {
        setTitle("Tableau de bord - Chef de Département");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header avec titre centré
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Sidebar bleue
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
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel titre = new JLabel("Espace Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.BLACK);

        headerPanel.add(titre, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(70, 130, 180));
        navPanel.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo Panel
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
            JOptionPane.showMessageDialog(this, "Image non trouvée ", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(logoPanel);
        navPanel.add(Box.createVerticalStrut(20));

        // Boutons de navigation
        JButton btnDashboard = createNavButton("Tableau de bord", "dashboard");
        JButton btnSeances = createNavButton("Gestion des séances", "seances");
        JButton btnUtilisateurs = createNavButton("Gestion des utilisateurs", "utilisateurs");
        JButton btnCours = createNavButton("Assignation des cours", "assignation");
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

        // Effet de survol
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
        // Panel Dashboard
        contentPanel.add(createDashboardPanel(), "dashboard");

        // Panel Séances
        seancesPanel = new VoirSeancesTousEnseignants(appState, true);
        contentPanel.add(seancesPanel, "seances");

        // Panel Ajout Utilisateur
        ajoutUtilisateurPanel = new AjoutUtilisateur();
        contentPanel.add(ajoutUtilisateurPanel, "ajout");

        // Panel Assignation Cours
        assignerCoursPanel = new AssignerCours(appState);
        contentPanel.add(assignerCoursPanel, "assignation");

        // Panel Gestion Utilisateurs
        gestionUtilisateursPanel = createGestionUtilisateursPanel();
        contentPanel.add(gestionUtilisateursPanel, "utilisateurs");

        // Panel Génération PDF
        contentPanel.add(createGenererPDFPanel(), "genererpdf");
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

    private void genererPDF() {
        cardLayout.show(contentPanel, "genererpdf");
    }

    private void genererEmploiDuTempsPDF() {
        JOptionPane.showMessageDialog(this,
                "Génération de l'emploi du temps PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void genererListeEnseignantsPDF() {
        JOptionPane.showMessageDialog(this,
                "Génération de la liste des enseignants PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statsPanel.add(createStatCard("Séances cette semaine", getSeancesCount()));
        statsPanel.add(createStatCard("Enseignants", getEnseignantsCount()));
        statsPanel.add(createStatCard("Cours assignés", getCoursAssignesCount()));

        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String getSeancesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM seance WHERE dateseance BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "NULL";
        }
    }

    private String getEnseignantsCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateurs WHERE role = 'enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "NULL";
        }
    }

    private String getCoursAssignesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM enseignants_cours")) {
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
                return column == 5;
            }
        };
        usersTableModel.setColumnIdentifiers(new String[]{"ID", "Nom", "Prénom", "Login", "Rôle", "Actions"});

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
                     "SELECT id, nom, prenom, login, role FROM utilisateurs ORDER BY nom, prenom")) {

            while (rs.next()) {
                usersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("role"),
                        "Actions"
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
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"enseignant", "responsable", "chef"});
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
        editPanel.add(new JLabel("Nouveau mot de passe (laisser vide pour ne pas changer):"));
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
        }
    }

    private void updateUserInDatabase(int userId, String nom, String prenom, String login,
                                      String role, char[] password) {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, login=?, role=?";
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

            if (changePassword) {
                pstmt.setString(5, hashPassword(new String(password)));
                pstmt.setInt(6, userId);
            } else {
                pstmt.setInt(5, userId);
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Utilisateur mis à jour avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(this,
                        "Ce login est déjà utilisé par un autre utilisateur",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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
                         "DELETE FROM utilisateurs WHERE id = ?")) {

                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur supprimé avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadUsersData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Gestion facultative de la fermeture
            }
        });
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        if ("seances".equals(panelName)) {
            seancesPanel.chargerDonnees();
        } else if ("utilisateurs".equals(panelName)) {
            loadUsersData();
        } else if ("assignation".equals(panelName)) {
            assignerCoursPanel.rafraichirDonnees();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceChefDepartement().setVisible(true);
        });
    }
}


 */
package interfaces;

import DBO.DBconnect;
import models.Utilisateur;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InterfaceChefDepartement extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, Object> appState;
    private Utilisateur currentUser;

    // Références aux panels
    private VoirSeancesTousEnseignants seancesPanel;
    private AjoutUtilisateur ajoutUtilisateurPanel;
    private AssignerCours assignerCoursPanel;
    private JPanel gestionUtilisateursPanel;
    private DefaultTableModel usersTableModel;

    public InterfaceChefDepartement(Utilisateur user) {
        this.currentUser = user;
        this.appState = new HashMap<>();
        appState.put("currentUser", currentUser);
        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    public InterfaceChefDepartement() {
        this(new Utilisateur());
    }

    private void initializeUI() {
        setTitle("Tableau de bord - Chef de Département");
        setSize(700, 600);
        setSize(new Dimension(900, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header avec bande grise
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Sidebar bleue
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
        // Panel principal pour la bande grise
        JPanel greyBandPanel = new JPanel(new BorderLayout());
        greyBandPanel.setBackground(new Color(240, 240, 240)); // Couleur grise identique à l'interface enseignant
        greyBandPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        greyBandPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Titre centré
        JLabel titre = new JLabel("Espace Chef de Département", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.BLACK);

        greyBandPanel.add(titre, BorderLayout.CENTER);
        return greyBandPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(70, 130, 180));
        navPanel.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo Panel
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
            JOptionPane.showMessageDialog(this, "Image non trouvée ", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(logoPanel);
        navPanel.add(Box.createVerticalStrut(20));

        // Boutons de navigation
        JButton btnDashboard = createNavButton("Tableau de bord", "dashboard");
        JButton btnSeances = createNavButton("Gestion des séances", "seances");
        JButton btnUtilisateurs = createNavButton("Gestion des utilisateurs", "utilisateurs");
        JButton btnCours = createNavButton("Assignation des cours", "assignation");
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

        // Effet de survol
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
        // Panel Dashboard
        contentPanel.add(createDashboardPanel(), "dashboard");

        // Panel Séances
        seancesPanel = new VoirSeancesTousEnseignants(appState, true);
        contentPanel.add(seancesPanel, "seances");

        // Panel Ajout Utilisateur
        ajoutUtilisateurPanel = new AjoutUtilisateur();
        contentPanel.add(ajoutUtilisateurPanel, "ajout");

        // Panel Assignation Cours
        assignerCoursPanel = new AssignerCours(appState);
        contentPanel.add(assignerCoursPanel, "assignation");

        // Panel Gestion Utilisateurs
        gestionUtilisateursPanel = createGestionUtilisateursPanel();
        contentPanel.add(gestionUtilisateursPanel, "utilisateurs");

        // Panel Génération PDF
        contentPanel.add(createGenererPDFPanel(), "genererpdf");
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

    private void genererPDF() {
        cardLayout.show(contentPanel, "genererpdf");
    }

    private void genererEmploiDuTempsPDF() {
        JOptionPane.showMessageDialog(this,
                "Génération de l'emploi du temps PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void genererListeEnseignantsPDF() {
        JOptionPane.showMessageDialog(this,
                "Génération de la liste des enseignants PDF en cours...",
                "Génération PDF",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface Chef de Département", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statsPanel.add(createStatCard("Séances cette semaine", getSeancesCount()));
        statsPanel.add(createStatCard("Enseignants", getEnseignantsCount()));
        statsPanel.add(createStatCard("Cours assignés", getCoursAssignesCount()));

        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String getSeancesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM seance WHERE dateseance BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "NULL";
        }
    }

    private String getEnseignantsCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateurs WHERE role = 'enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "0";
        } catch (SQLException e) {
            return "NULL";
        }
    }

    private String getCoursAssignesCount() {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM enseignants_cours")) {
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
                return column == 5;
            }
        };
        usersTableModel.setColumnIdentifiers(new String[]{"ID", "Nom", "Prénom", "Login", "Rôle", "Actions"});

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
                     "SELECT id, nom, prenom, login, role FROM utilisateurs ORDER BY nom, prenom")) {

            while (rs.next()) {
                usersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("role"),
                        "Actions"
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
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"enseignant", "responsable", "chef"});
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
        editPanel.add(new JLabel("Nouveau mot de passe (laisser vide pour ne pas changer):"));
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
        }
    }

    private void updateUserInDatabase(int userId, String nom, String prenom, String login,
                                      String role, char[] password) {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, login=?, role=?";
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

            if (changePassword) {
                pstmt.setString(5, hashPassword(new String(password)));
                pstmt.setInt(6, userId);
            } else {
                pstmt.setInt(5, userId);
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Utilisateur mis à jour avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(this,
                        "Ce login est déjà utilisé par un autre utilisateur",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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
                         "DELETE FROM utilisateurs WHERE id = ?")) {

                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur supprimé avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadUsersData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Gestion facultative de la fermeture
            }
        });
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        if ("seances".equals(panelName)) {
            seancesPanel.chargerDonnees();
        } else if ("utilisateurs".equals(panelName)) {
            loadUsersData();
        } else if ("assignation".equals(panelName)) {
            assignerCoursPanel.rafraichirDonnees();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceChefDepartement().setVisible(true);
        });
    }
}


