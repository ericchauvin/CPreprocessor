// Copyright Eric Chauvin 2019 - 2020.




public class PreProcessLines
  {
  private MainApp mApp;
  private MacroDictionary macroDictionary;
  private HeaderFileDictionary headerDictionary;
  private boolean[] levelArray;
  private int levelArrayLast = 0;



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
    levelArray = new boolean[2];
    }



  private void resizeLevelArray( int toAdd )
    {
    int oldLength = levelArray.length;

    boolean[] tempLevelArray = new boolean[oldLength + toAdd];
    for( int count = 0; count < levelArrayLast; count++ )
      {
      tempLevelArray[count] = levelArray[count];
      }

    levelArray = tempLevelArray;
    }



  private boolean getLevelValue()
    {
    // It is always true at the zero level.
    if( levelArrayLast == 0 )
      return true;

    // If any level above this is false, then
    // this level is false.
    for( int count = 0; count < arrayLast; count++ )
      {
      if( !levelArray[count] )
        return false;

      }

    return true;
    }



  private boolean setLevelValue( boolean setTo )
    {
    // It is always true at the zero level.
    if( levelArrayLast == 0 )
      {
      if( !setTo )
        return false; 

      }
     
    levelArray[levelArrayLast] = setTo;
    return true;
    }



  private boolean subtractBooleanLevel()
    {
    if( levelArrayLast == 0 )
      {
      mApp.showStatusAsync( "Level is less than zero." );
      return false;
      }

    levelArrayLast--;
    return true;
    }



  private void addNewBooleanLevel( boolean setTo )
    {
    if( levelArrayLast >= levelArray.length )
      resizeArrays( 16 );

    levelArray[levelArrayLast] = setTo;
    levelArrayLast++;
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



  public String mainFileLoop( String in )
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
        if( boolLevArray.getValue() )
          sBuilder.append( line + "\n" );
        else
          sBuilder.append( "/" + "/  " + line + "\n" );

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

      // mApp.showStatus( "Preproc: " + line );

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
      String result = processDirective( directive,
                                        directiveBody,
                                        boolLevArray );

      if( result.length() == 0 )
        return "";

      // This might be appending a whole include file
      // worth of stuff.  And the include files it
      // contains.
      sBuilder.append( result );
      }

    if( boolLevArray.getCurrentLevel() != 0 )
      {
      mApp.showStatusAsync( "Level is not zero at end." );
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
      result = processIfDef( directive,
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
    if( !subtractBooleanLevel())
      return "";

    return "// #endif\n";
    }



  private String processIfDef( String directive,
                               String directiveBody )
    {
    String result = "// #" + directive + " " +
                                directiveBody + "\n";

    if( !getLevelValue())
      {
      addNewBooleanLevel( false );
      return result;
      }

    boolean isDefined = macroDictionary.
                            keyExists( directiveBody );

    mApp.showStatusAsync( " " );

    if( isDefined )
      {
      mApp.showStatusAsync( "Is defined: " + directiveBody );
      addNewBooleanLevel( true );
      return result;
      }
    else
      {
      mApp.showStatusAsync( "Is not defined: " + directiveBody );
      addNewBooleanLevel( false );
      return result;
      }
    }



  private String processIfNDef( String directive,
                                String directiveBody )
    {
    String result = "// #" + directive + " " +
                                directiveBody + "\n";

    if( !getLevelValue())
      {
      addNewBooleanLevel( false );
      return result;
      }

    boolean isDefined = macroDictionary.
                            keyExists( directiveBody );

    mApp.showStatusAsync( " " );

    if( !isDefined )
      {
      mApp.showStatusAsync( "Is not defined: " + directiveBody );
      addNewBooleanLevel( true );
      return result;
      }
    else
      {
      mApp.showStatusAsync( "Is defined: " + directiveBody );
      addNewBooleanLevel( false );
      return result;
      }
    }



  private String processError( String directiveBody )
    {
    if( !getLevelValue())
      return "// #error " + directiveBody + "\n";

    mApp.showStatusAsync( "Error directive: " + directiveBody );
    return "";
    }


  private String processPragma(
                        BooleanLevelArray boolLevArray,
                        String directiveBody ) 
    {
    String result = "// #pragma " + directiveBody;

    if( !getLevelValue())
      return result;

    mApp.showStatusAsync( "#pragma needs to be done: " + directiveBody );
    return result;
    }



  private String processUndef( String directiveBody ) 
    {
    if( !getLevelValue() )
      return "// #undef " + directiveBody;

    // mApp.showStatus( "It's a bad idea to use undef." );
    // mApp.showStatus( directiveBody );

    directiveBody = directiveBody.trim();
    if( macroDictionary.keyExists( directiveBody ))
      {
      macroDictionary.setMacroEnabled( directiveBody,
                                            false );
      }
    else
      {
      mApp.showStatusAsync( "Trying to undef something that doesn't exist." );
      mApp.showStatusAsync( directiveBody );
      }

    return "// #undef " + directiveBody;
    }



  private String processElse( String directiveBody ) 
    {
    if( getLevelValue() )
      {
      if( !setLevelValue( false ))
        return "";

      }
    else
      {
      if( !setLevelValue( true ))
        return "";

      }

    return "// #else " + directiveBody;
    }






  private String processInclude( 
                        BooleanLevelArray boolLevArray,
                        String directiveBody )
    {
    if( !boolLevArray.getValue() )
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
                         getFileFromList( inclFileName );
    
    if( fileName.length() == 0 )
      {
      mApp.showStatusAsync( "No file found for: " + inclFileName );
      return ""; 
      }
    
    // String showS = "Filename found is:\n" + fileName;
    // mApp.showStatusAsync( showS );


    /*
        Do preprocessing on the included file.
        String test = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary );

        Once this returns from processing the
        included files it will have a bunch of new
        macros defined in the dictionary.

    Add a newline at the end of the included file.
    And comments to show where it ends.
    */

    return "// #include " + directiveBody + "\n";
    }



  private String processDefine(
                       BooleanLevelArray boolLevArray,
                       String directiveBody )
    {
    return "Not yet.";

    /*
    if( !boolLevel.getValue() )
      return "// #define " + directiveBody + "\n";

    Macro macro = new Macro( mApp );
    if( !macro.setKeyFromString( directiveBody ))
      return "";

    // String showKey = macro.getKey();
    // mApp.showStatusAsync( "Key: " + showKey );

    // This adds it to the macroDictionary. 
    if( !macro.markUpFromString( directiveBody,
                                 macroDictionary ))
      return "";

    return "// #define " + directiveBody + "\n";
    */
    }






  private String processIf( 
                       BooleanLevelArray boolLevArray,
                       String directiveBody ) 
    {
    return "Not yet.";
/*
    boolLevel.setLevel( "if" );
    if( boolLevel.getCurrentLevel() < 0 )
      {
      mApp.showStatusAsync( "Level is less than zero." );
      return "";
      }

    if( !boolLevel.getValue() )
      return "// #if " + directiveBody;

    mApp.showStatusAsync( "If line: " + directiveBody );
    return "// #if " + directiveBody;
*/



/*
        levelBool = evaluateIfExpression( macroBody );
        if( levelBool )
          {
          // This means to leave it as-is if 
          // it's true.
          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );
          }
*/
    }




  private String processElif(
                        BooleanLevelArray boolLevArray,
                        String directiveBody ) 
    {
    return "Not yet.";
/*    return "// elif";



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
    }




  }
