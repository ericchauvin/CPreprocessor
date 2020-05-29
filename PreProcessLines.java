// Copyright Eric Chauvin 2019 - 2020.




public class PreProcessLines
  {
  private MainApp mApp;
  private MacroDictionary macroDictionary;



  private PreProcessLines()
    {
    }


  public PreProcessLines( MainApp useApp, 
                   MacroDictionary dictionaryToUse )
    {
    mApp = useApp;
    macroDictionary = dictionaryToUse;
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

    BooleanLevel boolLevel = new BooleanLevel( mApp );
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
        if( boolLevel.getValue() )
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
      // Remove the line number markers.
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

      boolLevel.setLevel( directive );
      if( boolLevel.getCurrentLevel() < 0 )
        {
        mApp.showStatusAsync( "Level is less than zero." );
        return "";
        }

      paramBuilder.setLength( 0 );
      for( int countP = 1; countP < lastPart; countP++ )
        {
        String field = lineSplitter.
                               getStringAt( countP );

        // Can't do this here because it would reformat
        // some things like formatted strings.
        // if( field.length() == 0 )
          // continue;

        paramBuilder.append( field + " " );
        }

      String directiveBody = paramBuilder.toString();
      String result = processDirective( directive,
                                        directiveBody,
                                        boolLevel );

      if( result.length() == 0 )
        return "";

      sBuilder.append( result );
      }

    if( boolLevel.getCurrentLevel() != 0 )
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
                              String directiveBody,
                              BooleanLevel boolLevel )
    {
    if( directive.equals( "define" ))
      {
      if( !boolLevel.getValue() )
        {
        return "/" + "/ #define " +
                               directiveBody + "\n";

        }
    
      Macro macro = new Macro( mApp, macroDictionary );
      if( !macro.setKeyFromString( directiveBody ))
        return "";

      String showKey = macro.getKey();
      // mApp.showStatusAsync( "Key: " + showKey );
 
      if( !macro.markUpFromString( directiveBody ))
        return "";

      return "#" + directive + " " + directiveBody;
      // "#define " + macro.getString();
      }



    /*
    if( levelBool )
      {
    if( directive.equals( "error" ))
      {
      // mApp.showStatus( "This is an error." );
      // return "";
      }
      


    if( levelBool )
      {
      if( directive.equals( "undef" ))
        {
        mApp.showStatus( " " );
        mApp.showStatus( "It's a bad idea to use undef." );
        mApp.showStatus( originalLine );
        mApp.showStatus( " " );

Get the key and then disable it.
        Macro macro = new Macro( mApp, macroDictionary );
        if( !macro.setKeyFromString( directiveBody ))
          return "";

        }
*/



    if( boolLevel.getValue() )
      {
      // Add comments back in to the code to show
      // where a file was included andl all that.
      if( directive.equals( "include" ))
        {
        String inclFile = StringsUtility.
                                    removeSections(
                                        directiveBody,
                                        Markers.Begin,
                                        Markers.End );

        mApp.showStatusAsync( inclFile );

/*
        Do preprocessing on the included file.
        String test = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary );

        Once this returns from processing the
        included files it will have a bunch of new
        macros defined in the dictionary.
*/
        }
      }



/*
    if( levelBool )
      {
      if( directive.equals( "pragma" ))
        {
        }



    if( levelBool )
      {
      if( directive.equals( "if" ))
        {

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



    if( levelBool )
      {
      if( directive.equals( "ifdef" ))
        {
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );

   if( key.contains( "(" ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "This is a function-like macro." );
////////

        // mApp.showStatus( command + ") " + level );
        }




    if( levelBool )
      {
      if( directive.equals( "ifndef" ))
        {
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );
        // mApp.showStatus( command + ") " + level );
        }



      if( directive.equals( "else" ))
        {
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



      if( directive.equals( "elif" ))
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




      if( directive.equals( "endif" ))
        {
        if( levelBool )
          {
          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );
          }

        levelBool = true;
        }
*/

    // mApp.showStatus( "Unrecognized command in processCommand()." );
    // return "";
    return "/" + "/ Unrecognized: " + directive +
                " " + directiveBody + "\n";
    }



  }
