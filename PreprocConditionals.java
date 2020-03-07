// Copyright Eric Chauvin 2018 - 2020.



public class PreprocConditionals
  {
  private MainApp mApp;
  StringArray fileLines;
  StringArray outFileLines;
  DefinesDictionary definesDictionary;



  private PreprocConditionals()
    {
    }


  public PreprocConditionals( MainApp useApp )
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




  }
