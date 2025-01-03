/**
 * @name        Simple Java NotePad
 * @package ph.notepad
 * @file UI.java
 *
 * @author Pierre-Henry Soria
 * @email pierrehenrysoria@gmail.com
 * @link        http://github.com/pH-7
 *
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-04-05
 * @update      2017-02-18
 *
 * @modifiedby  Achintha Gunasekara
 * @modemail    contact@achinthagunasekara.com
 *
 * @modifiedby  Marcus Redgrave-Close
 * @modemail    marcusrc1@hotmail.co.uk
 *
 * @Modifiedby SidaDan
 * @modemail Fschultz@sinf.de
 * Added Tooltip Combobox Font type and Font size
 * Overwrite method processWindowEvent to detect window closing event.
 * Added safety query to save the file before exit
 * or the user select "newfile"
 * Only available if the user has pressed a key
 * Added safety query if user pressed the clearButton
 *
 * @Modifiedby SidaDan
 * @modemail Fschultz@sinf.de
 * Removed unuse objects like container,  Border emptyBorder
 * Removed unsused imports
 *
 * @Modifiedby Giorgos Pasentsis
 * @modemail gpasents@gmail.com
 */
package simplejavatexteditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.text.DefaultEditorKit;

public class UI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final JTextArea textArea;
    private final JMenuBar menuBar;
    private final JComboBox<String> fontSize;
    private final JComboBox<String> fontType;
    private final JMenu menuFile, menuEdit, menuFind, menuAbout, menuCustomize;
    private final JMenuItem newFile, openFile, saveFile, close, cut, copy, paste, clearFile, selectAll, quickFind,
            aboutMe, aboutSoftware, wordWrap, changeBgColor, changeFgColor;
    private final JToolBar mainToolbar;
    JButton newButton, openButton, saveButton, clearButton, quickButton, aboutMeButton, aboutButton, closeButton, boldButton, italicButton;
    private final Action selectAllAction;

    //setup icons - application icon
    private final ImageIcon appIcon = new ImageIcon(UI.class.getResource("/new-icons/notepad.png"));

    //setup icons - Bold and Italic
    private final ImageIcon boldIcon = new ImageIcon(UI.class.getResource("/new-icons/bold.png"));
    private final ImageIcon italicIcon = new ImageIcon(UI.class.getResource("/new-icons/italic.png"));

    // setup icons - File Menu
    private final ImageIcon newIcon = new ImageIcon(UI.class.getResource("/new-icons/new.png"));
    private final ImageIcon openIcon = new ImageIcon(UI.class.getResource("/new-icons/open.png"));
    private final ImageIcon saveIcon = new ImageIcon(UI.class.getResource("/new-icons/save.png"));
    private final ImageIcon closeIcon = new ImageIcon(UI.class.getResource("/new-icons/exit.png"));

    // setup icons - Edit Menu
    private final ImageIcon clearIcon = new ImageIcon(UI.class.getResource("/new-icons/brush.png"));
    private final ImageIcon cutIcon = new ImageIcon(UI.class.getResource("/new-icons/cut.png"));
    private final ImageIcon copyIcon = new ImageIcon(UI.class.getResource("/new-icons/copy.png"));
    private final ImageIcon pasteIcon = new ImageIcon(UI.class.getResource("/new-icons/paste.png"));
    private final ImageIcon selectAllIcon = new ImageIcon(UI.class.getResource("/new-icons/select.png"));
    private final ImageIcon wordwrapIcon = new ImageIcon(UI.class.getResource("/new-icons/word_wrap.png"));

    // setup icons - Search Menu
    private final ImageIcon searchIcon = new ImageIcon(UI.class.getResource("/new-icons/search.png"));

    // setup icons - Help Menu
    private final ImageIcon aboutMeIcon = new ImageIcon(UI.class.getResource("/new-icons/about_me.png"));
    private final ImageIcon aboutIcon = new ImageIcon(UI.class.getResource("/new-icons/about.png"));

    private SupportedKeywords kw = new SupportedKeywords();
    private HighlightText languageHighlighter = new HighlightText(Color.GRAY);
    AutoComplete autocomplete;
    private boolean hasListener = false;
    private boolean edit = false;

    public UI() {
        // Set the initial size of the window
        setSize(800, 500);

        // Set the title of the window
        setTitle("new | " + SimpleJavaTextEditor.NAME);

        // Set the default close operation (exit when it gets closed)
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set a default icon for the window
        setIconImage(appIcon.getImage());

        // center the frame on the monitor
        setLocationRelativeTo(null);

        // Set a default font for the TextArea
        textArea = new JTextArea("", 0, 0);
        textArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        textArea.setTabSize(2);
        textArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        textArea.setTabSize(2);

        // Set the colors for the TextArea
        textArea.setForeground(Color.BLACK);
        textArea.setBackground(Color.WHITE);
        textArea.setCaretColor(Color.BLACK);

        /* SETTING BY DEFAULT WORD WRAP ENABLED OR TRUE */
        textArea.setLineWrap(true);

        // Set an higlighter to the JTextArea
        textArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                edit = true;
                languageHighlighter.highLight(textArea, kw.getCppKeywords());
                languageHighlighter.highLight(textArea, kw.getJavaKeywords());
            }
        });

        // This is why we didn't have to worry about the size of the TextArea!
        getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
        getContentPane().add(textArea);

        // Set the Menus
        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuFind = new JMenu("Search");
        menuCustomize = new JMenu("Customize");
        menuAbout = new JMenu("About");

        // Font Settings menu

        // Set the Items Menu
        newFile = new JMenuItem("New", newIcon);
        openFile = new JMenuItem("Open", openIcon);
        saveFile = new JMenuItem("Save", saveIcon);
        close = new JMenuItem("Quit", closeIcon);
        clearFile = new JMenuItem("Clear", clearIcon);
        quickFind = new JMenuItem("Quick", searchIcon);
        aboutMe = new JMenuItem("About Me", aboutMeIcon);
        aboutSoftware = new JMenuItem("About Software", aboutIcon);
        changeBgColor = new JMenuItem("Change Background Color");
        changeFgColor = new JMenuItem("Change Foreground Color");

        menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuFind);
        menuBar.add(menuCustomize);
        menuBar.add(menuAbout);

        this.setJMenuBar(menuBar);

        // Set Actions:
        selectAllAction = new SelectAllAction("Select All", clearIcon, "Select all text", Integer.valueOf(KeyEvent.VK_A),
                textArea);

        this.setJMenuBar(menuBar);

        // New File
        newFile.addActionListener(this);  // Adding an action listener (so we know when it's been clicked).
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)); // Set a keyboard shortcut
        menuFile.add(newFile); // Adding the file menu

        // Open File
        openFile.addActionListener(this);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        menuFile.add(openFile);

        // Save File
        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        menuFile.add(saveFile);

        // Close File
        /*
         * Along with our "CTRL+F4" shortcut to close the window, we also have
         * the default closer, as stated at the beginning of this tutorial. this
         * means that we actually have TWO shortcuts to close:
         * 1) the default close operation (example, Alt+F4 on Windows)
         * 2) CTRL+F4, which we are
         * about to define now: (this one will appear in the label).
         */
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        close.addActionListener(this);
        menuFile.add(close);

        // Select All Text
        selectAll = new JMenuItem(selectAllAction);
        selectAll.setText("Select All");
        selectAll.setIcon(selectAllIcon);
        selectAll.setToolTipText("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));;
        menuEdit.add(selectAll);

        // Clear File (Code)
        clearFile.addActionListener(this);
        clearFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK));;
        menuEdit.add(clearFile);

        // Cut Text
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        cut.setIcon(cutIcon);
        cut.setToolTipText("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        menuEdit.add(cut);

        // WordWrap
        wordWrap = new JMenuItem();
        wordWrap.setText("Word Wrap");
        wordWrap.setIcon(wordwrapIcon);
        wordWrap.setToolTipText("Word Wrap");

        // Change Background & Foreground Colors
        changeBgColor.addActionListener(this);
        changeFgColor.addActionListener(this);
        menuCustomize.add(changeBgColor);
        menuCustomize.add(changeFgColor);

        //Short cut key or key stroke
        wordWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        menuEdit.add(wordWrap);

        /* CODE FOR WORD WRAP OPERATION
         * BY DEFAULT WORD WRAPPING IS ENABLED.
         */
        wordWrap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // If wrapping is false then after clicking on menuitem the word wrapping will be enabled
                if (textArea.getLineWrap() == false) {
                    /* Setting word wrapping to true */
                    textArea.setLineWrap(true);
                } else {
                    // else  if wrapping is true then after clicking on menuitem the word wrapping will be disabled
                    /* Setting word wrapping to false */
                    textArea.setLineWrap(false);
                }
            }
        });

        // Copy Text
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setIcon(copyIcon);
        copy.setToolTipText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        menuEdit.add(copy);

        // Paste Text
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.setIcon(pasteIcon);
        paste.setToolTipText("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        menuEdit.add(paste);

        // Find Word
        quickFind.addActionListener(this);
        quickFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));;
        menuFind.add(quickFind);

        // About Me
        aboutMe.addActionListener(this);
        aboutMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuAbout.add(aboutMe);

        // About Software
        aboutSoftware.addActionListener(this);
        aboutSoftware.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        menuAbout.add(aboutSoftware);

        mainToolbar = new JToolBar();
        this.add(mainToolbar, BorderLayout.NORTH);
        // used to create space between button groups
        newButton = new JButton(newIcon);
        newButton.setToolTipText("New");
        newButton.addActionListener(this);
        mainToolbar.add(newButton);
        mainToolbar.addSeparator();

        openButton = new JButton(openIcon);
        openButton.setToolTipText("Open");
        openButton.addActionListener(this);
        mainToolbar.add(openButton);
        mainToolbar.addSeparator();

        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(this);
        mainToolbar.add(saveButton);
        mainToolbar.addSeparator();

        clearButton = new JButton(clearIcon);
        clearButton.setToolTipText("Clear All");
        clearButton.addActionListener(this);
        mainToolbar.add(clearButton);
        mainToolbar.addSeparator();

        quickButton = new JButton(searchIcon);
        quickButton.setToolTipText("Quick Search");
        quickButton.addActionListener(this);
        mainToolbar.add(quickButton);
        mainToolbar.addSeparator();

        aboutMeButton = new JButton(aboutMeIcon);
        aboutMeButton.setToolTipText("About Me");
        aboutMeButton.addActionListener(this);
        mainToolbar.add(aboutMeButton);
        mainToolbar.addSeparator();

        aboutButton = new JButton(aboutIcon);
        aboutButton.setToolTipText("About NotePad PH");
        aboutButton.addActionListener(this);
        mainToolbar.add(aboutButton);
        mainToolbar.addSeparator();

        closeButton = new JButton(closeIcon);
        closeButton.setToolTipText("Quit");
        closeButton.addActionListener(this);
        mainToolbar.add(closeButton);
        mainToolbar.addSeparator();

        boldButton = new JButton(boldIcon);
        boldButton.setToolTipText("Bold");
        boldButton.addActionListener(this);
        mainToolbar.add(boldButton);
        mainToolbar.addSeparator();

        italicButton = new JButton(italicIcon);
        italicButton.setToolTipText("Italic");
        italicButton.addActionListener(this);
        mainToolbar.add(italicButton);
        mainToolbar.addSeparator();

        // set menu and toolbar colors
        setMenuAndToolbarColors(Color.LIGHT_GRAY, Color.BLACK);

        /**
         * **************** FONT SETTINGS SECTION **********************
         */
        
        //FONT FAMILY SETTINGS SECTION START
        fontType = new JComboBox<>(new String[]{});

        //GETTING ALL AVAILABLE FONT FOMILY NAMES
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (int i = 0; i < fonts.length; i++) {
            //Adding font family names to font[] array
            fontType.addItem(fonts[i]);
        }
        //Setting maximize size of the fontType ComboBox
        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        mainToolbar.add(fontType);
        mainToolbar.addSeparator();

        //Adding Action Listener on fontType JComboBox
        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                //Getting the selected fontType value from ComboBox
                String p = fontType.getSelectedItem().toString();
                //Getting size of the current font or text
                int s = textArea.getFont().getSize();
                textArea.setFont(new Font(p, Font.PLAIN, s));
            }
        });

        //FONT FAMILY SETTINGS SECTION END
        //FONT SIZE SETTINGS START
        fontSize = new JComboBox<>(new String[]{});

        for (int i = 5; i <= 100; i++) {
            fontSize.addItem(String.valueOf(i));
        }
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        mainToolbar.add(fontSize);

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sizeValue = fontSize.getSelectedItem().toString();
                int sizeOfFont = Integer.parseInt(sizeValue);
                String fontFamily = textArea.getFont().getFamily();

                Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
                textArea.setFont(font1);
            }
        });
        //FONT SIZE SETTINGS SECTION END
    }

    private void setMenuAndToolbarColors(Color background, Color foreground) {
        menuBar.setBackground(background);
        menuBar.setForeground(foreground);
        menuFile.setBackground(background);
        menuFile.setForeground(foreground);
        menuEdit.setBackground(background);
        menuEdit.setForeground(foreground);
        menuFind.setBackground(background);
        menuFind.setForeground(foreground);
        menuCustomize.setBackground(background);
        menuCustomize.setForeground(foreground);
        menuAbout.setBackground(background);
        menuAbout.setForeground(foreground);
        mainToolbar.setBackground(background);
        mainToolbar.setForeground(foreground);

        // Iterate through toolbar components and set their colors
        for (Component comp : mainToolbar.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(background);
                comp.setForeground(foreground);
            }
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (edit) {
                Object[] options = {"Save and exit", "Exit without saving", "Cancel"};
                int n = JOptionPane.showOptionDialog(this, "Save before exiting?", "Save",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();// dispose all resources and close the application
                } else if (n == 1) {// no save and exit
                    this.dispose();// dispose all resources and close the application
                }
            } else {
                System.exit(99);
            }
        }
    }

    // Make the TextArea available to the autocomplete handler
    protected JTextArea getEditor() {
        return textArea;
    }

    // Enable autocomplete option
    public void enableAutoComplete(File file) {
        if (hasListener) {
            textArea.getDocument().removeDocumentListener(autocomplete);
            hasListener = false;
        }

        ArrayList<String> arrayList;
        String[] list = kw.getSupportedLanguages();

        for (int i = 0; i < list.length; i++) {
            if (file.getName().endsWith(list[i])) {
                switch (i) {
                    case 0:
                        String[] jk = kw.getJavaKeywords();
                        arrayList = kw.setKeywords(jk);
                        autocomplete = new AutoComplete(this, arrayList);
                        textArea.getDocument().addDocumentListener(autocomplete);
                        hasListener = true;
                        break;
                    case 1:
                        String[] ck = kw.getCppKeywords();
                        arrayList = kw.setKeywords(ck);
                        autocomplete = new AutoComplete(this, arrayList);
                        textArea.getDocument().addDocumentListener(autocomplete);
                        hasListener = true;
                        break;
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        // If the source of the event was our "close" option
        if (e.getSource() == close || e.getSource() == closeButton) {
            if (edit) {
                Object[] options = {"Save and exit", "No Save and exit", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();// dispose all resources and close the application
                } else if (n == 1) {// no save and exit
                    this.dispose();// dispose all resources and close the application
                }
            } else {
                this.dispose();// dispose all resources and close the application
            }
        } // If the source was the "new" file option
        else if (e.getSource() == newFile || e.getSource() == newButton) {
            if (edit) {
                Object[] options = {"Save", "Don't Save", "Cancel"};
                int n = JOptionPane.showOptionDialog(this, "Would you like to save the current file before closing?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save
                    saveFile();
                    edit = false;
                } else if (n == 1) {
                    edit = false;
                    FEdit.clear(textArea);
                }
            } else {
                FEdit.clear(textArea);
            }

        } // If the source was the "open" option
        else if (e.getSource() == openFile || e.getSource() == openButton) {
            JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to  browse files to open)
            int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)

            /*
             * NOTE: because we are OPENing a file, we call showOpenDialog~ if
             * the user clicked OK, we have "APPROVE_OPTION" so we want to open
             * the file
             */
            if (option == JFileChooser.APPROVE_OPTION) {
                FEdit.clear(textArea); // clear the TextArea before applying the file contents
                try {
                    File openFile = open.getSelectedFile();
                    setTitle(openFile.getName() + " | " + SimpleJavaTextEditor.NAME);
                    try (Scanner scan = new Scanner(new FileReader(openFile.getPath()))) {
                        while (scan.hasNext()) {
                            textArea.append(scan.nextLine() + "\n");
                        }
                    }

                    enableAutoComplete(openFile);
                } catch (Exception ex) { // catch any exceptions, and...
                    // ...write to the debug console
                    System.err.println(ex.getMessage());
                }
            }
        } // If the source of the event was the "save" option
        else if (e.getSource() == saveFile || e.getSource() == saveButton) {
            saveFile();
        }// If the source of the event was the "Bold" button
        else if (e.getSource() == boldButton) {
            if (textArea.getFont().getStyle() == Font.BOLD){
                textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
            } else {
                textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
            }
        }// If the source of the event was the "Italic" button
        else if (e.getSource() == italicButton) {
            if (textArea.getFont().getStyle() == Font.ITALIC){
                textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
            } else {
                textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));
            }
        }
        // Clear File (Code)
        if (e.getSource() == clearFile || e.getSource() == clearButton) {

            Object[] options = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(this, "Are you sure to clear the text Area ?", "Question",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {// clear
                FEdit.clear(textArea);
            }
        }
        // Find
        if (e.getSource() == quickFind || e.getSource() == quickButton) {
            new Find(textArea);
        } // About Me
        else if (e.getSource() == aboutMe || e.getSource() == aboutMeButton) {
            new About(this).me();
        } // About Software
        else if (e.getSource() == aboutSoftware || e.getSource() == aboutButton) {
            new About(this).software();
        } // Change Background Color
        else if (e.getSource() == changeBgColor) {
            // Show color picker dialog for background color
            Color newBgColor = JColorChooser.showDialog(this, "Choose Background Color", textArea.getBackground());
            if (newBgColor != null) {
                setMenuAndToolbarColors(newBgColor, menuBar.getForeground());
                textArea.setBackground(newBgColor);
            }
        } else if (e.getSource() == changeFgColor) {
            // Show color picker dialog for foreground color
            Color newFgColor = JColorChooser.showDialog(this, "Choose Foreground Color", textArea.getForeground());
            if (newFgColor != null) {
                setMenuAndToolbarColors(menuBar.getBackground(), newFgColor);
                textArea.setForeground(newFgColor);
            }
        }
    }

    class SelectAllAction extends AbstractAction {

        /**
         * Used for Select All function
         */
        private static final long serialVersionUID = 1L;

        public SelectAllAction(String text, ImageIcon icon, String desc, Integer mnemonic, final JTextArea textArea) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            textArea.selectAll();
        }
    }

    private void saveFile() {
        // Open a file chooser
        JFileChooser fileChoose = new JFileChooser();
        // Open the file, only this time we call
        int option = fileChoose.showSaveDialog(this);

        /*
             * ShowSaveDialog instead of showOpenDialog if the user clicked OK
             * (and not cancel)
         */
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File openFile = fileChoose.getSelectedFile();
                setTitle(openFile.getName() + " | " + SimpleJavaTextEditor.NAME);

                BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
                out.write(textArea.getText());
                out.close();

                enableAutoComplete(openFile);
                edit = false;
            } catch (Exception ex) { // again, catch any exceptions and...
                // ...write to the debug console
                System.err.println(ex.getMessage());
            }
        }
    }

}
