// Copyright Eric Chauvin 2018 - 2020.


// https://en.wikipedia.org/wiki/Typedef

// https://en.wikipedia.org/wiki/C_preprocessor


 


public class MarkupForPreproc
  {

  public static String MarkItUp( MainApp mApp,
                                    String result )

    {

    result = markStrings( mApp, result );

    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "There was an error after markStrings." );
      mApp.showStatus( " " );
      mApp.showStatus( result );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "TestBeginEnd returned false after mareStrings." );
      return "";
      }


    result = markCharacters( mApp, result );

    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "There was an error marker after markCharacters." );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "TestBeginEnd returned false after markCharacters." );
      return "";
      }


    /////////////
    mApp.showStatus( result );
    mApp.showStatus( " " );
    mApp.showStatus( "Finished markup." );
    mApp.showStatus( " " );
    mApp.showStatus( " " );
    return result;




/*
    Result = CSharpToIdentifiers.MakeIdentifierObjects( MForm, Result );
    if( !MForm.CheckEvents())
      return null;

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToIdentifiers." );
      return null;
      }

    Result = CSharpToNumbers.MakeNumberObjects( Result );
    if( !MForm.CheckEvents())
      return null;

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToNumbers." );
      return null;
      }

    Result = CSharpToOperators.MakeOperatorObjects( Result );
    if( !MForm.CheckEvents())
      return null;

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToOperators." );
      return null;
      }




// After macros replace identifiers and all that.
// So this below doesn't get done here.
// This is just markup.
    Result = TestMarkers.RemoveOutsideWhiteSpace( Result );

    if( !TestMarkers.TestBrackets( MForm, Result ))
      {
      // ShowStatus( Result );
      return null;
      }

    IDDictionary IdentDictionary = new
                               IDDictionary( MForm );

    // CSharpFixIdentifiers CSFixIDs = new
    //                    CSharpFixIdentifiers( MForm,
    //                                IdentDictionary );

    // if( !CSFixIDs.GetIdentifiers( Result ))
      // {
      // ShowStatus( "GetIdentifiers returned false." );
      // return "";
      // }

    // Result = CSFixIDs.MakeIdentifiersLowerCase( Result );

    // IdentDictionary.ShowIDs();

    // CSFixIDs = null;
    if( !MForm.CheckEvents())
      return null;


    Result = BracketLevel.SetLevelChars( MForm, Result );
    if( Result == "" )
      {
      // ShowStatus( Result );
      return null;
      }


    Token Tk = new Token( MForm );
    Tk.AddTokensFromString( Result );
    Tk.SetLowestTokenBlocks();

    ShowStatus( " " );
    Tk.ShowTokensAtLevel( 1 );

    return Tk;
    */

    }




  public static String markPreprocessorLines(
                                      MainApp mApp,
                                      String in )
    {
    if( in == null )
      return "";

    if( in.trim().length() == 0 )
      return "";

    StringBuilder sBuilder = new StringBuilder();
     
    String[] splitS = in.split( "\n" );
    int last = splitS.length;
    for( int count = 0; count < last; count++ )
      {
      String line = splitS[count];
      if( '#' == StringsUtility.firstNonSpaceChar(
                                             line ))
        {
        String preProcLine = makeBasicPreprocessorMarks(
                                           mApp,
                                           line );

        sBuilder.append( preProcLine );
        }
      else
        {
        sBuilder.append( line + "\n" );
        }
      }

    return sBuilder.toString();
    }



  private static String makeBasicPreprocessorMarks(
                                      MainApp mApp,
                                      String in )
    {
    // return in + "\n";

    // mApp.showStatus( "Preproc: " + in ); 
    in = StringsUtility.replaceFirstChar( in,
                                         '#',
                                         ' ' );

    mApp.showStatus( "Preproc: " + in ); 
   
    String[] splitS = in.split( Character.toString(
                                  Markers.Begin ));

    int last = splitS.length;
    if( last != 2 )
      {
      String showError = in + "\n" +
           "This preprocessor line should have" +
           " exactly one Begin marker for the line" +
           " number."; 

      mApp.showStatus( showError ); 
      return "ErrorPoint: " + Markers.ErrorPoint;
      }
   
    String result = Character.toString(
                    Markers.Begin ) +
                    Markers.TypePreprocessor +
                    splitS[0] +
                    Markers.End +
                    Markers.Begin +
                    splitS[1] + "\n";

    return result;
    }





  private static String markStrings( MainApp mApp,
                                     String in )
    {
    // In C, a wide character string literal looks
    // like:  L"This string." with the L in front of
    // it.

    StringBuilder sBuilder = new StringBuilder();

    // Notice the double slash in front of the quote
    // character here at the end of the string:
    // "c:\\BrowserECFiles\\PageFiles\\";

    in = in.replace( "\\\\",
                             Character.toString(
                             Markers.EscapedSlash ));

    in = in.replace( "\\\"",
                                  Character.toString(
                        Markers.EscapedDoubleQuote ));

    String singleQuoteCharStr = "\'\"\'";
    if( singleQuoteCharStr.length() != 3 )
      {
      mApp.showStatus( "SingleQuoteCharStr.length() != 3" );
      return Character.toString( Markers.ErrorPoint );
      }

    // Is this the spec?
    // This means that the double quote inside single
    // quotes will just be put inside the string
    // literal.
    in = in.replace( singleQuoteCharStr,
                   Character.toString(
                   Markers.QuoteAsSingleCharacter ));

    boolean isInsideObject = false;
    boolean isInsideString = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      if( testChar == Markers.Begin )
        {
        isInsideObject = true;
        sBuilder.append( Character.toString( testChar ));
        continue;
        }

      if( isInsideObject )
        {
        // You can't go inside another object without
        // finding the end of the string.
        if( isInsideString )
          {
          sBuilder.append( Character.toString(
                        Markers.ErrorPoint ));
          sBuilder.append( "String doesn't end before the next object." );
          return sBuilder.toString();
          }

        if( testChar == Markers.End )
          isInsideObject = false;

        sBuilder.append( Character.toString( testChar ));
        continue;
        }

      if( !isInsideString )
        {
        if( testChar == '"' )
          {
          isInsideString = true;
          sBuilder.append( Character.toString(
                                    Markers.Begin ));
          sBuilder.append( Character.toString(
                                Markers.TypeString ));
          continue;
          }
        }
      else
        {
        // It is inside.
        if( testChar == '"' )
          {
          isInsideString = false;
          sBuilder.append( Character.toString(
                                      Markers.End ));
          continue;
          }
        }

      sBuilder.append( Character.toString( testChar ));
      }

    String result = sBuilder.toString();

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



  private static String markCharacters( MainApp mApp,
                                        String in )
    {
    StringBuilder sBuilder = new StringBuilder();

    in = in.replace( "\\\\",
       Character.toString( Markers.EscapedSlash ));

    // It could be '\''.
    in = in.replace( "\\\'",
       Character.toString( Markers.EscapedSingleQuote ));

    boolean isInsideChar = false;
    boolean isInsideObject = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      if( isInsideObject )
        {
        // You can't go inside another object without
        // finding the end of the character.
        if( isInsideChar )
          {
          sBuilder.append( Character.toString(
                        Markers.ErrorPoint ));
          sBuilder.append( "Character doesn't end before next object." );
          return sBuilder.toString();
          }

        if( testChar == Markers.End )
          isInsideObject = false;

        sBuilder.append( Character.toString( testChar ));
        continue;
        }

      if( testChar == Markers.Begin )
        {
        isInsideObject = true;
        sBuilder.append( Character.toString( testChar ));
        continue;
        }

      if( !isInsideChar )
        {
        if( testChar == '\'' )
          {
          isInsideChar = true;
          sBuilder.append( Character.toString(
                             Markers.Begin ));
          sBuilder.append( Character.toString(
                          Markers.TypeChar ));
          continue;
          }
        }
      else
        {
        // It is inside.
        if( testChar == '\'' )
          {
          isInsideChar = false;
          sBuilder.append( Character.toString(
                               Markers.End ));
          continue;
          }
        }

      sBuilder.append( Character.toString( testChar ));
      }

    String result = sBuilder.toString();

    // Put the single quote character back in.
    result = result.replace( Character.toString(
             Markers.EscapedSingleQuote ), "\\\'" );

    result = result.replace(
                Character.toString( Markers.EscapedSlash ),
                "\\\\" );

    return result;
    }



  }
