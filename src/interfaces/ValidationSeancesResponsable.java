
package interfaces;

import DBO.SeanceDAO;
import models.Seance;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ValidationSeancesResponsable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public ValidationSeancesResponsable() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // En-tête

        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel titre = new JLabel("Gestion des séances", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(titre, BorderLayout.CENTER);

        // Barre de recherche

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> filterTable());
        searchPanel.add(new JLabel("Filtrer: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Table et modèle

        model = new DefaultTableModel(new String[]{
                "ID", "Cours", "Date", "Heure", "Contenu", "Commentaire", "Statut", "Actions"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 7;
            }
        };

        table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Centrage et rendu personnalisé pour la colonne Statut

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 6) { // Toutes les colonnes sauf Statut
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Rendu personnalisé pour la colonne Statut

        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);

                if ("Validée".equals(value)) {
                    c.setForeground(new Color(0, 128, 0));
                } else if ("Refusée".equals(value)) {
                    c.setForeground(new Color(200, 0, 0));
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Rendu et édition de la colonne "Actions"

        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }

    private void validerSeance(int row) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer la validation de cette séance ?",
                "Confirmation de validation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int idSeance = (int) model.getValueAt(row, 0);
            boolean ok = SeanceDAO.mettreAJourStatut(idSeance, "validee", null);
            if (ok) {
                model.setValueAt("Validée", row, 6);
                model.setValueAt("", row, 5);
                JOptionPane.showMessageDialog(this, "Séance validée avec succès !");
                // Désactive les boutons pour cette ligne apres validation
                table.getColumnModel().getColumn(7).setCellRenderer(new DisabledButtonRenderer());
            }
        }
    }

    private void refuserSeance(int row) {
        String commentaire = (String) model.getValueAt(row, 5);
        if (commentaire == null || commentaire.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez saisir un commentaire expliquant le refus.",
                    "Commentaire requis",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer le refus de cette séance ?",
                "Confirmation de refus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int idSeance = (int) model.getValueAt(row, 0);
            boolean ok = SeanceDAO.mettreAJourStatut(idSeance, "refusee", commentaire);
            if (ok) {
                model.setValueAt("Refusée", row, 6);
                JOptionPane.showMessageDialog(this, "Séance refusée avec succès !");
                // Désactive les boutons pour cette ligne apres validation
                table.getColumnModel().getColumn(7).setCellRenderer(new DisabledButtonRenderer());
            }
        }
    }

    public void chargerSeances() {
        model.setRowCount(0);
        List<Seance> seances = SeanceDAO.getToutesLesSeances();

        for (Seance s : seances) {
            String statut = "";
            if ("validee".equals(s.getStatut())) {
                statut = "Validée";
            } else if ("refusee".equals(s.getStatut())) {
                statut = "Refusée";
            } else {
                statut = "En attente";
            }

            model.addRow(new Object[]{
                    s.getId(),
                    s.getNomCours(),
                    s.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    s.getHeureDebut().format(DateTimeFormatter.ofPattern("HH:mm")),
                    s.getContenu(),
                    s.getCommentaireRefus() != null ? s.getCommentaireRefus() : "",
                    statut,

            });
        }
    }
    private void filterTable() {
        String query = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        if (query.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }

    // Classes pour les boutons

    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton validerBtn = new JButton("Valider");
        private final JButton refuserBtn = new JButton("Refuser");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            styleButton(validerBtn, new Color(46, 125, 50));
            styleButton(refuserBtn, new Color(198, 40, 40));
            add(validerBtn);
            add(refuserBtn);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            String statut = (String) table.getModel().getValueAt(row, 6);
            validerBtn.setEnabled(!"Validée".equals(statut) && !"Refusée".equals(statut));
            refuserBtn.setEnabled(!"Validée".equals(statut) && !"Refusée".equals(statut));
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton validerBtn = new JButton("Valider");
        private final JButton refuserBtn = new JButton("Refuser");

        public ButtonEditor() {
            styleButton(validerBtn, new Color(46, 125, 50));
            styleButton(refuserBtn, new Color(198, 40, 40));

            validerBtn.addActionListener(e -> {
                int row = table.getEditingRow();
                validerSeance(row);
                fireEditingStopped();
            });

            refuserBtn.addActionListener(e -> {
                int row = table.getEditingRow();
                refuserSeance(row);
                fireEditingStopped();
            });

            panel.setBackground(Color.WHITE);
            panel.add(validerBtn);
            panel.add(refuserBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
               boolean isSelected, int row, int column) {
            String statut = (String) table.getModel().getValueAt(row, 6);
            validerBtn.setEnabled(!"Validée".equals(statut) && !"Refusée".equals(statut));
            refuserBtn.setEnabled(!"Validée".equals(statut) && !"Refusée".equals(statut));
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // Renderer pour désactiver les boutons après validation/refus

    private class DisabledButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton validerBtn = new JButton("Valider");
        private final JButton refuserBtn = new JButton("Refuser");

        public DisabledButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            validerBtn.setEnabled(false);
            refuserBtn.setEnabled(false);
            validerBtn.setBackground(Color.LIGHT_GRAY);
            refuserBtn.setBackground(Color.LIGHT_GRAY);
            add(validerBtn);
            add(refuserBtn);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }
}


