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
                                     StrA fileName )
    {
    String showError = "";
    StrA result = in;

    File file = new File( fileName.toString() );
    fileName = new StrA( file.getName());
    // String pathName = file.getPath();
    // showStatus( "Path name picked is: " + pathName );

    // This fixes line splices too.  (Lines with 
    // a newline character at the end.)
    result = markLineNumbers( mApp, result, fileName );
    if( result.containsChar( Markers.ErrorPoint ))
      {
      showError = "\n\nThere was an error marker after" +
                               " markLineNumbers.";

      // mApp.showStatusAsync( result );
      mApp.showStatusAsync( showError );
      return StrA.Empty;
      }

    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      showError = "\n\nTestBeginEnd returned false" +
                       " after markLineNumbers.";

      mApp.showStatusAsync( showError );
      return StrA.Empty;
      }

    result = removeStarComments( mApp, result );
    if( result.containsChar( Markers.ErrorPoint ))
      {
      showError = "\n\nThere was an error marker after" +
                               " removeStarComments.";

      mApp.showStatusAsync( showError );
      return StrA.Empty;
      }

    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      showError = "\n\nTestBeginEnd returned false" +
                       " after removeStarComments.";

      mApp.showStatusAsync( showError );
      return StrA.Empty;
      }

    result = removeAllDoubleSlashComments( mApp,
                                           result );

    if( result.containsChar( Markers.ErrorPoint ))
      {
      showError = "\n\nThere was an error marker after" +
                     " removeAllDoubleSlashComments.";

      mApp.showStatusAsync( showError );
      return StrA.Empty;
      }

    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      showError = "\n\nTestBeginEnd returned false" +
               " after removeAllDoubleSlashComments.";

      mApp.showStatusAsync( showError );
      return StrA.Empty;
      }

    return result;
    }



  private static StrA markLineNumbers( MainApp mApp,
                                     StrA in,
                                     StrA fileName )
    {
    if( in.length() == 0 )
      return StrA.Empty;

    StrABld sBuilder = new StrABld( 1024 );

    StrArray splitS = in.splitChar( '\n' );
    final int last = splitS.length();
    if( last == 0 )
      return StrA.Empty;

    StrA starSlash = new StrA( "*" + "/" );
    StrA newline = new StrA( "\n" );
    StrA starSlashNewline = starSlash.concat( newline );


    for( int count = 0; count < last; count++ )
      {
      StrA line = splitS.getStrAt( count );
      StrA tLine = line.trim();
      if( tLine.length() == 0 )
        continue;

      if( tLine.equalTo( starSlash ))
        {
        // Don't mark this empty line with a number.
        sBuilder.appendStrA( starSlashNewline );
        continue;
        }

      // Fix line splices.
      if( line.endsWithChar( '\\' ))
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
          line = StrA.Empty;
          }
        else
          {
          // substring( int begin, int end )
          line = line.substring( 0, lineLength - 2 );
          }

        sBuilder.appendStrA( line );
        continue;
        }

      int lineNumber = count + 1;
      line = new StrA( "" + line.toString() +
         Markers.Begin +
         Markers.TypeLineNumber +
         lineNumber +
         ";" +
         fileName +
         Markers.End +
         "\n" );

      sBuilder.appendStrA( line );
      }

    return sBuilder.toStrA();
    }



  private static StrA removeStarComments(
                                      MainApp mApp,
                                      StrA in )
    {
    // This ignores Markers.Begin, Markers.End
    // and any other markers.

    if( in.trim().length() == 0 )
      return StrA.Empty;

    StrABld sBuilder = new StrABld( 1024 );

    // There is a form of comment like this:
    StrA strangeComment = new StrA(
                             "/" + "/" + "*" + "*" );

    StrA twoSlashes = new StrA( "/" + "/" );
    in = in.replace( strangeComment, twoSlashes );

    StrA slashStar = new StrA( "/" + "*" );
    StrA starSlash = new StrA( "*" + "/" );

    // This replaces the comment marker strings
    // anywhere and everywhere in the file.  Whether
    // they are inside quotes or not.
    in = in.replace( slashStar, new StrA(
                           "" + Markers.SlashStar ));

    in = in.replace( starSlash, new StrA( 
                            "" + Markers.StarSlash ));


    boolean isInsideComment = false;
    final int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      if( testChar == Markers.SlashStar )
        {
        if( isInsideComment )
          {
          // It shouldn't find this start marker
          // if it's already inside a comment.
          sBuilder.appendChar( Markers.ErrorPoint );

          mApp.showStatusAsync( "\n\nError with nested comment at: " + count );
          return sBuilder.toStrA();
          }

        isInsideComment = true;
        // Replace a comment with white space so
        // that identifier strings don't come
        // together.
        sBuilder.appendChar( ' ' );
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
          sBuilder.appendChar( Markers.ErrorPoint );

          mApp.showStatusAsync( "\n\nError with star-slash outside of a comment at: " + count );
          return sBuilder.toStrA();
          }

        sBuilder.appendChar( testChar );
        }
      }

    return sBuilder.toStrA();
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



  private static StrA removeAllDoubleSlashComments(
                                       MainApp mApp,
                                       StrA in )
    {
    StrABld sBuilder = new StrABld( 1024 );
    StrArray splitS = in.splitChar( '\n' );
    final int last = splitS.length();
    for( int count = 0; count < last; count++ )
      {
      StrA line = splitS.getStrAt( count );
      line = removeDoubleSlashComments( mApp, line );

      StrA startS = new StrA( "" + Markers.Begin +
                 Markers.TypeLineNumber );

      // Don't keep empty lines.
      StrA tLine = line.trim();
      if( !tLine.startsWith( startS ))
        {
        sBuilder.appendStrA( line );
        sBuilder.appendStrA( new StrA( "\n" ));
        }
      }

    return sBuilder.toStrA();
    }



  private static StrA removeDoubleSlashComments(
                                        MainApp mApp,
                                        StrA line )
    {
    // This line with the two back slashes in the
    // URL should not be considered to be a comment.
    //     GetPage( "https://gcc.gnu.org/" );

    StrABld sBuilder = new StrABld( 1024 );

    line = line.replace( new StrA( "\\\\" ), new StrA( 
                       "" + Markers.EscapedSlash ));

    // Notice the escaped forward slash in front of
    // the quote character here at the end of the
    // string:
    // "c:\\BrowserECFiles\\PageFiles\\";

    line = line.replace( new StrA( "\\\"" ),
                    new StrA(
                  "" + Markers.EscapedDoubleQuote ));

    // This double quote inside single quotes can't
    // be inside of a normal string literal.
    StrA singleQuoteCharStr = new StrA( "\'\"\'" );
    if( singleQuoteCharStr.length() != 3 )
      {
      mApp.showStatusAsync( "SingleQuoteCharStr.Length != 3" );
      return new StrA( "" + Markers.ErrorPoint );
      }

    line = line.replace( singleQuoteCharStr,
                new StrA(
              "" + Markers.QuoteAsSingleCharacter ));

    StrA doubleSlash = new StrA( "/" + "/" );
    line = line.replace( doubleSlash, new StrA(
                           "" + Markers.DoubleSlash ));

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
        sBuilder.appendChar( testChar );

      }

    StrA result = sBuilder.toStrA();

    result = result.replace( new StrA( 
             "" + Markers.DoubleSlash ),
                         new StrA( doubleSlash ));

    result = result.replace( new StrA(
              "" + Markers.QuoteAsSingleCharacter ),
              new StrA( singleQuoteCharStr ));

    result = result.replace( new StrA(
               "" + Markers.EscapedDoubleQuote ),
               new StrA( "\\\"" ));

    result = result.replace( new StrA( 
                          "" + Markers.EscapedSlash ),
                          new StrA( "\\\\" ));

    return result;
    }



 }
