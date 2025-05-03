/*
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



 */
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
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("Assignation des Cours");
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
        enseignantCombo.setBackground(Color.WHITE);
        enseignantCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        listModel = new DefaultListModel<>();
        coursList = new JList<>(listModel);
        coursList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursList.setBackground(Color.WHITE);
        coursList.setFixedCellHeight(60);
        coursList.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        coursList.setCellRenderer(new CoursListRenderer());

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setBackground(new Color(240, 240, 240));
        JLabel filterLabel = new JLabel("Filtrer :");
        JRadioButton allFilter = new JRadioButton("Tous", true);
        JRadioButton assignedFilter = new JRadioButton("Assignés");
        JRadioButton unassignedFilter = new JRadioButton("Non assignés");

        ButtonGroup filterGroup = new ButtonGroup();
        filterGroup.add(allFilter);
        filterGroup.add(assignedFilter);
        filterGroup.add(unassignedFilter);

        filterPanel.add(filterLabel);
        filterPanel.add(allFilter);
        filterPanel.add(assignedFilter);
        filterPanel.add(unassignedFilter);

        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Enseignant:"), gbc);

        gbc.gridy = 1;
        formPanel.add(enseignantCombo, gbc);

        gbc.gridy = 2;
        formPanel.add(new JLabel("Liste des cours:"), gbc);

        gbc.gridy = 3;
        formPanel.add(filterPanel, gbc);

        gbc.gridy = 4;
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

        coursList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) &&
                        coursList.locationToIndex(e.getPoint()) != -1) {
                    contextMenu.show(coursList, e.getX(), e.getY());
                }
            }
        });

        // Add main components
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnAssigner.addActionListener(e -> assignerCours());
        btnRafraichir.addActionListener(e -> rafraichirDonnees());
        btnModifier.addActionListener(e -> modifierAssignation());
        btnSupprimer.addActionListener(e -> supprimerAssignation());


        allFilter.addActionListener(e -> filterCours());
        assignedFilter.addActionListener(e -> filterCours());
        unassignedFilter.addActionListener(e -> filterCours());

        rafraichirDonnees();
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
                     "SELECT c.id, c.nom, c.description, " +
                             "IFNULL(GROUP_CONCAT(DISTINCT CONCAT(u.prenom, ' ', u.nom) SEPARATOR ', '), 'Non assigné') AS enseignants " +
                             "FROM cours c " +
                             "LEFT JOIN enseignants_cours ec ON c.id = ec.id_cours " +
                             "LEFT JOIN utilisateur u ON ec.id_enseignant = u.id " +
                             "GROUP BY c.id, c.nom, c.description " +
                             "ORDER BY c.nom"
             )) {

            while (rs.next()) {
                listModel.addElement(new CoursItem(
                        rs.getString("nom"),
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("enseignants")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement cours: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterCours() {
        boolean showAssigned = true;
        boolean showUnassigned = true;

        // Determine filter from radio buttons
        Component[] components = ((JPanel) coursList.getParent().getParent()).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JRadioButton && ((JRadioButton) subComp).isSelected()) {
                        String text = ((JRadioButton) subComp).getText();
                        if (text.equals("Assignés")) {
                            showUnassigned = false;
                        } else if (text.equals("Non assignés")) {
                            showAssigned = false;
                        }
                    }
                }
            }
        }

        List<CoursItem> filteredList = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            CoursItem item = listModel.getElementAt(i);
            boolean matchesFilter = (showAssigned && !item.enseignants.equals("Non assigné")) ||
                    (showUnassigned && item.enseignants.equals("Non assigné"));

            if (matchesFilter) {
                filteredList.add(item);
            }
        }

        DefaultListModel<CoursItem> filteredModel = new DefaultListModel<>();
        filteredList.forEach(filteredModel::addElement);
        coursList.setModel(filteredModel);
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
    }

    private static class CoursItem {
        String nom;
        int id;
        String description;
        String enseignants;

        public CoursItem(String nom, int id, String description, String enseignants) {
            this.nom = nom;
            this.id = id;
            this.description = description;
            this.enseignants = enseignants;
        }
    }
    private static class CoursListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof CoursItem) {
                CoursItem item = (CoursItem) value;

                // Construction d'un texte simple multi-ligne sans HTML
                StringBuilder sb = new StringBuilder();
                sb.append("Cours : ").append(item.nom).append("\n");
                sb.append("Enseignants : ").append(item.enseignants).append("\n");
                sb.append("Description : ").append(
                        item.description != null ? item.description : "Pas de description");

                setText(sb.toString());
                setToolTipText(sb.toString()); // Utile si texte trop long

                if (isSelected) {
                    setBackground(new Color(220, 240, 255));
                    setBorder(BorderFactory.createLineBorder(new Color(180, 210, 255)));
                } else {
                    setBackground(item.enseignants.equals("Non assigné") ?
                            new Color(255, 240, 240) : Color.WHITE);
                }
            }
            return this;
        }
    }


}
