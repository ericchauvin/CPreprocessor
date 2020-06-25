// Copyright Eric Chauvin 2019 - 2020.



// C/C++ Preprocessor, written in Java.


import javax.swing.SwingUtilities;



public class MainApp implements Runnable
  {
  public static final String versionDate = "6/25/2020";
  private MainWindow mainWin;
  // public ConfigureFile mainConfigFile;
  private String[] argsArray;



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



  private void setupProgram()
    {
    // checkSingleInstance()
 
    /*
    String programDirectory = "\\Eric\\CPreprocessor\\";
    int length = argsArray.length;
    if( length > 0 )
      programDirectory = argsArray[0];

    String mainConfigFileName = programDirectory +
                                      "MainConfigure.txt";

    mainConfigFile = new ConfigureFile( this,
                                mainConfigFileName );
    */

    mainWin = new MainWindow( this, "C Preprocessor" );
    mainWin.initialize();

    /*
    showStatus( " " );
    showStatus( "argsArray length: " + length );
    for( int count = 0; count < length; count++ )
      showStatus( argsArray[count] );
    */

    // showStatus( " " );
    }



  public void showStatusAsync( String toShow )
    {
    if( mainWin == null )
      return;

    mainWin.showStatusAsync( toShow );
    }


/*
  public void showStatus( String toShow )
    {
    if( mainWin == null )
      return;

    mainWin.showStatus( toShow );
    }
*/


  public void clearStatus()
    {
    if( mainWin == null )
      return;

    mainWin.clearStatus();
    }




  }
