// Copyright Eric Chauvin 2018 - 2020.



public class MarkupString
  {

  public static StrA MarkItUp( MainApp mApp,
                                    StrA result )

    {
    result = markStrings( mApp, result );
    if( !testMarkers( result, new StrA(
                "markStrings() at top." ), mApp ))
      return StrA.Empty;

    result = markCharacters( mApp, result );
    if( !testMarkers( result, new StrA(
                      "markCharacters()." ), mApp ))
      return StrA.Empty;

    result = markIdentifiers( mApp, result );
    if( !testMarkers( result, new StrA(
                     "markIdentifiers()." ), mApp ))
      return StrA.Empty;

    result = markNumbers( result );
    if( !testMarkers( result, new StrA( 
                           "markNumbers()." ), mApp ))
      return StrA.Empty;

    result = markOperators( result );
    if( !testMarkers( result, new StrA( 
                         "markOperators()." ), mApp ))
      return StrA.Empty;

    result = removeOutsideWhiteSpace( result );
    if( !testMarkers( result, new StrA( 
               "removeOutsideWhiteSpace()." ), mApp ))
      return StrA.Empty;

    // mApp.showStatus( result );
    // mApp.showStatus( " " );
    return result;
    }



  public static boolean testMarkers( StrA testS, 
                                     StrA errorS,
                                     MainApp mApp )
    {
    if( testS.length() == 0 )
      {
      mApp.showStatusAsync( "Testmarkers() had a zero length input string.\n" + errorS );
      return false;
      }

    if( testS.contains( new StrA( 
                      "" + Markers.ErrorPoint )))
      {
      mApp.showStatusAsync( " " );
      mApp.showStatusAsync( "There was an error after: " + errorS );
      mApp.showStatusAsync( " " );
      mApp.showStatusAsync( testS.toString() );
      return false;
      }

    if( !testBeginEnd( mApp, testS ))
      {
      mApp.showStatusAsync( " " );
      mApp.showStatusAsync( "TestBeginEnd returned false after: " + errorS );
      return false;
      }

    return true;
    }



  private static StrA markStrings( MainApp mApp,
                                     StrA in )
    {
    // A wide character string literal looks
    // like:  L"This string." with the L in front of
    // it.

    StrABld sBuilder = new StrABld( 1024 );

    // Notice the double slash in front of the quote
    // character here at the end of the string:
    // "c:\\BrowserECFiles\\PageFiles\\";

    in = in.replace( new StrA( "\\\\" ),
                     new StrA( "" + 
                             Markers.EscapedSlash ));

    in = in.replace( new StrA( "\\\"" ),
                     new StrA( "" + 
                        Markers.EscapedDoubleQuote ));

    // This string marking is done before character
    // marking.  char thisQuoteChar = '"';
    // Don't use those quote characters that are
    // defined as characters.
    StrA singleQuoteCharStr = new StrA( "\'\"\'" );

    in = in.replace( singleQuoteCharStr,
                   new StrA( "" + 
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
        sBuilder.appendChar( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.appendChar( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.appendChar( testChar );
        continue;
        }

      if( testChar == '"' )
        {
        if( isInsideString )
          {
          isInsideString = false;
          // It doesn't keep the quote character as
          // part of the marked string.
          sBuilder.appendChar( Markers.End );
          continue;
          }
        else
          {
          isInsideString = true;
          sBuilder.appendChar( Markers.Begin );
          sBuilder.appendChar( Markers.TypeString );
          continue;
          }
        }

      sBuilder.appendChar( testChar );
      }

    StrA result = sBuilder.toStrA();

    result = result.replace(
                     new StrA( "" + 
                     Markers.QuoteAsSingleCharacter ),
                     singleQuoteCharStr );

    result = result.replace( new StrA( "" +
                       Markers.EscapedDoubleQuote ),
                       new StrA( "\\\"" ));

    result = result.replace( new StrA( "" +
                            Markers.EscapedSlash ),
                            new StrA( "\\\\" ));
    if( isInsideString )
      return result.concat( new StrA(
            "\nNever found the end quote." +
            Markers.ErrorPoint ));

    return result;
    }



  private static StrA markCharacters( MainApp mApp,
                                        StrA in )
    {
    StrABld sBuilder = new StrABld( 1024 );

    // A character could look like this: '\n' or '\0'
    // But this is an integer cast to a char:
    //  (char)0x2710
    in = in.replace( new StrA( "\\\\" ),
                     new StrA( "" + 
                     Markers.EscapedSlash ));

    // It could be '\''.
    in = in.replace( new StrA( "\\\'" ),
                     new StrA( "" +
                     Markers.EscapedSingleQuote ));

    boolean isInsideChar = false;
    boolean isInsideObject = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        sBuilder.appendChar( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.appendChar( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.appendChar( testChar );
        continue;
        }

      if( testChar == '\'' )
        {
        if( isInsideChar )
          {
          sBuilder.appendChar( Markers.End );
          isInsideChar = false;
          continue;
          }
        else
          {
          sBuilder.appendChar( Markers.Begin );
          sBuilder.appendChar( Markers.TypeChar );
          isInsideChar = true;
          continue;
          }
        }

      sBuilder.appendChar( testChar );
      }

    StrA result = sBuilder.toStrA();

    // Put the single quote character back in.
    result = result.replace( new StrA( "" +
                    Markers.EscapedSingleQuote ),
                    new StrA( "\\\'" ));

    result = result.replace( new StrA( "" +
                  Markers.EscapedSlash ),
                  new StrA( "\\\\" ));

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



  private static StrA markIdentifiers( MainApp mApp,
                                       StrA in )
    {
    StrABld sBuilder = new StrABld( 1024 );

    char previousChar = '\n';
    boolean isInsideID = false;
    boolean isInsideObject = false;
    int position = 0;
    final int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        if( isInsideID )
          {
          isInsideID = false;
          sBuilder.appendChar( Markers.End );
          }

        sBuilder.appendChar( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.appendChar( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.appendChar( testChar );
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
          sBuilder.appendChar( Markers.End );
          sBuilder.appendChar( testChar );
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
          sBuilder.appendChar( Markers.Begin );
          sBuilder.appendChar( Markers.TypeIdentifier );
          sBuilder.appendChar( testChar );
          continue;
          }
        }

      sBuilder.appendChar( testChar );
      }

    // If the ID went to the end of the line.
    if( isInsideID )
      sBuilder.appendChar( Markers.End );

    StrA result = sBuilder.toStrA();
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

// See if it's valid.
// float a = Float.parseFloat( SomeString );

  private static StrA markNumbers( StrA in )
    {
    StrABld sBuilder = new StrABld( 1024 );

    char previousChar = '\n';
    char nextChar = '\n';
    boolean isInsideNumber = false;
    boolean isInsideObject = false;
    final int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        if( isInsideNumber )
          {
          isInsideNumber = false;
          sBuilder.appendChar( Markers.End );
          }

        sBuilder.appendChar( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.appendChar( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.appendChar( testChar );
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
          sBuilder.appendChar( Markers.Begin );
          sBuilder.appendChar( Markers.TypeNumber );
          sBuilder.appendChar( testChar );
          continue;
          }

        sBuilder.appendChar( testChar );
        continue;
        }

      // At this point it is inside a number.
      if( !isNumberCharacterContinued( testChar,
                                       previousChar,
                                       nextChar ))
        {
        isInsideNumber = false;
        sBuilder.appendChar( Markers.End );
        sBuilder.appendChar( testChar );
        continue;
        }

      // It is continuing inside a number.
      sBuilder.appendChar( testChar );
      }

    // If the number went to the end of the line.
    if( isInsideNumber )
      sBuilder.appendChar( Markers.End );
   
    return sBuilder.toStrA();
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
    // operators.

    // Ternary x?y:z
    // +=, -=, *=, /=,
    // *** Pointer to a pointer to a... indefintely.

  private static StrA markOperators( StrA in )
    {
    StrABld sBuilder = new StrABld( 1024 );

    boolean isInsideObject = false;
    final int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      // It can't have nested Begin/End markers.
      if( testChar == Markers.Begin )
        {
        sBuilder.appendChar( testChar );
        isInsideObject = true;
        continue;
        }

      if( testChar == Markers.End )
        {
        sBuilder.appendChar( testChar );
        isInsideObject = false;
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.appendChar( testChar );
        continue;
        }

      if( isBasicOpCharacter( testChar ))
        {
        sBuilder.appendChar( Markers.Begin );
        sBuilder.appendChar( Markers.TypeOperator );
        sBuilder.appendChar( testChar );
        sBuilder.appendChar( Markers.End );
        continue;
        }

      sBuilder.appendChar( testChar );
      }

    return sBuilder.toStrA();
    }



  private static boolean isBasicOpCharacter(
                                      char testChar )
    {
    // This does not look inside of marked strings,
    // characters, identifiers, or numbers.

    // How to mess up a language:
    // C++ user defined literals 
    // long double operator"" _kg( long double x ) 

    if( testChar == '#' )
      return true;

    if( testChar == ',' )
      return true;

    if( testChar == '+' )
      return true;

    if( testChar == '-' )
      return true;

    if( testChar == '*' )
      return true;

    if( testChar == '/' )
      return true;

    if( testChar == '%' )
      return true;

    if( testChar == '=' )
      return true;

    if( testChar == '>' )
      return true;

    if( testChar == '<' )
      return true;

    if( testChar == '.' )
      return true;

    if( testChar == '!' )
      return true;

    if( testChar == '~' )
      return true;

    if( testChar == '&' )
      return true;

    if( testChar == '|' )
      return true;

    if( testChar == '^' )
      return true;

    if( testChar == '?' )
      return true;

    if( testChar == ':' )
      return true;

    if( testChar == ';' )
      return true;

    if( testChar == '{' )
      return true;

    if( testChar == '}' )
      return true;

    if( testChar == '(' )
      return true;

    if( testChar == ')' )
      return true;

    if( testChar == '[' )
      return true;

    if( testChar == ']' )
      return true;

    return false;
    }



  public static boolean testBeginEnd( MainApp mApp,
                                      StrA in )
    {
    if( in.length() == 0 )
      return true;

    // If it is not marked up at all.
    if( 0 == Markers.countMarkers( in ))
      return true;

    StrABld sBuilder = new StrABld( 1024 );

    StrArray splitS = in.splitChar( Markers.Begin );

    // This can be a partially marked up line.
    // Like if only the line numbers have been added.
    StrA testEmpty = splitS.getStrAt( 0 );
    if( testEmpty.length() != 0 )
      {
      sBuilder.appendStrA( testEmpty );
      sBuilder.appendStrA( new StrA( "\n" ));
      }

    final int last = splitS.length();
    // This starts at 1, at the first
    // Markers.Begin.
    for( int count = 1; count < last; count++ )
      {
      StrA line = splitS.getStrAt( count );
      sBuilder.appendStrA( line );
      sBuilder.appendStrA( new StrA( "\n" ));
      int howMany = countEndMarkers( line );
      if( 1 != howMany )
        {
        // mApp.showStatusAsync( sBuilder.toString() );
        mApp.showStatusAsync( "\n\n(" + howMany + ") The line should have one end marker." );
        mApp.showStatusAsync( "Line: " + line.toString() );
        return false;
        }
      }

    return true;
    }



  public static int countEndMarkers( StrA in )
    {
    int howMany = 0;

    final int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      if( Markers.End == in.charAt( count ))
        howMany++;

      }

    return howMany;
    }



  public static StrA removeOutsideWhiteSpace( StrA in )
    {
    StrABld sBuilder = new StrABld( 1024 );

    boolean isInsideObject = false;
    final int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      if( testChar == Markers.Begin )
        {
        isInsideObject = true;
        sBuilder.appendChar( testChar );
        continue;
        }

      if( testChar == Markers.End )
        {
        isInsideObject = false;
        sBuilder.appendChar( testChar );
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.appendChar( testChar );
        continue;
        }

      if( testChar == ' ' )
        continue;

      if( testChar == '\n' )
        continue;

      sBuilder.appendStrA( new StrA(
                 "Right here >" + testChar + "<" ));

      sBuilder.appendChar( testChar );
      sBuilder.appendChar( Markers.ErrorPoint );
      return sBuilder.toStrA();
      }

    return sBuilder.toStrA();
    }



  }
