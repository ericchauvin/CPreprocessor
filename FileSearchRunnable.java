// Copyright Eric Chauvin 2020.



import java.io.File;




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

    listFiles( directory, true );

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
    String d = "\\jdk7hotspotmaster\\src\\";
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
          listFiles( fileName, recursive );
          }

        continue;
        }

      if( fileName.startsWith( 
         "\\jdk7hotspotmaster\\src\\cpu\\sparc\\" ))
        continue;

      if( fileName.startsWith(
            "\\jdk7hotspotmaster\\src\\cpu\\zero\\" ))
        continue;

      if( fileName.startsWith(
             "\\jdk7hotspotmaster\\src\\os\\bsd\\" ))
        continue;

      if( fileName.startsWith( 
            "\\jdk7hotspotmaster\\src\\os\\linux\\" ))
        continue;

      if( fileName.startsWith( 
          "\\jdk7hotspotmaster\\src\\os\\solaris\\" ))
        continue;

      if( fileName.startsWith( 
        "\\jdk7hotspotmaster\\src\\os\\posix\\" ))
        continue;

      if( fileName.startsWith( 
        "\\jdk7hotspotmaster\\src\\os_cpu\\bsd_x86\\" ))
        continue;

      if( fileName.startsWith( 
        "\\jdk7hotspotmaster\\src\\os_cpu\\bsd_zero\\" ))
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


      mApp.showStatusAsync( fileName );
      }
    }



  }
