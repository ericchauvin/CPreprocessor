// Copyright Eric Chauvin 2020.



import java.io.File;


/*

Make a files dictionary.
endsWith()
#include "this\that.h" shows the key for the file.
So get its path to go with the key.
*/


// while (t.isAlive()) {
//            Still waiting

*/


public class FileSearchRunnable  implements Runnable
  {
  private MainApp mApp;
  private String directory = "";



  private FileSearchRunnable()
    {
    }



  public FileSearchRunnable( MainApp appToUse,
                             String dirToUse )
    {
    mApp = appToUse;
    directory = dirToUse;
    }



  public void run()
    {
    try
    {
    // It has to be showStatusAsync().
    // mApp.showStatus( "Test async." );

    listFiles( directory, false );

/*
    for( int count = 0; count < 10; count++ )
      {
      if( Thread.interrupted() )
        {
        mApp.showStatusAsync( "Thread interrupted." );
        return;
        }

      mApp.showStatusAsync( "Showing it async " +
                                              count );
      Thread.sleep( 1000 );
      }
*/

    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in FileSearchRunnable.run()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }


  private void listFiles( String dir,
                          boolean recursive )
    {
    mApp.showStatusAsync( " " );
    mApp.showStatusAsync( "Listing: " + dir );

    File dirFile = new File( dir );
    String[] filesArray = dirFile.list();
    int max = filesArray.length;
    for( int count = 0; count < max; count++ )
      {
      String fileName = dir + "\\" + filesArray[count]; 
      if( fileName.contains( "\\jdk7hotspotmaster\\src\\cpu\\sparc" ))
        continue;

      if( fileName.contains( "\\jdk7hotspotmaster\\src\\os\\solaris\\" ))
        continue;

      File listFile = new File( fileName );
      if( recursive )
        {
        if( listFile.isDirectory())
          {
          listFiles( fileName, recursive );
          continue;
          }
        }

      mApp.showStatusAsync( fileName );
      }

    }



  }
