/*package interfaces ;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfaceEnseignant extends JFrame {
    private int enseignantId;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;
    private JPanel listeSeancesPanel;

    public InterfaceEnseignant() {

        setTitle("Espace Enseignant");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ----------- Formulaire au centre --------------
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        coursCombo = new JComboBox<>();
        chargerCours();

        dateField = new JTextField("2025-04-05");
        heureDebutField = new JTextField("08:00");
        heureFinField = new JTextField("10:00");
        contenuArea = new JTextArea(3, 20);
        JScrollPane contenuScroll = new JScrollPane(contenuArea);

        formPanel.add(new JLabel("Cours :"));
        formPanel.add(coursCombo);
        formPanel.add(new JLabel("Date (AAAA-MM-JJ) :"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Heure début (HH:MM) :"));
        formPanel.add(heureDebutField);
        formPanel.add(new JLabel("Heure fin (HH:MM) :"));
        formPanel.add(heureFinField);
        formPanel.add(new JLabel("Contenu :"));
        formPanel.add(contenuScroll);

        JButton ajouterBtn = new JButton("Ajouter séance");
        ajouterBtn.addActionListener(e -> ajouterSeance());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(ajouterBtn);


    JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // ----------- Liste des séances à droite --------------
        listeSeancesPanel = new JPanel();
        listeSeancesPanel.setLayout(new BoxLayout(listeSeancesPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(listeSeancesPanel);
        scroll.setPreferredSize(new Dimension(400, 0));
        scroll.setBorder(BorderFactory.createTitledBorder("Mes séances ajoutées"));

        add(scroll, BorderLayout.EAST);

        // ---------- Charger la liste + afficher -------------
        chargerSeancesEnseignant();
        setVisible(true);
    }

    private void chargerCours() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                coursCombo.addItem(result.getString("nom"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement cours : " + e.getMessage());
        }
    }

    private void ajouterSeance() {
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
            // Récupération de l'id du cours
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");

                insert.setInt(1, coursId);
                insert.setInt(2, enseignantId);
                insert.setString(3, date);
                insert.setString(4, debut);
                insert.setString(5, fin);
                insert.setString(6, contenu);

                insert.executeUpdate();
                JOptionPane.showMessageDialog(this, "Séance ajoutée !");
                contenuArea.setText("");
                chargerSeancesEnseignant(); // recharge la liste

            } else {
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + e.getMessage());
        }
    }

    private void chargerSeancesEnseignant() {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panel.setBackground(Color.WHITE);
                panel.setMaximumSize(new Dimension(380, 120));

                panel.add(new JLabel("Cours : " + cours));
                panel.add(new JLabel("Date : " + date + " (" + debut + " - " + fin + ")"));
                panel.add(new JLabel("Statut : " + statut));

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif du refus : " + refus);
                    refusLabel.setForeground(Color.RED);
                    panel.add(refusLabel);
                }

                listeSeancesPanel.add(panel);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de  chargement des séances : " + e.getMessage());
        }
    }
}

 */




/*
package interfaces;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfaceEnseignant extends JFrame {
    private int enseignantId;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;
    private JPanel listeSeancesPanel;

    public InterfaceEnseignant() {

        setLocationRelativeTo(null);
        setTitle("Espace Enseignant");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Formulaire  centre
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        coursCombo = new JComboBox<>();
        chargerCours();

        dateField = new JTextField("2025-04-05");
        heureDebutField = new JTextField("08:00");
        heureFinField = new JTextField("10:00");
        contenuArea = new JTextArea(3, 20);
        JScrollPane contenuScroll = new JScrollPane(contenuArea);

        formPanel.add(new JLabel("Cours :"));
        formPanel.add(coursCombo);
        formPanel.add(new JLabel("Date (AAAA-MM-JJ) :"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Heure début (HH:MM) :"));
        formPanel.add(heureDebutField);
        formPanel.add(new JLabel("Heure fin (HH:MM) :"));
        formPanel.add(heureFinField);
        formPanel.add(new JLabel("Contenu :"));
        formPanel.add(contenuScroll);

        JButton ajouterBtn = new JButton("Ajouter séance");
        ajouterBtn.addActionListener(e -> ajouterSeance());

        JPanel bottomFormPanel = new JPanel();
        bottomFormPanel.add(ajouterBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(bottomFormPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        //  Liste des séances à droite
        listeSeancesPanel = new JPanel();
        listeSeancesPanel.setLayout(new BoxLayout(listeSeancesPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(listeSeancesPanel);
        scroll.setPreferredSize(new Dimension(400, 0));
        scroll.setBorder(BorderFactory.createTitledBorder("Mes séances ajoutées"));

        add(scroll, BorderLayout.EAST);

        //Bouton de déconnexion en bas
        JButton btnDeconnexion = new JButton("Se déconnecter");
        btnDeconnexion.addActionListener(e -> {
            dispose(); // Fermer cette fenêtre
            new LoginFrame().setVisible(true); // Retourner à la page de connexion
        });

        JPanel panelBas = new JPanel();
        panelBas.add(btnDeconnexion);

        add(panelBas, BorderLayout.SOUTH);

        // Charger la liste + afficher
        chargerSeancesEnseignant();
        setVisible(true);
    }

    private void chargerCours() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                coursCombo.addItem(result.getString("nom"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement cours : " + e.getMessage());
        }
    }

    private void ajouterSeance() {
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
            // Récupération de l'id du cours
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");

                insert.setInt(1, coursId);
                insert.setInt(2, enseignantId);
                insert.setString(3, date);
                insert.setString(4, debut);
                insert.setString(5, fin);
                insert.setString(6, contenu);

                insert.executeUpdate();
                JOptionPane.showMessageDialog(this, "Séance ajoutée !");
                contenuArea.setText("");
                chargerSeancesEnseignant(); // recharge la liste

            } else {
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + e.getMessage());
        }
    }

    private void chargerSeancesEnseignant() {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panel.setBackground(Color.WHITE);
                panel.setMaximumSize(new Dimension(380, 120));

                panel.add(new JLabel("Cours : " + cours));
                panel.add(new JLabel("Date : " + date + " (" + debut + " - " + fin + ")"));
                panel.add(new JLabel("Statut : " + statut));

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif du refus : " + refus);
                    refusLabel.setForeground(Color.RED);
                    panel.add(refusLabel);
                }

                listeSeancesPanel.add(panel);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de  chargement des séances : " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        new InterfaceEnseignant().setVisible(true);
    }


}


 */


/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;

public class InterfaceEnseignant extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;

    public InterfaceEnseignant() {
        setTitle("Espace Enseignant");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titre
        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Panel des boutons à droite
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(70, 130, 180));

        JButton btnAjouter = createMenuButton("Ajouter séance");
        JButton btnVoir = createMenuButton("Voir séances");
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        buttonPanel.add(btnAjouter);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(btnVoir);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnDeconnexion);

        mainPanel.add(buttonPanel, BorderLayout.EAST);

        // Panel de contenu
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        // Panel d'ajout de séance
        JPanel addPanel = createAddPanel();
        contentPanel.add(addPanel, "add");

        // Panel de visualisation
        JPanel viewPanel = createViewPanel();
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Écouteurs
        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            chargerSeances();
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        coursCombo = new JComboBox<>();
        dateField = new JTextField(15);
        heureDebutField = new JTextField(15);
        heureFinField = new JTextField(15);
        contenuArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(contenuArea);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cours:"), gbc);
        gbc.gridx = 1;
        panel.add(coursCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Date(AAAA-MM-JJ):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Heure début:"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Heure fin:"), gbc);
        gbc.gridx = 1;
        panel.add(heureFinField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Contenu:"), gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(this::ajouterSeance);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea seancesArea = new JTextArea();
        seancesArea.setEditable(false);
        panel.add(new JScrollPane(seancesArea), BorderLayout.CENTER);
        return panel;
    }

    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(50, 100, 150));
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void chargerCours() {
        // Implémentation existante
    }

    private void chargerSeances() {
        // Implémentation existante
    }

    private void ajouterSeance(ActionEvent e) {
        // Implémentation existante
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceEnseignant().setVisible(true);
        });
    }
}

 */
/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class InterfaceEnseignant extends JFrame {
    private int enseignantId ; // À adapter si besoin
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;
    private JPanel listeSeancesPanel;

    public InterfaceEnseignant() {
        setTitle("Espace Enseignant");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titre
        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Menu latéral droit
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(70, 130, 180));

        JButton btnAjouter = createMenuButton("Ajouter séance");
        JButton btnVoir = createMenuButton("Voir séances");
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        buttonPanel.add(btnAjouter);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(btnVoir);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnDeconnexion);

        mainPanel.add(buttonPanel, BorderLayout.EAST);

        // Panel central à bascule
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Actions
        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            chargerSeancesEnseignant();
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        coursCombo = new JComboBox<>();
        dateField = new JTextField("2025-04-05", 15);
        heureDebutField = new JTextField("08:00", 15);
        heureFinField = new JTextField("10:00", 15);
        contenuArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(contenuArea);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cours:"), gbc);
        gbc.gridx = 1;
        panel.add(coursCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Date(AAAA-MM-JJ):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Heure début:"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Heure fin:"), gbc);
        gbc.gridx = 1;
        panel.add(heureFinField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Contenu:"), gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(this::ajouterSeance);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createViewPanel() {
        listeSeancesPanel = new JPanel();
        listeSeancesPanel.setLayout(new BoxLayout(listeSeancesPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(listeSeancesPanel);
        scroll.setBorder(BorderFactory.createTitledBorder("Mes séances ajoutées"));
        return new JPanel(new BorderLayout()) {{
            add(scroll, BorderLayout.CENTER);
        }};
    }

    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(50, 100, 150));
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                coursCombo.addItem(result.getString("nom"));
            }

        } catch (Exception e) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");

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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panel.setBackground(Color.WHITE);
                panel.setMaximumSize(new Dimension(500, 120));

                panel.add(new JLabel("Cours : " + cours));
                panel.add(new JLabel("Date : " + date + " (" + debut + " - " + fin + ")"));
                panel.add(new JLabel("Statut : " + statut));

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif du refus : " + refus);
                    refusLabel.setForeground(Color.RED);
                    panel.add(refusLabel);
                }

                listeSeancesPanel.add(panel);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement des séances : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceEnseignant().setVisible(true));
    }
}




package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class InterfaceEnseignant extends JFrame {
    private int enseignantId ; // À adapter si besoin
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;
    private JPanel listeSeancesPanel;

    public InterfaceEnseignant() {
        setTitle("Espace Enseignant");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titre
        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Menu latéral droit
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(70, 130, 180));

        JButton btnAjouter = createMenuButton("Ajouter séance");
        JButton btnVoir = createMenuButton("Voir séances");
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        buttonPanel.add(btnAjouter);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(btnVoir);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnDeconnexion);

        mainPanel.add(buttonPanel, BorderLayout.EAST);

        // Panel central à bascule
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Actions
        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            chargerSeancesEnseignant();
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Titre section
        JLabel titleLabel = new JLabel("Ajouter une séance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        // Séparateur
        JSeparator separator = new JSeparator();
        gbc.gridy = 1;
        panel.add(separator, gbc);

        // Réinitialisation gridwidth
        gbc.gridwidth = 1;

        coursCombo = new JComboBox<>();
        coursCombo.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Cours :"), gbc);
        gbc.gridx = 1;
        panel.add(coursCombo, gbc);

        dateField = new JTextField("2025-04-05", 15);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Date (AAAA-MM-JJ) :"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        heureDebutField = new JTextField("08:00", 15);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Heure début (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10:00", 15);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Heure fin (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureFinField, gbc);

        contenuArea = new JTextArea(8, 30);
        contenuArea.setLineWrap(true);
        contenuArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contenuArea);
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Contenu :"), gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.addActionListener(this::ajouterSeance);

        gbc.gridx = 1; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createViewPanel() {
        listeSeancesPanel = new JPanel();
        listeSeancesPanel.setLayout(new BoxLayout(listeSeancesPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(listeSeancesPanel);
        scroll.setBorder(BorderFactory.createTitledBorder("Mes séances ajoutées"));
        return new JPanel(new BorderLayout()) {{
            add(scroll, BorderLayout.CENTER);
        }};
    }

    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(50, 100, 150));
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                coursCombo.addItem(result.getString("nom"));
            }

        } catch (Exception e) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");

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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panel.setBackground(Color.WHITE);
                panel.setMaximumSize(new Dimension(500, 120));

                panel.add(new JLabel("Cours : " + cours));
                panel.add(new JLabel("Date : " + date + " (" + debut + " - " + fin + ")"));
                panel.add(new JLabel("Statut : " + statut));

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif du refus : " + refus);
                    refusLabel.setForeground(Color.RED);
                    panel.add(refusLabel);
                }

                listeSeancesPanel.add(panel);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement des séances : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceEnseignant().setVisible(true));
    }
}

 */



/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class InterfaceEnseignant extends JFrame {
    private int enseignantId ; // À adapter si besoin
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;
    private JPanel listeSeancesPanel;

    public InterfaceEnseignant() {
        setTitle("Espace Enseignant");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titre
        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Menu latéral droit
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 20));
        buttonPanel.setBackground(new Color(70, 130, 180));

        JButton btnAjouter = createMenuButton("Ajouter séance");
        JButton btnVoir = createMenuButton("Voir séances");
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        buttonPanel.add(btnAjouter);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        buttonPanel.add(btnVoir);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnDeconnexion);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Panel central à bascule
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Actions
        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            chargerSeancesEnseignant();
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Titre section

        JLabel titleLabel = new JLabel("Ajouter une séance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        // Séparateur

        JSeparator separator = new JSeparator();
        gbc.gridy = 1;
        panel.add(separator, gbc);

        // Réinitialisation gridwidth

        gbc.gridwidth = 1;

        coursCombo = new JComboBox<>();
        coursCombo.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Cours :"), gbc);
        gbc.gridx = 1;
        panel.add(coursCombo, gbc);

        dateField = new JTextField("2025-04-05", 15);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Date (AAAA-MM-JJ) :"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        heureDebutField = new JTextField("08:00", 15);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Heure début (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10:00", 15);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Heure fin (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureFinField, gbc);

        contenuArea = new JTextArea(8, 30);
        contenuArea.setLineWrap(true);
        contenuArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contenuArea);
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Contenu :"), gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.addActionListener(this::ajouterSeance);

        gbc.gridx = 1; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createViewPanel() {

        // Nouveau design pour le panel de visualisation

        JPanel mainViewPanel = new JPanel(new BorderLayout());
        mainViewPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainViewPanel.setBackground(new Color(240, 240, 240));

        // Titre amélioré

        JLabel titleLabel = new JLabel("Mes séances ajoutées", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 1, 20, 0));
        mainViewPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de contenu avec scroll

        listeSeancesPanel = new JPanel();
        listeSeancesPanel.setLayout(new BoxLayout(listeSeancesPanel, BoxLayout.Y_AXIS));
        listeSeancesPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listeSeancesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainViewPanel.add(scrollPane, BorderLayout.CENTER);

        return mainViewPanel;
    }

    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(50, 100, 150));
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                coursCombo.addItem(result.getString("nom"));
            }

        } catch (Exception e) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");

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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                // Nouveau design des cartes de séance
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)));
                card.setBackground(Color.WHITE);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

                // Style des labels
                Font labelFont = new Font("Arial", Font.PLAIN, 14);

                JLabel coursLabel = new JLabel("Cours: " + cours);
                coursLabel.setFont(labelFont);

                JLabel dateLabel = new JLabel("Date: " + date + " (" + debut + " - " + fin + ")");
                dateLabel.setFont(labelFont);

                JLabel statutLabel = new JLabel("Statut: " + statut);
                statutLabel.setFont(labelFont);
                statutLabel.setForeground(
                        "validée".equalsIgnoreCase(statut) ? new Color(50, 150, 50) :
                                "refusée".equalsIgnoreCase(statut) ? new Color(200, 50, 50) : Color.BLACK);

                card.add(coursLabel);
                card.add(Box.createRigidArea(new Dimension(0, 5)));
                card.add(dateLabel);
                card.add(Box.createRigidArea(new Dimension(0, 5)));
                card.add(statutLabel);

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif: " + refus);
                    refusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    refusLabel.setForeground(new Color(150, 50, 50));
                    card.add(Box.createRigidArea(new Dimension(0, 5)));
                    card.add(refusLabel);
                }

                listeSeancesPanel.add(card);
                listeSeancesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement des séances : " + e.getMessage());
        }
    }
      private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'espace Enseignant", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }
    contentPanel.add(createAddPanel(), "default");

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new InterfaceEnseignant().setVisible(true));
    }
}


 */


/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class InterfaceEnseignant extends JFrame {
    private int enseignantId ; // Remplacez avec la vraie valeur
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JComboBox<String> coursCombo;
    private JTextArea contenuArea;
    private JTextField dateField, heureDebutField, heureFinField;
    private JPanel listeSeancesPanel;

    public InterfaceEnseignant() {
        setTitle("Espace Enseignant");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel welcomePanel = createWelcomePanel();
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(new Color(0, 0, 0));
        mainPanel.add(titre, BorderLayout.NORTH);



        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));
        sidePanel.setBackground(new Color(70, 130, 180));

        JPanel logoButtonPanel = new JPanel();
        logoButtonPanel.setLayout(new BoxLayout(logoButtonPanel, BoxLayout.Y_AXIS));
        logoButtonPanel.setOpaque(false);
        logoButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            ImageIcon originalIcon = new ImageIcon("src/UNIV.jpg");
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoButtonPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        JButton btnAjouter = createMenuButton("Ajouter séance");
        JButton btnVoir = createMenuButton("Voir séances");
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        btnAjouter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);


        Frame frame = null;
        frame.setMinimumSize(new Dimension(1000, 1000)); // Taille minimale
        frame.setResizable(true); // Assure-toi que c'est bien activé

        logoButtonPanel.add(Box.createVerticalStrut(10));
        logoButtonPanel.add(btnAjouter);
        logoButtonPanel.add(Box.createVerticalStrut(20));
        logoButtonPanel.add(btnVoir);
        logoButtonPanel.add(Box.createVerticalGlue());
        logoButtonPanel.add(btnDeconnexion);

        sidePanel.add(logoButtonPanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            try {
                chargerSeancesEnseignant();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
    }

    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(70, 130, 180));
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 45));
        button.setMaximumSize(new Dimension(200, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }//Interface par default
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'espace Enseignant", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Ajouter une séance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 0, 0));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        panel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        coursCombo = new JComboBox<>();
        coursCombo.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Cours :"), gbc);
        gbc.gridx = 1;
        panel.add(coursCombo, gbc);

        dateField = new JTextField("2025-04-05", 15);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date (AAAA-MM-JJ) :"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        heureDebutField = new JTextField("08:00", 15);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Heure début (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10:00", 15);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Heure fin (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureFinField, gbc);

        contenuArea = new JTextArea(8, 30);
        JScrollPane scrollPane = new JScrollPane(contenuArea);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Contenu :"), gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.addActionListener(this::ajouterSeance);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createViewPanel() {
        JPanel mainViewPanel = new JPanel(new BorderLayout());
        mainViewPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainViewPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Mes séances ajoutées");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainViewPanel.add(titleLabel, BorderLayout.NORTH);

        listeSeancesPanel = new JPanel();
        listeSeancesPanel.setLayout(new BoxLayout(listeSeancesPanel, BoxLayout.Y_AXIS));
        listeSeancesPanel.setBackground(Color.WHITE);
        listeSeancesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(listeSeancesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainViewPanel.add(scrollPane, BorderLayout.CENTER);
        return mainViewPanel;
    }

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                coursCombo.addItem(result.getString("nom"));
            }

        } catch (Exception e) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");

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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() throws SQLException {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {

            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)));
                card.setBackground(Color.WHITE);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

                Font labelFont = new Font("Arial", Font.PLAIN, 14);

                JLabel coursLabel = new JLabel("Cours: " + cours);
                coursLabel.setFont(labelFont);

                JLabel dateLabel = new JLabel("Date: " + date + " (" + debut + " - " + fin + ")");
                dateLabel.setFont(labelFont);

                JLabel statutLabel = new JLabel("Statut: " + statut);
                statutLabel.setFont(labelFont);
                statutLabel.setForeground(
                        "validée".equalsIgnoreCase(statut) ? new Color(50, 150, 50) :
                                "refusée".equalsIgnoreCase(statut) ? new Color(200, 50, 50) : Color.BLACK);

                card.add(coursLabel);
                card.add(Box.createRigidArea(new Dimension(0, 5)));
                card.add(dateLabel);
                card.add(Box.createRigidArea(new Dimension(0, 5)));
                card.add(statutLabel);

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif: " + refus);
                    refusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    refusLabel.setForeground(new Color(150, 50, 50));
                    card.add(Box.createRigidArea(new Dimension(0, 5)));
                    card.add(refusLabel);
                }

                listeSeancesPanel.add(card);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceEnseignant().setVisible(true);
        });
    }
}


 */

/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

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

        setTitle("Espace Enseignant");
        setSize(700, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());

        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titre, BorderLayout.NORTH);

        // menu latéral
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(220, getHeight()));
        sidePanel.setBackground(new Color(70, 130, 180));

        JPanel logoButtonPanel = new JPanel();
        logoButtonPanel.setLayout(new BoxLayout(logoButtonPanel, BoxLayout.Y_AXIS));
        logoButtonPanel.setOpaque(false);
        logoButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            ImageIcon originalIcon = new ImageIcon("src/logo.PNG");
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoButtonPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée ", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        JButton btnAjouter = createMenuButton("Ajouter seance");
        JButton btnVoir = createMenuButton("Voir séances");
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        btnAjouter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoButtonPanel.add(Box.createVerticalStrut(10));
        logoButtonPanel.add(btnAjouter);
        logoButtonPanel.add(Box.createVerticalStrut(10));
        logoButtonPanel.add(btnVoir);
        logoButtonPanel.add(Box.createVerticalGlue());
        logoButtonPanel.add(btnDeconnexion);

        sidePanel.add(logoButtonPanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.WEST);

        //  partie centrale
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        JPanel welcomePanel = createWelcomePanel();
        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            try {
                chargerSeancesEnseignant();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
        cardLayout.show(contentPanel, "welcome"); // Affiche l'accueil au démarrage
    }

    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(70, 130, 180));
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        coursCombo = new JComboBox<>();
        coursCombo.setPreferredSize(new Dimension(200 , 40));
        panel.add(new JLabel("Cours :"), gbc);
        gbc.gridx = 1;
        panel.add(coursCombo, gbc);

        dateField = new JTextField("2025-04-05", 18);
        dateField.setPreferredSize(new Dimension(200 , 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Date (AAAA-MM-JJ) :"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        heureDebutField = new JTextField("08:00", 18);
        heureDebutField.setPreferredSize(new Dimension(200 , 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure début (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10:00", 18);
        heureFinField.setPreferredSize(new Dimension(200 , 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure fin (HH:MM) :"), gbc);
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
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this::ajouterSeance);
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
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

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");
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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() throws SQLException {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {
            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                card.setBackground(Color.WHITE);

                JLabel coursLabel = new JLabel("Cours: " + cours);
                JLabel dateLabel = new JLabel("Date: " + date + " (" + debut + " - " + fin + ")");
                JLabel statutLabel = new JLabel("Statut: " + statut);

                statutLabel.setForeground(
                        "validée".equalsIgnoreCase(statut) ? new Color(50, 150, 50) :
                                "refusée".equalsIgnoreCase(statut) ? new Color(200, 50, 50) : Color.BLACK);

                card.add(coursLabel);
                card.add(dateLabel);
                card.add(statutLabel);

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif: " + refus);
                    refusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    refusLabel.setForeground(new Color(150, 50, 50));
                    card.add(refusLabel);
                }

                listeSeancesPanel.add(card);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceEnseignant(1).setVisible(true));
    }
}


 */



/*
package interfaces;

import javax.swing.*;
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

        setTitle("Espace Enseignant");
        setSize(700, 600);
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuration du panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Titre
        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Panel latéral
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(220, getHeight()));
        sidePanel.setBackground(new Color(70, 130, 180));

        JPanel logoButtonPanel = new JPanel();
        logoButtonPanel.setLayout(new BoxLayout(logoButtonPanel, BoxLayout.Y_AXIS));
        logoButtonPanel.setOpaque(false);
        logoButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
           // ImageIcon originalIcon = new ImageIcon("src/interfaces/UIDT.PNG");
            //ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("UIDT.PNG")));
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));

            Image scaledImage = originalIcon.getImage().getScaledInstance(140, 130, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoButtonPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Boutons avec nouvelles couleurs
        JButton btnAjouter = createMenuButton("Ajouter séance", new Color(50, 100, 150));
        JButton btnVoir = createMenuButton("Voir séances", new Color(50, 100, 150));
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        btnAjouter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoButtonPanel.add(Box.createVerticalStrut(10));
        logoButtonPanel.add(btnAjouter);
        logoButtonPanel.add(Box.createVerticalStrut(10));
        logoButtonPanel.add(btnVoir);
        logoButtonPanel.add(Box.createVerticalGlue());
        logoButtonPanel.add(btnDeconnexion);

        sidePanel.add(logoButtonPanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Panel central avec CardLayout
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        // Création des différents panels
        JPanel welcomePanel = createWelcomePanel();
        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Gestion des événements
        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            try {
                chargerSeancesEnseignant();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
        cardLayout.show(contentPanel, "welcome");
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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

        heureDebutField = new JTextField("08:00", 18);
        heureDebutField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure début (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10:00", 18);
        heureFinField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure fin (HH:MM) :"), gbc);
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
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);

        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(70, 130, 180).brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(70, 130, 180));
            }
        });
        addButton.addActionListener(this::ajouterSeance);
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
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

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");
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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() throws SQLException {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {
            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                card.setBackground(Color.WHITE);

                JLabel coursLabel = new JLabel("Cours: " + cours);
                JLabel dateLabel = new JLabel("Date: " + date + " (" + debut + " - " + fin + ")");
                JLabel statutLabel = new JLabel("Statut: " + statut);

                statutLabel.setForeground(
                        "validée".equalsIgnoreCase(statut) ? new Color(50, 150, 50) :
                                "refusée".equalsIgnoreCase(statut) ? new Color(200, 50, 50) : Color.BLACK);

                card.add(coursLabel);
                card.add(dateLabel);
                card.add(statutLabel);

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif: " + refus);
                    refusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    refusLabel.setForeground(new Color(150, 50, 50));
                    card.add(refusLabel);
                }

                listeSeancesPanel.add(card);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new InterfaceEnseignant(1).setVisible(true));
    }
}



 */



/*

package interfaces;

import javax.swing.*;
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

        setTitle("Espace Enseignant");
        setSize(700, 600);
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Titre
        JLabel titre = new JLabel("Espace Enseignant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Panel latéral
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(220, getHeight()));
        sidePanel.setBackground(new Color(70, 130, 180));

        JPanel logoButtonPanel = new JPanel();
        logoButtonPanel.setLayout(new BoxLayout(logoButtonPanel, BoxLayout.Y_AXIS));
        logoButtonPanel.setOpaque(false);
        logoButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
            Image scaledImage = originalIcon.getImage().getScaledInstance(140, 130, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoButtonPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Boutons
        JButton btnAjouter = createMenuButton("Ajouter séance", new Color(50, 100, 150));
        JButton btnVoir = createMenuButton("Voir séances", new Color(50, 100, 150));
        JButton btnDeconnexion = createMenuButton("Déconnexion", new Color(220, 80, 60));

        btnAjouter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoButtonPanel.add(Box.createVerticalStrut(10));
        logoButtonPanel.add(btnAjouter);
        logoButtonPanel.add(Box.createVerticalStrut(10));
        logoButtonPanel.add(btnVoir);
        logoButtonPanel.add(Box.createVerticalGlue());
        logoButtonPanel.add(btnDeconnexion);

        sidePanel.add(logoButtonPanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Panel central
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        // Création des panels
        JPanel welcomePanel = createWelcomePanel();
        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Gestion des événements
        btnAjouter.addActionListener(e -> {
            chargerCours();
            cardLayout.show(contentPanel, "add");
        });

        btnVoir.addActionListener(e -> {
            try {
                chargerSeancesEnseignant();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cardLayout.show(contentPanel, "view");
        });

        btnDeconnexion.addActionListener(e -> confirmAndExit());

        setContentPane(mainPanel);
        cardLayout.show(contentPanel, "welcome");
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

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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

        heureDebutField = new JTextField("08:00", 18);
        heureDebutField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure début (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10:00", 18);
        heureFinField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure fin (HH:MM) :"), gbc);
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
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);

        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(70, 130, 180).brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(70, 130, 180));
            }
        });
        addButton.addActionListener(this::ajouterSeance);
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addButton, gbc);

        return panel;
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

    private void chargerCours() {
        coursCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");
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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() throws SQLException {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {
            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                card.setBackground(Color.WHITE);

                JLabel coursLabel = new JLabel("Cours: " + cours);
                JLabel dateLabel = new JLabel("Date: " + date + " (" + debut + " - " + fin + ")");
                JLabel statutLabel = new JLabel("Statut: " + statut);

                statutLabel.setForeground(
                        "validée".equalsIgnoreCase(statut) ? new Color(50, 150, 50) :
                                "refusée".equalsIgnoreCase(statut) ? new Color(200, 50, 50) : Color.BLACK);

                card.add(coursLabel);
                card.add(dateLabel);
                card.add(statutLabel);

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif: " + refus);
                    refusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    refusLabel.setForeground(new Color(150, 50, 50));
                    card.add(refusLabel);
                }

                listeSeancesPanel.add(card);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new InterfaceEnseignant(1).setVisible(true));
    }
}

 */

package interfaces;

import javax.swing.*;
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
        setSize(700, 600);
        setSize(new Dimension(900, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
    }

    private void initComponents() {
        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header avec bande grise
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Sidebar bleue
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Création des panels
        JPanel welcomePanel = createWelcomePanel();
        JPanel addPanel = createAddPanel();
        JPanel viewPanel = createViewPanel();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        // Panel principal pour la bande grise
        JPanel greyBandPanel = new JPanel(new BorderLayout());
        greyBandPanel.setBackground(new Color(240, 240, 240));
        greyBandPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        greyBandPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Titre centré
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
            JOptionPane.showMessageDialog(this, "Image non trouvée", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(logoPanel);
        navPanel.add(Box.createVerticalStrut(20));

        // Boutons de navigation (style identique à ChefDept)
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

        // Effet de survol
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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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

        heureDebutField = new JTextField("08:00", 18);
        heureDebutField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure début (HH:MM) :"), gbc);
        gbc.gridx = 1;
        panel.add(heureDebutField, gbc);

        heureFinField = new JTextField("10:00", 18);
        heureFinField.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heure fin (HH:MM) :"), gbc);
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
             PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM cours WHERE enseignant_id = ?")) {
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
            PreparedStatement coursStmt = conn.prepareStatement("SELECT id FROM cours WHERE nom = ? AND enseignant_id = ?");
            coursStmt.setString(1, coursNom);
            coursStmt.setInt(2, enseignantId);
            ResultSet rs = coursStmt.executeQuery();

            if (rs.next()) {
                int coursId = rs.getInt("id");

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO seance (cours_id, enseignant_id, date_seance, heure_debut, heure_fin, contenu, statut) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'non validée')");
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
                JOptionPane.showMessageDialog(this, "Cours non trouvé.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur ajout séance : " + ex.getMessage());
        }
    }

    private void chargerSeancesEnseignant() throws SQLException {
        listeSeancesPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, c.nom AS cours, s.date_seance, s.heure_debut, s.heure_fin, s.statut, s.commentaire_refus " +
                             "FROM seance s JOIN cours c ON s.cours_id = c.id " +
                             "WHERE s.enseignant_id = ? ORDER BY s.date_seance DESC")) {
            stmt.setInt(1, enseignantId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String cours = result.getString("cours");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");
                String refus = result.getString("commentaire_refus");

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                card.setBackground(Color.WHITE);

                JLabel coursLabel = new JLabel("Cours: " + cours);
                JLabel dateLabel = new JLabel("Date: " + date + " (" + debut + " - " + fin + ")");
                JLabel statutLabel = new JLabel("Statut: " + statut);

                statutLabel.setForeground(
                        "validée".equalsIgnoreCase(statut) ? new Color(50, 150, 50) :
                                "refusée".equalsIgnoreCase(statut) ? new Color(200, 50, 50) : Color.BLACK);

                card.add(coursLabel);
                card.add(dateLabel);
                card.add(statutLabel);

                if ("refusée".equalsIgnoreCase(statut) && refus != null) {
                    JLabel refusLabel = new JLabel("Motif: " + refus);
                    refusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    refusLabel.setForeground(new Color(150, 50, 50));
                    card.add(refusLabel);
                }

                listeSeancesPanel.add(card);
                listeSeancesPanel.add(Box.createVerticalStrut(10));
            }

            listeSeancesPanel.revalidate();
            listeSeancesPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new InterfaceEnseignant(1).setVisible(true));
    }
}
