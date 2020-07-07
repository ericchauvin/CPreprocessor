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
    // #line

    // #warning

    if( in.equalTo( DirectiveError ))
      return true;

    if( in.equalTo( DirectiveDefine ))
      return true;

    if( in.equalTo( DirectiveUndef ))
      return true;

    // #include_next is a GNU extension.
    // It means to include the next file with
    // the same name.  Next in the search path for
    // include files.

    if( in.equalTo( DirectiveInclude ))
      return true;

    if( in.equalTo( DirectivePragma ))
      return true;

    if( in.equalTo( DirectiveIf ))
      return true;

    if( in.equalTo( DirectiveIfdef ))
      return true;

    if( in.equalTo( DirectiveIfndef ))
      return true;

    if( in.equalTo( DirectiveElse ))
      return true;

    if( in.equalTo( DirectiveElif ))
      return true;

    if( in.equalTo( DirectiveEndif ))
      return true;

    return false;
    }



  public StrA mainFileLoop( StrA in, StrA fileName )
    {
    try
    {
    if( in.trim().length() == 0 )
      return StrA.Empty;

    StrABld sBuilder = new StrABld( 1024 );
    StrABld paramBuilder = new StrABld( 1024 );


    StrArray fileLines = in.splitChar( '\n' );
    final int last = fileLines.length();
    for( int count = 0; count < last; count++ )
      {
      StrA line = fileLines.getStrAt( count );
      if( '#' != line.firstNonSpaceChar())
        {
/*
Do this.
        // Markers.TypeCodeBlock and the number
        of the code block for the dictionary. 

        if( boolLevArray.getCurrentValue())
          sBuilder.append( line + "\n" );
        else
          sBuilder.append( "//  " + line + "\n" );
*/
        continue;
        }

     
      // StrA originalLine = line;
      line = line.replaceFirstChar( '#', ' ' );

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

      StrArray lineSplitter = line.splitChar( ' ' );
      final int lastPart = lineSplitter.length();
      if( lastPart == 0 )
        {
        mApp.showStatusAsync(
             "Preprocessor line doesn't have parts." );

        return StrA.Empty;
        }

      StrA directive = lineSplitter.getStrAt( 0 );

      // if(WINVER
      StrA directiveExtra = StrA.Empty;
      if( directive.containsChar( '(' ))
        {
        StrArray dirSplitter = directive.splitChar(
                                                '(' );
        final int dirPart = dirSplitter.length();
        directive = dirSplitter.getStrAt( 0 );

        StrABld extraBuilder = new StrABld( 1024 );
        for( int countD = 1; countD < dirPart;
                                           countD++ )
          {
          extraBuilder.appendStrA( 
                    dirSplitter.getStrAt( countD ));
 
          }

        directiveExtra = extraBuilder.toStrA();
        }

      // Remove the line number markers if they are
      // there.  Like endif with nothing following it.
      directive = directive.removeSections( 
                                        Markers.Begin,
                                        Markers.End );

      if( !isValidDirective( directive ))
        {
        mApp.showStatusAsync( directive.toString() +
                       " is not a valid directive." );
        return StrA.Empty;
        }

      paramBuilder.clear();
      for( int countP = 1; countP < lastPart; countP++ )
        {
        StrA field = lineSplitter.getStrAt( countP );
        field.concat( new StrA( " " ));
        paramBuilder.appendStrA( field );
        }

      StrA directiveBody = new StrA( directiveExtra,
                             new StrA( " " ),
                             paramBuilder.toStrA());

      directiveBody = directiveBody.trim();

      mApp.showStatusAsync( directive + " " + directiveBody );

      StrA result = processDirective( directive,
                                       directiveBody );

      if( result.length() == 0 )
        return StrA.Empty;

      // This might be appending a whole include file
      // worth of stuff.  And the include files it
      // contains.
      sBuilder.appendStrA( result );
      }

    if( boolLevArray.getLevel() != 0 )
      {
      String showS = "Level is not zero at end.\n" +
                     "Level: " +
                     boolLevArray.getLevel() + "\n" +
                     fileName.toString();

      mApp.showStatusAsync( showS );
      return StrA.Empty;
      }

    return sBuilder.toStrA();
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in mainFileLoop()." );
      mApp.showStatusAsync( e.getMessage() );
      return StrA.Empty;
      }
    }




  private StrA processDirective( StrA directive,
                                StrA directiveBody )
    {
    // Anything that gets returned here should end
    // with a newline.  And include files will have
    // many newlines.

    StrA result = StrA.Empty;

    if( directive.equalTo( DirectiveDefine ))
      {
      result = processDefine( directiveBody );
      }

    if( directive.equalTo( DirectiveError ))
      {
      result = processError( directiveBody );
      }

    if( directive.equalTo( DirectiveUndef ))
      {
      result = processUndef( directiveBody );
      }


    if( directive.equalTo( DirectiveInclude ))
      {
      result = processInclude( directiveBody );
      }

    if( directive.equalTo( DirectivePragma ))
      {
      result = processPragma( directiveBody ); 
      }

    if( directive.equalTo( DirectiveIf ))
      {
      result = processIf( directiveBody );
      }

    if( directive.equalTo( DirectiveIfdef ))
      {
      result = processIfDef( directive,
                                      directiveBody );
      }

    if( directive.equalTo( DirectiveIfndef ))
      {
      result = processIfNDef( directive,
                                     directiveBody );
      }

    if( directive.equalTo( DirectiveElse ))
      {
      result = processElse( directiveBody );
      }

    if( directive.equalTo( DirectiveElif ))
      {
      result = processElif( directiveBody );
      }

    if( directive.equalTo( DirectiveEndif ))
      {
      result = processEndIf();
      }

    if( result.length() == 0 )
      return StrA.Empty;

    if( !result.endsWithChar( '\n' ))
      {
      mApp.showStatusAsync( "processDirective() line has to end with a line feed." );
      mApp.showStatusAsync( directive.toString() );
      mApp.showStatusAsync( directiveBody.toString() );
      return StrA.Empty;
      }

    return result;
    }




  private StrA processEndIf()
    {
    if( !boolLevArray.subtractLevel())
      return StrA.Empty;

    return new StrA( "// #endif\n" );
    }



  private StrA processIfDef( StrA directive,
                               StrA directiveBody )
    {
    StrA result = new StrA( "// #" + directive +
                        " " + directiveBody + "\n" );

    if( !boolLevArray.getCurrentValue())
      {
      // If what's above it is false, then this
      // has to be false.
      boolLevArray.addNewLevel( false );
      return result;
      }

    directiveBody = directiveBody.removeSections(
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




  private StrA processIfNDef( StrA directive,
                                StrA directiveBody )
    {
    StrA result = new StrA( "// #" + directive +
                         " " + directiveBody + "\n" );

    if( !boolLevArray.getCurrentValue())
      {
      // mApp.showStatusAsync( "CurrentValue is false." );
      boolLevArray.addNewLevel( false );
      return result;
      }

    directiveBody = directiveBody.removeSections(
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




  private StrA processError( StrA directiveBody )
    {
    if( !boolLevArray.getCurrentValue())
      return new StrA( "// #error " +
                             directiveBody + "\n" );

    mApp.showStatusAsync( "Error directive: " +
                        directiveBody.toString() );

    return StrA.Empty;
    }




  private StrA processPragma( StrA directiveBody ) 
    {
    // === Use the ProcessPragma.java file.

    // pragma GCC system_header  15;stddef.h 

    // mApp.showStatusAsync( "#pragma: " + directiveBody );

    StrA result = new StrA( "// #pragma " +
                            directiveBody + "\n" );

    if( !boolLevArray.getCurrentValue())
      return result;

    // Do something with it.

    return result;
    }




  private StrA processUndef( StrA directiveBody ) 
    {
    StrA result = new StrA( "// #undef " + 
                              directiveBody + "\n" );
 
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




  private StrA processInclude( StrA directiveBody )
    {
    if( !boolLevArray.getCurrentValue())
      return new StrA( "// #include " +
                              directiveBody + "\n" );
      
    // Add comments back in to the code to show
    // where a file was included and all that.

    StrA inclFileName = directiveBody.removeSections(
                                        Markers.Begin,
                                        Markers.End );

    inclFileName = inclFileName.replace( 
                      new StrA( "\"" ), StrA.Empty );
    inclFileName = inclFileName.replace( 
                       new StrA( "<" ), StrA.Empty );
    inclFileName = inclFileName.replace( 
                       new StrA( ">" ), StrA.Empty );
    inclFileName = inclFileName.trim();
    inclFileName = inclFileName.toLowerCase();
    
    // Make a configuration file.
    // Make this work for your operating system.
    char pathDelim = '\\';
    char linuxPathDelim = '/';
    inclFileName = inclFileName.replaceChar(
                                     linuxPathDelim,
                                     pathDelim );

    if( inclFileName.equalTo( new StrA( 
                            "precompiled.hpp" )))
      {
      return new StrA( 
        "// #include precompiled.hpp is not used.\n" );
      }

    // mApp.showStatusAsync( "Looking for: " + inclFileName );
    StrA fileName = headerDictionary.
                            getValue( inclFileName );
    
    if( fileName.length() == 0 )
      {
      mApp.showStatusAsync( "No file found for: " +
                            inclFileName.toString() );
      return StrA.Empty; 
      }
    
    // String showS = "Filename found is:\n" + fileName;
    // mApp.showStatusAsync( showS );

    StrA inclFileStr = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary,
                                    headerDictionary );

    if( inclFileStr.length() == 0 )
      return StrA.Empty;

    // Once this returns from processing the
    // included files it will have a bunch of new
    // macros defined in the dictionary.

    inclFileStr = new StrA( 
               "\n\n\\\\\\\\\\\\\\\\\\\\\\\\\n" +
               "// #include " + fileName + "\n" +
               inclFileStr +
               "\n\n\\\\\\\\\\\\\\\\\\\\\\\\\n\n" );

    return inclFileStr;
    }




  private StrA processDefine( StrA directiveBody )
    {
    StrA result = new StrA( 
              "// #define " + directiveBody + "\n" );

    // mApp.showStatusAsync( "At top: " + result );
 
    if( !boolLevArray.getCurrentValue())
      return result;

    // mApp.showStatusAsync( "Before constructor." );
    Macro macro = new Macro( mApp, directiveBody,
                                              false );

    StrA showKey = macro.getKey();
    if( showKey.length() == 0 )
      {
      mApp.showStatusAsync( "Key is empty." );
      return StrA.Empty;
      }

    // mApp.showStatusAsync( "Key: " + showKey );

    if( !macro.markUpForDefine( directiveBody,
                                 macroDictionary ))
      {
      mApp.showStatusAsync( "markUpForDefine had an error." );
      return StrA.Empty;
      }

    boolean doStrict = false;
    macroDictionary.setNewMacro( doStrict,
                                 macro.getKey(),
                                 macro );

    return result;
    }



  private StrA processIf( StrA directiveBody ) 
    {
    StrA result = new StrA( "if " +
                             directiveBody + "\n" );
    if( !boolLevArray.getCurrentValue())
      {
      // If what's above it is false, then this
      // has to be false.
      boolLevArray.addNewLevel( false );
      return new StrA( "// false  " + result );
      }

    StrA exprValue = IfExpression.
                                  evaluateExpression(
                                        mApp,
                                        directiveBody,
                                     macroDictionary );

    if( exprValue.length() == 0 )
      return StrA.Empty;

    if( exprValue.equalTo( BoolExp.True ))
      {
      boolLevArray.addNewLevel( true );
      return new StrA( "// true  " + result );
      }

    if( exprValue.equalTo( BoolExp.False ))
      {
      boolLevArray.addNewLevel( false );
      return new StrA( "// false  " + result );
      }

    return new StrA( "// unknown if value returned." );
    }




  private StrA processElif( StrA directiveBody ) 
    {
    int currentLevel = boolLevArray.getLevel();
    if( currentLevel == 0 )
      {
      mApp.showStatusAsync( "elif can't be at zero." );
      return StrA.Empty;
      }

    StrA result = new StrA( 
                "// #elif " + directiveBody + "\n" );

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
        return StrA.Empty;

      return result;
      }
    else
      {
      if( !boolLevArray.setCurrentValue( true ))
        return StrA.Empty;

      return result;
      }
    }



  private StrA processElse( StrA directiveBody ) 
    {
    int currentLevel = boolLevArray.getLevel();
    if( currentLevel == 0 )
      {
      mApp.showStatusAsync( "else can't be at zero." );
      return StrA.Empty;
      }

    StrA result = new StrA( 
                "// #else " + directiveBody + "\n" );

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
        return StrA.Empty;

      return result;
      }
    else
      {
      if( !boolLevArray.setCurrentValue( true ))
        return StrA.Empty;

      return result;
      }
    }



  }
