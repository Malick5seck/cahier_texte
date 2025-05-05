
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

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
        tableSeances.removeColumn(tableSeances.getColumnModel().getColumn(0)); // Cache colonne ID
    }

    private void initializeControls() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));

        // Panel de filtres
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterCombo = new JComboBox<>(new String[]{"Toutes", "Validees", "En attente", "Refusees"});
        filterCombo.addActionListener(e -> applyFilters());

        JButton refreshBtn = createActionButton("Actualiser", Color.GRAY, e -> chargerDonnees());

        filterPanel.add(new JLabel("Filtrer:"));
        filterPanel.add(filterCombo);
        filterPanel.add(refreshBtn);

        // Panel d'actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton exportBtn = createActionButton("Exporter", Color.BLUE, e -> exporterPDF());
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
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.addActionListener(action);

        // Effet de survol
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

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
                             "JOIN utilisateur u ON s.Enseignant_id = u.id " +
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
        if (sorter == null) return;

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!filter.equals("Toutes")) {
            filters.add(RowFilter.regexFilter(filter.substring(0, filter.length() - 1), 6));
        }

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void exporterPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter en PDF");
        fileChooser.setSelectedFile(new File("seances_" + System.currentTimeMillis() + ".pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                Paragraph title = new Paragraph("RAPPORT DES SEANCES DES ENSEIGNANTS ")
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(16)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20);
                document.add(title);

                int nbColonnes = model.getColumnCount() - 1;
                Table table = new Table(UnitValue.createPercentArray(nbColonnes)).useAllAvailableWidth();

                // En-têtes
                for (int i = 1; i < model.getColumnCount(); i++) {
                    table.addHeaderCell(new Cell().add(new Paragraph(model.getColumnName(i)).setBold()));
                }

                // Données
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 1; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        table.addCell(new Cell().add(new Paragraph(value != null ? value.toString() : "")));
                    }
                }

                document.add(table);
                document.close();

                JOptionPane.showMessageDialog(this, "Exportation PDF réussie : " + file.getName(),
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur PDF : " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}



