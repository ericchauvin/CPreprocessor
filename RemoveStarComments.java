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




public class RemoveStarComments
  {


  public static String removeAllComments( MainApp mApp,
                                     String inString )
    {
    String result = inString;

    result = markLineNumbers( mApp, result );
    result = removeComments( mApp, result );
    // Result = FixLineSplices();

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



  private static String removeComments( MainApp mApp,
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



/*
Do something like this?

  private void FixLineSplices()
    {
    int Last = MainSArray.GetLast();
    StringBuilder SBuilder = new StringBuilder();
    for( int Count = 0; Count < Last; Count++ )
      {
      string Line = MainSArray.GetStringAt( Count );
      // Something could be in a string literal.
      // So this would have to be tested after
      // strings are in tokens.
      if( ContainsTriGraph( Line ))
        throw( new Exception( "Is this an actual Trigraph in here?\r\n" + Line ));

      string DoubleSlash = "/" + "/";
      if( Line.Contains( DoubleSlash ))
        Line = RemoveDoubleSlashComments( Line );

      // Using \n to be compatible with Linux.
      SBuilder.Append( Line.TrimEnd() + "\n" );
      }

    string FileS = SBuilder.ToString();

    // This would be a bad idea, but somebody
    // could split a word right at the end
    // of a line and it should join the word
    // together with no space.
    // FileS = FileS.Replace( "\\\n", " " );
    FileS = FileS.Replace( "\\\n", "" );

    // Remove the beginning and ending white space.
    FileS = FileS.Trim();

    MainSArray.Clear();
    string[] FileLines = FileS.Split( new Char[] { '\n' } );

    Last = FileLines.Length;
    for( int Count = 0; Count < Last; Count++ )
      MainSArray.AppendStringToArray( FileLines[Count] );

    }



  private bool ContainsTriGraph( string Line )
    {
    // There are also DiGraphs.
    // Digraph:        <%  %>   <:  :>  %:  %:%:
    // Punctuator:      {   }   [   ]   #    ##

    // Test:
    // if( Line.Contains( "??" ))
      // return true;

    if( Line.Contains( "??=" ))
      return true;

    if( Line.Contains( "??/" ))
      return true;

    if( Line.Contains( "??'" ))
      return true;

    if( Line.Contains( "??(" ))
      return true;

    if( Line.Contains( "??)" ))
      return true;

    if( Line.Contains( "??!" ))
      return true;

    if( Line.Contains( "??<" ))
      return true;

    if( Line.Contains( "??>" ))
      return true;

    if( Line.Contains( "??-" ))
      return true;

    return false;
    }
*/



 }
