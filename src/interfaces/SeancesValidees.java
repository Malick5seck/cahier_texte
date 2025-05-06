package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class SeancesValidees extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;

    public SeancesValidees() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initUI();
        chargerSeancesValidees();
    }

    // Panel principal

    private void initUI() {

        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel titre = new JLabel("Seances validees", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(titre, BorderLayout.NORTH);

        // Panel de recherche

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        filterPanel.setBackground(Color.WHITE);

        // Filtre par cours

        filterComboBox = new JComboBox<>();
        filterComboBox.addItem("Toutes les cours");
        filterComboBox.addActionListener(e -> filterTable());

        // Barre de recherche

        searchField = new JTextField(20);
        searchField.addActionListener(e -> filterTable());

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> filterTable());

        filterPanel.add(new JLabel("Cours: "));
        filterPanel.add(filterComboBox);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Rechercher: "));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Modèle de table

        model = new DefaultTableModel(
                new Object[]{"ID", "Date", "Heure", "Cours", "Enseignant", "Contenu"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Configuration de la table

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                return c;
            }
        };

        // Style de la table

        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);

        // Centrer le texte dans les cellules

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 4 && i != 5) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Personnalisation de la colonne contenu

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                label.setToolTipText(value != null ? value.toString() : "");
                label.setText("Voir contenu");
                label.setForeground(Color.BLUE);
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return label;
            }
        });

        // Listener pour voir le contenu

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 5) {
                    String contenu = (String) model.getValueAt(row, 5);
                    JOptionPane.showMessageDialog(SeancesValidees.this,
                            contenu, "Contenu de la seance", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void chargerSeancesValidees() {
        model.setRowCount(0);
        filterComboBox.removeAllItems();
        filterComboBox.addItem("Toutes les cours");

        try (Connection conn = DBconnect.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT s.id, s.dateseance, s.heure_debut, m.nom AS cours, " +
                             "CONCAT(u.nom, ' ', u.prenom) AS enseignant, s.contenu " +
                             "FROM seance s " +
                             "JOIN cours m ON s.cours_id = m.id " +
                             "JOIN utilisateur u ON s.Enseignant_id = u.id " +
                             "WHERE s.statut = 'validee' " +
                             "ORDER BY s.dateseance DESC")) {

            while (rs.next()) {
                String cours = rs.getString("cours");
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDate("dateseance").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getTime("heure_debut").toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        cours,
                        rs.getString("enseignant"),
                        rs.getString("contenu")
                });

                if (!coursExistsInFilter(cours)) {
                    filterComboBox.addItem(cours);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des séances:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean coursExistsInFilter(String cours) {
        for (int i = 0; i < filterComboBox.getItemCount(); i++) {
            if (filterComboBox.getItemAt(i).equals(cours)) {
                return true;
            }
        }
        return false;
    }


    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String selectedcours = (String) filterComboBox.getSelectedItem();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                if (!"Toutes les cours".equals(selectedcours) &&
                        !selectedcours.equals(entry.getStringValue(3))) {
                    return false;
                }

                if (searchText.isEmpty()) return true;

                for (int i = 1; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                        return true;
                    }
                }
                return false;
            }
        };

        sorter.setRowFilter(filter);
    }


}