/*package interfaces;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfaceChef extends JFrame {

    private JTextField loginField, passwordField;
    private JComboBox<String> roleCombo;

    private JTextField coursField;
    private JComboBox<String> enseignantsCombo;

    private JPanel tableauSeances;

    public InterfaceChef() {
        setTitle("Espace Chef de Département");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // Onglet 1 : Ajouter utilisateur
        tabs.addTab("Ajouter utilisateur", panelAjoutUtilisateur());

        // Onglet 2 : Ajouter cours
        tabs.addTab("Ajouter cours", panelAjoutCours());

        // Onglet 3 : Vue globale cahier
        tabs.addTab("Cahier de texte", panelVueGlobale());

        add(tabs);
        setVisible(true);
    }
    JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setForeground(Color.RED);
        btnDeconnexion.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment vous déconnecter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Ferme la fenêtre actuelle
            // Tu peux aussi rouvrir une interface de connexion ici, si elle existe
            // new InterfaceConnexion().setVisible(true);
        }
    });




    private JPanel panelAjoutUtilisateur() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

      loginField = new JTextField();
        passwordField = new JTextField();
        roleCombo = new JComboBox<>(new String[]{"enseignant", "responsable"});

        panel.add(new JLabel("Prénom :"));
        panel.add(loginField);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(passwordField);
        panel.add(new JLabel("Rôle :"));
        panel.add(roleCombo);

        JButton ajouterBtn = new JButton("Ajouter");
        ajouterBtn.addActionListener(e -> ajouterUtilisateur());

        panel.add(new JLabel());
        panel.add(ajouterBtn);

        return panel;
    }

    private void ajouterUtilisateur() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Champs vides !");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO utilisateurs (nom,login, password, role) VALUES (?, ?, ?)")) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, role);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Utilisateur ajouté !");
            loginField.setText("");
            passwordField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private JPanel panelAjoutCours() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        coursField = new JTextField();
        enseignantsCombo = new JComboBox<>();

        chargerEnseignants();

        panel.add(new JLabel("Nom du cours :"));
        panel.add(coursField);
        panel.add(new JLabel("Assigner à :"));
        panel.add(enseignantsCombo);

        JButton ajouterBtn = new JButton("Ajouter cours");
        ajouterBtn.addActionListener(e -> ajouterCours());

        panel.add(new JLabel());
        panel.add(ajouterBtn);

        return panel;
    }

    private void chargerEnseignants() {
        enseignantsCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT id, login FROM utilisateurs WHERE role = 'enseignant'")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("login");
                enseignantsCombo.addItem(id + " - " + nom);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement enseignants : " + e.getMessage());
        }
    }

    private void ajouterCours() {
        String nomCours = coursField.getText().trim();
        if (nomCours.isEmpty() || enseignantsCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Champs vides !");
            return;
        }

        int enseignantId = Integer.parseInt(enseignantsCombo.getSelectedItem().toString().split(" - ")[0]);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cours (nom, enseignant_id) VALUES (?, ?)")) {

            stmt.setString(1, nomCours);
            stmt.setInt(2, enseignantId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cours ajouté et assigné !");
            coursField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private JPanel panelVueGlobale() {
        JPanel panel = new JPanel(new BorderLayout());
        tableauSeances = new JPanel();
        tableauSeances.setLayout(new BoxLayout(tableauSeances, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(tableauSeances);

        scroll.setBorder(BorderFactory.createTitledBorder("Toutes les séances"));

        JButton rechargerBtn = new JButton("Actualiser");
        rechargerBtn.addActionListener(e -> chargerToutesSeances());

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(rechargerBtn, BorderLayout.SOUTH);

        chargerToutesSeances();
        return panel;
    }

    private void chargerToutesSeances() {
        tableauSeances.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/teste", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.*, c.nom AS nom_cours, u.login AS enseignant " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "JOIN utilisateurs u ON s.enseignant_id = u.id " +
                             "ORDER BY s.date_seance DESC")) {

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                String cours = result.getString("nom_cours");
                String enseignant = result.getString("enseignant");
                String date = result.getString("date_seance");
                String debut = result.getString("heure_debut");
                String fin = result.getString("heure_fin");
                String statut = result.getString("statut");

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panel.setBackground(Color.WHITE);
                panel.setMaximumSize(new Dimension(800, 100));

                panel.add(new JLabel("Cours : " + cours));
                panel.add(new JLabel("Enseignant : " + enseignant));
                panel.add(new JLabel("Date : " + date + " (" + debut + " - " + fin + ")"));
                panel.add(new JLabel("Statut : " + statut));

                tableauSeances.add(panel);
                tableauSeances.add(Box.createVerticalStrut(10));
            }

            tableauSeances.revalidate();
            tableauSeances.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement séances : " + e.getMessage());
        }
    }
}

 */




