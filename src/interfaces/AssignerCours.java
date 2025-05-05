

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

    public AssignerCours(Map<String, Object> appState) {
        this.appState = appState;
        enseignantsMap = new HashMap<>();
        initUI();
        rafraichirDonnees();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("Assignation des Cours", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(60, 60, 60));
        headerPanel.add(title, BorderLayout.CENTER);

        // Main Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        enseignantCombo = new JComboBox<>();
        styleComboBox(enseignantCombo);

        listModel = new DefaultListModel<>();
        coursList = new JList<>(listModel);
        styleJList(coursList);

        // Police monospace pour l'alignement des colonnes
        Font monospaceFont = new Font(Font.MONOSPACED , Font.BOLD, 12);
        coursList.setFont(monospaceFont);

        // Renderer personnalisé avec alignement en colonnes
        coursList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof CoursItem) {
                    CoursItem item = (CoursItem) value;
                    setText(item.toFormattedString());

                    if (item.estAssigné) {
                        setForeground(new Color(0, 100, 0)); // Vert foncé pour les cours assignés
                        if (isSelected) {
                            setBackground(new Color(200, 255, 200));
                        }
                    } else {
                        setForeground(Color.BLACK);
                        if (isSelected) {
                            setBackground(new Color(0, 120, 215));
                            setForeground(Color.WHITE);
                        }
                    }
                }
                return c;
            }
        });

        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Enseignants:"), gbc);

        gbc.gridy = 1;
        formPanel.add(enseignantCombo, gbc);

        gbc.gridy = 2;
        formPanel.add(new JLabel("Liste des cours:"), gbc);

        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(coursList), gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton btnAssigner = createStyledButton("Assigner", new Color(0, 141, 255));
        JButton btnRafraichir = createStyledButton("Actualiser", new Color(0, 150, 50));
        JButton btnModifier = createStyledButton("Modifier", new Color(255, 165, 0));
        JButton btnSupprimer = createStyledButton("Supprimer", new Color(220, 53, 69));

        buttonPanel.add(btnAssigner);
        buttonPanel.add(btnRafraichir);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        // Add main components
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnAssigner.addActionListener(e -> assignerCours());
        btnRafraichir.addActionListener(e -> rafraichirDonnees());
        btnModifier.addActionListener(e -> modifierAssignation());
        btnSupprimer.addActionListener(e -> supprimerAssignation());
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void styleJList(JList<CoursItem> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBackground(Color.WHITE);
        list.setFixedCellHeight(30);
        list.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(7, 15, 7, 15)));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public void rafraichirDonnees() {
        SwingUtilities.invokeLater(() -> {
            chargerEnseignants();
            chargerCours();
        });
    }

    private void chargerEnseignants() {
        enseignantsMap.clear();
        try (Connection conn = DBconnect.getconnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT id, CONCAT(prenom, ' ', nom) AS nom_complet " +
                             "FROM utilisateur WHERE role = 'Enseignant' ORDER BY nom_complet")) {

            while (rs.next()) {
                enseignantsMap.put(rs.getString("nom_complet"), rs.getInt("id"));
            }
            enseignantCombo.removeAllItems();
            for (String nom : enseignantsMap.keySet()) {
                enseignantCombo.addItem(nom);
            }
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
                     "SELECT c.id, c.nom, c.description, " +
                             "CASE WHEN ec.cours_id IS NULL THEN 0 ELSE 1 END AS est_assigné " +
                             "FROM cours c " +
                             "LEFT JOIN enseignants_cours ec ON c.id = ec.cours_id " +
                             "GROUP BY c.id, c.nom, c.description " +
                             "ORDER BY c.nom")) {

            while (rs.next()) {
                listModel.addElement(new CoursItem(
                        rs.getString("nom"),
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getBoolean("est_assigné")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement cours: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
                     "SELECT 1 FROM enseignants_cours WHERE Enseignant_id = ? AND cours_id = ?")) {

            psCheck.setInt(1, idEnseignant);
            psCheck.setInt(2, idCours);

            if (psCheck.executeQuery().next()) {
                JOptionPane.showMessageDialog(this,
                        "Cet enseignant est déjà assigné à ce cours",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try (PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO enseignants_cours (Enseignant_id, cours_id) VALUES (?, ?)")) {
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
                     "SELECT Enseignant_id FROM enseignants_cours WHERE cours_id = ?")) {

            ps.setInt(1, idCours);
            ResultSet rs = ps.executeQuery();

            List<Integer> enseignantsAssignes = new ArrayList<>();
            while (rs.next()) {
                enseignantsAssignes.add(rs.getInt("Enseignant_id"));
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
                    "DELETE FROM enseignants_cours WHERE cours_id = ?")) {
                deletePs.setInt(1, idCours);
                deletePs.executeUpdate();
            }

            try (PreparedStatement insertPs = conn.prepareStatement(
                    "INSERT INTO enseignants_cours (Enseignant_id, cours_id) VALUES (?, ?)")) {

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
                         "DELETE FROM enseignants_cours WHERE cours_id = ?")) {

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

    private static class CoursItem {
        String nom;
        int id;
        String description;
        boolean estAssigné;

        public CoursItem(String nom, int id, String description, boolean estAssigné) {
            this.nom = nom;
            this.id = id;
            this.description = description;
            this.estAssigné = estAssigné;
        }

        public String toFormattedString() {
            String assignation = estAssigné ? "[ASSIGNÉ]" : "[LIBRE]  ";
            String desc = (description != null && !description.isEmpty())
                    ? description.length() > 40
                    ? description.substring(0, 37) + "..."
                    : description
                    : "Pas de description";

            return String.format("%-6d | %-25s | %-10s | %s",
                    id,
                    nom.length() > 25 ? nom.substring(0, 22) + "..." : nom,
                    assignation,
                    desc);
        }

        @Override
        public String toString() {
            return toFormattedString();
        }
    }
}