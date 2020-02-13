// Copyright Eric Chauvin 2019 - 2020.


// Code Analysis for C++ code, written in Java.



import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.io.File;



/*
// import java.awt.Insets;
// import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;
import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.Timer;
// import java.io.BufferedInputStream;
import java.lang.ProcessBuilder.Redirect;
*/


// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.



public class MainWindow extends JFrame implements
                                WindowListener,
                                WindowFocusListener,
                                WindowStateListener,
                                ActionListener
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private MainApp mApp;
  private Font mainFont;
  private boolean windowIsClosing = false;
  private JTextArea statusTextArea;
  private String statusFileName = "";
  private JPanel mainPanel = null;


/*
  private String searchText = "";
  private Timer keyboardTimer;
  private Process buildProcess = null;
  private int buildTimerCount = 0;
  private int exeTimerCount = 0;
  private Process executableProcess = null;
*/


  private MainWindow()
    {
    }



  public MainWindow( MainApp useApp, String showText )
    {
    super( showText );

    mApp = useApp;

    mainFont = new Font( Font.MONOSPACED, Font.PLAIN, 40 );

    setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

    setSize( 1200, 600 );
    addComponents( getContentPane() );
    // pack();
    // setExtendedState( JFrame.MAXIMIZED_BOTH );

    setupMenus();

    addWindowListener( this );
    addWindowFocusListener( this );
    addWindowStateListener( this );

    // Center it.
    setLocationRelativeTo( null );
    setVisible( true );
    }


  // Do this after the constructor has returned.
  public void initialize()
    {
    showStatus( "Programming by Eric Chauvin." );
    showStatus( "Version date: " + MainApp.versionDate );
 
    // setupTimer();
    }



  public void windowStateChanged( WindowEvent e )
    {
    // showStatus( "windowStateChanged" );
    }


  public void windowGainedFocus( WindowEvent e )
    {
    // showStatus( "windowGainedFocus" );
    }


  public void windowLostFocus( WindowEvent e )
    {
    // showStatus( "windowLostFocus" );
    }



  public void windowOpened( WindowEvent e )
    {
    // showStatus( "windowOpened" );
    }



  public void windowClosing( WindowEvent e )
    {
    windowIsClosing = true;
    }


  public void windowClosed( WindowEvent e )
    {
    // showStatus( "windowClosed" );
    }


  public void windowIconified( WindowEvent e )
    {
    // keyboardTimer.stop();
    // showStatus( "windowIconified" );
    }



  public void windowDeiconified( WindowEvent e )
    {
    // keyboardTimer.start();
    // showStatus( "windowDeiconified" );
    }



  public void windowActivated( WindowEvent e )
    {
    // showStatus( "windowActivated" );
    }


  public void windowDeactivated( WindowEvent e )
    {
    // showStatus( "windowDeactivated" );
    }



  private void addComponents( Container pane )
    {
    // pane.setBackground( Color.red );
    // pane.setForeground( Color.red );
    pane.setLayout( new LayoutSimpleVertical());

    mainPanel = new JPanel();
    mainPanel.setLayout( new LayoutSimpleVertical());
    mainPanel.setBackground( Color.black );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    mainPanel.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    pane.add( mainPanel );
    addStatusTextPane();
    }



  public void showStatus( String toShow )
    {
    if( statusTextArea == null )
      return;

    statusTextArea.append( toShow + "\n" );
    }



  public void clearStatus()
    {
    if( statusTextArea == null )
      return;

    statusTextArea.setText( "" );
    }



  private void addStatusTextPane()
    {
    statusTextArea = new JTextArea();
    statusTextArea.setDragEnabled( false );
    statusTextArea.setFont( mainFont );
    statusTextArea.setLineWrap( true );
    statusTextArea.setWrapStyleWord( true );
    statusTextArea.setBackground( Color.black );
    statusTextArea.setForeground( Color.white );
    statusTextArea.setSelectedTextColor( Color.white );
    statusTextArea.setSelectionColor( Color.darkGray );
    // black, blue, cyan, darkGray, gray, green, lightGray,
    // magenta, orange, pink, red, white, yellow.

    CaretWide cWide = new CaretWide();
    statusTextArea.setCaret( cWide );
    statusTextArea.setCaretColor( Color.white );

    JScrollPane scrollPane1 = new JScrollPane(
                                     statusTextArea );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );

    scrollPane1.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    // mainPanel.add( statusTextArea );
    mainPanel.add( scrollPane1 );
    }



  private void setupMenus()
    {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground( Color.black );

    ///////////////////////
    // File Menu:
    JMenu fileMenu = new JMenu( "File" );
    fileMenu.setMnemonic( KeyEvent.VK_F );
    fileMenu.setFont( mainFont );
    fileMenu.setForeground( Color.white );
    menuBar.add( fileMenu );

    JMenuItem menuItem;

    menuItem = new JMenuItem( "List Files" );
    menuItem.setMnemonic( KeyEvent.VK_L );
    menuItem.setActionCommand( "FileList" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Exit" );
    menuItem.setMnemonic( KeyEvent.VK_X );
    menuItem.setActionCommand( "FileExit" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );


    ///////////////////////
    // Edit Menu:
    JMenu editMenu = new JMenu( "Edit" );
    editMenu.setMnemonic( KeyEvent.VK_E );
    editMenu.setForeground( Color.white );
    editMenu.setFont( mainFont );
    menuBar.add( editMenu );

    menuItem = new JMenuItem( "Copy" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditCopy" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

/*
    menuItem = new JMenuItem( "Cut" );
    menuItem.setMnemonic( KeyEvent.VK_T );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditCut" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Paste" );
    menuItem.setMnemonic( KeyEvent.VK_P );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditPaste" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Find" );
    menuItem.setMnemonic( KeyEvent.VK_F );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditFind" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Find Next" );
    menuItem.setMnemonic( KeyEvent.VK_X );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditFindNext" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );
*/


    ///////////////////////
    // Help Menu:
    JMenu helpMenu = new JMenu( "Help" );
    helpMenu.setMnemonic( KeyEvent.VK_H );
    helpMenu.setForeground( Color.white );
    helpMenu.setFont( mainFont );
    menuBar.add( helpMenu );

    menuItem = new JMenuItem( "About" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "HelpAbout" );
    menuItem.addActionListener( this );
    helpMenu.add( menuItem );

    menuItem = new JMenuItem( "Show Non-ASCII" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "HelpShowNonASCII" );
    menuItem.addActionListener( this );
    helpMenu.add( menuItem );

    setJMenuBar( menuBar );
    }



  public void actionPerformed( ActionEvent event )
    {
    try
    {
    // String paramS = event.paramString();

    String command = event.getActionCommand();

    if( command == null )
      {
      // keyboardTimerEvent();
      return;
      }

    // showStatus( "ActionEvent Command is: " + command );

    //////////////
    // File Menu:
    if( command == "FileList" )
      {
      String fileName = "\\jdk7hotspotmaster\\src\\share\\tools\\launcher\\java.c";
      TranslateCppFile.GetTokensFromFile( mApp,
                                         fileName );

      // listFiles();
      return;
      }

    if( command == "FileExit" )
      {
      System.exit( 0 );
      // return;
      }



    /////////////
    // Edit Menu:
    if( command == "EditCopy" )
      {
      editCopy();
      return;
      }

/*
    if( command == "EditCut" )
      {
      editCut();
      return;
      }

    if( command == "EditPaste" )
      {
      editPaste();
      return;
      }

    if( command == "EditFind" )
      {
      findText();
      return;
      }

    if( command == "EditFindNext" )
      {
      findTextNext();
      return;
      }
*/

    //////////////
    // Help Menu:
    if( command == "HelpAbout" )
      {
      showAboutBox();
      return;
      }

    if( command == "HelpShowNonASCII" )
      {
      showNonAsciiCharacters();
      return;
      }
    }
    catch( Exception e )
      {
      showStatus( "Exception in actionPerformed()." );
      showStatus( e.getMessage() );
      }
    }



  private void listFiles()
    {
    showStatus( "Listing the files." );

    File mainDirFile = new 
         File( "\\jdk7hotspotmaster\\src\\cpu\\x86\\vm" );
    String[] filesArray = mainDirFile.list();
    int max = filesArray.length;
    for( int count = 0; count < max; count++ )
      {
      showStatus( filesArray[count] );

      }

    }



/*
Execute C++ programs with this?
  private void runBuildFile()
    {
    try
    {
    showStatusTab();

    if( buildProcess != null )
      {
      showStatus( "The build file is already running." );
      return;
      }

    String runFile = mApp.projectConfigFile.getString(
                                          "BuildFile" );

    showStatus( "Running Build File: " + runFile );

    // java.lang.Runtime
    // java.lang.ProcessBuilder
    // java.lang.Process

    buildTimerCount = 0;
    buildProcess = Runtime.getRuntime().exec( runFile );
    if( buildProcess == null )
      {
      showStatus( "exec() returned null for the Build Process." );
      return;
      }

    showStatus( "Started Build File." );

    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in runBuildFile()." );
      showStatus( e.getMessage() );
      }
    }
*/




/*
  private void setupTimer()
    {
    int delay = 250;
    keyboardTimer = new Timer( delay, this );
    keyboardTimer.start();
    }
*/


/*
    // FileFilter filter = new FileNameExtensionFilter( "Text file", "txt" );
    // fc.setFileFilter( filter );
    // fc.addChoosableFileFilter( filter );
*/


/*
  private void checkBuildProcess()
    {
    if( buildProcess == null )
      return;

    boolean buildHasReturned = false;
    try
    {
    int returnVal = buildProcess.exitValue();
    buildHasReturned = true;
    }
    catch( IllegalThreadStateException e )
      {
      // Make sure there is no "pause" at the end
      // of the batch file.
      // It has not yet terminated.
      buildTimerCount++;
      if( (buildTimerCount % 4) == 1 )
        showStatus( "Build process is running. " + buildTimerCount );

      // Kill the process.
      // buildProcess.destroy();
      }

    if( buildHasReturned )
      {
      buildProcess = null;
      showBuildLog();
      }
    }
*/



/*
  private void keyboardTimerEvent()
    {
    try
    {
    // showStatus( "keyboardTimerEvent called." );
    // keyboardTimer.stop();

    if( windowIsClosing )
      {
      keyboardTimer.stop();
      return;
      }

    checkBuildProcess();
    checkExecutableProcess();

    // readFromExecutableOutput();

    int selectedIndex = mainTabbedPane.getSelectedIndex();

    // The status page is at zero.
    if( selectedIndex == 0 )
      {
      statusLabel.setText( "Status page." );
      return;
      }

    if( selectedIndex < 1 )
      {
      statusLabel.setText( "No text area selected." );
      return;
      }

    if( selectedIndex >= tabPagesArrayLast )
      {
      statusLabel.setText( "No text area selected." );
      return;
      }

    String tabTitle = tabPagesArray[selectedIndex].getTabTitle();

    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    int position = selectedTextArea.getCaretPosition();

    int line = selectedTextArea.getLineOfOffset( position );

    // The +1 is for display and matching with
    // the compiler error line number.
    line++;

    String showText = "Line: " + line +
                    "     " + tabTitle +
                    "      Proj: " + showProjectText;

    statusLabel.setText( showText );

    // keyboardTimer.start();
    }
    catch( Exception e )
      {
      showStatus( "Exception in keyboardTimerEvent()." );
      showStatus( e.getMessage() );
      }
    }
*/




  private void editCopy()
    {
    try
    {
    if( statusTextArea == null )
      return;

    statusTextArea.copy();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editCopy()." );
      showStatus( e.getMessage() );
      }
    }



/*
  private void editCut()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.cut();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editCut()." );
      showStatus( e.getMessage() );
      }
    }
*/


  /*
private void editPaste()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.paste();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editPaste()." );
      showStatus( e.getMessage() );
      }
    }
*/



  private void showAboutBox()
    {
    JOptionPane.showMessageDialog( this,
                 "Programming by Eric Chauvin.  Version date: " + MainApp.versionDate );

    }



  private void showNonAsciiCharacters()
    {
    /*

    Symbols:
        General Punctuation (2000206F)
        Superscripts and Subscripts (2070209F)
        Currency Symbols (20A020CF)
        Combining Diacritical Marks for Symbols (20D020FF)
        Letterlike Symbols (2100214F)
        Number Forms (2150218F)
        Arrows (219021FF)
        Mathematical Operators (220022FF)
        Miscellaneous Technical (230023FF)
        Control Pictures (2400243F)
        Optical Character Recognition (2440245F)
        Enclosed Alphanumerics (246024FF)
        Box Drawing (2500257F)
        Block Elements (2580259F)
        Geometric Shapes (25A025FF)
        Miscellaneous Symbols (260026FF)
        Dingbats (270027BF)
        Miscellaneous Mathematical Symbols-A (27C027EF)
        Supplemental Arrows-A (27F027FF)
        Braille Patterns (280028FF)
        Supplemental Arrows-B (2900297F)
        Miscellaneous Mathematical Symbols-B (298029FF)
        Supplemental Mathematical Operators (2A002AFF)
        Miscellaneous Symbols and Arrows (2B002BFF)

    // See the MarkersDelimiters.cs file.
    // Don't exclude any characters in the Basic
    // Multilingual Plane except these Dingbat characters
    // which are used as markers or delimiters.

    //    Dingbats (270027BF)

    // for( int Count = 0x2700; Count < 0x27BF; Count++ )
      // ShowStatus( Count.ToString( "X2" ) + ") " + Char.ToString( (char)Count ));

    // for( int Count = 128; Count < 256; Count++ )
      // ShowStatus( "      case (int)'" + Char.ToString( (char)Count ) + "': return " + Count.ToString( "X4" ) + ";" );


    // for( int Count = 32; Count < 256; Count++ )
      // ShowStatus( "    CharacterArray[" + Count.ToString() + "] = '" + Char.ToString( (char)Count ) + "';  //  0x" + Count.ToString( "X2" ) );

     // &#147;

    // ShowStatus( " " );
    */

    int getVal = 0x252F;
    showStatus( "Character: " + (char)getVal );
    }



/*
  private void findText()
    {
    try
    {
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( (selectedIndex < 1) ||
        (selectedIndex >= tabPagesArrayLast))
      {
      showStatusTab();
      showStatus( "No tab page selected." );
      return;
      }

    String sResult = (String)JOptionPane.showInputDialog(
                    this,
                    "Search for:",
                    "Search For", // Dialog title.
                    JOptionPane.PLAIN_MESSAGE,
                    null, // icon,
                    null, // possibilities to choose from.
                    "" ); // The default value.

    if( sResult == null )
      return;

    searchText = sResult.trim().toLowerCase();
    if( searchText.length() < 1 )
      return;

    // showStatus( "Search text: " + searchText );

    findTextNext();
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in findText()." );
      showStatus( e.getMessage() );
      }
    }
*/


/*
  private int searchTextMatches( int position,
                                 String textToSearch,
                                 String searchText )
    {
    int sLength = searchText.length();
    if( sLength < 1 )
      return -1;

    if( (position + sLength - 1) >= textToSearch.length() )
      return -1;

    for( int count = 0; count < sLength; count++ )
      {
      if( searchText.charAt( count ) != textToSearch.
                            charAt( position + count ) )
        return -1;

      }

    return position;
    }
*/


/*
  private void findTextNext()
    {
    try
    {
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( (selectedIndex < 1) ||
        (selectedIndex >= tabPagesArrayLast ))
      {
      showStatusTab();
      showStatus( "No tab page selected." );
      return;
      }

    if( searchText.length() < 1 )
      {
      showStatusTab();
      showStatus( "There is no searchText." );
      return;
      }

    // showStatus( "Search text: " + searchText );

    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      {
      showStatusTab();
      showStatus( "No text area selected." );
      return;
      }

    int start = selectedTextArea. getCaretPosition();
    if( start < 0 )
      start = 0;

    String textS = selectedTextArea.getText().toLowerCase();
    int textLength = textS.length();
    for( int count = start; count < textLength; count++ )
      {
      if( textS.charAt( count ) == searchText.charAt( 0 ) )
        {
        int where = searchTextMatches( count,
                                       textS,
                                       searchText );
        if( where >= 0 )
          {
          // showStatus( "Found at: " + where );
          selectedTextArea. setCaretPosition( where );
          return;
          }
        }
      }

    showStatusTab();
    showStatus( "Nothing found." );
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in findTextNext()." );
      showStatus( e.getMessage() );
      }
    }
*/


/*
  private void showBuildLog()
    {
    clearStatus();

    String fileName = mApp.projectConfigFile.getString( "ProjectDirectory" );
    fileName += "\\Build.log";

    showStatus( "Log file: " + fileName );
    // BuildLog Log = new BuildLog( FileName, this );
    // Log.ReadFromTextFile();

    String fileS = FileUtility.readAsciiFileToString(
                                         mApp,
                                         fileName,
                                         false );

    if( fileS == "" )
      {
      showStatus( "Nothing in the log file." );
      return;
      }

    fileS = fileS.trim();
    fileS = fileS + "\n";

    StringBuilder sBuilder = new StringBuilder();
    String[] lines = fileS.split( "\n" );
    for( int count = 0; count < lines.length; count++ )
      {
      showStatus( lines[count] );
      }
    }
*/




  }
