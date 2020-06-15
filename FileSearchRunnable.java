// Copyright Eric Chauvin 2020.



import java.io.File;




public class FileSearchRunnable  implements Runnable
  {
  private MainApp mApp;
  private String directory = "";
  private boolean recursive = true;
  private String fileToFind = "";



  private FileSearchRunnable()
    {
    }



  public FileSearchRunnable( MainApp appToUse,
                             String dirToUse,
                             boolean useRecursive,
                             String useFile )
    {
    mApp = appToUse;
    directory = dirToUse;
    recursive = useRecursive;
    fileToFind = useFile;
    }



  public void run()
    {
    try
    {
    // It has to be showStatusAsync().
    // mApp.showStatus( "Test async." );

    listFiles( directory );
    mApp.showStatusAsync( "\nFinished listing files." );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in FileSearchRunnable.run()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }



  private void listFiles( String dir )
    {
    // String d = "\\jdk7hotspotmaster\\src\\";
    // mApp.showStatusAsync( " " );
    // mApp.showStatusAsync( "// Listing: " + dir );
    // mApp.showStatusAsync( " " );

    File dirFile = new File( dir );
    String[] filesArray = dirFile.list();
    int max = filesArray.length;
    for( int count = 0; count < max; count++ )
      {
      // Thread.sleep( 1000 );
      if( Thread.interrupted() )
        {
        mApp.showStatusAsync( "Thread interrupted." );
        return;
        }

      String fileName = dir + "\\" + filesArray[count]; 

      File listFile = new File( fileName );
      if( listFile.isDirectory())
        {
        // mApp.showStatusAsync( " " );
        // mApp.showStatusAsync( "// dir: " + fileName );
        // mApp.showStatusAsync( " " );

        if( recursive )
          {
          listFiles( fileName );
          }

        continue;
        }

/*
      if( fileName.startsWith( 
                               d + "cpu\\sparc\\" ))
        continue;

      if( fileName.startsWith(
                                d + "cpu\\zero\\" ))
        continue;

      if( fileName.startsWith(
                                  d + "os\\bsd\\" ))
        continue;

      if( fileName.startsWith( 
                                 d + "os\\linux\\" ))
        continue;

      if( fileName.startsWith( 
                               d + "os\\solaris\\" ))
        continue;

      if( fileName.startsWith( 
                                d + "os\\posix\\" ))
        continue;

      if( fileName.startsWith( 
                         d + "os_cpu\\bsd_x86\\" ))
        continue;

      if( fileName.startsWith( 
                         d + "os_cpu\\bsd_zero\\" ))
        continue;

      if( fileName.startsWith( 
                       d + "os_cpu\\linux_sparc\\" ))
        continue;

      if( fileName.startsWith( 
                         d + "os_cpu\\linux_x86\\" ))
        continue;

      if( fileName.startsWith( 
                        d + "os_cpu\\linux_zero\\" ))
        continue;

      if( fileName.startsWith( 
                      d + "os_cpu\\solaris_sparc\\" ))
        continue;

      if( fileName.startsWith( 
                       d + "os_cpu\\solaris_x86\\" ))
        continue;
*/

      // if( fileName.contains( fileToFind ))
        mApp.showStatusAsync( fileName );

      }
    }



  }
