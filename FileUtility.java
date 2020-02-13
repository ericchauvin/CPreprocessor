// Copyright Eric Chauvin 2019 - 2020.




import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;



  public class FileUtility
  {

  public static String readFileToString( MainApp mApp,
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

    int nonAsciiCount = 0;
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

      // Handle UTF8 in the future.
      // Valid/verified UTF8.
      // Don't exclude any characters in the Basic
      // Multilingual Plane except markers that
      // shouldn't be in this.  See the Markers.cs
      // file.
      if( Markers.isMarker( (char)sChar ))
        sChar = ' ';


      // Basic Multilingual Plane
      // C0 Controls and Basic Latin (Basic Latin)
      //                                 (0000 007F)
      // C1 Controls and Latin-1 Supplement (0080 00FF)
      // Latin Extended-A (0100 017F)
      // Latin Extended-B (0180 024F)
      // IPA Extensions (0250 02AF)
      // Spacing Modifier Letters (02B0 02FF)
      // Combining Diacritical Marks (0300 036F)
      // General Punctuation (2000 206F)
      // Superscripts and Subscripts (2070 209F)
      // Currency Symbols (20A0 20CF)
      // Combining Diacritical Marks for Symbols
      //                                (20D0 20FF)
      // Letterlike Symbols (2100 214F)
      // Number Forms (2150 218F)
      // Arrows (2190 21FF)
      // Mathematical Operators (2200 22FF)
      // Box Drawing (2500 257F)
      // Geometric Shapes (25A0 25FF)
      // Miscellaneous Symbols (2600 26FF)
      // Dingbats (2700 27BF)
      // Miscellaneous Symbols and Arrows (2B00 2BFF)


      //  Don't go higher than D800 (Surrogates).
      // if( ToCheck >= 0xD800 )
      if( sChar > 126 ) // >= 0xD800
        {
        nonAsciiCount++;
        // Mark this as non-ASCII.
        sChar = Markers.ErrorPoint;
        }

      if( sChar < space )
        {
        // It ignores the \r character.
        if( !((sChar == newline) ||
              (sChar == tab )))
          continue;

        }

      sBuilder.append( (char)sChar );
      }

    String resultS = sBuilder.toString();
    if( nonAsciiCount == 0 )
      return resultS;
    else
      return "**** Non-ASCII *****\n" + resultS;

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

      if( sChar > 126 )
        continue;

        /*
        {
        nonAsciiCount++;
        sChar = 0x2700; // Mark this as non-ASCII.
        }
        */

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
