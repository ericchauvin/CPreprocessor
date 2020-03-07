// Copyright Eric Chauvin 2018 - 2020.



public class PreprocConditionals
  {
  private MainApp mApp;
  StringArray fileLines;
  // StringArray outFileLines;
  DefinesDictionary definesDictionary;



  private PreprocConditionals()
    {
    }


  public PreprocConditionals( MainApp useApp )
    {
    mApp = useApp;
    fileLines = new StringArray();
    // outFileLines = new StringArray();
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
    StringBuilder sBuilder = new StringBuilder();
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
      String cResult = processCommand( command,
                                       macroBody,
                                       level,
                                       levelBool );

      if( cResult.length() == 0 )
        return "";

      // if cResult starts with comment characters
      // then what?

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



  private String processCommand( String command,
                                 String macroBody,
                                 int level,
                                 boolean levelBool )
    {
    if( command.equals( "define" ))
      {
      if( !levelBool )
        return "/" + "/ " + command + " " + macroBody;

      return processDefineStatement( macroBody );
      }


    /*
    if( levelBool )
      {
    if( command.equals( "error" ))
      {
      // mApp.showStatus( "This is an error." );
      // return "";
      }
      */

/*
    if( levelBool )
      {
      if( command.equals( "undef" ))
        {
        mApp.showStatus( " " );
        mApp.showStatus( "It's a bad idea to use undef." );
        mApp.showStatus( originalLine );
        mApp.showStatus( " " );
        }
*/


/*
    if( levelBool )
      {
      if( command.equals( "include" ))
        {
        // Get the dictionary of #define statements
        // from this included file.
        }
*/

/*
    if( levelBool )
      {
      if( command.equals( "pragma" ))
        {
        }
*/

/*
    if( levelBool )
      {
      if( command.equals( "if" ))
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
*/

/*
    if( levelBool )
      {
      if( command.equals( "ifdef" ))
        {
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
    if( levelBool )
      {
      if( command.equals( "ifndef" ))
        {
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );
        // mApp.showStatus( command + ") " + level );
        }
*/

/*
      if( command.equals( "else" ))
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
    return "/" + "/ Unrecognized: " + command + " " + macroBody;
    }



  private String processDefineStatement( String in )
    {
    try
    {
    String[] splitS = in.split( " " );
    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatus( "There is nothing in the define statement." );
      return "";
      }

    String key = splitS[0];

    StringBuilder paramBuilder = new StringBuilder();
    for( int count = 1; count < last; count++ )
      paramBuilder.append( splitS[count] + " " );

    String paramStr = paramBuilder.toString();
    // Make sure this paramStr starts with a space.
    paramStr = " " + paramStr.trim();

    if( key.contains( "(" ))
      {
      // This is a function-like macro because there
      // was no space before the first parentheses.

      // This key could end with the parentheses,
      // but sometimes they have no space after the
      // parentheses too like this:
      // DEF_SANITIZER_BUILTIN_1(ENUM,

      StringArray lineSplitter = new StringArray();
      lineSplitter.makeFieldsFromString( key, '(' );

      int lastPart = lineSplitter.length();

      key = lineSplitter.getStringAt( 0 );
      if( lastPart == 2 )
        {
        paramStr = "(" + lineSplitter.getStringAt( 1 ) +
                              " " + paramStr.trim();
        }
      else
        {
        // What does that space mean?
        // paramStr = "( " + paramStr.trim();

        paramStr = "(" + paramStr.trim();
        }
      }

    definesDictionary.setString( key, paramStr );

    mApp.showStatus( " " );
    mApp.showStatus( "key: " + key );
    mApp.showStatus( "paramStr: " + paramStr );
    mApp.showStatus( " " );

    return key + paramStr;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in processDefineStatement()." );
      mApp.showStatus( e.getMessage() );
      return "";
      }
    }




  }
