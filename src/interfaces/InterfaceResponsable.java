/*
package interfaces;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class InterfaceResponsable extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final ValidationSeancesResponsable validationPanel;
    private final SeancesValidees seancesValideesPanel;

    public InterfaceResponsable() {
        setTitle("Espace Responsable de Classe");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel titre = new JLabel("Espace Responsable de Classe", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titre, BorderLayout.NORTH);

        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBoutons.setBackground(new Color(70, 130, 180));

        // Logo
        JPanel logoButtonPanel = new JPanel();
        logoButtonPanel.setLayout(new BoxLayout(logoButtonPanel, BoxLayout.Y_AXIS));
        logoButtonPanel.setOpaque(false);
        logoButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            URL imageUrl = getClass().getResource("/logo.png");
            System.out.println("Image URL: " + imageUrl); // Pour débogage
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(imageUrl));
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoButtonPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée ", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        panelBoutons.add(logoButtonPanel);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 20)));

        // Boutons
        JButton btnValiderSeances = createStyledButton("Valider les séances en attente");
        JButton btnSeancesValidees = createStyledButton("Consulter les séances validées");
        JButton btnSeDeconnecter = createStyledButton("Se déconnecter", new Color(220, 80, 60));

        panelBoutons.add(btnValiderSeances);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 15)));
        panelBoutons.add(btnSeancesValidees);
        panelBoutons.add(Box.createVerticalGlue());
        panelBoutons.add(btnSeDeconnecter);

        mainPanel.add(panelBoutons, BorderLayout.WEST);

        // Panels du centre
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel welcomePanel = createWelcomePanel();
        validationPanel = new ValidationSeancesResponsable();
        seancesValideesPanel = new SeancesValidees();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(validationPanel, "validation");
        contentPanel.add(seancesValideesPanel, "validees");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Événements
        btnValiderSeances.addActionListener(e -> {
            validationPanel.chargerSeancesNonValidees();
            cardLayout.show(contentPanel, "validation");
        });

        btnSeancesValidees.addActionListener(e -> {
            seancesValideesPanel.chargerSeancesValidees();
            cardLayout.show(contentPanel, "validees");
        });

        btnSeDeconnecter.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(mainPanel);
        pack(); // ajuste la fenêtre
    }

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(50, 100, 150));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

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

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'espace Responsable de Classe", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceResponsable().setVisible(true);
        });
    }


}



 */


/*
package interfaces;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class InterfaceResponsable extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final ValidationSeancesResponsable validationPanel;
    private final SeancesValidees seancesValideesPanel;

    public InterfaceResponsable() {
        setTitle("Espace Responsable de Classe");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel titre = new JLabel("Espace Responsable de Classe", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titre, BorderLayout.NORTH);

        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBoutons.setBackground(new Color(70, 130, 180));

        // Logo
        JPanel logoButtonPanel = new JPanel();
        logoButtonPanel.setLayout(new BoxLayout(logoButtonPanel, BoxLayout.Y_AXIS));
        logoButtonPanel.setOpaque(false);
        logoButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            URL imageUrl = getClass().getResource("/logo.png");
            System.out.println("Image URL: " + imageUrl); // Pour débogage
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(imageUrl));
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoButtonPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée ", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        panelBoutons.add(logoButtonPanel);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 20)));

        // Boutons
        JButton btnValiderSeances = createStyledButton("Valider les séances en attente");
        JButton btnSeancesValidees = createStyledButton("Consulter les séances validées");
        JButton btnSeDeconnecter = createStyledButton("Se déconnecter", new Color(220, 80, 60));

        panelBoutons.add(btnValiderSeances);
        panelBoutons.add(Box.createRigidArea(new Dimension(0, 15)));
        panelBoutons.add(btnSeancesValidees);
        panelBoutons.add(Box.createVerticalGlue());
        panelBoutons.add(btnSeDeconnecter);

        mainPanel.add(panelBoutons, BorderLayout.WEST);

        // Panels du centre
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel welcomePanel = createWelcomePanel();
        validationPanel = new ValidationSeancesResponsable();
        seancesValideesPanel = new SeancesValidees();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(validationPanel, "validation");
        contentPanel.add(seancesValideesPanel, "validees");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Événements
        btnValiderSeances.addActionListener(e -> {
            validationPanel.chargerSeancesNonValidees();
            cardLayout.show(contentPanel, "validation");
        });

        btnSeancesValidees.addActionListener(e -> {
            seancesValideesPanel.chargerSeancesValidees();
            cardLayout.show(contentPanel, "validees");
        });

        btnSeDeconnecter.addActionListener(e -> confirmAndExit());

        setContentPane(mainPanel);
        pack();
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

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(50, 100, 150));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

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

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'espace Responsable de Classe", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceResponsable().setVisible(true);
        });
    }
}



 */

/*
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class InterfaceResponsable extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final ValidationSeancesResponsable validationPanel;
    private final SeancesValidees seancesValideesPanel;

    public InterfaceResponsable() {
        setTitle("Espace Responsable de Classe");
        setSize(700, 600);
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
      //  setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Titre
        JLabel titre = new JLabel("Espace Responsable de Classe", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titre.setOpaque(true);
        titre.setBackground(new Color(240, 240, 240));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(70, 130, 180)); // Bleu
        sidebar.setPreferredSize(new Dimension(300, Integer.MAX_VALUE)); // Largeur fixe

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            URL imageUrl = getClass().getResource("/logo.png");
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(imageUrl));
            Image scaledImage = originalIcon.getImage().getScaledInstance(140, 130, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image non trouvée", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Boutons
        JButton btnValiderSeances = createStyledButton("Validation des seances");
        JButton btnSeancesValidees = createStyledButton("Séances validées");
        JButton btnSeDeconnecter = createStyledButton("Se déconnecter", new Color(220, 80, 60));

        sidebar.add(btnValiderSeances);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnSeancesValidees);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnSeDeconnecter);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // Panel de contenu central et fond blanc
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE); // Fond blanc
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel welcomePanel = createWelcomePanel();
        validationPanel = new ValidationSeancesResponsable();
        seancesValideesPanel = new SeancesValidees();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(validationPanel, "validation");
        contentPanel.add(seancesValideesPanel, "validees");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Événements
        btnValiderSeances.addActionListener(e -> {
            validationPanel.chargerSeancesNonValidees();
            cardLayout.show(contentPanel, "validation");
        });

        btnSeancesValidees.addActionListener(e -> {
            seancesValideesPanel.chargerSeancesValidees();
            cardLayout.show(contentPanel, "validees");
        });

        btnSeDeconnecter.addActionListener(e -> confirmAndExit());

        setContentPane(mainPanel);
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

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(50, 100, 150));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

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

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'espace Responsable de Classe", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceResponsable().setVisible(true);
        });
    }
}

 */
package interfaces;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InterfaceResponsable extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final ValidationSeancesResponsable validationPanel;
    private final SeancesValidees seancesValideesPanel;

    public InterfaceResponsable() {
        setTitle("Espace Responsable de Classe");
        setSize(700, 600);
        setSize(new Dimension(950, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Sidebar bleue
        mainPanel.add(createNavigationPanel(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panels de contenu
        JPanel welcomePanel = createWelcomePanel();
        validationPanel = new ValidationSeancesResponsable();
        seancesValideesPanel = new SeancesValidees();

        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(validationPanel, "validation");
        contentPanel.add(seancesValideesPanel, "validees");

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
        JLabel titre = new JLabel("Espace Responsable de Classe", SwingConstants.CENTER);
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

        // Boutons de navigation
        JButton btnValidation = createNavButton("Validation des séances", "validation");
        JButton btnValidees = createNavButton("Séances validées", "validees");
        JButton btnDeconnexion = createNavButton("Déconnexion", "logout", new Color(220, 80, 60));

        navPanel.add(btnValidation);
        navPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        navPanel.add(btnValidees);
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
                cardLayout.show(contentPanel, panelName);
                if ("validation".equals(panelName)) {
                    validationPanel.chargerSeancesNonValidees();
                } else if ("validees".equals(panelName)) {
                    seancesValideesPanel.chargerSeancesValidees();
                }
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

        JLabel welcomeLabel = new JLabel("Bienvenue dans l'espace Responsable de Classe", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceResponsable().setVisible(true);
        });
    }
}