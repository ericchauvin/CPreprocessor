// Copyright Eric Chauvin 2019 - 2020.


/*
java.nio.file.Files

    public static Path createDirectory(
             Path dir, FileAttribute<?>... attrs)
This creates all the parent directories if they
are not there.
    public static Path createDirectories(Path dir, FileAttribute<?>... attrs)
        throws IOException


    public static Path copy(Path source, Path target, CopyOption... options)


*/


/*
java.io.File
    public File(String pathname) {

    public String getName()
    // Get the parent directory name.
    public String getParent()

    // Gets the parent directory as a File.
    public File getParentFile()
    public String getPath()
    public boolean exists()

    public boolean isDirectory()
    public boolean isFile()
    public boolean isHidden()

    // in milliseconds since the epoch
    //(00:00:00 GMT, January 1, 1970),
    public long lastModified()
    public long length()
    public boolean delete()

    If it's not a directory then it returns null.
    public String[] list()

    public String[] list(FilenameFilter filter)

    public boolean mkdir()

    public boolean renameTo(File dest)

   // For a script or batch file?
    public boolean setExecutable(boolean executable, boolean ownerOnly)


    public boolean canExecute()

    public long getTotalSpace()
    public long getFreeSpace()
    public long getUsableSpace()
    public Path toPath()

*/


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;



  public class FileUtility
  {

  public static String readAsciiFileToString( MainApp mApp,
                                              String fileName,
                                              boolean keepTabs )
    {
    try
    {
    Path path = Paths.get( fileName );

    if( !Files.exists( path, LinkOption.NOFOLLOW_LINKS ))
      {
      return "";
      }

    byte[] fileArray;
    fileArray = Files.readAllBytes( path );

    StringBuilder sBuilder = new StringBuilder();

    // There is a String constructor that reads a byte array
    // using a particular character set, but this tests it
    // to make sure it doesn't have bad data in it.

    short newline = (short)'\n';
    short space = (short)' ';
    short tab = (short)'\t';
    int max = fileArray.length;
    for( int count = 0; count < max; count++ )
      {
      short sChar = Utility.ByteToShort( fileArray[count] );

      if( !keepTabs )
        {
        if( sChar == tab )
          sChar = space;

        }

      if( sChar > 127 )
        continue; // Don't want this character.

      if( sChar < space )
        {
        if( !((sChar == newline) ||
              (sChar == tab )))
          continue;

        }

      sBuilder.append( (char)sChar );
      }

    return sBuilder.toString();
    }
    catch( Exception e )
      {
      mApp.showStatus( "Could not read the file: \n" + fileName );
      mApp.showStatus( e.getMessage() );
      return "";
      }
    }




  public static void writeAsciiStringToFile( MainApp mApp,
                                             String fileName,
                                             String textS,
                                             boolean keepTabs )
    {
    try
    {
    if( textS == null )
      return;

    if( textS.trim().length() < 1 )
      return;

    Path path = Paths.get( fileName );

    char newline = '\n';
    char space = ' ';
    char tab = '\t';
    StringBuilder sBuilder = new StringBuilder();
    int max = textS.length();
    for( int count = 0; count < max; count++ )
      {
      char sChar = textS.charAt( count );
      if( sChar > 127 )
        continue;

      if( !keepTabs )
        {
        if( sChar == tab )
          sChar = space;

        }

      if( sChar < space )
        {
        if( !((sChar == newline) ||
              (sChar == tab )))
          continue;

        }

      sBuilder.append( sChar );
      }

    String outString = sBuilder.toString();
    if( outString == null )
      return;

    if( outString.trim().length() < 1 )
      return;

    char[] outCharArray = outString.toCharArray();
    byte[] outBuffer = new byte[outCharArray.length];
    max = outCharArray.length;
    for( int count = 0; count < max; count++ )
      {
      outBuffer[count] = (byte)outCharArray[count];
      }

    Files.write( path, outBuffer,  StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE );

    }
    catch( Exception e )
      {
      mApp.showStatus( "Could not write to the file: \n" + fileName );
      mApp.showStatus( e.getMessage() );
      }
    }



  }
