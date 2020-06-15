// Copyright Eric Chauvin 2019 - 2020.



public class PreProcessLines
  {
  private MainApp mApp;
  private MacroDictionary macroDictionary;
  private HeaderFileDictionary headerDictionary;
  private BoolLevelArray boolLevArray;



  private PreProcessLines()
    {
    }


  public PreProcessLines( MainApp useApp, 
                               MacroDictionary
                               macroDictionaryToUse,
                               HeaderFileDictionary
                               headerDictionaryToUse )
    {
    mApp = useApp;
    macroDictionary = macroDictionaryToUse;
    headerDictionary = headerDictionaryToUse;
    boolLevArray = new BoolLevelArray( mApp );
    }



  private boolean isValidDirective( String in )
    {
    // #line

    if( in.equals( "error" ))
      return true;

    if( in.equals( "define" ))
      return true;

    if( in.equals( "undef" ))
      return true;

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



  public String mainFileLoop( String in, String fileName )
    {
    try
    {
    if( in.trim().length() == 0 )
      return "";

    StringArray fileLines = new StringArray();
    StringBuilder sBuilder = new StringBuilder();
    StringBuilder paramBuilder = new StringBuilder();
    StringArray lineSplitter = new StringArray();

    int last = fileLines.makeFieldsFromString( in, '\n' );
    for( int count = 0; count < last; count++ )
      {
      String line = fileLines.getStringAt( count );
      if( '#' != StringsUtility.firstNonSpaceChar(
                                             line ))
        {
        if( boolLevArray.getCurrentValue())
          sBuilder.append( line + "\n" );
        else
          sBuilder.append( "//  " + line + "\n" );

        continue;
        }
     
      // String originalLine = line;
      line = StringsUtility.replaceFirstChar( line,
                                              '#',
                                              ' ' );

      // This trim is needed to make sure the directive
      // is the first field.  So there are no empty
      // fields in front of it.
      line = line.trim();

      // mApp.showStatusAsync( "Preproc: " + line );

      // This is splitting the fields by space 
      // characters, but if there's more than one
      // space character in sequence then it will
      // create a zero-length string on the second
      // space because there was nothing after the
      // first space.
      int lastPart = lineSplitter.
                    makeFieldsFromString( line, ' ' );

      if( lastPart == 0 )
        {
        mApp.showStatusAsync(
             "Preprocessor line doesn't have parts." );

        return "";
        }

      String directive = lineSplitter.getStringAt( 0 );
      // Remove the line number markers if they are
      // there.  Like endif with nothing following it.
      directive = StringsUtility.removeSections(
                                        directive,
                                        Markers.Begin,
                                        Markers.End );

      // If it's not already lower case then that's
      // a problem.
      // directive = directive.toLowerCase();
      if( !isValidDirective( directive ))
        {
        mApp.showStatusAsync( directive + " is not a valid directive." );
        return "";
        }

      paramBuilder.setLength( 0 );
      for( int countP = 1; countP < lastPart; countP++ )
        {
        String field = lineSplitter.
                               getStringAt( countP );

        paramBuilder.append( field + " " );
        }

      String directiveBody = paramBuilder.toString().
                                               trim();

      // mApp.showStatusAsync( directive + " " + directiveBody );

      String result = processDirective( directive,
                                       directiveBody );

      if( result.length() == 0 )
        return "";

      // This might be appending a whole include file
      // worth of stuff.  And the include files it
      // contains.
      sBuilder.append( result );
      }

    if( boolLevArray.getLevel() != 0 )
      {
      String showS = "Level is not zero at end.\n" +
                     "Level: " +
                     boolLevArray.getLevel() + "\n" +
                     fileName;

      mApp.showStatusAsync( showS );
      return "";
      }

    return sBuilder.toString();
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in mainFileLoop()." );
      mApp.showStatusAsync( e.getMessage() );
      return "";
      }
    }



  private String processDirective( String directive,
                                String directiveBody )
    {
    // Anything that gets returned here should end
    // with a newline.  And include files will have
    // many newlines.

    String result = "";
    if( directive.equals( "define" ))
      {
      result = processDefine( directiveBody );
      }

    if( directive.equals( "error" ))
      {
      result = processError( directiveBody );
      }

    if( directive.equals( "undef" ))
      {
      result = processUndef( directiveBody );
      }

    if( directive.equals( "include" ))
      {
      result = processInclude( directiveBody );
      }

    if( directive.equals( "pragma" ))
      {
      result = processPragma( directiveBody ); 
      }

    if( directive.equals( "if" ))
      {
      result = processIf( directiveBody );
      }

    if( directive.equals( "ifdef" ))
      {
      result = processIfDef( directive,
                                      directiveBody );
      }

    if( directive.equals( "ifndef" ))
      {
      result = processIfNDef( directive,
                                     directiveBody );
      }

    if( directive.equals( "else" ))
      {
      result = processElse( directiveBody );
      }

    if( directive.equals( "elif" ))
      {
      result = processElif( directiveBody );
      }

    if( directive.equals( "endif" ))
      {
      result = processEndIf();
      }

    if( result.length() == 0 )
      return "";

    if( !result.endsWith( "\n" ))
      {
      mApp.showStatusAsync( "processDirective() line has to end with a line feed." );
      mApp.showStatusAsync( directive );
      mApp.showStatusAsync( directiveBody );
      return "";
      }

    return result;
    }



  private String processEndIf()
    {
    if( !boolLevArray.subtractLevel())
      return "";

    return "// #endif\n";
    }



  private String processIfDef( String directive,
                               String directiveBody )
    {
    String result = "// #" + directive + " " +
                                directiveBody + "\n";

    if( !boolLevArray.getCurrentValue())
      {
      // If what's above it is false, then this
      // has to be false.
      boolLevArray.addNewLevel( false );
      return result;
      }

    directiveBody = StringsUtility.removeSections(
                                        directiveBody,
                                        Markers.Begin,
                                        Markers.End );

    boolean isDefined = macroDictionary.
                           keyExists( directiveBody );

    if( isDefined )
      {
      // mApp.showStatusAsync( "Is defined: " + directiveBody );
      boolLevArray.addNewLevel( true );
      return result;
      }
    else
      {
      // mApp.showStatusAsync( "Is not defined: " + directiveBody );
      boolLevArray.addNewLevel( false );
      return result;
      }
    }



  private String processIfNDef( String directive,
                                String directiveBody )
    {
    String result = "// #" + directive + " " +
                                directiveBody + "\n";

    if( !boolLevArray.getCurrentValue())
      {
      // mApp.showStatusAsync( "CurrentValue is false." );
      boolLevArray.addNewLevel( false );
      return result;
      }

    directiveBody = StringsUtility.removeSections(
                                        directiveBody,
                                        Markers.Begin,
                                        Markers.End );

    // mApp.showStatusAsync( "ifndef " + directiveBody );

    boolean isDefined = macroDictionary.
                            keyExists( directiveBody );

    if( !isDefined )
      {
      // mApp.showStatusAsync( "Is not defined: " + directiveBody );
      boolLevArray.addNewLevel( true );
      return result;
      }
    else
      {
      // mApp.showStatusAsync( "Is defined: " + directiveBody );
      boolLevArray.addNewLevel( false );
      return result;
      }
    }



  private String processError( String directiveBody )
    {
    if( !boolLevArray.getCurrentValue())
      return "// #error " + directiveBody + "\n";

    mApp.showStatusAsync( "Error directive: " + directiveBody );
    return "";
    }



  private String processPragma( String directiveBody ) 
    {
    // pragma GCC system_header  15;stddef.h 

    String result = "// #pragma " + directiveBody + "\n";

    if( !boolLevArray.getCurrentValue())
      return result;

    mApp.showStatusAsync( "#pragma needs to be done: " + directiveBody );
    return result;
    }



  private String processUndef( String directiveBody ) 
    {
    String result = "// #undef " + directiveBody + "\n"; 
    if( !boolLevArray.getCurrentValue())
      return result;

    // mApp.showStatusAsync( "It's a bad idea to use undef." );
    // mApp.showStatusAsync( directiveBody );

    directiveBody = directiveBody.trim();
    if( macroDictionary.keyExists( directiveBody ))
      {
      macroDictionary.setMacroEnabled( directiveBody,
                                            false );
      }
    else
      {
      // Things get routinely undefined before they
      // get defined.
      // mApp.showStatusAsync( "Trying to undef something that doesn't exist." );
      // mApp.showStatusAsync( directiveBody );
      }

    return result;
    }




  private String processInclude( String directiveBody )
    {
    if( !boolLevArray.getCurrentValue())
      return "// #include " + directiveBody + "\n";
      
    // Add comments back in to the code to show
    // where a file was included and all that.

    String inclFileName = StringsUtility.removeSections(
                                        directiveBody,
                                        Markers.Begin,
                                        Markers.End );

    inclFileName = inclFileName.replace( "\"", "" );
    inclFileName = inclFileName.replace( "<", "" );
    inclFileName = inclFileName.replace( ">", "" );
    inclFileName = inclFileName.trim();
    inclFileName = inclFileName.toLowerCase();
    
    // Make a configuration file.
    // Make this work for your operating system.
    String pathDelim = "\\";
    String linuxPathDelim = "/";
    inclFileName = inclFileName.replace(
                                     linuxPathDelim,
                                     pathDelim );

    if( inclFileName.equals( "precompiled.hpp" ))
      return "// #include precompiled.hpp is not used.\n";

    // mApp.showStatusAsync( "Looking for: " + inclFileName );
    String fileName = headerDictionary.
                            getValue( inclFileName );
    
    if( fileName.length() == 0 )
      {
      mApp.showStatusAsync( "No file found for: " + inclFileName );
      return ""; 
      }
    
    // String showS = "Filename found is:\n" + fileName;
    // mApp.showStatusAsync( showS );

    String inclFileStr = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary,
                                    headerDictionary );

    if( inclFileStr.length() == 0 )
      return "";

    // Once this returns from processing the
    // included files it will have a bunch of new
    // macros defined in the dictionary.

    inclFileStr = "\n\n\\\\\\\\\\\\\\\\\\\\\\\\\n" +
           "// #include " + fileName + "\n" +
           inclFileStr +
           "\n\n\\\\\\\\\\\\\\\\\\\\\\\\\n\n";

    return inclFileStr;
    }



  private String processDefine( String directiveBody )
    {
    if( !boolLevArray.getCurrentValue())
      return "// #define " + directiveBody + "\n";

    Macro macro = new Macro( mApp );
    if( !macro.setKeyFromString( directiveBody ))
      return "";

    // String showKey = macro.getKey();
    // mApp.showStatusAsync( "Key: " + showKey );

    // This adds it to the macroDictionary. 
    boolean doStrict = false;
    if( !macro.markUpFromString( directiveBody,
                                 macroDictionary,
                                 doStrict ))
      return "";

    return "// #define " + directiveBody + "\n";
    }



  private String processIf( String directiveBody ) 
    {
    String result = "// if " + directiveBody + "\n";
    if( !boolLevArray.getCurrentValue())
      {
      // If what's above it is false, then this
      // has to be false.
      boolLevArray.addNewLevel( false );
      return result;
      }

    boolean exprValue = evaluateExpression( 
                                      directiveBody );


    // Once I evaluate the expression...
    boolLevArray.addNewLevel( exprValue );

      
    //  if( !boolLevArray.setCurrentValue( false ))
    //    return "";


    return result;
    }



  private boolean evaluateExpression( String expr )
    {
    // #if !(defined(_BEGIN_STD_C) && defined(_END_STD_C))
    //   #if !defined(_POSIX_SOURCE) && 
    //       !defined(_POSIX_C_SOURCE) &&
    //       ((!defined(__STRICT_ANSI__) &&
    //       !defined(_ANSI_SOURCE)) ||
    //       (_XOPEN_SOURCE - 0) >= 500)

    String originalExpr = expr;
    // Remove line number markers.
    expr = StringsUtility.removeSections( expr,
                                        Markers.Begin,
                                        Markers.End );

    expr = expr.trim();
    if( expr.length() == 0 )
      return false;

    if( expr.equals( "1" ))
      return true;

    if( expr.equals( "0" ))
      return false;

    String markedUp = MarkupString.MarkItUp( 
                                         mApp, expr );

    if( markedUp.contains( "defined" ))
      {
      markedUp = setDefineValues( markedUp );
      if( markedUp.length() == 0 )
        return false;

      }

// float a = Float.parseFloat( SomeString );
    mApp.showStatusAsync( markedUp );

    return false;
    }



  private String setDefineValues( String markedUp )
    {
    mApp.showStatusAsync( " " );
    mApp.showStatusAsync( markedUp );

    String[] exprParts = markedUp.split( "" + 
                                     Markers.Begin );

    int last = exprParts.length;
    boolean isInside = false;
    StringBuilder sBuilder = new StringBuilder();
    for( int count = 0; count < last; count++ )
      {
      String part = exprParts[count];
      if( isInside )
        {
        if( part.startsWith( "" + 
                             Markers.TypeOperator +
                             ")" ))
          {
          // mApp.showStatusAsync( "Found closed" );
          sBuilder.append( "" + Markers.Begin + part );
          isInside = false;
          continue;
          }
        }

      if( part.startsWith( "" + 
                           Markers.TypeIdentifier +
                           "defined" ))
        {
        sBuilder.append( "" + Markers.Begin + part );
        // mApp.showStatusAsync( "Found defined" );
        isInside = true;
        continue;
        }

      if( isInside )
        {
        if( part.startsWith( "" + 
                             Markers.TypeIdentifier ))
          {
          String macroName = part.replace( "" +
                        Markers.TypeIdentifier, "" );

          macroName = macroName.replace( "" +
                                   Markers.End, "" );

          if( macroDictionary.keyExists( macroName ))
            {
            // mApp.showStatusAsync( "Is defined: " + macroName );
            sBuilder.append( "1" );
            }
          else
            {
            // mApp.showStatusAsync( "Not defined: " + macroName );
            sBuilder.append( "0" );
            }

          continue;
          }
        }

      sBuilder.append( "" + Markers.Begin + part );
      }
    
    String result = sBuilder.toString();
    String trueIn = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" + Markers.End +
                         Markers.Begin +
                         Markers.TypeOperator + "(" +
                         Markers.End +
                         "1" +
                         Markers.Begin +
                         Markers.TypeOperator + ")" +
                         Markers.End;
                         
    String falseIn = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" + Markers.End +
                         Markers.Begin +
                         Markers.TypeOperator + "(" +
                         Markers.End +
                         "0" +
                         Markers.Begin +
                         Markers.TypeOperator + ")" +
                         Markers.End;

    String trueOut = "" + Markers.Begin +
                     Markers.TypeBoolean +
                     "1" + 
                     Markers.End;

    String falseOut = "" + Markers.Begin +
                     Markers.TypeBoolean +
                     "0" + 
                     Markers.End;

    result = result.replace( trueIn, trueOut ); 
    result = result.replace( falseIn, falseOut ); 
    return result;
    }



  private String setDefineStatements( String expr )
    {
    return "";

/*
    String[] splitS = markedUpString.split(
                                 Character.toString(
                                 Markers.Begin ));

    int last = splitS.length;
    if( last < 2 )
      {
      mApp.showStatusAsync( "This macro has no key marked up." );
      return false;
      }
      
    // The string at zero is what's before the first
    // Begin marker, which is nothing.

    String testKey = splitS[1];
    char firstChar = testKey.charAt( 0 ); 
    if( firstChar != Markers.TypeIdentifier )
      {
      mApp.showStatusAsync( "The key is not an identifier." );
      mApp.showStatusAsync( markedUpString );
      return false;
      }

    testKey = Markers.removeAllMarkers( testKey );
    if( !key.equals( testKey ))
      {
      mApp.showStatusAsync( "The key is not equal to the first token." );
      mApp.showStatusAsync( "Key: >" + key + "<" );
      mApp.showStatusAsync( "TestKey: >" + testKey + "<" );
      mApp.showStatusAsync( markedUpString );
      return false;
      }
*/
    }



  private String processElif( String directiveBody ) 
    {
    int currentLevel = boolLevArray.getLevel();
    if( currentLevel == 0 )
      {
      mApp.showStatusAsync( "elif can't be at zero." );
      return "";
      }

    String result = "// #elif " + directiveBody + "\n";

    boolean currentVal = boolLevArray.
                           getValueAt( currentLevel );

    boolean oneUp = boolLevArray.
                       getValueAt( currentLevel - 1 );

    ///////////////////////
    // Test
    if( !oneUp && currentVal )
      {
      mApp.showStatusAsync( "This shouldn't happen with oneUp." );
      return result;
      }
    ///////////////////

    if( !oneUp )
      return result;

    mApp.showStatusAsync( "elif body: " + directiveBody );
      
    if( currentVal )
      {
      if( !boolLevArray.setCurrentValue( false ))
        return "";

      return result;
      }
    else
      {
      if( !boolLevArray.setCurrentValue( true ))
        return "";

      return result;
      }
    }



  private String processElse( String directiveBody ) 
    {
    int currentLevel = boolLevArray.getLevel();
    if( currentLevel == 0 )
      {
      mApp.showStatusAsync( "else can't be at zero." );
      return "";
      }

    String result = "// #else " + directiveBody + "\n";

    boolean currentVal = boolLevArray.
                           getValueAt( currentLevel );

    boolean oneUp = boolLevArray.
                       getValueAt( currentLevel - 1 );

    ///////////////////////
    // Test
    if( !oneUp && currentVal )
      {
      mApp.showStatusAsync( "This shouldn't happen with oneUp in else." );
      return result;
      }
    /////////////

    if( !oneUp )
      return result;
      
    if( currentVal )
      {
      if( !boolLevArray.setCurrentValue( false ))
        return "";

      return result;
      }
    else
      {
      if( !boolLevArray.setCurrentValue( true ))
        return "";

      return result;
      }
    }


  }
