// Copyright Eric Chauvin 2018 - 2020.


// float a = Float.parseFloat( SomeString );


  public class PreprocIfLevels
  {


  // This has to be broken up in to simpler functions.
  public static String markLevels( String in,
                                   MainApp mApp,
                       DefinesDictionary definesDict )
    {
    try
    {
    StringBuilder sBuilder = new StringBuilder();
    StringBuilder lineBuilder = new StringBuilder();

    String[] splitS = in.split( Character.toString(
                                    Markers.Begin ) );

    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatus( "There is nothing in the string for markLevels()." );
      return "";
      }

    // Before the first Begin marker.
    sBuilder.append( splitS[0] );

    int level = 0;
    boolean levelBool = true;
    // Count starts at one with the first Begin marker.
    for( int count = 1; count < last; count++ )
      {
      String originalLine = splitS[count];

      // if( originalLine.contains( Character.toString(
       //                   Markers.TypeLineNumber )))

      if( !originalLine.contains( Character.toString(
                          Markers.TypePreprocessor )))
        {
        // If it's false then it gets ignored.
        // It gets taken out of the file.
        if( !levelBool )
          continue;

        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );
        continue;
        }

      String line = originalLine.trim();

      String[] splitLine = line.split( " " );
      int lastPart = splitLine.length;
      if( lastPart == 0 )
        {
        mApp.showStatus( "Preprocessor line doesn't have parts." );
        return "";
        }

      String command = splitLine[0]; 
      command = command.replace( Character.toString(
                    Markers.TypePreprocessor ), "" ); 

      boolean commandHasNoBody = command.contains(
                   Character.toString( Markers.End ));

      if( commandHasNoBody )
        {
        command = command.replace( Character.toString(
                                  Markers.End ), "" ); 
        }

      command = command.toLowerCase();
      if( !isValidCommand( command ))
        {
        mApp.showStatus( command + " is not a valid command." );
        return "";
        }

      String macroBody = "";
      String testLine = "";
      lineBuilder.setLength( 0 );

      if( commandHasNoBody )
        {
        macroBody = "";
        testLine = Character.toString( 
                        Markers.TypePreprocessor ) +
                        command + Markers.End;
 
        }
      else
        {
        for( int count2 = 1; count2 < lastPart; count2++ )
          lineBuilder.append( splitLine[count2] + " " );
 
        macroBody = lineBuilder.toString();
        macroBody = macroBody.trim();

        // mApp.showStatus( "Command: " + command ); 
        // mApp.showStatus( "Body: " + macroBody ); 
        // mApp.showStatus( " " ); 

        testLine = Character.toString( 
                        Markers.TypePreprocessor ) +
                        command + " " + macroBody;
        }

      // mApp.showStatus( testLine ); 
      // mApp.showStatus( " " ); 
      
      if( !line.equals( testLine ))
        {
        mApp.showStatus( "Command + line parts don't match up." ); 
        mApp.showStatus( testLine ); 
        mApp.showStatus( line ); 
        return "";
        }


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

      if( command.equals( "define" ))
        {
        if( !processDefineStatement( macroBody,
                                     mApp,
                                     definesDict ))
          {
          return "";
          }
        }

      if( command.equals( "undef" ))
        {
        mApp.showStatus( " " );
        mApp.showStatus( "It's a bad idea to use undef." );
        mApp.showStatus( originalLine );
        mApp.showStatus( " " );
        }

      if( command.equals( "include" ))
        {
        // Get the dictionary of #define statements
        // from this included file.
        }

      if( command.equals( "pragma" ))
        {
        }

      if( command.equals( "if" ))
        {
        level++;
/*
        mApp.showStatus( "If line: " + originalLine );
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

      if( command.equals( "ifdef" ))
        {
        level++;
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );

/*
showWarning( )
This is a function-like macro so it shouldn't be
used in this ifdef statement.
Same with ifndef.
   if( key.contains( "(" ))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "This is a function-like macro." );
*/

        // mApp.showStatus( command + ") " + level );
        }

      if( command.equals( "ifndef" ))
        {
        level++;
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );
        // mApp.showStatus( command + ") " + level );
        }

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

      if( command.equals( "elif" ))
        {
        level--;
        if( levelBool )
          {
/*
if levelBool is true then this is false.
  And vice versa.

all the way down to more elifs and else statements.
*/

          sBuilder.append( Markers.Begin );
          sBuilder.append( originalLine );
          }

        level++;
        levelBool = true;
        }

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

      if( level < 0 )
        {
        mApp.showStatus( "Level is less than zero." );
        return "";
        }
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
      mApp.showStatus( "Exception in markLevels()." );
      mApp.showStatus( e.getMessage() );
      return "";
      }
    }



  private static boolean processDefineStatement( 
                                      String in,
                                   MainApp mApp,
                       DefinesDictionary definesDict )
    {
    try
    {
    // mApp.showStatus( " " );
    // mApp.showStatus( "define statement:" );
    // mApp.showStatus( in );
    // mApp.showStatus( " " );

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
      String[] splitKey = key.split( "(" );
/*
      int splitKeyLength = splitKey.length;
      if( splitKeyLength == 0 )
        {
        mApp.showStatus( "This key has no parts." );
        return false;
        }

      // Make the parentheses part of the key.
      key = splitKey[0] + "(";

      if( splitKeyLength > 2 )
        {
        mApp.showStatus( "What is the deal here?" );
        return false;
        }

      if( splitKeyLength == 2 )
        paramBuilder.append( splitKey[1] + " " );
*/
     
      // mApp.showStatus( " " );
      // mApp.showStatus( "This is a function-like macro." );
      }


    for( int count = 1; count < last; count++ )
      paramBuilder.append( splitS[count] + " " );

    String paramList = paramBuilder.toString();
    paramList = paramList.trim();

    definesDict.setString( key, paramList );

    mApp.showStatus( " " );
    mApp.showStatus( "key: " + key );
    mApp.showStatus( "paramList: " + paramList );
    mApp.showStatus( " " );

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in processDefineStatement()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }




  private static boolean evaluateIfExpression(
                                          String in )
    {
    // This means that I need to get the defined
    // value from a previous macro in this macro.
    // if SWITCHABLE_TARGET

// definesDict

    // in = in.trim();
    if( in.equals( "0" ))
      return false;

    if( in.equals( "1" ))
      return true;

    // For now.
    return true;
    }




  private static boolean isValidCommand( String in )
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


  }
