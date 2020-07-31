// Copyright Eric Chauvin 2019 - 2020.



// C/C++ Preprocessor, written in Java.


import javax.swing.SwingUtilities;



public class MainApp implements Runnable
  {
  public static final String versionDate = "7/31/2020";
  private MainWindow mainWin;
  // public ConfigureFile mainConfigFile;
  private String[] argsArray;
  private StrA programDirectory;



  public static void main( String[] args )
    {
    MainApp mApp = new MainApp( args );
    SwingUtilities.invokeLater( mApp );
    }



  @Override
  public void run()
    {
    setupProgram();
    }



  private MainApp()
    {
    }


  public MainApp( String[] args )
    {
    argsArray = args;
    }


  public StrA getProgramDirectory()
    {
    return programDirectory;
    }



  private void setupProgram()
    {
    // checkSingleInstance()

    // All programs need to have a batch file give
    // it the program directory, so it's not stuck
    // to that fixed directory. 
    programDirectory = new StrA( 
                          "\\EricMain\\CPreprocessor\\" );

    int length = argsArray.length;
    if( length > 0 )
      programDirectory = new StrA( argsArray[0] );

    // String mainConfigFileName = programDirectory +
    //                            "MainConfigure.txt";

    // mainConfigFile = new ConfigureFile( this,
    //                            mainConfigFileName );

    mainWin = new MainWindow( this, "C Preprocessor" );
    mainWin.initialize();

    // showStatusAsync( "argsArray length: " + length );
    // for( int count = 0; count < length; count++ )
      // showStatusAsync( argsArray[count] );


    showStatusAsync( "\nProgram Directory:\n" +
                                   programDirectory );
    }



  public void showStatusAsync( String toShow )
    {
    if( mainWin == null )
      return;

    mainWin.showStatusAsync( toShow );
    }



  public void clearStatus()
    {
    if( mainWin == null )
      return;

    mainWin.clearStatus();
    }




  }
