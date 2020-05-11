// Copyright Eric Chauvin 2019 - 2020.




public class PreProcessLines
  {
  private MainApp mApp;
  private StringArray fileLines;
  private MacroDictionary macroDictionary;


  private PreProcessLines()
    {
    }


  public PreProcessLines( MainApp useApp, 
                   MacroDictionary dictionaryToUse )
    {
    mApp = useApp;
    fileLines = new StringArray();
    macroDictionary = dictionaryToUse;
    }



  private boolean isValidCommand( String in )
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

    if( in.equals( "pragma" )) // like #pragma once
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

    if( in.trim().length() == 0 )
      return "";

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
        if( levelBool )
          sBuilder.append( line + "\n" );
        else
          sBuilder.append( "/" + "/  " + line + "\n" );

        continue;
        }
     
      String originalLine = line;
      line = StringsUtility.replaceFirstChar( line,
                                              '#',
                                              ' ' );

      // Remove the line number markers.
      line = StringsUtility.removeSections( line,
                                       Markers.Begin,
                                       Markers.End );

      // This trim is needed to make sure the directive
      // is the first field.  So there are no empty
      // fields in front of it.
      line = line.trim();

      // mApp.showStatus( "Preproc: " + line );

      // This is splitting the fields by space 
      // characters, but if there's more than one
      // space character then it will create a 
      // zero-length string on the second space
      // because there was nothing after the first
      // space.
      int lastPart = lineSplitter.
                    makeFieldsFromString( line, ' ' );

      if( lastPart == 0 )
        {
        mApp.showStatus( "Preprocessor line doesn't have parts." );
        return "";
        }

      String directive = lineSplitter.getStringAt( 0 );

      // If it's not already lower case then that's
      // a problem.
      // directive = directive.toLowerCase();
      if( !isValidCommand( directive ))
        {
        mApp.showStatus( directive + " is not a valid directive." );
        return "";
        }

      level = setLevel( directive, level );
      if( level < 0 )
        {
        mApp.showStatus( "Level is less than zero." );
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

        paramBuilder.append( 
                field +
                " " );

        }

      String directiveBody = paramBuilder.toString();
      String cResult = processDirective( directive,
                                       directiveBody,
                                       level,
                                       levelBool );

      if( cResult.length() == 0 )
        return "";

      sBuilder.append( cResult );
      }

    if( level != 0 )
      {
      mApp.showStatus( "Level is not zero at end." );
      return "";
      }

    return sBuilder.toString();
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



  private String processDirective( String directive,
                                 String directiveBody,
                                 int level,
                                 boolean levelBool )
    {
    if( directive.equals( "define" ))
      {
      if( !levelBool )
        {
        return "/" + "/ #define " +
                               directiveBody + "\n";

        }
    
      Macro macro = new Macro( mApp, macroDictionary );
      if( !macro.setKeyFromString( directiveBody ))
        return "";

      String showKey = macro.getKey();
      mApp.showStatus( "Key: " + showKey );
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




    if( levelBool )
      {
      // Add comments back in to the code to show
      // where a file was included and all that.
      if( directive.equals( "include" ))
        {
        Do preprocessing on the included file.
        String test = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary );

        Once this returns from processing the
        included files it will have a bunch of new
        macros defined in the dictionary.
        }



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
