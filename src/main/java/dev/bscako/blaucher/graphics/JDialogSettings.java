/**
 *
 */
package dev.bscako.blaucher.graphics;

import dev.bscako.blaucher.configuration.ApplicationProperties;
import dev.bscako.blaucher.graphics.tools.GuiTools;
import dev.bscako.blaucher.graphics.tools.GuiTools.JDialogDragListener;
import dev.bscako.blaucher.models.Application;
import dev.bscako.blaucher.models.Category;
import dev.bscako.blaucher.models.RunMode;
import dev.bscako.blaucher.models.services.ApplicationService;
import dev.bscako.blaucher.models.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.awt.shell.ShellFolder;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @author BSCAKO
 *
 */

@Component
public class JDialogSettings extends JDialog {

    private static final long serialVersionUID = -2574170182537181853L;
    private final JPanel contentPanel = new JPanel();
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ApplicationService applicationService;
    private JTextField textFieldTitle;
    private JTextField textFieldPath;
    private JTextField textFieldStartIn;
    private JList<Application> listApplications;
    private DefaultListModel<Application> listApplicationsModel = new DefaultListModel<>();
    private JList<Category> listCategories;
    private DefaultListModel<Category> listCategoriesModel = new DefaultListModel<>();
    private JComboBox<RunMode> comboBoxRunMode;
    private JMenuItem mntmEditCategory;
    private JMenuItem mntmRemoveCategory;
    private JMenuItem mntmAddCategory;
    private JMenuItem mntmAddShortcut;
    private JMenuItem mntmEditShortcut;
    private JMenuItem mntmRemoveShortcut;
    private JButton btnIcon;

    /**
     * Create the dialog.
     */
    @Autowired
    public JDialogSettings(ApplicationProperties applicationProperties) {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                JDialogSettings.this.updateCategories();
            }

        });
        // this.setUndecorated(true);
        JDialogDragListener jDialogDragListener = new JDialogDragListener(this);
        this.addMouseListener(jDialogDragListener);
        this.addMouseMotionListener(jDialogDragListener);
        GuiTools.setInterfaceDesign("Nimbus");
        this.initialize();

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setBounds(100, 100, 474, 586);
        this.getContentPane().setLayout(new BorderLayout());
        this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Informations", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Categories",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new TitledBorder(null, "Applications", TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(0, 0, 0)));
        GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_contentPanel.createSequentialGroup().addContainerGap()
                                .addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE).addGap(3))
                        .addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE).addGap(1))
        );
        gl_contentPanel.setVerticalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPanel.createSequentialGroup()
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                                        .addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
                                .addContainerGap()));

        this.listApplications = new JList<>(this.listApplicationsModel);

        JPopupMenu popupMenu_3 = new JPopupMenu();
        this.addPopupApplications(panel_3, popupMenu_3);
        this.mntmAddShortcut = new JMenuItem("Add");

        this.mntmAddShortcut.addActionListener(e -> JDialogSettings.this.addShortcutActionPerformed(e));
        popupMenu_3.add(this.mntmAddShortcut);

        GroupLayout gl_panel_3 = new GroupLayout(panel_3);
        gl_panel_3.setHorizontalGroup(
                gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
                        gl_panel_3.createSequentialGroup().addContainerGap()
                                .addComponent(this.listApplications, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                .addContainerGap()));
        gl_panel_3.setVerticalGroup(
                gl_panel_3.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_3.createSequentialGroup()
                                .addComponent(this.listApplications, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                                .addContainerGap())
        );

        JPopupMenu popupMenu_1 = new JPopupMenu();
        this.addPopupApplications(this.listApplications, popupMenu_1);

        this.mntmEditShortcut = new JMenuItem("Edit");
        this.mntmEditShortcut.addActionListener(e -> JDialogSettings.this.editShortcutActionPerformed(e));
        popupMenu_1.add(this.mntmEditShortcut);

        this.mntmRemoveShortcut = new JMenuItem("Remove");
        this.mntmRemoveShortcut.addActionListener(e -> {
            JDialogSettings.this.removeShortcutActionPerformed(e);
        });
        popupMenu_1.add(this.mntmRemoveShortcut);
        panel_3.setLayout(gl_panel_3);


        this.listCategories = new JList<>(this.listCategoriesModel);

        JPopupMenu popupMenu_2 = new JPopupMenu();
        this.addPopupCategories(panel_2, popupMenu_2);

        this.mntmAddCategory = new JMenuItem("Add");
        this.mntmAddCategory.addActionListener(e -> JDialogSettings.this.addCategoryActionPerformed(e));
        popupMenu_2.add(this.mntmAddCategory);

        GroupLayout gl_panel_2 = new GroupLayout(panel_2);
        gl_panel_2.setHorizontalGroup(
                gl_panel_2.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_2.createSequentialGroup()
                                .addComponent(this.listCategories, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                                .addGap(2))
        );
        gl_panel_2.setVerticalGroup(
                gl_panel_2.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_2.createSequentialGroup()
                                .addComponent(this.listCategories, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                                .addContainerGap())
        );

        JPopupMenu popupMenu = new JPopupMenu();
        this.addPopupCategories(this.listCategories, popupMenu);


        this.mntmEditCategory = new JMenuItem("Edit");
        this.mntmEditCategory.addActionListener(e -> JDialogSettings.this.editCategoryActionPerformed(e));
        popupMenu.add(this.mntmEditCategory);

        this.mntmRemoveCategory = new JMenuItem("Remove");
        this.mntmRemoveCategory.addActionListener(e -> JDialogSettings.this.removeCategoryActionPerformed(e));
        popupMenu.add(this.mntmRemoveCategory);
        panel_2.setLayout(gl_panel_2);

        JLabel lblTitle = new JLabel("Title");

        this.textFieldTitle = new JTextField();
        this.textFieldTitle.setColumns(10);

        this.textFieldPath = new JTextField();
        this.textFieldPath.setEditable(false);
        this.textFieldPath.setColumns(10);

        this.textFieldStartIn = new JTextField();
        this.textFieldStartIn.setEditable(false);
        this.textFieldStartIn.setColumns(10);

        List<RunMode> modes = new ArrayList<>(Arrays.asList(RunMode.values()));
        this.comboBoxRunMode = new JComboBox<>(new Vector<>(modes));

        JLabel lblPath = new JLabel("Path");

        JLabel lblStartIn = new JLabel("Start in");

        JLabel lblRun = new JLabel("Run");

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "Icon", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblTitle)
                                .addComponent(lblPath)
                                .addComponent(lblStartIn)
                                .addComponent(lblRun))
                        .addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
                                .addComponent(this.comboBoxRunMode, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addComponent(this.textFieldStartIn, Alignment.LEADING)
                                .addComponent(this.textFieldPath, Alignment.LEADING).addComponent(this.textFieldTitle,
                                        Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                        .addGap(18)
                        .addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                        .addGap(2))
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(lblTitle)
                                                        .addComponent(this.textFieldTitle, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(this.textFieldPath, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblPath))
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(this.textFieldStartIn, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblStartIn))
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(this.comboBoxRunMode, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblRun)))
                                        .addGroup(gl_panel.createSequentialGroup().addGap(21).addComponent(panel_1,
                                                GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.btnIcon = new JButton("");
        this.btnIcon.setBorderPainted(false);
        this.btnIcon.addActionListener(arg0 -> {
        });
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
                gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_1.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(this.btnIcon, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                .addContainerGap())
        );
        gl_panel_1.setVerticalGroup(
                gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_1.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(this.btnIcon, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                .addContainerGap())
        );
        panel_1.setLayout(gl_panel_1);
        panel.setLayout(gl_panel);
        this.contentPanel.setLayout(gl_contentPanel);

        this.setTitle(applicationProperties.getName());
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        {
            JMenuBar menuBar = new JMenuBar();
            this.setJMenuBar(menuBar);

            JMenu mnSettings = new JMenu("Settings");
            menuBar.add(mnSettings);

            JMenuItem mntmOptions = new JMenuItem("Options");
            mntmOptions.addActionListener(e -> this.optionsActionPerformed(e));
            mnSettings.add(mntmOptions);

            JSeparator separator = new JSeparator();
            mnSettings.add(separator);

            JMenuItem mntmClose = new JMenuItem("Close");
            mntmClose.addActionListener(e -> this.exitSettingsActionPerformed(e));
            mnSettings.add(mntmClose);

            JMenu mnHelp = new JMenu("Help");
            menuBar.add(mnHelp);

            JMenuItem mntmManual = new JMenuItem("Manual");
            mntmManual.addActionListener(e -> TrayMenu.manualActionPerformed(e));
            mnHelp.add(mntmManual);

            JMenuItem mntmAbout = new JMenuItem("About");
            mntmAbout.addActionListener(e -> TrayMenu.aboutActionPerformed(e));
            mnHelp.add(mntmAbout);
        }
    }

    protected void updateCategories() {
        this.listCategoriesModel.clear();
        List<Category> categories = this.categoryService.getCategoryRepository().findAll();
        for (Category category : categories) {
            this.listCategoriesModel.addElement(category);
            this.listCategories.setSelectedIndex(-1);
        }
    }

    protected void updateApplications(Category category) {
        this.listApplicationsModel.clear();
        List<Category> categories = this.categoryService.getCategoryRepository().findByName(category.getName());
        if (!categories.isEmpty()) {
            category = categories.get(categories.size() - 1);
        }
        for (Application application : category.getApplications()) {
            this.listApplicationsModel.addElement(application);
            this.listCategories.setSelectedIndex(-1);
        }
    }

    protected void addCategoryActionPerformed(ActionEvent e) {
        this.updateCategories();
        // prompt the user to enter their name
        Category category = null;
        String name = JOptionPane.showInputDialog(this, "Name", "Add Category", JOptionPane.NO_OPTION);
        if (name != null) {
            category = this.categoryService.addCategory(name, new ArrayList<>());
        }
        if ((category != null) && (category.getId() != null)) {
            this.listCategoriesModel.addElement(category);
        }
    }

    protected void editCategoryActionPerformed(ActionEvent e) {
        if (this.listCategories.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Choose category");
            return;
        }
        Category category = this.listCategories.getSelectedValue();
        String name = JOptionPane.showInputDialog(this, "Name", "Edit Category '" + category.getName() + "'",
                JOptionPane.NO_OPTION);
        if ((name != null) && (name != category.getName())) {
            category.setName(name);
            category = this.categoryService.getCategoryRepository().save(category);
            this.listCategoriesModel.set(this.listCategories.getSelectedIndex(), category);
        }

    }

    protected void removeCategoryActionPerformed(ActionEvent e) {
        if (this.listCategories.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Choose category");
            return;
        }
        Category category = this.listCategories.getSelectedValue();
        this.categoryService.getCategoryRepository().delete(category);
        this.updateCategories();
    }

    protected void addShortcutActionPerformed(ActionEvent e) {
        if (this.listCategories.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Choose category");
            return;
        }
        Category category = this.listCategories.getSelectedValue();
        // FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
        JFileChooser fileChooser = new JFileChooser("C:/Program Files (x86)/");
        FileNameExtensionFilter sdfFilter = new FileNameExtensionFilter("executable" + " files (*" + ".exe" + ")",
                "exe");
        fileChooser.addChoosableFileFilter(sdfFilter);
        fileChooser.setFileFilter(sdfFilter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());

        if (!file.getAbsolutePath().endsWith(".exe")) {
            file = new File(file + ".exe");
        }

        Application application = this.applicationService.findApplicationByExePath(file.getAbsolutePath());

        if (application == null) {
            application = new Application(file.getName(), file.getAbsolutePath(), file.getParentFile().getPath(),
                    RunMode.valueOf(this.comboBoxRunMode.getSelectedItem().toString()), Arrays.asList(category));
            application = this.applicationService.getApplicationRepository().save(application);
        } else {
            List<Category> categories = application.getCategories();
            categories.add(category);
            application.setCategories(categories);
            application = this.applicationService.getApplicationRepository().save(application);
        }


        if ((application.getId() != null) && (!application.getCategories().isEmpty())) {
            List<Application> applications = category.getApplications();
            applications.add(application);
            category.setApplications(applications);
            this.listApplicationsModel.addElement(application);
            this.listCategoriesModel.set(this.listCategories.getSelectedIndex(), category);
        }

    }

    protected void editShortcutActionPerformed(ActionEvent e) {
        if (this.listApplications.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Choose category");
            return;
        }
        Application application = this.listApplications.getSelectedValue();
        String name = JOptionPane.showInputDialog(this, "Name", "Edit Application name '" + application.getName() + "'",
                JOptionPane.NO_OPTION);
        if ((name != null) && (name != application.getName())) {
            application.setName(name);
            application = this.applicationService.getApplicationRepository().save(application);
            this.listApplicationsModel.set(this.listApplications.getSelectedIndex(), application);
        }
        this.updateGUIApplicationsInformation(application);

    }

    protected void removeShortcutActionPerformed(ActionEvent e) {
        if ((this.listCategories.getSelectedIndex() < 0) || (this.listApplications.getSelectedIndex() < 0)) {
            JOptionPane.showMessageDialog(this, "Choose category or  application");
            return;
        }
        Application application = this.listApplications.getSelectedValue();
        this.applicationService.getApplicationRepository().delete(application);
        Category category = this.listCategories.getSelectedValue();
        category.setApplications(category.getApplications().stream()
                .filter(p -> p.getName().equalsIgnoreCase(application.getName())).collect(Collectors.toList()));
        this.updateCategories();
        this.updateApplications(category);
    }

    /**
     * @param e
     * @return
     */
    private void exitSettingsActionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * @param e
     * @return
     */
    private void optionsActionPerformed(ActionEvent e) {

    }

    /**
     * Create the frame.
     */
    public void initialize() {

    }

    protected void updateGUIApplicationsInformation(Application application) {
        this.textFieldTitle.setText(application.getName());
        this.textFieldPath.setText(application.getExePath());
        this.textFieldStartIn.setText(application.getExeStartIn());
        this.comboBoxRunMode.setSelectedItem(application.getRunMode());
        Icon icon = null;
        if ((application.getExePath() != null) && (!application.getExePath().isEmpty())) {
            try {
                // Get metadata and create an icon
                ShellFolder sf = ShellFolder.getShellFolder(new File(application.getExePath()));
                icon = new ImageIcon(sf.getIcon(true));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        this.btnIcon.setIcon(icon);
    }

    private void addPopupCategories(java.awt.Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {


            @Override
            public void mousePressed(MouseEvent e) {
                this.initPopup();
                if (e.isPopupTrigger()) {
                    this.showMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                this.initPopup();
                if (e.isPopupTrigger()) {
                    this.showMenu(e);
                }
            }

            private void initPopup() {
                if (JDialogSettings.this.listCategories.getSelectedIndex() < 0) {
                    JDialogSettings.this.listApplicationsModel.clear();
                    JDialogSettings.this.mntmEditCategory.setVisible(false);
                    JDialogSettings.this.mntmRemoveCategory.setVisible(false);
                } else {
                    Category category = JDialogSettings.this.listCategories.getSelectedValue();
                    JDialogSettings.this.updateApplications(category);
                    JDialogSettings.this.mntmEditCategory.setVisible(true);
                    JDialogSettings.this.mntmRemoveCategory.setVisible(true);
                }
                JDialogSettings.this.updateGUIApplicationsInformation(new Application());

            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private void addPopupApplications(java.awt.Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                this.initPopup();
                if (e.isPopupTrigger()) {
                    this.showMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                this.initPopup();
                if (e.isPopupTrigger()) {
                    this.showMenu(e);
                }
            }

            private void initPopup() {
                Application application = JDialogSettings.this.listApplications.getSelectedValue();
                if (application == null) {
                    JDialogSettings.this.mntmEditShortcut.setVisible(false);
                    JDialogSettings.this.mntmRemoveShortcut.setVisible(false);
                    application = new Application();
                } else {
                    JDialogSettings.this.mntmEditShortcut.setVisible(true);
                    JDialogSettings.this.mntmRemoveShortcut.setVisible(true);
                }
                JDialogSettings.this.updateGUIApplicationsInformation(application);

            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    protected JList<Application> getListApplications() {
        return this.listApplications;
    }

    protected JList<Category> getListCategories() {
        return this.listCategories;
    }

    protected JComboBox<RunMode> getComboBoxRunMode() {
        return this.comboBoxRunMode;
    }

    protected JMenuItem getMntmEditCategory() {
        return this.mntmEditCategory;
    }

    protected JMenuItem getMntmRemoveCategory() {
        return this.mntmRemoveCategory;
    }

    protected JMenuItem getMntmAddCategory() {
        return this.mntmAddCategory;
    }

    protected JMenuItem getMntmAddShortcut() {
        return this.mntmAddShortcut;
    }

    protected JMenuItem getMntmEditShortcut() {
        return this.mntmEditShortcut;
    }

    protected JMenuItem getMntmRemoveShortcut() {
        return this.mntmRemoveShortcut;
    }

    protected JTextField getTextFieldTitle() {
        return this.textFieldTitle;
    }

    protected JTextField getTextFieldPath() {
        return this.textFieldPath;
    }

    protected JTextField getTextFieldStartIn() {
        return this.textFieldStartIn;
    }

    protected JButton getBtnIcon() {
        return this.btnIcon;
    }


}
