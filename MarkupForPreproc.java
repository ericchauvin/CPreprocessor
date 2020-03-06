// Copyright Eric Chauvin 2018 - 2020.



public class MarkupForPreproc
  {
  private MainApp mApp;
  StringArray fileLines;
  StringArray outFileLines;
  DefinesDictionary definesDictionary;



  private MarkupForPreproc()
    {
    }


  public MarkupForPreproc( MainApp useApp )
    {
    mApp = useApp;
    fileLines = new StringArray();
    outFileLines = new StringArray();
    definesDictionary = new DefinesDictionary( mApp );
    }



  private boolean isValidCommand( String in )
    {
    if( in.equals( "error" ))
      return true;

    if( in.equals( "define" ))
      return true;

    if( in.equals( "undef" ))
      return true;

    // Add comments back in to the code to show
    // where a file was included and all that.
    if( in.equals( "include" ))
      return true;

    if( in.equals( "pragma" ))
      return true;

    if( in.equals( "if" ))
      return true;

    if( in.equals( "ifdef" ))
      return true;

    if( in.equals( "ifndef" ))
      return true;

    if( in.equals( "else" ))
      return true;

    if( in.equals( "elif" ))
      return true;

    if( in.equals( "endif" ))
      return true;

    return false;
    }



  public String mainFileLoop( String in )
    {
    try
    {
    boolean levelBool = true;
    int level = 0;
    StringBuilder paramBuilder = new StringBuilder();
    StringArray lineSplitter = new StringArray();

    if( in.trim().length() == 0 )
      return "";

    // CharArray cArray = new CharArray();
    fileLines.makeFieldsFromString( in, '\n' );
    int last = fileLines.length();
    for( int count = 0; count < last; count++ )
      {
      String line = fileLines.getStringAt( count );
      // mApp.showStatus( "Line " + count + ") " + line );

      if( '#' != StringsUtility.firstNonSpaceChar(
                                             line ))
        {
        if( levelBool )
          outFileLines.appendString( line );
        else
          outFileLines.appendString( "/" + "/  " + line );

        continue;
        }
     
      line = StringsUtility.replaceFirstChar( line,
                                              '#',
                                              ' ' );

      // Remove the line number markers.
      line = StringsUtility.removeSections( line,
                                       Markers.Begin,
                                       Markers.End );

      line = line.trim();

      // mApp.showStatus( "Preproc: " + line );

      lineSplitter.makeFieldsFromString( line, ' ' );

      int lastPart = lineSplitter.length();
      if( lastPart == 0 )
        {
        mApp.showStatus( "Preprocessor line doesn't have parts." );
        return "";
        }

      String command = lineSplitter.getStringAt( 0 );
      command = command.toLowerCase();
      mApp.showStatus( "Command: " + command ); 

      if( !isValidCommand( command ))
        {
        mApp.showStatus( command + " is not a valid command." );
        return "";
        }

      level = setLevel( command, level );
      if( level < 0 )
        {
        mApp.showStatus( "Level is less than zero." );
        return "";
        }

      paramBuilder.setLength( 0 );
      for( int countP = 1; countP < lastPart; countP++ )
        {
        paramBuilder.append( 
                lineSplitter.getStringAt( countP ) +
                " " );

        }

      String macroBody = paramBuilder.toString();
      macroBody = macroBody.trim();

      if( !processCommand( command, macroBody ))
        return "";

      }

    if( level != 0 )
      {
      mApp.showStatus( "Level is not zero at end." );
      return "";
      }

    // return sBuilder.toString();
    return in;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in mainFileLoop()." );
      mApp.showStatus( e.getMessage() );
      return "";
      }
    }


  private int setLevel( String command, int inLevel )
    {
    if( command.equals( "if" ))
      return inLevel + 1;

    if( command.equals( "ifdef" ))
      return inLevel + 1;

    if( command.equals( "ifndef" ))
      return inLevel + 1;

    if( command.equals( "endif" ))
      return inLevel - 1;

    // else and elseif don't change it.

    return inLevel;
    }



  private boolean processCommand( String command,
                                  String macroBody )
    {
    if( command.equals( "define" ))
      {
      if( !processDefineStatement( macroBody ))
        {
        return false;
        }
      }


    /*
    if( command.equals( "error" ))
      {
      if( levelBool )
        {
        // It should not get here to an error 
        // if levelBool is true.
        // mApp.showStatus( "This is an error." );
        // return "";
        }
      }
      */

/*
      if( command.equals( "undef" ))
        {
        mApp.showStatus( " " );
        mApp.showStatus( "It's a bad idea to use undef." );
        mApp.showStatus( originalLine );
        mApp.showStatus( " " );
        }
*/


/*
      if( command.equals( "include" ))
        {
        // Get the dictionary of #define statements
        // from this included file.
        }
*/

/*
      if( command.equals( "pragma" ))
        {
        }
*/

/*
      if( command.equals( "if" ))
        {
        level++;
/////////
        mApp.showStatus( "If line: " + originalLine );
        levelBool = evaluateIfExpression( macroBody );
        if( levelBool )
          {
          // This means to leave it as-is if 
          // it's true.
          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );
          }
//////////
        }
*/

/*
      if( command.equals( "ifdef" ))
        {
        level++;
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );

/////////
Put comments in the code that is output.
showWarning( )
This is a function-like macro so it shouldn't be
used in this ifdef statement.
Same with ifndef.
   if( key.contains( "(" ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "This is a function-like macro." );
////////

        // mApp.showStatus( command + ") " + level );
        }
*/


/*
      if( command.equals( "ifndef" ))
        {
        level++;
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );
        // mApp.showStatus( command + ") " + level );
        }
*/

/*
      if( command.equals( "else" ))
        {
        level--;
        if( levelBool )
          {
          // If it's coming in here as true, then
          // this else=part is false.
          levelBool = false;

          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );
          }
        else
          {
          levelBool = true;
          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );

          }

        level++;
        }
*/

/*
      if( command.equals( "elif" ))
        {
        level--;
        if( levelBool )
          {
////////
if levelBool is true then this is false.
  And vice versa.

all the way down to more elifs and else statements.
/////////

          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );
          }

        level++;
        levelBool = true;
        }
*/


/*
      if( command.equals( "endif" ))
        {
        level--;
        if( levelBool )
          {
          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );
          }

        levelBool = true;
        }
*/


    return true;
    }



  private boolean processDefineStatement( String in )
    {
    try
    {
    String[] splitS = in.split( " " );
    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatus( "There is nothing in the define statement." );
      return false;
      }

    String key = splitS[0];

    StringBuilder paramBuilder = new StringBuilder();

    if( key.contains( "(" ))
      {
      // For keys like this with no space after the
      // parentheses:
      // DEF_SANITIZER_BUILTIN_1(ENUM,

      StringArray lineSplitter = new StringArray();
      lineSplitter.makeFieldsFromString( key, ')' );

      int lastPart = lineSplitter.length();
      if( lastPart == 0 )
        {
        mApp.showStatus( "This key has no parts." );
        return false;
        }

      // Make the parentheses part of the key.
      key = lineSplitter.getStringAt( 0 ) + "(";


/*
      if( splitKeyLength > 2 )
        {
        mApp.showStatus( "What is the deal here?" );
        return false;
        }

      if( splitKeyLength == 2 )
        paramBuilder.append( splitKey[1] + " " );
//////
     
      // mApp.showStatus( " " );
      // mApp.showStatus( "This is a function-like macro." );
*/
      }


/*
    for( int count = 1; count < last; count++ )
      paramBuilder.append( splitS[count] + " " );

    String paramList = paramBuilder.toString();
    paramList = paramList.trim();

    definesDict.setString( key, paramList );

    mApp.showStatus( " " );
    mApp.showStatus( "key: " + key );
    mApp.showStatus( "paramList: " + paramList );
    mApp.showStatus( " " );
*/

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in processDefineStatement()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }




/*
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

    if( !testBeginEnd( mApp, result ))
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

    if( !testBeginEnd( mApp, result ))
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

    if( !testBeginEnd( mApp, result ))
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

    if( !testBeginEnd( mApp, result ))
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
      mApp.showStatus( result );
      mApp.showStatus( "There was an error marker after markOperators." );
      return "";
      }

    if( !testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "TestBeginEnd returned false after markOperators." );
      return "";
      }


    /////////
    result = removeOutsideWhiteSpace( result );
    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( result );
      mApp.showStatus( " " );
      mApp.showStatus( "There was an error marker after removeOutsideWhiteSpace." );
      return "";
      }


    /////////////
    // mApp.showStatus( result );
    // mApp.showStatus( " " );
    // mApp.showStatus( "Finished markup." );
    // mApp.showStatus( " " );
    // mApp.showStatus( " " );
    return result;





/////////////////
    if( !TestBrackets( MForm, Result ))
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
    ///////////

    }
*/




/*
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

    // mApp.showStatus( "Preproc: " + in ); 
   
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
                    // Does trimming it mess up a macro?
                    // splitS[0] +
                    splitS[0].trim() +
                    Markers.End +
                    Markers.Begin +
                    splitS[1] + "\n";

    return result;
    }
*/




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


// See if it's valid.
// float a = Float.parseFloat( SomeString );

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
    // operators.

    // Ternary x?y:z
    // +=, -=, *=, /=,
    // *** Pointer to a pointer to a... indefintely.

  private static String markOperators( String in )
    {
    StringBuilder sBuilder = new StringBuilder();

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

      if( isBasicOpCharacter( testChar ))
        {
        sBuilder.append( Markers.Begin );
        sBuilder.append( Markers.TypeOperator );
        sBuilder.append( testChar );
        sBuilder.append( Markers.End );
        continue;
        }

      sBuilder.append( testChar );
      }

    return sBuilder.toString();
    }



  private static boolean isBasicOpCharacter(
                                      char testChar )
    {
    // This does not look inside of marked strings,
    // characters, identifiers, or numbers.

    // How to mess up a language:
    // C++ user defined literals 
    // long double operator"" _kg( long double x ) 

    // Preprocessing?
    if( testChar == '#' )
      return false;

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
                                      String inString )
    {
    StringBuilder sBuilder = new StringBuilder();

    String[] splitS = inString.split(
                  Character.toString( Markers.Begin ));


    // This contains the string before the first
    // marker.
    sBuilder.append( splitS[0] + "\n" );

    int last = splitS.length;
    // This starts at 1, after the first
    // Markers.Begin.
    for( int count = 1; count < last; count++ )
      {
      String line = splitS[count];
      sBuilder.append( line + "\n" );

      if( !line.contains( Character.toString(
                                       Markers.End )))
        {
        mApp.showStatus( sBuilder.toString() );
        mApp.showStatus( " " );
        mApp.showStatus( "The line has no end marker." );
        mApp.showStatus( "Line: " + line );
        return false;
        }
      }

    return true;
    }




  public static String removeOutsideWhiteSpace(
                                            String in )
    {
    StringBuilder sBuilder = new StringBuilder();

    boolean isInsideObject = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      if( testChar == Markers.Begin )
        {
        isInsideObject = true;
        sBuilder.append( testChar );
        continue;
        }

      if( testChar == Markers.End )
        {
        isInsideObject = false;
        sBuilder.append( testChar );
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.append( testChar );
        continue;
        }

      if( testChar == ' ' )
        continue;

      if( testChar == '\n' )
        continue;

      sBuilder.append( "Right here >" + testChar + "<" );

      sBuilder.append( testChar );
      sBuilder.append( Markers.ErrorPoint );
      return sBuilder.toString();
      }

    return sBuilder.toString();
    }



/*
  internal static bool TestBrackets( MainForm MForm, string InString )
    {
    StringBuilder SBuilder = new StringBuilder();
    int BracketCount = 0;
    bool IsInsideObject = false;
    int Last = InString.Length;
    for( int Count = 0; Count < Last; Count++ )
      {
      char TestChar = InString[Count];
      SBuilder.Append( Char.ToString( TestChar ));

      if( IsInsideObject )
        {
        if( TestChar == Markers.End )
          IsInsideObject = false;

        continue;
        }

      if( TestChar == Markers.Begin )
        {
        IsInsideObject = true;
        continue;
        }

      if( !((TestChar == '{') || (TestChar == '}')))
        {
        string ShowS = SBuilder.ToString();
        ShowS = ShowS.Replace(
             Char.ToString( Markers.Begin ), "\r\n" );

        MForm.ShowStatus( ShowS );
        MForm.ShowStatus( "This is not a bracket: " + Char.ToString( TestChar ));
        return false;
        }

      if( TestChar == '{' )
        BracketCount++;

      if( TestChar == '}' )
        BracketCount--;

      if( BracketCount < 0 )
        {
        string ShowS = SBuilder.ToString();
        ShowS = ShowS.Replace(
             Char.ToString( Markers.Begin ), "\r\n" );

        MForm.ShowStatus( ShowS );
        MForm.ShowStatus( "Bracket count went negative." );
        return false;
        }

      }

    if( BracketCount != 0 )
      {
      MForm.ShowStatus( "Bracket count is not zero at the end." );
      return false;
      }

    return true;
    }
*/



  }
