/*
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class VoirSeancesTousEnseignants extends JPanel {
    private JTable tableSeances;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> filterCombo;
    private JTextField searchField;
    private Map<String, Object> appState;

    public VoirSeancesTousEnseignants(Map<String, Object> appState, boolean b) {
        this.appState = appState;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initializeModel();
        initializeTable();
        initializeControls();
        chargerDonnees();
    }

    private void initializeModel() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Seule la colonne Actions est éditable
            }
        };

        model.addColumn("ID");
        model.addColumn("Enseignant");
        model.addColumn("Cours");
        model.addColumn("Date");
        model.addColumn("Heure");
        model.addColumn("Contenu");
        model.addColumn("Statut");
        model.addColumn("Commentaire");
        model.addColumn("Actions");
    }

    private void initializeTable() {
        tableSeances = new JTable(model);
        tableSeances.setFont(new Font("Arial", Font.PLAIN, 14));
        tableSeances.setRowHeight(25);
        tableSeances.setAutoCreateRowSorter(true);
        tableSeances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sorter = new TableRowSorter<>(model);
        tableSeances.setRowSorter(sorter);
        tableSeances.removeColumn(tableSeances.getColumnModel().getColumn(0));

        TableColumn actionsColumn = tableSeances.getColumnModel().getColumn(7);
        actionsColumn.setCellRenderer(new ButtonRenderer());
        actionsColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void initializeControls() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));

        // Panel de filtres
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterCombo = new JComboBox<>(new String[]{"Toutes", "Validées", "En attente", "Refusées"});
        filterCombo.addActionListener(e -> applyFilters());

        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.addActionListener(e -> chargerDonnees());

        filterPanel.add(new JLabel("Filtrer:"));
        filterPanel.add(filterCombo);
        filterPanel.add(refreshBtn);

        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Rechercher");
        searchBtn.addActionListener(e -> applyFilters());

        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Panel d'actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton validerBtn = createActionButton("Valider", Color.GREEN, e -> validerSeances());
        JButton refuserBtn = createActionButton("Refuser", Color.RED, e -> refuserSeances());
        JButton exportBtn = createActionButton("Exporter", Color.BLUE, e -> exporterCSV());

        actionPanel.add(validerBtn);
        actionPanel.add(refuserBtn);
        actionPanel.add(exportBtn);

        controlPanel.add(filterPanel, BorderLayout.WEST);
        controlPanel.add(searchPanel, BorderLayout.CENTER);
        controlPanel.add(actionPanel, BorderLayout.EAST);

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(tableSeances), BorderLayout.CENTER);
    }

    private JButton createActionButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(action);
        return button;
    }

    public void chargerDonnees() {
        model.setRowCount(0);

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT s.id, u.login AS enseignant, c.nom AS cours, " +
                             "s.dateseance AS date_seance, s.heure_debut, s.heure_fin, " +
                             "s.contenu, s.statut, s.commentaire_refus AS commentaire " +
                             "FROM seance s " +
                             "JOIN utilisateurs u ON s.enseignant_id = u.id " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "ORDER BY s.dateseance DESC, s.heure_debut DESC")) {

            ResultSet rs = ps.executeQuery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("enseignant"),
                        rs.getString("cours"),
                        dateFormat.format(rs.getDate("date_seance")),
                        rs.getString("heure_debut") + " - " + rs.getString("heure_fin"),
                        rs.getString("contenu"),
                        rs.getString("statut"),
                        rs.getString("commentaire"),
                        "Actions"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String filter = (String) filterCombo.getSelectedItem();
        String searchText = searchField.getText().trim().toLowerCase();

        ArrayList<Object> filters = new ArrayList<>();

        if (!filter.equals("Toutes")) {
            filters.add(RowFilter.regexFilter(filter.substring(0, filter.length()-1), 6));
        }

        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchText));
        }

      //  sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter( ));
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            editorComponent = new JButton();
            ((JButton)editorComponent).setOpaque(true);
            ((JButton)editorComponent).addActionListener(e -> {
                fireEditingStopped();
                showActionMenu(currentRow);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            ((JButton)editorComponent).setText(label);
            currentRow = row;
            return editorComponent;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }

    private void showActionMenu(int row) {
        int modelRow = tableSeances.convertRowIndexToModel(row);
        int idSeance = (int) model.getValueAt(modelRow, 0);

        JPopupMenu popup = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Modifier");
        JMenuItem deleteItem = new JMenuItem("Supprimer");

        editItem.addActionListener(e -> modifierSeance(idSeance));
        deleteItem.addActionListener(e -> supprimerSeance(idSeance, modelRow));

        popup.add(editItem);
        popup.add(deleteItem);
        popup.show(tableSeances,
                tableSeances.getCellRect(row, 7, true).x,
                tableSeances.getCellRect(row, 7, true).y);
    }

    private void modifierSeance(int idSeance) {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM seance WHERE id = ?")) {

            ps.setInt(1, idSeance);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JPanel editPanel = new JPanel(new GridLayout(0, 2, 10, 10));

                JTextField dateField = new JTextField(rs.getDate("dateseance").toString());
                JTextField heureDebutField = new JTextField(rs.getString("heure_debut"));
                JTextField heureFinField = new JTextField(rs.getString("heure_fin"));
                JTextArea contenuArea = new JTextArea(rs.getString("contenu"));
                JComboBox<String> statutCombo = new JComboBox<>(
                        new String[]{"validé", "en attente", "refusé"});
                statutCombo.setSelectedItem(rs.getString("statut"));
                JTextField commentaireField = new JTextField(rs.getString("commentaire_refus"));

                editPanel.add(new JLabel("Date (AAAA-MM-JJ):"));
                editPanel.add(dateField);
                editPanel.add(new JLabel("Heure début (HH:MM):"));
                editPanel.add(heureDebutField);
                editPanel.add(new JLabel("Heure fin (HH:MM):"));
                editPanel.add(heureFinField);
                editPanel.add(new JLabel("Contenu:"));
                editPanel.add(new JScrollPane(contenuArea));
                editPanel.add(new JLabel("Statut:"));
                editPanel.add(statutCombo);
                editPanel.add(new JLabel("Commentaire:"));
                editPanel.add(commentaireField);

                int result = JOptionPane.showConfirmDialog(this, editPanel,
                        "Modifier la séance", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try (PreparedStatement updatePs = conn.prepareStatement(
                            "UPDATE seance SET dateseance=?, heure_debut=?, heure_fin=?, " +
                                    "contenu=?, statut=?, commentaire_refus=? WHERE id=?")) {

                        updatePs.setDate(1, Date.valueOf(dateField.getText()));
                        updatePs.setString(2, heureDebutField.getText());
                        updatePs.setString(3, heureFinField.getText());
                        updatePs.setString(4, contenuArea.getText());
                        updatePs.setString(5, (String) statutCombo.getSelectedItem());
                        updatePs.setString(6, commentaireField.getText());
                        updatePs.setInt(7, idSeance);

                        int rowsUpdated = updatePs.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(this,
                                    "Séance mise à jour avec succès",
                                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                            chargerDonnees();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Format de date ou heure invalide",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerSeance(int idSeance, int modelRow) {
        int response = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette séance?",
                "Confirmer suppression", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            try (Connection conn = DBconnect.getconnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM seance WHERE id = ?")) {

                ps.setInt(1, idSeance);
                int result = ps.executeUpdate();

                if (result > 0) {
                    model.removeRow(modelRow);
                    JOptionPane.showMessageDialog(this,
                            "Séance supprimée avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void validerSeances() {
        int[] rows = tableSeances.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner au moins une séance",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Valider " + rows.length + " séance(s) sélectionnée(s) ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            modifierStatutSeances(rows, "validé", null);
        }
    }

    private void refuserSeances() {
        int[] rows = tableSeances.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner au moins une séance",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String commentaire = JOptionPane.showInputDialog(this,
                "Veuillez saisir le motif de refus:",
                "Motif de refus", JOptionPane.QUESTION_MESSAGE);

        if (commentaire != null && !commentaire.trim().isEmpty()) {
            modifierStatutSeances(rows, "refusé", commentaire.trim());
        }
    }

    private void modifierStatutSeances(int[] viewRows, String statut, String commentaire) {
        try (Connection conn = DBconnect.getconnection()) {
            conn.setAutoCommit(false);
            int successCount = 0;

            for (int viewRow : viewRows) {
                int modelRow = tableSeances.convertRowIndexToModel(viewRow);
                int id = (int) model.getValueAt(modelRow, 0);

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE seance SET statut = ?, commentaire_refus = ? WHERE id = ?")) {
                    ps.setString(1, statut);
                    ps.setString(2, "validé".equals(statut) ? null : commentaire);
                    ps.setInt(3, id);

                    if (ps.executeUpdate() > 0) {
                        successCount++;
                    }
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(this,
                    successCount + " séance(s) mise(s) à jour avec succès",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            chargerDonnees();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la mise à jour: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exporterCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter en CSV");
        fileChooser.setSelectedFile(new File("seances_" + System.currentTimeMillis() + ".csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (PrintWriter writer = new PrintWriter(file)) {
                // En-têtes
                for (int i = 1; i < model.getColumnCount(); i++) {
                    writer.print("\"" + model.getColumnName(i) + "\"");
                    if (i < model.getColumnCount() - 1) writer.print(",");
                }
                writer.println();

                // Données
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 1; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        writer.print("\"" + (value != null ? value.toString().replace("\"", "\"\"") : "") + "\"");
                        if (j < model.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(this,
                        "Exportation réussie vers " + file.getName(),
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'export: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}



 */
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class VoirSeancesTousEnseignants extends JPanel {
    private JTable tableSeances;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> filterCombo;
    private JTextField searchField;
    private Map<String, Object> appState;

    public VoirSeancesTousEnseignants(Map<String, Object> appState, boolean b) {
        this.appState = appState;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initializeModel();
        initializeTable();
        initializeControls();
        chargerDonnees();
    }

    private void initializeModel() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("ID");
        model.addColumn("Enseignant");
        model.addColumn("Cours");
        model.addColumn("Date");
        model.addColumn("Heure");
        model.addColumn("Contenu");
        model.addColumn("Statut");
        model.addColumn("Commentaire");
    }

    private void initializeTable() {
        tableSeances = new JTable(model);
        tableSeances.setFont(new Font("Arial", Font.PLAIN, 14));
        tableSeances.setRowHeight(25);
        tableSeances.setAutoCreateRowSorter(true);
        tableSeances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sorter = new TableRowSorter<>(model);
        tableSeances.setRowSorter(sorter);
        tableSeances.removeColumn(tableSeances.getColumnModel().getColumn(0)); // Cache la colonne ID
    }

    private void initializeControls() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));

        // Panel de filtres
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterCombo = new JComboBox<>(new String[]{"Toutes", "Validees", "En attente", "Refusees"});
        filterCombo.addActionListener(e -> applyFilters());

        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.addActionListener(e -> chargerDonnees());

        filterPanel.add(new JLabel("Filtrer:"));
        filterPanel.add(filterCombo);
        filterPanel.add(refreshBtn);

        // Panel d'actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton exportBtn = createActionButton("Exporter", Color.BLUE, e -> exporterCSV());
        actionPanel.add(exportBtn);

        controlPanel.add(filterPanel, BorderLayout.WEST);
        controlPanel.add(actionPanel, BorderLayout.EAST);

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(tableSeances), BorderLayout.CENTER);
    }

    private JButton createActionButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(action);
        return button;
    }

    public void chargerDonnees() {
        model.setRowCount(0);

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT s.id, u.login AS enseignant, c.nom AS cours, " +
                             "s.dateseance AS date_seance, s.heure_debut, s.heure_fin, " +
                             "s.contenu, s.statut, s.commentaire_refus AS commentaire " +
                             "FROM seance s " +
                             "JOIN utilisateur u ON s.enseignant_id = u.id " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "ORDER BY s.dateseance DESC, s.heure_debut DESC")) {

            ResultSet rs = ps.executeQuery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("enseignant"),
                        rs.getString("cours"),
                        dateFormat.format(rs.getDate("date_seance")),
                        rs.getString("heure_debut") + " - " + rs.getString("heure_fin"),
                        rs.getString("contenu"),
                        rs.getString("statut"),
                        rs.getString("commentaire")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String filter = (String) filterCombo.getSelectedItem();
        String searchText = searchField.getText().trim().toLowerCase();

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!filter.equals("Toutes")) {
            filters.add(RowFilter.regexFilter(filter.substring(0, filter.length()-1), 6)); // Filtre sur colonne Statut
        }

        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchText));
        }

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void exporterCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter en CSV");
        fileChooser.setSelectedFile(new File("seances_" + System.currentTimeMillis() + ".csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (PrintWriter writer = new PrintWriter(file)) {
                // En-têtes sans colonne ID
                for (int i = 1; i < model.getColumnCount(); i++) {
                    writer.print("\"" + model.getColumnName(i) + "\"");
                    if (i < model.getColumnCount() - 1) writer.print(",");
                }
                writer.println();

                // Données sans colonne ID
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 1; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        writer.print("\"" + (value != null ? value.toString().replace("\"", "\"\"") : "") + "\"");
                        if (j < model.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(this,
                        "Exportation réussie vers " + file.getName(),
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'export: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}