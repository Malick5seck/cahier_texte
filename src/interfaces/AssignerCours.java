
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class AssignerCours extends JPanel {
    private JComboBox<String> enseignantCombo;
    private JList<CoursItem> coursList;
    private DefaultListModel<CoursItem> listModel;
    private HashMap<String, Integer> enseignantsMap;
    private Map<String, Object> appState;
    private JPopupMenu contextMenu;

    public AssignerCours(Map<String, Object> appState) {
        this.appState = appState;
        enseignantsMap = new HashMap<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 15));

        enseignantCombo = new JComboBox<>();
        listModel = new DefaultListModel<>();
        coursList = new JList<>(listModel);
        coursList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Modifier assignation");
        JMenuItem deleteItem = new JMenuItem("Supprimer assignation");

        editItem.addActionListener(e -> modifierAssignation());
        deleteItem.addActionListener(e -> supprimerAssignation());

        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        coursList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) &&
                        coursList.locationToIndex(e.getPoint()) != -1) {
                    contextMenu.show(coursList, e.getX(), e.getY());
                }
            }
        });

        formPanel.add(new JLabel("Enseignant:"));
        formPanel.add(enseignantCombo);
        formPanel.add(new JLabel("Cours disponibles:"));
        formPanel.add(new JScrollPane(coursList));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAssigner = createActionButton("Assigner", Color.BLUE, e -> assignerCours());
        JButton btnRafraichir = createActionButton("Actualiser", Color.GRAY, e -> rafraichirDonnees());

        buttonPanel.add(btnAssigner);
        buttonPanel.add(btnRafraichir);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        rafraichirDonnees();
    }

    private void chargerEnseignants() {
        enseignantsMap.clear();
        try (Connection conn = DBconnect.getconnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT id, CONCAT(prenom, ' ', nom) AS nom_complet " +
                             "FROM utilisateur WHERE role = 'Enseignant'"
             )) {

            while (rs.next()) {
                enseignantsMap.put(rs.getString("nom_complet"), rs.getInt("id"));
            }
            enseignantCombo.removeAllItems();
            enseignantsMap.keySet().forEach(enseignantCombo::addItem);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement enseignants: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerCours() {
        listModel.clear();
        try (Connection conn = DBconnect.getconnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT c.id, c.nom, GROUP_CONCAT(u.login SEPARATOR ', ') AS enseignants " +
                             "FROM cours c LEFT JOIN enseignants_cours ec ON c.id = ec.id_cours " +
                             "LEFT JOIN utilisateur u ON ec.id_enseignant = u.id " +
                             "GROUP BY c.id, c.nom ORDER BY c.nom"
             )) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String enseignants = rs.getString("enseignants");
                listModel.addElement(new CoursItem(nom, id, enseignants));
            }
            coursList.setModel(listModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement cours: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierAssignation() {
        CoursItem selected = coursList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int idCours = selected.id;
        String coursNom = selected.nom;

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id_enseignant FROM enseignants_cours WHERE id_cours = ?")) {

            ps.setInt(1, idCours);
            ResultSet rs = ps.executeQuery();

            List<Integer> enseignantsAssignes = new ArrayList<>();
            while (rs.next()) {
                enseignantsAssignes.add(rs.getInt("id_enseignant"));
            }

            JPanel editPanel = new JPanel(new BorderLayout());

            JList<String> enseignantsList = new JList<>(
                    enseignantsMap.keySet().toArray(new String[0]));
            enseignantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            List<Integer> selectedIndices = new ArrayList<>();
            int index = 0;
            for (String enseignant : enseignantsMap.keySet()) {
                if (enseignantsAssignes.contains(enseignantsMap.get(enseignant))) {
                    selectedIndices.add(index);
                }
                index++;
            }

            int[] indicesArray = selectedIndices.stream().mapToInt(i -> i).toArray();
            enseignantsList.setSelectedIndices(indicesArray);

            editPanel.add(new JLabel("Sélectionnez les enseignants pour " + coursNom + ":"), BorderLayout.NORTH);
            editPanel.add(new JScrollPane(enseignantsList), BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(this, editPanel,
                    "Modifier assignation", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                updateAssignations(idCours, enseignantsList.getSelectedValuesList());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAssignations(int idCours, List<String> enseignantsSelectionnes) {
        try (Connection conn = DBconnect.getconnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deletePs = conn.prepareStatement(
                    "DELETE FROM enseignants_cours WHERE id_cours = ?")) {
                deletePs.setInt(1, idCours);
                deletePs.executeUpdate();
            }

            try (PreparedStatement insertPs = conn.prepareStatement(
                    "INSERT INTO enseignants_cours (id_enseignant, id_cours) VALUES (?, ?)")) {

                for (String enseignant : enseignantsSelectionnes) {
                    insertPs.setInt(1, enseignantsMap.get(enseignant));
                    insertPs.setInt(2, idCours);
                    insertPs.addBatch();
                }

                insertPs.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this,
                    "Assignations mises à jour avec succès",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            rafraichirDonnees();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la mise à jour: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerAssignation() {
        CoursItem selected = coursList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int idCours = selected.id;
        String coursNom = selected.nom;

        int response = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer toutes les assignations pour " + coursNom + "?",
                "Confirmer suppression", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            try (Connection conn = DBconnect.getconnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM enseignants_cours WHERE id_cours = ?")) {

                ps.setInt(1, idCours);
                int result = ps.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Assignations supprimées avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    rafraichirDonnees();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void rafraichirDonnees() {
        chargerEnseignants();
        chargerCours();
        updateUI();
    }

    private void assignerCours() {
        if (enseignantCombo.getSelectedIndex() == -1 || coursList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant et un cours",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String enseignant = (String) enseignantCombo.getSelectedItem();
        int idEnseignant = enseignantsMap.get(enseignant);
        CoursItem selectedCourse = coursList.getSelectedValue();
        int idCours = selectedCourse.id;

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement psCheck = conn.prepareStatement(
                     "SELECT 1 FROM enseignants_cours WHERE id_enseignant = ? AND id_cours = ?")) {

            psCheck.setInt(1, idEnseignant);
            psCheck.setInt(2, idCours);

            if (psCheck.executeQuery().next()) {
                JOptionPane.showMessageDialog(this,
                        "Cet enseignant est déjà assigné à ce cours",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try (PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO enseignants_cours (id_enseignant, id_cours) VALUES (?, ?)")) {
                psInsert.setInt(1, idEnseignant);
                psInsert.setInt(2, idCours);
                psInsert.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Cours assigné avec succès à " + enseignant,
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                rafraichirDonnees();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'assignation: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createActionButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(action);
        return button;
    }

    // Classe interne représentant un cours
    private static class CoursItem {
        String nom;
        int id;
        String enseignants;

        public CoursItem(String nom, int id, String enseignants) {
            this.nom = nom;
            this.id = id;
            this.enseignants = enseignants;
        }

        @Override
        public String toString() {
            return nom + " (Enseignants: " + (enseignants != null ? enseignants : "Aucun") + ")";
        }
    }
}
