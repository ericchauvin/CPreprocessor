// Copyright Eric Chauvin 2018 - 2020.




public class MarkupForPreproc
  {

  public static String MarkItUp( MainApp mApp,
                                    String result )

    {
    // If each of these passes was done by a state
    // machine, then the state machine would be
    // relatively simple for each pass.

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


    ///////////
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
    result = markIdentifiers( mApp, result );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "There was an error marker after markIdentifiers." );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "TestBeginEnd returned false after markIdentifiers." );
      return "";
      }


    //////////
    result = markNumbers( result );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "There was an error marker after markNumbers." );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "TestBeginEnd returned false after markNumbers." );
      return "";
      }


    ////////
    result = markOperators( result );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "There was an error marker after markOperators." );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "TestBeginEnd returned false after markOperators." );
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
    // mApp.showStatus( "Preproc: " + in ); 
    in = StringsUtility.replaceFirstChar( in,
                                         '#',
                                         ' ' );

    // Does trimming it mess up a macro?
    in = in.trim();

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
    // A string can have zero length.  For example it
    // can be used in a printf function to tell it 
    // that the first thing is a string.
    // Like printf( "" + count + " that" );

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

    // This string marking is done before character
    // marking.  char thisQuoteChar = '"';
    // Don't use those quote characters that are
    // defined as characters.
    String singleQuoteCharStr = "\'\"\'";
    if( singleQuoteCharStr.length() != 3 )
      {
      mApp.showStatus( "SingleQuoteCharStr.length() != 3" );
      return Character.toString( Markers.ErrorPoint );
      }

    in = in.replace( singleQuoteCharStr,
                   Character.toString(
                   Markers.QuoteAsSingleCharacter ));

    boolean isInsideObject = false;
    boolean isInsideString = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        sBuilder.append( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.append( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.append( testChar );
        continue;
        }

      if( testChar == '"' )
        {
        if( isInsideString )
          {
          isInsideString = false;
          // It doesn't keep the quote character as
          // part of the marked string.
          sBuilder.append( Markers.End );
          continue;
          }
        else
          {
          isInsideString = true;
          sBuilder.append( Markers.Begin );
          sBuilder.append( Markers.TypeString );
          continue;
          }
        }

      sBuilder.append( testChar );
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

    // A character could look like this: '\n' or '\0'
    // But this is an integer cast to a char:
    //  (char)0x2710
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

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        sBuilder.append( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.append( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.append( testChar );
        continue;
        }

      if( testChar == '\'' )
        {
        if( isInsideChar )
          {
          sBuilder.append( Markers.End );
          isInsideChar = false;
          continue;
          }
        else
          {
          sBuilder.append( Markers.Begin );
          sBuilder.append( Markers.TypeChar );
          isInsideChar = true;
          continue;
          }
        }

      sBuilder.append( testChar );
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



  private static boolean isNumeral( char toTest )
    {
    if( (toTest >= '0') && (toTest <= '9'))
      return true;

    return false;
    }



  private static boolean isLetter( char toTest )
    {
    if( (toTest >= 'a') && (toTest <= 'z'))
      return true;

    if( (toTest >= 'A') && (toTest <= 'Z'))
      return true;

    // You could add Chinese characters or something
    // like that, so it's not just ASCII letters.

    return false;
    }



  private static String markIdentifiers( MainApp mApp,
                                         String in )
    {
    StringBuilder sBuilder = new StringBuilder();

    char previousChar = '\n';
    boolean isInsideID = false;
    boolean isInsideObject = false;
    int position = 0;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        if( isInsideID )
          {
          isInsideID = false;
          sBuilder.append( Markers.End );
          }

        sBuilder.append( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.append( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.append( testChar );
        continue;
        }

      if( count > 0 )
        previousChar = in.charAt( count - 1 );

      if( isInsideID )
        {
        position++;
        if( !isIdentifierCharacter( testChar,
                                    previousChar,
                                    position ))
          {
          isInsideID = false;
          sBuilder.append( Markers.End );
          sBuilder.append( testChar );
          continue;
          }
        }
      else
        {
        // It's not inside an ID.
        position = 0;
        if( isIdentifierCharacter( testChar,
                                   previousChar,
                                   position ))
          {
          isInsideID = true;
          sBuilder.append( Markers.Begin );
          sBuilder.append( Markers.TypeIdentifier );
          sBuilder.append( testChar );
          continue;
          }
        }

      sBuilder.append( testChar );
      }

    String result = sBuilder.toString();
    return result;
    }



  private static boolean isIdentifierCharacter(
                                   char toTest,
                                   char previousChar,
                                   int where )
    {
    // What's the maximum length of an identifier?
    if( where > 1024 )
      return false;

    if( where == 0 )
      {
      if( isNumeral( toTest ))
        return false;

      // No matter what the first character of the
      // identifier is, it can't start off
      // immediately following a numeral with no
      // space or anything between the two.
      // 123.45f;
      if( isNumeral( previousChar ))
        return false;

      // You can't _start_ an identifier with a
      // letter immediately before it.
      if( isLetter( previousChar ))
        return false;

      if( previousChar == '_' )
        return false;

      }

    if( (toTest == '_') ||
         isLetter( toTest ) ||
         isNumeral( toTest ))
      return true;

    return false;
    }



    // This is only the first approximation.
    // It will have to parse the whole number in a
    // later stage before it can figure out if it's
    // really a valid number or not.
    // Octal numbers are not used here.

  private static String markNumbers( String in )
    {
    StringBuilder sBuilder = new StringBuilder();

    char previousChar = '\n';
    char nextChar = '\n';
    boolean isInsideNumber = false;
    boolean isInsideObject = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        if( isInsideNumber )
          {
          isInsideNumber = false;
          sBuilder.append( Markers.End );
          }

        sBuilder.append( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.append( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.append( testChar );
        continue;
        }

      if( count > 0 )
        previousChar = in.charAt( count - 1 );

      if( (count + 1) < last )
        nextChar = in.charAt( count + 1 );
      else
        nextChar = '\n';

      if( !isInsideNumber )
        {
        if( isNumberStart( testChar ))
          {
          isInsideNumber = true;
          sBuilder.append( Markers.Begin );
          sBuilder.append( Markers.TypeNumber );
          sBuilder.append( testChar );
          continue;
          }

        sBuilder.append( testChar );
        continue;
        }

      // At this point it is inside a number.
      if( !isNumberCharacterContinued( testChar,
                                       previousChar,
                                       nextChar ))
        {
        isInsideNumber = false;
        sBuilder.append( Markers.End );
        sBuilder.append( testChar );
        continue;
        }

      // It is continuing inside a number.
      sBuilder.append( testChar );
      }

   
    return sBuilder.toString();
    }



  private static boolean isNumberStart( char testChar )
    {
    // At this point any numeral is necessarily a
    // number, since this is not looking inside
    // of identifiers or strings or characters.
    if( isNumeral( testChar ))
      return true;

    // If it is before the number starts then the
    // negative sign is an operator.  Possibly a
    // unary operator.
    // if( testChar == '-' )

    return false;
    }




  private static boolean isNumberCharacterContinued(
                                  char testChar,
                                  char previousChar,
                                  char nextChar )
    {
    if( isNumeral( testChar ))
      return true;

    // This would allow 1.2.3.4.5.6 and so on...
    if( testChar == '.' )
      return true;

    if( testChar == '-' )
      {
      if( !isNumeral( nextChar ))
        return false;

      // 18.46e-2
      if( !((previousChar == 'e') ||
            (previousChar == 'E')))
        return false;

      }

    // This would allow 0x0x0x0x0 and so on...
    if( (testChar == 'x') || (testChar == 'X'))
      {
      if( previousChar == '0' )
        return true;

      }

    // 18.46e-2d
    // 0xABCD
    if( (testChar == 'a') ||
        (testChar == 'A') ||
        (testChar == 'b') ||
        (testChar == 'B') ||
        (testChar == 'c') ||
        (testChar == 'C') ||
        (testChar == 'd') ||
        (testChar == 'D') ||
        (testChar == 'e') ||
        (testChar == 'E') ||
        (testChar == 'f') ||
        (testChar == 'F') ||
        (testChar == 'l') ||
        (testChar == 'L') ||
        (testChar == 'p') || // Binary exponent.
        (testChar == 'P') ||
        (testChar == 'u') ||
        (testChar == 'U'))
      {
      // 12lu is 12 as an unsigned long in C.

      // This still allows all kinds of things that
      // aren't numbers, like 01abcdefu0x.

      return true;
      }

    return false;
    }



    // I'm using the word 'operator' in a broader
    // sense here in this part, like for example a
    // parentheses or a comma.  But it will be
    // narrowed down later to different types of
    // operators, like the mathematical operators.

    // Ternary x?y:z
    // +=, -=, *=, /=,
    // *** Pointer to a pointer to a...

  private static String markOperators( String in )
    {
    StringBuilder sBuilder = new StringBuilder();

    char previousChar = ' ';
    boolean isInsideOp = false;
    boolean isInsideObject = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        if( isInsideOp )
          {
          isInsideOp = false;
          sBuilder.append( Markers.End );
          }

        sBuilder.append( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.append( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.append( testChar );
        continue;
        }

      if( count > 0 )
        previousChar = in.charAt( count - 1 );
      else
        previousChar = ' ';

=======
      if( TestChar == Markers.Begin )
        {
        if( IsInsideOp )
          {
          IsInsideOp = false;
          SBuilder.Append( Char.ToString(
                               Markers.End ));

          }

        IsInsideObject = true;
        SBuilder.Append( Char.ToString( TestChar ));
        continue;
        }

      if( TestChar == Markers.End )
        {
        IsInsideObject = false;
        SBuilder.Append( Char.ToString( TestChar ));
        continue;
        }

      if( IsInsideObject )
        {
        SBuilder.Append( Char.ToString( TestChar ));
        continue;
        }

      IsInsideOp = MarkOperator( SBuilder,
                                 PreviousChar,
                                 TestChar,
                                 IsInsideOp );
      }

    string Result = SBuilder.ToString();
    return Result;
    }


MarkOneOperator or what?
  private static bool MarkOperator( StringBuilder SBuilder,
                             char PreviousChar,
                             char TestChar,
                             bool IsInsideOp )
    {
    if( !IsInsideOp )
      {
      if( IsStartCharacter( TestChar ))
        {
        SBuilder.Append( Char.ToString(
                                   Markers.Begin ));
        SBuilder.Append( Char.ToString(
                         Markers.TypeOperator ));
        SBuilder.Append( Char.ToString( TestChar ));
        return true;
        }

      // It is not the start of an operator.
      SBuilder.Append( Char.ToString( TestChar ));
      return false;
      }

    // if( !IsInsideOp )
      // {
      // ShowStatus( "This can't happen with IsInsideOp." );
      // return false;
      // }

    // At this point IsInsideOp is true and it is
    // checking if this character continues a
    // two-character operation symbol.
    if( IsSecondOperatorCharacter( TestChar,
                                   PreviousChar ))
      {
      SBuilder.Append( Char.ToString( TestChar ));
      SBuilder.Append( Char.ToString( Markers.End ));
      return false;
      }

    // At this point it's not the continuation of
    // the last operator.
    SBuilder.Append( Char.ToString( Markers.End ));

    // IsInsideOp = false;

    // Is this the start of a new operator?
    if( IsStartCharacter( TestChar ))
      {
      // IsInsideOp = true;
      SBuilder.Append( Char.ToString(
                       Markers.Begin ));
      SBuilder.Append( Char.ToString(
                       Markers.TypeOperator ));
      SBuilder.Append( Char.ToString( TestChar ));
      return true;
      }

    // This is not the start of a new operator.
    SBuilder.Append( Char.ToString( TestChar ));
    return false;
    }



  private static bool IsStartCharacter( char TestChar )
    {
    // Preprocessing is not allowed here.
    if( TestChar == '#' )
      return false;

    if( TestChar == ',' )
      return true;

    if( TestChar == '+' )
      return true;

    if( TestChar == '-' )
      return true;

    if( TestChar == '*' )
      return true;

    if( TestChar == '/' )
      return true;

    if( TestChar == '%' )
      return true;

    if( TestChar == '=' )
      return true;

    if( TestChar == '>' )
      return true;

    if( TestChar == '<' )
      return true;

    if( TestChar == '.' )
      return true;

    if( TestChar == '!' )
      return true;

    if( TestChar == '~' )
      return true;

    if( TestChar == '&' )
      return true;

    if( TestChar == '|' )
      return true;

    if( TestChar == '^' )
      return true;

    if( TestChar == ':' )
      return true;

    if( TestChar == ';' )
      return true;

    if( TestChar == '(' )
      return true;

    if( TestChar == ')' )
      return true;

    if( TestChar == '[' )
      return true;

    if( TestChar == ']' )
      return true;

    return false;
    }




  private static bool IsSecondOperatorCharacter(
                                   char TestChar,
                                   char PreviousChar )
    {
    if( PreviousChar == '+' )
      {
      // ++
      if( TestChar == '+' )
        return true;

      return false;
      }

    if( PreviousChar == '-' )
      {
      // --
      if( TestChar == '-' )
        return true;

      return false;
      }

    if( PreviousChar == '=' )
      {
      // ==
      if( TestChar == '=' )
        return true;

      return false;
      }

    if( PreviousChar == '>' )
      {
      // >=
      if( TestChar == '=' )
        return true;

      // >>
      if( TestChar == '>' )
        return true;

      return false;
      }

    if( PreviousChar == '<' )
      {
      // <=
      if( TestChar == '=' )
        return true;

      // <<
      if( TestChar == '<' )
        return true;

      return false;
      }

    if( PreviousChar == '!' )
      {
      // !=
      if( TestChar == '=' )
        return true;

      return false;
      }

    if( PreviousChar == '&' )
      {
      // &&
      if( TestChar == '&' )
        return true;

      return false;
      }

    if( PreviousChar == '|' )
      {
      // ||
      if( TestChar == '|' )
        return true;

      return false;
      }

    return false;
    }



  }
