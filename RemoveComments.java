// Copyright Eric Chauvin 2018 - 2020.

// When you use star-slash comments you usually use
// it to block out a larger section of code.  Maybe
// even hundreds of lines.  So you can't worry about
// if there is a string literal somewhere in there
// that would cause a problem.

// This removes anything inside the star-slash
// comments no matter what it is.  Whether it's in
// a string literal or not.

// In C# code I have to define the string
// with separated characters like this:
// string StarSlash = "*" + "/";
// Otherwise it will interpret it as the end
// of a comment, not as part of a string literal.




public class RemoveComments
  {


  public static String removeAllComments( MainApp mApp,
                                     String inString )
    {
    String showError = "";
    String result = inString;
    
    // This fixes line splices too.  (Lines with 
    // an escaped newline character at the end.)
    // But the lines are spliced after it marks
    // the original line numbers so that the
    // programmer can see which line has an error
    // in it.
    result = markLineNumbers( mApp, result );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      showError = "There was an error marker after" +
                               " markLineNumbers.";

      // mApp.showStatus( result );
      mApp.showStatus( showError );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      showError = "TestBeginEnd returned false" +
                       " after markLineNumbers.";

      mApp.showStatus( showError );
      return "";
      }

    result = removeStarComments( mApp, result );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      showError = "There was an error marker after" +
                               " removeStarComments.";

      mApp.showStatus( showError );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      showError = "TestBeginEnd returned false" +
                       " after removeStarComments.";

      mApp.showStatus( showError );
      return "";
      }

    result = removeAllDoubleSlashComments( mApp,
                                           result );

    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      showError = "There was an error marker after" +
                     " removeAllDoubleSlashComments.";

      mApp.showStatus( showError );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      showError = "TestBeginEnd returned false" +
               " after removeAllDoubleSlashComments.";

      mApp.showStatus( showError );
      return "";
      }

    if( containsTriGraph( result ))
      {
      mApp.showStatus( "This file contains Trigraphs." );
      return "";
      }

    return result;
    }



  private static String markLineNumbers( MainApp mApp,
                                      String inString )
    {
    StringBuilder sBuilder = new StringBuilder();

    String[] splitS = inString.split( "\n" );
    int last = splitS.length;
    if( last == 0 )
      return "";

    for( int count = 0; count < last; count++ )
      {
      String line = splitS[count];
      String tLine = line.trim();
      if( tLine.length() == 0 )
        continue;

      if( tLine.equals( "*" + "/" ))
        {
        // Don't mark this empty line with a number.
        sBuilder.append( "*" + "/\n" );
        continue;
        }

      // Fix line splices.
      if( line.endsWith( "\\" ))
        {
        // This would be a bad idea, but somebody
        // could split a word right at the end
        // of a line and it should join the word
        // together with no space.
        // Not this: sBuilder.append( line + " " );
        sBuilder.append( line );
        continue;
        }

      int lineNumber = count + 1;
      line = line +
         Character.toString( Markers.Begin ) +
         Character.toString( Markers.TypeLineNumber ) +
         lineNumber +
         Character.toString( Markers.End ) +
         "\n";

      sBuilder.append( line );
      }

    return sBuilder.toString();
    }



  private static String removeStarComments( MainApp mApp,
                                    String inString )
    {
    // This ignores Markers.Begin, Markers.End
    // and any other markers.

    if( inString.trim().length() == 0 )
      return "";

    StringBuilder sBuilder = new StringBuilder();

    String SlashStar = "/" + "*";
    String StarSlash = "*" + "/";

    // This replaces the comment marker strings
    // anywhere and everywhere in the file.  Whether
    // they are inside quotes or not.
    inString = inString.replace( SlashStar, Character.toString( Markers.SlashStar ));
    inString = inString.replace( StarSlash, Character.toString( Markers.StarSlash ));

    boolean isInsideComment = false;
    int last = inString.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = inString.charAt( count );

      if( testChar == Markers.SlashStar )
        {
        if( isInsideComment )
          {
          // It shouldn't find this start marker
          // if it's already inside a comment.
          sBuilder.append( Character.toString( Markers.ErrorPoint ));

          mApp.showStatus( " " );
          mApp.showStatus( "Error with nested comment at: " + count );
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
          sBuilder.append( Character.toString( Markers.ErrorPoint ));

          mApp.showStatus( " " );
          mApp.showStatus( "Error with star-slash outside of a comment at: " + count );
          return sBuilder.toString();
          }

        sBuilder.append( Character.toString( testChar ));
        }
      }

    return sBuilder.toString();
    }



  private static boolean containsTriGraph( String line )
    {
    // There are also DiGraphs.
    // Digraph:        <%  %>   <:  :>  %:  %:%:
    // Punctuator:      {   }   [   ]   #    ##

    // To keep this file itself from containing
    // trigraphs it would have to be like:
    // testString = "?" + "?" + "=";
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
         Character.toString( Markers.EscapedSlash ));

    // Notice the escaped forward slash in front of
    // the quote character here at the end of the
    // string:
    // "c:\\BrowserECFiles\\PageFiles\\";

    line = line.replace( "\\\"",
                        Character.toString( 
                        Markers.EscapedDoubleQuote ));

    // This double quote inside single quotes can't
    // be inside of a normal string literal.
    String singleQuoteCharStr = "\'\"\'";
    if( singleQuoteCharStr.length() != 3 )
      {
      mApp.showStatus( "SingleQuoteCharStr.Length != 3" );
      return Character.toString( Markers.ErrorPoint );
      }

    line = line.replace( singleQuoteCharStr,
                    Character.toString(
                    Markers.QuoteAsSingleCharacter ));

    String doubleSlash = "/" + "/";
    line = line.replace( doubleSlash,
           Character.toString( Markers.DoubleSlash ));

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

    result = result.replace( 
            Character.toString( Markers.DoubleSlash ),
                                doubleSlash );

    result = result.replace(
                          Character.toString(
                     Markers.QuoteAsSingleCharacter ),
                     singleQuoteCharStr );

    result = result.replace( Character.toString(
                         Markers.EscapedDoubleQuote ),
                         "\\\"" );

    result = result.replace( Character.toString(
                             Markers.EscapedSlash ),
                             "\\\\" );

    return result;
    }



 }
