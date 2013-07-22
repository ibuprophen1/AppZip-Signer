/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.ui.prefs;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.utils.Preferences;
import group.pals.desktop.app.apksigner.utils.ui.UI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * The preferences dialog.
 * 
 * @author Hai Bison
 * 
 */
public class DialogPreferences extends JDialog {

    /**
     * Auto-generated by Eclipse.
     */
    private static final long serialVersionUID = -2538536937457934560L;

    /**
     * The class name.
     */
    private static final String CLASSNAME = DialogPreferences.class.getName();

    /**
     * This key holds the last tab index.
     */
    private static final String PKEY_LAST_TAB_INDEX = CLASSNAME
            + ".last_tab_index";

    /*
     * CONTROLS
     */

    private final JPanel contentPanel = new JPanel();
    private JTabbedPane mTabbedPane;

    /**
     * Create the dialog.
     */
    public DialogPreferences(Window owner) {
        super(owner, Messages.getString(R.string.settings),
                Dialog.ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        getRootPane().registerKeyboardAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog(false);
            }// actionPerformed()
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        setBounds(0, 0, 630, 270);
        getContentPane().setLayout(new BorderLayout(10, 10));
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.SOUTH);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton(Messages.getString(R.string.ok));
                okButton.addActionListener(mBtnOkActionListener);
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton(
                        Messages.getString(R.string.cancel));
                cancelButton.addActionListener(mBtnCancelActionListener);
                buttonPane.add(cancelButton);
            }
        }
        {
            mTabbedPane = new JTabbedPane(JTabbedPane.TOP);
            getContentPane().add(mTabbedPane, BorderLayout.CENTER);
            UI.initJTabbedPaneHeaderMouseWheelListener(mTabbedPane);
        }
        {
            JLabel lblNewLabel = new JLabel(Messages.getString(
                    R.string.pmsg_sensitive_data_encryption,
                    Preferences.PREFS_FILENAME));
            lblNewLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
            getContentPane().add(lblNewLabel, BorderLayout.NORTH);
        }

        /*
         * CUSTOM INITIALIZATIONS
         */

        addWindowListener(mWindowAdapter);
        Preferences.getInstance().beginTransaction();
        initTabs();
        pack();
    }// DialogPreferences()

    /**
     * Initializes tabs.
     */
    private void initTabs() {
        if (Beans.isDesignTime())
            return;

        /*
         * Initialization of panels are slow. So we should put this block into a
         * `Runnable`.
         */
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                mTabbedPane.add(
                        Messages.getString(R.string.internet_connection),
                        new PanelInternetConnection());

                /*
                 * Select the last tab index.
                 */

                int lastTabIndex = 0;
                try {
                    lastTabIndex = Integer.parseInt(Preferences.getInstance()
                            .get(PKEY_LAST_TAB_INDEX));
                } catch (Exception e) {
                    /*
                     * Ignore it.
                     */
                }

                if (lastTabIndex >= mTabbedPane.getTabCount())
                    lastTabIndex = mTabbedPane.getTabCount() - 1;
                if (lastTabIndex < 0)
                    lastTabIndex = 0;

                mTabbedPane.setSelectedIndex(lastTabIndex);
            }// run()
        });
    }// initTabs()

    /**
     * Closes this dialog.
     * 
     * @param commitTransaction
     *            {@code true} or {@code false}.
     */
    public void closeDialog(boolean commitTransaction) {
        if (commitTransaction)
            Preferences.getInstance().endTransaction();
        else
            Preferences.getInstance().cancelTransaction();

        dispatchEvent(new WindowEvent(DialogPreferences.this,
                WindowEvent.WINDOW_CLOSING));
    }// closeDialog()

    /*
     * LISTENERS
     */

    private final WindowAdapter mWindowAdapter = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            Preferences.getInstance().set(PKEY_LAST_TAB_INDEX,
                    Integer.toString(mTabbedPane.getSelectedIndex()));
            Preferences.getInstance().store();
        }// windowClosing()
    };// mWindowAdapter

    private final ActionListener mBtnOkActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < mTabbedPane.getTabCount(); i++) {
                if (!((PreferencesFrame) mTabbedPane.getComponentAt(i)).store()) {
                    mTabbedPane.setSelectedIndex(i);
                    return;
                }
            }

            closeDialog(true);
        }// actionPerformed()
    };// mBtnOkActionListener

    private final ActionListener mBtnCancelActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            closeDialog(false);
        }// actionPerformed()
    };// mBtnCancelActionListener

}
