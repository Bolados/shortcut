/**
 *
 */
package dev.bscako.blaucher.graphics;

import dev.bscako.blaucher.configuration.ApplicationProperties;
import dev.bscako.blaucher.graphics.tools.GuiTools;
import dev.bscako.blaucher.graphics.tools.GuiTools.JDialogDragListener;
import dev.bscako.blaucher.models.Application;
import dev.bscako.blaucher.models.Category;
import dev.bscako.blaucher.models.repositories.CategoryRepository;
import org.jnativehook.GlobalScreen;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.springframework.stereotype.Component;
import sun.awt.shell.ShellFolder;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author BSCAKO
 *
 */

@Component
public class JDialogBLaucher extends JDialog {


    private static final long serialVersionUID = 7610974713033553255L;
    private static final int MAX_SHOWED_LINKS = 50;
    // for properly relauching frame
    private static JDialogBLaucher uniqueInstance;
    private final JPanel contentPanel = new JPanel();

    private JTabbedPane tabbedPane;

    /**
     * Create the frame.
     */

    private JDialogBLaucher(ApplicationProperties applicationProperties, CategoryRepository categoryRepository) {
        // this.initialize();
        this.initializeGUI(applicationProperties, categoryRepository);
    }

    public static JDialogBLaucher getNewInstance(ApplicationProperties applicationProperties,
                                                 CategoryRepository categoryRepository) {
        int tabbedPaneIndex = -1;
        if (JDialogBLaucher.uniqueInstance != null) {
            tabbedPaneIndex = uniqueInstance.tabbedPane.getSelectedIndex();
            uniqueInstance.dispose();
        }
        JDialogBLaucher.uniqueInstance = new JDialogBLaucher(applicationProperties, categoryRepository);
        if ((tabbedPaneIndex != -1) && (uniqueInstance.tabbedPane.getTabCount() > tabbedPaneIndex)) {
            uniqueInstance.tabbedPane.setSelectedIndex(tabbedPaneIndex);
        }
        return JDialogBLaucher.uniqueInstance;
    }

    protected void RunApplicationActionPerformed(ActionEvent e, Application application) {

        if (application == null) {
            return;
        }
        final String OS = System.getProperty("os.name").toLowerCase();

        try {
            if (OS.contains("windows")) {
                // Running the above command
                Runtime run = Runtime.getRuntime();
                run.exec(application.getExePath());
                System.out.println("RunIT " + application.getName());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    protected void addApplicationsComponents(JPanel panel, Category category, final int maxComponent) {
        for (Application application : category.getApplications()) {
            if ((application.getExePath() != null) && (!application.getExePath().isEmpty())) {
                try {
                    // Get metadata and create an icon
                    ShellFolder sf = ShellFolder.getShellFolder(new File(application.getExePath()));
                    Icon icon = new ImageIcon(sf.getIcon(true));
                    JButton btnNewButton = new JButton(application.getName(), icon);
                    btnNewButton.setEnabled(true);
                    btnNewButton.setToolTipText(application.getName());
                    btnNewButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                    btnNewButton.setHorizontalTextPosition(SwingConstants.CENTER);
                    btnNewButton.setBorderPainted(false);
                    btnNewButton.setMargin(new Insets(0, 0, 0, 0));
                    btnNewButton.addActionListener(
                            e -> JDialogBLaucher.this.RunApplicationActionPerformed(e, application));
                    panel.add(btnNewButton);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

        if (category.getApplications().size() < maxComponent) {
            for (int i = category.getApplications().size(); i < maxComponent; i++) {
                JButton btnNewButton = new JButton("");
                btnNewButton.setEnabled(false);
                panel.add(btnNewButton);
            }
        }
    }

    protected void addCategoriesComponents(JTabbedPane tabbedPane, CategoryRepository categoryRepository) {
        for (Category category : categoryRepository.findAll()) {
            JPanel panel = new JPanel();
            tabbedPane.addTab(category.getName(), null, panel, category.getName());
            panel.setName(category.getName());
            panel.setLayout(new GridLayout(0, 10, 15, 15));
        }
    }

    /**
     * @param e
     * @return
     */
    protected void tabbedPaneStateChanged(ChangeEvent e, CategoryRepository categoryRepository) {
        List<Category> categories = categoryRepository.findByName(JDialogBLaucher.this.tabbedPane.getTitleAt(JDialogBLaucher.this.tabbedPane.getSelectedIndex()));
        if ((categories != null) && (!categories.isEmpty()) && (categories.size() == 1)) {
            Category category = categories.get(categories.size() - 1);
            JPanel panel = (JPanel) JDialogBLaucher.this.tabbedPane
                    .getComponentAt(JDialogBLaucher.this.tabbedPane.getSelectedIndex());
            panel.removeAll();
            this.addApplicationsComponents(panel, category, MAX_SHOWED_LINKS);
            panel.revalidate();
            panel.repaint();
        }
    }

    protected void initializeGUI(ApplicationProperties applicationProperties, CategoryRepository categoryRepository) {
        // this.setUndecorated(true);
        // this.setOpacity(0.8F);
        // this.setResizable(false);
        this.setTitle(applicationProperties.getName());
        // Set the event dispatcher to a swing safe executor service.
        GlobalScreen.setEventDispatcher(new SwingDispatchService());
        JDialogDragListener jDialogDragListener = new JDialogDragListener(this);
        this.addMouseListener(jDialogDragListener);
        this.addMouseMotionListener(jDialogDragListener);
        GuiTools.setSize(this, 25);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new BorderLayout());
        this.contentPanel
                .setBorder(new TitledBorder(null, applicationProperties.getName(), TitledBorder.LEADING,
                        TitledBorder.TOP, null, null));
        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        this.contentPanel.setLayout(new GridLayout(0, 1, 0, 0));

        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        this.tabbedPane.addChangeListener(e -> this.tabbedPaneStateChanged(e, categoryRepository));
        GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup()
                        .addComponent(this.tabbedPane, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addContainerGap()));
        gl_contentPanel.setVerticalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup()
                        .addComponent(this.tabbedPane, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE).addContainerGap()));

        this.contentPanel.add(this.tabbedPane);
        this.addCategoriesComponents(this.tabbedPane, categoryRepository);
    }


    public void initialize() {

        // this.setUndecorated(true);

        // this.setOpacity(0.8F);

        this.setTitle("Shortcuts");
        // Set the event dispatcher to a swing safe executor service.
        GlobalScreen.setEventDispatcher(new SwingDispatchService());

        JDialogDragListener jDialogDragListener = new JDialogDragListener(this);
        this.addMouseListener(jDialogDragListener);
        this.addMouseMotionListener(jDialogDragListener);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        // this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBounds(100, 100, 450, 300);
        this.getContentPane().setLayout(new BorderLayout());
        this.contentPanel
                .setBorder(new TitledBorder(null, "Shortcuts", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        this.contentPanel.setLayout(new GridLayout(0, 1, 0, 0));

        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        this.tabbedPane.addChangeListener(
                e -> System.out.println("Tab: " + JDialogBLaucher.this.tabbedPane.getSelectedIndex()));

        JPanel panel = new JPanel();
        this.tabbedPane.addTab("Tab 1", null, panel, null);

        JButton btnNewButton = new JButton("");
        btnNewButton.addActionListener(e -> {
        });
        btnNewButton.setIcon(new ImageIcon(
                "C:\\Users\\BSCAKO\\Documents\\workspace-sts-3.8.3.RELEASE\\Shortcuts\\src\\main\\resources\\Images\\omt.gif"));

        JButton button = new JButton("");

        JButton button_1 = new JButton("");

        JButton button_2 = new JButton("");

        JButton button_3 = new JButton("");

        JButton button_4 = new JButton("");

        JButton button_5 = new JButton("");

        JButton button_6 = new JButton("");
        button_6.setIcon(new ImageIcon(
                "C:\\Users\\BSCAKO\\Documents\\workspace-sts-3.8.3.RELEASE\\Shortcuts\\src\\main\\resources\\Images\\icon32x32.png"));

        JButton button_7 = new JButton("");

        JButton button_8 = new JButton("");
        panel.setLayout(new GridLayout(0, 4, 15, 15));
        panel.add(btnNewButton);
        panel.add(button);
        panel.add(button_1);
        panel.add(button_2);
        panel.add(button_3);
        panel.add(button_5);
        panel.add(button_6);
        panel.add(button_7);
        panel.add(button_8);
        panel.add(button_4);

        JPanel panel_1 = new JPanel();
        this.tabbedPane.addTab("Tab 2", null, panel_1, null);
        this.contentPanel.add(this.tabbedPane);


    }

}
