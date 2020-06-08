// Copyright Eric Chauvin 2019 - 2020.


// Code Analysis for C++ code, written in Java.


import javax.swing.SwingUtilities;
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
  private Thread fileThread;
  private String uiThreadName = "";


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
    uiThreadName = Thread.currentThread().getName();

    mainFont = new Font( Font.MONOSPACED, Font.PLAIN,
                                                 40 );

    setDefaultCloseOperation( WindowConstants.
                                      EXIT_ON_CLOSE );

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
    String allTogether = 
             "Programming by Eric Chauvin.\n" + 
             "Version date: " +
             MainApp.versionDate + "\n\n";

    showStatusAsync( allTogether );
 
    // setupTimer();
    }



  public void windowStateChanged( WindowEvent e )
    {
    // showStatusAsync( "windowStateChanged" );
    }


  public void windowGainedFocus( WindowEvent e )
    {
    // showStatusAsync( "windowGainedFocus" );
    }


  public void windowLostFocus( WindowEvent e )
    {
    // showStatusAsync( "windowLostFocus" );
    }



  public void windowOpened( WindowEvent e )
    {
    // showStatusAsync( "windowOpened" );
    }



  public void windowClosing( WindowEvent e )
    {
    windowIsClosing = true;
    }


  public void windowClosed( WindowEvent e )
    {
    // showStatusAsync( "windowClosed" );
    }


  public void windowIconified( WindowEvent e )
    {
    // keyboardTimer.stop();
    // showStatusAsync( "windowIconified" );
    }



  public void windowDeiconified( WindowEvent e )
    {
    // keyboardTimer.start();
    // showStatusAsync( "windowDeiconified" );
    }



  public void windowActivated( WindowEvent e )
    {
    // showStatusAsync( "windowActivated" );
    }


  public void windowDeactivated( WindowEvent e )
    {
    // showStatusAsync( "windowDeactivated" );
    }



  private void addComponents( Container pane )
    {
    // The red colors are for testing.
    this.setBackground( Color.red );
    this.setForeground( Color.red );

    pane.setBackground( Color.black );
    pane.setForeground( Color.red );
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



  public void showStatusAsync( String toShow )
    {
    SwingUtilities.invokeLater( new Runnable()
      {
      public void run()
        {
        showStatusA( toShow );
        }
      });
    }



  private void showStatusA( String toShow )
    {
    if( statusTextArea == null )
      return;

    String thisThreadName = Thread.currentThread().
                                           getName();

    if( !uiThreadName.equals( thisThreadName ))
      {
      String showWarn = "showStatusA() is being" +
                " called from the wrong thread.\n\n";
 
      statusTextArea.setText( showWarn );
      return;
      }

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
    // statusTextArea.setEditable( false );
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

    menuItem = new JMenuItem( "Test" );
    menuItem.setMnemonic( KeyEvent.VK_T );
    menuItem.setActionCommand( "FileTest" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Cancel" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setActionCommand( "FileCancel" );
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

    // showStatusAsync( "ActionEvent Command is: " + command );

    //////////////
    // File Menu:
    if( command == "FileTest" )
      {
      // String fileName = "\\jdk7hotspotmaster\\src\\share\\tools\\launcher\\java.c";
      // String fileName = "\\Eric\\CodeAnalysisCpp\\main.cpp";

      testFiles();

      // listHeaderFiles();

      return;
      }

    if( command == "FileCancel" )
      {
      showStatusAsync( "Cancel got called." );
      if( fileThread != null )
        {
        showStatusAsync( "Canceling..." );
        fileThread.interrupt();
        fileThread = null;
        return;
        }
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
      showStatusAsync( "Exception in actionPerformed()." );
      showStatusAsync( e.getMessage() );
      }
    }



  private void listHeaderFiles()
    {
    // String dir = "\\jdk7hotspotmaster\\src";
    String dir = "\\cygwin64\\usr\\include";

    // Add it to the semicolon delimited dictionary file.
    // endsWith( fileToFind )
    String fileToFind = ""; // this/that.h";
    listFiles( dir, fileToFind );
    }



  private void listFiles( String dir,
                          String fileToFind )
    {
    try
    {
    if( fileThread != null )
      {
      // If it's not still doing something.
      if( !fileThread.isAlive())
        {
        fileThread = null;
        }
      else
        {
        showStatusAsync( "The thread is already running." );
        return;
        }
      }

// ===== fileToFind
    FileSearchRunnable fileSearch = new 
                       FileSearchRunnable( mApp,
                       dir );

    fileThread = new Thread( fileSearch );
    fileThread.start();
    }
    catch( Exception e )
      {
      showStatusAsync( "Exception in listFiles()." );
      showStatusAsync( e.getMessage() );
      }
    }



/*
Execute a C++ program with this.
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

    showStatusAsync( "Running Build File: " + runFile );

    // java.lang.Runtime
    // java.lang.ProcessBuilder
    // java.lang.Process

    buildTimerCount = 0;
    buildProcess = Runtime.getRuntime().exec( runFile );
    if( buildProcess == null )
      {
      showStatusAsync( "exec() returned null for the Build Process." );
      return;
      }

    showStatusAsync( "Started Build File." );

    }
    catch( Exception e )
      {
      showStatusTab();
      showStatusAsync( "Exception in runBuildFile()." );
      showStatusAsync( e.getMessage() );
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
        showStatusAsync( "Build process is running. " + buildTimerCount );

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
    // showStatusAsync( "keyboardTimerEvent called." );
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
      showStatusAsync( "Exception in keyboardTimerEvent()." );
      showStatusAsync( e.getMessage() );
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
      showStatusAsync( "Exception in editCopy()." );
      showStatusAsync( e.getMessage() );
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
      showStatusAsync( "Exception in editCut()." );
      showStatusAsync( e.getMessage() );
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
      showStatusAsync( "Exception in editPaste()." );
      showStatusAsync( e.getMessage() );
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
      // ShowStatusAsync( Count.ToString( "X2" ) + ") " + Char.ToString( (char)Count ));

    // for( int Count = 128; Count < 256; Count++ )
      // ShowStatusAsync( "      case (int)'" + Char.ToString( (char)Count ) + "': return " + Count.ToString( "X4" ) + ";" );


    // for( int Count = 32; Count < 256; Count++ )
      // ShowStatusAsync( "    CharacterArray[" + Count.ToString() + "] = '" + Char.ToString( (char)Count ) + "';  //  0x" + Count.ToString( "X2" ) );

     // &#147;

    // ShowStatusAsync( " " );
    */

    int getVal = 0x252F;
    showStatusAsync( "Character: " + (char)getVal );
    }




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



  private void testFiles()
    {
    try
    {
    // This test code will be replaced with a 
    // PreprocessProject.java object, which would 
    // preprocess a whole project and it would have
    // configuration files and header file lists
    // as part of the project.

 
    String mainDir = "C:\\jdk7hotspotmaster\\src\\share\\vm\\code\\";
    // String mainDir = "C:\\gccmaster\\gcc\\";
    String outDir = "C:\\PreprocessOut\\";

    // This list of files would be different for 
    // different projects.  Like if it's a Linux
    // or Windows project.

    String projectFileList = 
           "\\Eric\\CodeAnalysisJava\\FileList.txt";

    HeaderFileDictionary headerDictionary = new
                        HeaderFileDictionary( mApp );

    headerDictionary.readFileList( projectFileList );

    String[] fileArray = { "codeBlob.cpp",
                           "codeBlob.hpp",
                           "codeCache.cpp",
                           "codeCache.hpp",
/*
                           "compiledIC.cpp",
                           "compiledIC.hpp",
                           "compressedStream.cpp",
                           "compressedStream.hpp",
                           "debugInfo.cpp",
                           "debugInfo.hpp",
                           "debugInfoRec.cpp",
                           "debugInfoRec.hpp",
                           "dependencies.cpp",
                           "dependencies.hpp",
                           "exceptionHandlerTable.cpp",
                           "exceptionHandlerTable.hpp",
                           "icBuffer.cpp",
                           "icBuffer.hpp",
                           "jvmticmlr.h",
                           "location.cpp",
                           "location.hpp",
                           "nmethod.cpp",
                           "nmethod.hpp",
                           "oopRecorder.cpp",
                           "oopRecorder.hpp",
                           "pcDesc.cpp",
                           "pcDesc.hpp",
                           "relocInfo.cpp",
                           "relocInfo.hpp",
                           "scopeDesc.cpp",
                           "scopeDesc.hpp",
                           "stubs.cpp",
                           "stubs.hpp",
                           "vmreg.cpp",
                           "vmreg.hpp",
                           "vtableStubs.cpp",
*/
                           "vtableStubs.hpp" };


    int max = fileArray.length;
    for( int count = 0; count < max; count++ )
      {
      String fileName = mainDir + fileArray[count];
      String outFileName = outDir + fileArray[count];
      MacroDictionary macroDictionary = new
                           MacroDictionary( mApp );

      // Make a configuration file for these.

      // Target Architechture.
      // "Zero-Assembler Project: Zero is a port of
      // OpenJDK that uses no assembler and therefore
      // can trivially be built on any system."

      // TARGET_ARCH_zero, TARGET_ARCH_arm,
      Macro macro = new Macro( mApp );
      macro.setMacroWithEmptyParams( "TARGET_ARCH_x86" );
      macroDictionary.setMacro( "TARGET_ARCH_x86",
                                macro );

      // JIT compilers.    
      // C1 compiler.  Bytecode compiler.
      // C2 compiler.  Called opto?  Higher
      // optimization.

      macro = new Macro( mApp );
      macro.setMacroWithEmptyParams( "COMPILER1" );
      macroDictionary.setMacro( "COMPILER1", macro );

      // .pch is Precompiled header.
      // or  .gch for Gnu.  Gnu compiled header.

      String test = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary,
                                    headerDictionary );

      if( test.length() == 0 )
        {
        showStatusAsync( " " );
        showStatusAsync( "The file had an error." );
        showStatusAsync( fileName );
        return;
        }

      FileUtility.writeStringToFile( mApp,
                                     outFileName,
                                     test,
                                     false );

      }

    showStatusAsync( " " );
    showStatusAsync( "Finished processing files." );
    showStatusAsync( " " );

    }
    catch( Exception e )
      {
      showStatusAsync( "Exception in testFiles()." );
      showStatusAsync( e.getMessage() );
      }
    }




  }
