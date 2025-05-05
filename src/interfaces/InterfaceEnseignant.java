
package interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Objects;

public class InterfaceEnseignant extends JFrame {
    private int enseignantId;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;
    private JPanel listeSeancesPanel;

    public InterfaceEnseignant(int enseignantId) {
        this.enseignantId = enseignantId;
        initializeUI();
        initComponents();
        setupEventHandlers();
    }

    private void initializeUI() {
        setTitle("Espace Enseignant");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Navigation
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        // Content
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        contentPanel.add(createWelcomePanel(), "welcome");
        contentPanel.add(createAddPanel(), "add");
        contentPanel.add(createViewPanel(), "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel greyBandPanel = new JPanel(new BorderLayout());
        greyBandPanel.setBackground(new Color(240, 240, 240));
        greyBandPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        greyBandPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
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

        // Boutons
        JButton btnAjouter = createNavButton("Ajouter une séance", "add");
        JButton btnVoir = createNavButton("Voir séances", "view");
        JButton btnDeconnexion = createNavButton("Déconnexion", "logout", new Color(220, 80, 60));

        navPanel.add(btnAjouter);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnVoir);
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
                if ("add".equals(panelName)) {
                    chargerCours();
                } else if ("view".equals(panelName)) {
                    try {
                        chargerSeancesEnseignant();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                cardLayout.show(((JPanel)getContentPane().getComponent(2)), panelName);
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'espace Enseignant", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Ajouter une séance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        coursCombo = new JComboBox<>();
        coursCombo.setPreferredSize(new Dimension(200, 40));
        panel.add(new JLabel("Cours :"), gbc);
        gbc.gridx = 1;
        panel.add(coursCombo, gbc);

        dateField = new JTextField("2025-04-05", 18);
        dateField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Date (AAAA-MM-JJ) :"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        heureDebutField = new JTextField("08h:00", 18);
        heureDebutField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure début (HH:mm) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10h:00", 18);
        heureFinField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure fin (HH:00) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureFinField, gbc);

        contenuArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(contenuArea);
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Contenu :"), gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        JButton addButton = new JButton("Ajouter");
        styleHoverButton(addButton, new Color(70, 130, 180));
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        addButton.addActionListener(this::ajouterSeance);

        return panel;
    }

    private void styleHoverButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Mes séances ajoutées");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(titleLabel, BorderLayout.NORTH);

        listeSeancesPanel = new JPanel();
        listeSeancesPanel.setLayout(new BoxLayout(listeSeancesPanel, BoxLayout.Y_AXIS));
        listeSeancesPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listeSeancesPanel);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        // Initialisation déjà faite dans les méthodes de création des boutons
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

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.nom FROM cours c " +
                             "JOIN enseignants_cours ec ON c.id = ec.cours_id " +
                             "WHERE ec.Enseignant_id = ?")) {

            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                coursCombo.addItem(rs.getString("nom"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement cours : " + e.getMessage());
        }
    }

    private void ajouterSeance(ActionEvent e) {
        String coursNom = (String) coursCombo.getSelectedItem();
        String date = dateField.getText().trim();
        String debut = heureDebutField.getText().trim();
        String fin = heureFinField.getText().trim();
        String contenu = contenuArea.getText().trim();

        if (coursNom == null || date.isEmpty() || debut.isEmpty() || fin.isEmpty() || contenu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "")) {
            PreparedStatement coursStmt = conn.prepareStatement(
                    "SELECT c.id FROM cours c " +
                            "JOIN enseignants_cours ec ON c.id = ec.cours_id " +
                            "WHERE c.nom = ? AND ec.Enseignant_id = ?");

            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, Enseignant_id, dateseance, heure_debut, heure_fin, contenu) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                );
                insert.setInt(1, coursId);
                insert.setInt(2, enseignantId);
                insert.setString(3, date);
                insert.setString(4, debut);
                insert.setString(5, fin);
                insert.setString(6, contenu);
                insert.executeUpdate();

                JOptionPane.showMessageDialog(this, "Séance ajoutée !");
                contenuArea.setText("");
                chargerSeancesEnseignant();
            } else {
                JOptionPane.showMessageDialog(this, "Cours non trouvé ou non assigné à cet enseignant.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() throws SQLException {
        // Modèle de données pour le tableau
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre toutes les cellules non éditables
            }
        };

        // Définir les colonnes
        model.setColumnIdentifiers(new String[]{"Cours", "Date", "Heure début", "Heure fin", "Statut", "Motif"});

        // Créer le tableau avec le modèle
        JTable table = new JTable(model);

        // Personnaliser l'apparence du tableau
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        // Centrer le texte dans les cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Couleur des lignes selon le statut
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                String statut = (String) table.getModel().getValueAt(row, 4);
                if ("validée".equalsIgnoreCase(statut)) {
                    c.setBackground(new Color(220, 255, 220)); // Vert clair
                } else if ("refusée".equalsIgnoreCase(statut)) {
                    c.setBackground(new Color(255, 220, 220)); // Rouge clair
                } else {
                    c.setBackground(Color.WHITE);
                }

                if (isSelected) {
                    c.setBackground(new Color(200, 200, 255)); // Bleu clair pour sélection
                }

                return c;
            }
        });

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.nom AS cours, s.dateseance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "JOIN enseignants_cours ec ON c.id = ec.cours_id " +
                             "WHERE s.Enseignant_id = ? AND ec.Enseignant_id = ? " +
                             "ORDER BY s.dateseance DESC")) {

            stmt.setInt(1, enseignantId);
            stmt.setInt(2, enseignantId);
            ResultSet result = stmt.executeQuery();

            // Remplir le modèle avec les données
            while (result.next()) {
                model.addRow(new Object[]{
                        result.getString("cours"),
                        result.getString("dateseance"),
                        result.getString("heure_debut"),
                        result.getString("heure_fin"),
                        result.getString("statut"),
                        result.getString("commentaire_refus") != null ?
                                result.getString("commentaire_refus") : "N/A"
                });
            }
        }

        // Nettoyer le panel et ajouter le tableau avec scroll
        listeSeancesPanel.removeAll();
        listeSeancesPanel.setLayout(new BorderLayout());
        listeSeancesPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        listeSeancesPanel.revalidate();
        listeSeancesPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new InterfaceEnseignant(1).setVisible(true));
    }
}