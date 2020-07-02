// Copyright Eric Chauvin 2019 - 2020.



public class PreProcessLines
  {
  private MainApp mApp;
  private MacroDictionary macroDictionary;
  private HeaderFileDictionary headerDictionary;
  private BoolLevelArray boolLevArray;

    // #line

    // #warning

  private static final StrA DirectiveError =
                                new StrA( "error" );

  private static final StrA DirectiveDefine =
                                new StrA( "define" );

  private static final StrA DirectiveUndef =
                                new StrA( "undef" );

    // #include_next is a GNU extension.
    // It means to include the next file with
    // the same name.  Next in the search path for
    // include files.
  private static final StrA DirectiveInclude =
                             new StrA( "include" );

  private static final StrA DirectivePragma =
                             new StrA( "pragma" );

  private static final StrA DirectiveIf =
                                new StrA( "if" );

  private static final StrA DirectiveIfdef =
                                  new StrA( "ifdef" );

  private static final StrA DirectiveIfndef = 
                              new StrA( "ifndef" );

  private static final StrA DirectiveElse =
                              new StrA( "else" );

  private static final StrA DirectiveElif =
                              new StrA( "elif" );

  private static final StrA DirectiveEndif =
                             new StrA( "endif" );




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



  private boolean isValidDirective( StrA in )
    {
    // These strA values should be constants.
    // Don't create them each time.

    // #line

    // #warning

    if( in.equals( DirectiveError ))
      return true;

    if( in.equals( DirectiveDefine ))
      return true;

    if( in.equals( DirectiveUndef ))
      return true;

    // #include_next is a GNU extension.
    // It means to include the next file with
    // the same name.  Next in the search path for
    // include files.
    if( in.equals( DirectiveInclude ))
      return true;

    if( in.equals( DirectivePragma ))
      return true;

    if( in.equals( DirectiveIf ))
      return true;

    if( in.equals( DirectiveIfdef ))
      return true;

    if( in.equals( DirectiveIfndef ))
      return true;

    if( in.equals( DirectiveElse ))
      return true;

    if( in.equals( DirectiveElif ))
      return true;

    if( in.equals( DirectiveEndif ))
      return true;

    return false;
    }



  public StrA mainFileLoop( StrA in, StrA fileName )
    {
    return new StrA( "" );
/*
    try
    {


    if( in.trim().length() == 0 )
      return "";

    StrArray fileLines = new StrArray();
    StringBuilder sBuilder = new StringBuilder();
    StringBuilder paramBuilder = new StringBuilder();
    StrA inA = new StrA( in );

======    StrArray lineSplitter = inA.splitChar( '\n' );

    final int last = lineSplitter.length();
    for( int count = 0; count < last; count++ )
      {
      StrA line = fileLines.getStrAt( count );
      if( '#' != line.firstNonSpaceChar())
        {
        if( boolLevArray.getCurrentValue())
          sBuilder.append( line + "\n" );
        else
          sBuilder.append( "//  " + line + "\n" );

        continue;
        }
     
      // String originalLine = line;
      line = line.replaceFirstChar( '#',
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
====
This can't be right to use LineSplitter again here.

      lineSplitter = line.splitChar( ' ' );

      int lastPart = lineSplitter.
                    makeFieldsFromString( line, ' ' );

      if( lastPart == 0 )
        {
        mApp.showStatusAsync(
             "Preprocessor line doesn't have parts." );

        return "";
        }

      String directive = lineSplitter.getStringAt( 0 );

      // if(WINVER
      String directiveExtra = "";
      if( directive.contains( "(" ))
        {
        StringArray dirSplitter = new StringArray();
        int dirPart = dirSplitter.
                     makeFieldsFromString( directive,
                                             '(' );

        directive = dirSplitter.getStringAt( 0 );

        StringBuilder extraBuilder = new StringBuilder();
        for( int countD = 1; countD < dirPart; countD++ )
          {
          extraBuilder.append( 
                    dirSplitter.getStringAt( countD ));
 
          }

        directiveExtra = extraBuilder.toString();
        }

      // Remove the line number markers if they are
      // there.  Like endif with nothing following it.
      directive = StringsUtility.removeSections(
                                        directive,
                                        Markers.Begin,
                                        Markers.End );

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

      String directiveBody = directiveExtra + " " +
                             paramBuilder.toString();

      directiveBody = directiveBody.trim();

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
*/
    }



/*
  private StrA processDirective( StrA directive,
                                StrA directiveBody )
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
*/


/*
  private String processEndIf()
    {
    if( !boolLevArray.subtractLevel())
      return "";

    return "// #endif\n";
    }
*/


/*
  private String processIfDef( String directive,
                               String directiveBody )
    {
    return "";
//////////
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
////////
    }
*/


/*
  private String processIfNDef( String directive,
                                String directiveBody )
    {
    return "";
//////
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
//////
    }
*/



/*
  private String processError( String directiveBody )
    {
    if( !boolLevArray.getCurrentValue())
      return "// #error " + directiveBody + "\n";

    mApp.showStatusAsync( "Error directive: " + directiveBody );
    return "";
    }
*/


/*
  private String processPragma( String directiveBody ) 
    {
    // === Use the ProcessPragma.java file.

    // pragma GCC system_header  15;stddef.h 

    // mApp.showStatusAsync( "#pragma: " + directiveBody );

    String result = "// #pragma " + directiveBody + "\n";

    if( !boolLevArray.getCurrentValue())
      return result;

    // Do something with it.

    return result;
    }
*/


/*
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
*/


/*
  private String processInclude( String directiveBody )
    {
    return "";
/////////
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
//////
    }
*/


/*
  private String processDefine( String directiveBody )
    {
    return "";
///////

    if( !boolLevArray.getCurrentValue())
      return "// #define " + directiveBody + "\n";

    Macro macro = new Macro( mApp );
    if( !macro.setKeyFromString( directiveBody ))
      return "";

    // String showKey = macro.getKey();
    // mApp.showStatusAsync( "Key: " + showKey );

    boolean doStrict = false;
    if( !macro.markUpFromString( directiveBody,
                                 macroDictionary,
                                 doStrict ))
      {
      mApp.showStatusAsync( "markUpFromString had an error." );
      return "";
      }

    macroDictionary.setNewMacro( doStrict,
                                 macro.getKey(),
                                 macro );

    return "// #define " + directiveBody + "\n";
///////
    }
*/



/*
  private String processIf( String directiveBody ) 
    {
    return "";
//////
    String result = "if " + directiveBody + "\n";
    if( !boolLevArray.getCurrentValue())
      {
      // If what's above it is false, then this
      // has to be false.
      boolLevArray.addNewLevel( false );
      return "// false  " + result;
      }

    String exprValue = IfExpression.
                                  evaluateExpression(
                                        mApp,
                                        directiveBody,
                                     macroDictionary );

    if( exprValue.length() == 0 )
      return "";

    if( exprValue.equals( "true" ))
      {
      boolLevArray.addNewLevel( true );
      return "// true  " + result;
      }

    if( exprValue.equals( "false" ))
      {
      boolLevArray.addNewLevel( false );
      return "// false  " + result;
      }

    return "// unknown if value returned.";
///////
    }
*/



/*
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

    // mApp.showStatusAsync( "elif body: " + directiveBody );
      
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
*/


/*
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
*/


  }
