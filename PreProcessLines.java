// Copyright Eric Chauvin 2019 - 2020.


// A macro is a section of code that is given a name.
// A directive is something like:
// #include "thisfile.h"
// The statement directs the preprocessor to do
// something.
// But the statement:
// #define THIS that
// is a directive to define a macro.



public class PreProcessLines
  {
  private MainApp mApp;
  StringArray fileLines;
  MacroDictionary macroDictionary;


  private PreProcessLines()
    {
    }


  public PreProcessLines( MainApp useApp )
    {
    mApp = useApp;
    fileLines = new StringArray();
    macroDictionary = new MacroDictionary( mApp );
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

      line = line.trim();

      // mApp.showStatus( "Preproc: " + line );

      int lastPart = lineSplitter.
                    makeFieldsFromString( line, ' ' );

      if( lastPart == 0 )
        {
        mApp.showStatus( "Preprocessor line doesn't have parts." );
        return "";
        }

      String command = lineSplitter.getStringAt( 0 );

      // If it's not already lower case then that's
      // a problem.
      // command = command.toLowerCase();
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

      String directiveBody = paramBuilder.toString();
      directiveBody = directiveBody.trim();
      String cResult = processCommand( command,
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



  private String processCommand( String command,
                                 String directiveBody,
                                 int level,
                                 boolean levelBool )
    {
    if( command.equals( "define" ))
      {
      if( !levelBool )
        {
        return "/" + "/ #define " +
                               directiveBody + "\n";

        }

      Macro macro = new Macro( mApp );
      if( !macro.setFromString( directiveBody ))
        return "";

      return "#define " + macro.getString();
      }


    /*
    if( levelBool )
      {
    if( command.equals( "error" ))
      {
      // mApp.showStatus( "This is an error." );
      // return "";
      }
      


    if( levelBool )
      {
      if( command.equals( "undef" ))
        {
        mApp.showStatus( " " );
        mApp.showStatus( "It's a bad idea to use undef." );
        mApp.showStatus( originalLine );
        mApp.showStatus( " " );
        }




    if( levelBool )
      {
      // Add comments back in to the code to show
      // where a file was included and all that.
      if( command.equals( "include" ))
        {
        // Get the dictionary of #define statements
        // from this included file.
        }



    if( levelBool )
      {
      if( command.equals( "pragma" ))
        {
        }



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



    if( levelBool )
      {
      if( command.equals( "ifdef" ))
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
      if( command.equals( "ifndef" ))
        {
        levelBool = true;
        sBuilder.append( Markers.Begin );
        sBuilder.append( originalLine );
        // mApp.showStatus( command + ") " + level );
        }



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
    return "/" + "/ Unrecognized: " + command + " " +
                             directiveBody + "\n";
    }



  }
