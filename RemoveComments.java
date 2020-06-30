// Copyright Eric Chauvin 2018 - 2020.


// This removes anything inside the star-slash
// comments no matter what it is.  Whether it's in
// a string literal or not.

// In C# code (for example) I have to define the
// string with separated characters like this:
// string StarSlash = "*" + "/";
// Otherwise it will interpret it as the end
// of a comment, not as part of a string-literal.
// That's how it should be written inside a string-
// literal.



import java.io.File;



public class RemoveComments
  {

  public static StrA removeAllComments( MainApp mApp,
                                     StrA in,
                                     String fileName )
    {
    String showError = "";
    StrA result = in;

    File file = new File( fileName );
    fileName = file.getName();
    // String pathName = file.getPath();
    // showStatus( "Path name picked is: " + pathName );

    // This fixes line splices too.  (Lines with 
    // a newline character at the end.)
    result = markLineNumbers( mApp, result, fileName );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatusAsync( " " );
      showError = "There was an error marker after" +
                               " markLineNumbers.";

      // mApp.showStatusAsync( result );
      mApp.showStatusAsync( showError );
      return "";
      }

    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      showError = "\n\nTestBeginEnd returned false" +
                       " after markLineNumbers.";

      mApp.showStatusAsync( showError );
      return "";
      }

    result = removeStarComments( mApp, result );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatusAsync( " " );
      showError = "There was an error marker after" +
                               " removeStarComments.";

      mApp.showStatusAsync( showError );
      return "";
      }

    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      mApp.showStatusAsync( " " );
      showError = "TestBeginEnd returned false" +
                       " after removeStarComments.";

      mApp.showStatusAsync( showError );
      return "";
      }

    result = removeAllDoubleSlashComments( mApp,
                                           result );

    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatusAsync( " " );
      showError = "There was an error marker after" +
                     " removeAllDoubleSlashComments.";

      mApp.showStatusAsync( showError );
      return "";
      }

    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      mApp.showStatusAsync( " " );
      showError = "TestBeginEnd returned false" +
               " after removeAllDoubleSlashComments.";

      mApp.showStatusAsync( showError );
      return "";
      }

    return result;
    }



  private static StrA markLineNumbers( MainApp mApp,
                                     StrA in,
                                     StrA fileName )
    {
    if( in.length() == 0 )
      return new StrA( "" );

    StrABld sBuilder = new StrABld();

    StrArray splitS = in.splitChar( '\n' );
    final int last = splitS.length();
    if( last == 0 )
      return new StrA( "" );

    StrA starSlash = new StrA( "*" + "/" );
    StrA newline = new StrA( "\n" );
    StrA starSlashNewline = starSlash.concat( newline );


    for( int count = 0; count < last; count++ )
      {
      StrA line = splitS.getStrAt( count );
      StrA tLine = line.trim();
      if( tLine.length() == 0 )
        continue;

      if( tLine.equals( starSlash ))
        {
        // Don't mark this empty line with a number.
        sBuilder.appendStrA( starSlashNewline );
        continue;
        }

      // Fix line splices.
      if( line.endsWith( "\\" ))
        {
        // This would be a bad idea, but somebody
        // could split an identifier right at the end
        // of a line and it should (according to
        // the specs) join the word
        // together with no space.
        int lineLength = line.length();
        if( lineLength == 1 )
          {
          // The line contains only the slash character.
          line = "";
          }
        else
          {
          // substring( int begin, int end )
          line = line.substring( 0, lineLength - 2 );
          }

        sBuilder.append( line );
        continue;
        }

      int lineNumber = count + 1;
      line = "" + line +
         Markers.Begin +
         Markers.TypeLineNumber +
         lineNumber +
         ";" +
         fileName +
         Markers.End +
         "\n";

      sBuilder.append( line );
      }

    return sBuilder.toString();
    }



  private static String removeStarComments( MainApp mApp,
                                    String in )
    {
    // This ignores Markers.Begin, Markers.End
    // and any other markers.

    if( in.trim().length() == 0 )
      return "";

    StringBuilder sBuilder = new StringBuilder();

    // There is a form of comment like this:
    String strangeComment = "/" + "/" + "*" + "*";
    String twoSlashes = "/" + "/";
    in = in.replace( strangeComment, twoSlashes );

    String slashStar = "/" + "*";
    String starSlash = "*" + "/";

    // This replaces the comment marker strings
    // anywhere and everywhere in the file.  Whether
    // they are inside quotes or not.
    in = in.replace( slashStar,
                            "" + Markers.SlashStar );

    in = in.replace( starSlash,
                            "" + Markers.StarSlash );


    boolean isInsideComment = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      if( testChar == Markers.SlashStar )
        {
        if( isInsideComment )
          {
          // It shouldn't find this start marker
          // if it's already inside a comment.
          sBuilder.append( "" + Markers.ErrorPoint );

          mApp.showStatusAsync( " " );
          mApp.showStatusAsync( "Error with nested comment at: " + count );
          return sBuilder.toString();
          }

        isInsideComment = true;
        // Replace a comment with white space so
        // that identifier strings don't come
        // together.
        sBuilder.append( " " );
        continue;
        }

      if( testChar == Markers.StarSlash )
        {
        isInsideComment = false;
        continue;
        }

      if( !isInsideComment )
        {
        if( testChar == Markers.StarSlash )
          {
          // It shouldn't find this end marker
          // if it's not already inside a comment.
          sBuilder.append( "" + Markers.ErrorPoint );

          mApp.showStatusAsync( " " );
          mApp.showStatusAsync( "Error with star-slash outside of a comment at: " + count );
          return sBuilder.toString();
          }

        sBuilder.append( "" + testChar );
        }
      }

    return sBuilder.toString();
    }



/*
  Trigraphs were used back in the days of teletype
  machines, before they had full keyboards.
  It would be _very_ old code it if had trigraphs
  in it.
  private static boolean containsTriGraph( String line )
    {
    // There are also DiGraphs.
    // Digraph:        <%  %>   <:  :>  %:  %:%:
    // Punctuator:      {   }   [   ]   #    ##

    if( line.contains( "??=" ))
      return true;

    if( line.contains( "??/" ))
      return true;

    if( line.contains( "??'" ))
      return true;

    if( line.contains( "??(" ))
      return true;

    if( line.contains( "??)" ))
      return true;

    if( line.contains( "??!" ))
      return true;

    if( line.contains( "??<" ))
      return true;

    if( line.contains( "??>" ))
      return true;

    if( line.contains( "??-" ))
      return true;

    return false;
    }
*/



  private static String removeAllDoubleSlashComments(
                                       MainApp mApp,
                                       String in )
    {
    StringBuilder sBuilder = new StringBuilder();
    String[] splitS = in.split( "\n" );
    int last = splitS.length;
    for( int count = 0; count < last; count++ )
      {
      String line = splitS[count];
      line = removeDoubleSlashComments( mApp, line );

      String startString =
              Character.toString( Markers.Begin ) +
              Character.toString( 
                             Markers.TypeLineNumber );

      // Don't keep empty lines.
      String tLine = line.trim();
      if( !tLine.startsWith( startString ))
        sBuilder.append( line + "\n" );

      }

    return sBuilder.toString();
    }



  private static String removeDoubleSlashComments(
                                        MainApp mApp,
                                        String line )
    {
    // This line with the two back slashes in the
    // URL should not be considered to be a comment.
    //     GetPage( "https://gcc.gnu.org/" );

    StringBuilder sBuilder = new StringBuilder();

    line = line.replace( "\\\\",
                         "" + Markers.EscapedSlash );

    // Notice the escaped forward slash in front of
    // the quote character here at the end of the
    // string:
    // "c:\\BrowserECFiles\\PageFiles\\";

    line = line.replace( "\\\"",
                    "" + Markers.EscapedDoubleQuote );

    // This double quote inside single quotes can't
    // be inside of a normal string literal.
    String singleQuoteCharStr = "\'\"\'";
    if( singleQuoteCharStr.length() != 3 )
      {
      mApp.showStatusAsync( "SingleQuoteCharStr.Length != 3" );
      return Character.toString( Markers.ErrorPoint );
      }

    line = line.replace( singleQuoteCharStr,
                "" + Markers.QuoteAsSingleCharacter );

    String doubleSlash = "/" + "/";
    line = line.replace( doubleSlash,
                           "" + Markers.DoubleSlash );

    int lineLength = line.length();
    boolean isInside = true;
    boolean isInsideString = false;
    for( int count = 0; count < lineLength; count++ )
      {
      char testChar = line.charAt( count );
      if( isInsideString )
        {
        if( testChar == '"' )
          isInsideString = false;

        }
      else
        {
        // It's not inside the string.
        if( testChar == '"' )
          isInsideString = true;

        }

      if( !isInsideString )
        {
        if( testChar == Markers.DoubleSlash )
          {
          // This will stay false until it gets to
          // the Begin marker for the line number.
          isInside = false;
          }
        }

      // This is for the line number markers.
      if( testChar == Markers.Begin )
        isInside = true;

      if( isInside )
        sBuilder.append( testChar );

      }

    String result = sBuilder.toString();

    result = result.replace( "" + Markers.DoubleSlash,
                              doubleSlash );

    result = result.replace( "" + Markers.QuoteAsSingleCharacter,
                  singleQuoteCharStr );

    result = result.replace( "" + Markers.EscapedDoubleQuote,
                     "\\\"" );

    result = result.replace( "" + Markers.EscapedSlash,
                           "\\\\" );

    return result;
    }



 }
