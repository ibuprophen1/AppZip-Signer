/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.ui;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.utils.Texts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 * Editor popup menu for {@link JTextComponent}.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class JEditorPopupMenu extends JPopupMenu {

    /**
     * Auto-generated by Eclipse.
     */
    private static final long serialVersionUID = 5578010916105435603L;

    private static final String CLASSNAME = JEditorPopupMenu.class.getName();

    private static JEditorPopupMenu mInstance;

    /**
     * Gets the global instance of the popup menu.
     * 
     * @return the global instance of the popup menu.
     */
    public static JEditorPopupMenu getInstance() {
        return mInstance != null ? mInstance
                : (mInstance = new JEditorPopupMenu());
    }// getInstance()

    /**
     * Applies the global instance of this menu to all sub components of
     * {@code container} which are {@link JTextComponent}.
     * <p>
     * You can also obtain the global instance of this menu via
     * {@link #getInstance()}.
     * </p>
     * 
     * @param container
     *            the container.
     */
    public static void apply(Container container) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            final Component comp = container.getComponent(i);
            if (comp instanceof JTextComponent)
                ((JTextComponent) comp).setComponentPopupMenu(getInstance());
            else if (comp instanceof Container)
                apply((Container) comp);
        }
    }// setEditorPopupMenu()

    /**
     * Action CUT.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_CUT = CLASSNAME + ".cut";

    /**
     * Action COPY.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_COPY = CLASSNAME + ".copy";

    /**
     * Action COPY ALL.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_COPY_ALL = CLASSNAME + ".copy-all";

    /**
     * Action PASTE.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_PASTE = CLASSNAME + ".paste";

    /**
     * Action CLEAR AND PASTE.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_CLEAR_AND_PASTE = CLASSNAME
            + ".clear-and-paste";

    /**
     * Action CLEAR.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_CLEAR = CLASSNAME + ".clear";

    /**
     * Action DELETE.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_DELETE = CLASSNAME + ".delete";

    /**
     * Action SELECT ALL.
     * <p>
     * Used in {@link Component#firePropertyChange(String, long, long)}.
     * </p>
     */
    public final static String ACTION_NAME_SELECT_ALL = CLASSNAME
            + ".select-all";

    /**
     * Extended class of {@link TextAction}.
     * 
     * @author Hai Bison
     * @since v1.6 beta
     */
    public static abstract class TextActionEx extends TextAction {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = 852449530907004277L;

        /**
         * Creates new action.
         * 
         * @param name
         *            the action name.
         */
        public TextActionEx(String name) {
            super(name);
        }// TextActionEx()

        /**
         * Checks whether this action is enabled with {@code component}.
         * 
         * @param component
         *            the owner component of this action.
         * @return {@code true} or {@code false}.
         */
        abstract boolean isEnabledWith(JTextComponent component);
    }// TextActionEx

    /**
     * Creates new instance.
     */
    public JEditorPopupMenu() {
        super();
        initMenuItems();
    }// JEditorPopupMenu()

    /**
     * Initializes all menu items.
     */
    private void initMenuItems() {
        final String itemSeparator = "-";
        final String[] itemTitles = {
                Messages.getString(R.string.context_menu_cut),
                Messages.getString(R.string.context_menu_copy),
                Messages.getString(R.string.context_menu_copy_all),
                Messages.getString(R.string.context_menu_paste), itemSeparator,
                Messages.getString(R.string.context_menu_clear_and_paste),
                Messages.getString(R.string.context_menu_clear),
                Messages.getString(R.string.context_menu_delete),
                itemSeparator,
                Messages.getString(R.string.context_menu_select_all) };
        final Action[] itemActions = { new CutAction(itemTitles[0]),
                new CopyAction(itemTitles[1]),
                new CopyAllAction(itemTitles[2]),
                new PasteAction(itemTitles[3]), null,
                new ClearAndPasteAction(itemTitles[4]),
                new ClearAction(itemTitles[5]),
                new DeleteAction(itemTitles[6]), null,
                new SelectAllAction(itemTitles[7]) };
        for (int i = 0; i < itemTitles.length; i++) {
            if (itemTitles[i].equals(itemSeparator)) {
                Separator separator = new JPopupMenu.Separator();
                this.add(separator);
            } else {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setAction(itemActions[i]);
                menuItem.setText(itemTitles[i]);
                menuItem.setToolTipText(itemTitles[i]);
                this.add(menuItem);
            }
        }// for
    }// initMenuItems()

    /**
     * Fires an action... (TODO ???)
     * 
     * @param actionName
     *            the action name.
     */
    private void fireAfterActionPerformed(String actionName) {
        Component invoker = getInvoker();
        if (invoker != null)
            invoker.firePropertyChange(actionName, 0, 1);
    }// fireAfterActionPerformed()

    /*
     * EDITOR ACTIONS
     */

    /**
     * The CUT action.
     */
    private class CutAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = -1982804538265472293L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public CutAction(String name) {
            super(name);
        }// CutAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        textComponent.cut();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_CUT);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            return !(component instanceof JPasswordField)
                    && component.isEnabled() && component.isEditable()
                    && !Texts.isEmpty(component.getSelectedText());
        }// isEnabledWith()
    }// CutAction

    /**
     * The COPY action.
     */
    private class CopyAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = -3254837350008467760L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public CopyAction(String name) {
            super(name);
        }// CopyAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        textComponent.copy();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_COPY);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            return !(component instanceof JPasswordField)
                    && component.isEnabled()
                    && !Texts.isEmpty(component.getSelectedText());
        }// isEnabledWith()
    }// CopyAction

    /**
     * The COPY ALL action.
     */
    private class CopyAllAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = 4615951043416333486L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public CopyAllAction(String name) {
            super(name);
        }// CopyAllAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        textComponent.selectAll();
                        textComponent.copy();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_COPY_ALL);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            return !(component instanceof JPasswordField)
                    && component.isEnabled()
                    && !Texts.isEmpty(component.getText());
        }// isEnabledWith()
    }// CopyAllAction

    /**
     * PASTE action.
     * 
     * @author Hai Bison
     */
    private class PasteAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = 7589082114137081076L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public PasteAction(String name) {
            super(name);
        }// PasteAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        textComponent.paste();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_PASTE);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            if (component.isEnabled() && component.isEditable()) {
                CharSequence clipboard = null;
                try {
                    clipboard = Toolkit.getDefaultToolkit()
                            .getSystemClipboard()
                            .getData(DataFlavor.stringFlavor).toString();
                } catch (Throwable t) {
                    clipboard = null;
                }

                return !Texts.isEmpty(clipboard);
            } else
                return false;
        }// isEnabledWith()
    }// PasteAction

    /**
     * CLEAR action.
     * 
     * @author Hai Bison
     */
    private class ClearAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = 2003384687851877644L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public ClearAction(String name) {
            super(name);
        }// ClearAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        textComponent.setText(null);
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_CLEAR);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            return component.isEnabled() && component.isEditable()
                    && !Texts.isEmpty(component.getText());
        }// isEnabledWith()
    }// ClearAction

    /**
     * DELETE action.
     * 
     * @author Hai Bison
     */
    private class DeleteAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = 7226805445589511342L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public DeleteAction(String name) {
            super(name);
        }// DeleteAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        int start = textComponent.getSelectionStart();
                        int end = textComponent.getSelectionEnd();
                        if (end > start) {
                            try {
                                String text = textComponent.getText();
                                if (end <= text.length()) {
                                    text = text.substring(0, start)
                                            + text.substring(end);
                                    textComponent.setText(text);
                                    textComponent.setCaretPosition(start);
                                }
                            } catch (Exception ex) {
                                return;
                            }
                        }
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_DELETE);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            return component.isEnabled() && component.isEditable()
                    && !Texts.isEmpty(component.getSelectedText());
        }// isEnabledWith()
    }// DeleteAction

    /**
     * CLEAR AND PASTE action.
     * 
     * @author Hai Bison
     */
    private class ClearAndPasteAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = -2872364748582253016L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public ClearAndPasteAction(String name) {
            super(name);
        }// ClearAndPasteAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        textComponent.setText(null);
                        textComponent.paste();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_CLEAR_AND_PASTE);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            return component.isEnabled() && component.isEditable();
        }// isEnabledWith()
    }// ClearAndPasteAction

    /**
     * SELECT ALL action.
     * 
     * @author Hai Bison
     */
    private class SelectAllAction extends TextActionEx {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = 4749618930737855868L;

        /**
         * Creates new instance.
         * 
         * @param name
         *            the action name.
         */
        public SelectAllAction(String name) {
            super(name);
        }// SelectAllAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e
                            .getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu
                                .getInvoker();
                        textComponent.selectAll();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_SELECT_ALL);
                    }
                }
            }
        }// actionPerformed()

        @Override
        boolean isEnabledWith(JTextComponent component) {
            return component.isEnabled() && !Texts.isEmpty(component.getText());
        }// isEnabledWith()
    }// SelectAllAction()

    @Override
    protected void firePopupMenuWillBecomeVisible() {
        /*
         * Checks the source invoker component and fix its actions' state.
         */

        final Component invoker = getInvoker();
        if (invoker instanceof JTextComponent) {
            for (int i = 0; i < getComponentCount(); i++) {
                Component comp = getComponent(i);
                if (comp instanceof JMenuItem) {
                    TextActionEx action = (TextActionEx) ((JMenuItem) comp)
                            .getAction();
                    action.setEnabled(action
                            .isEnabledWith((JTextComponent) invoker));
                }
            }// for
        }// if
    }// firePopupMenuWillBecomeVisible()
}
