// Copyright Eric Chauvin 2020.


// Stringification:
// https://gcc.gnu.org/onlinedocs/gcc-4.8.5/cpp/Stringification.html



// Notice the two starting brackets here, with the
// end brackets defined later in a different macro.
// #define _BEGIN_STD_C namespace std { extern "C" {
// #define _END_STD_C  } }



public class Macro
  {
  private MainApp mApp;
  private String key = "";
  private String markedUpS = "";
  private boolean isFunctionType = false;  
  private boolean enabled = true; // For undef.
  private StringArray paramArray = null;



  private Macro()
    {
    }



  public Macro( MainApp useApp )
    {
    mApp = useApp;
    enabled = true;
    }



  public void setMacroWithEmptyParams(
                                     String keyToUse )
    {
    key = keyToUse;
    markedUpS = "";
    }



  public boolean getEnabled()
    {
    return enabled;
    }


  public void setEnabled( boolean setTo )
    {
    enabled = setTo;
    }



  public boolean setKeyFromString( String in )
    {
    try
    {
    isFunctionType = false;

    String line = StringsUtility.removeSections( in,
                                        Markers.Begin,
                                        Markers.End );

    line = line.trim();
    String[] splitS = line.split( " " );
    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatusAsync( "There is no key in the directive." );
      return false;
      }

    key = splitS[0];

    // The undef, ifdef and ifndef statements all
    // use a key without the parentheses.  The
    // parentheses is not part of the key.
    if( key.contains( "(" ))
      {
      // This is a function-like macro because there
      // was no space before the first parentheses.
      isFunctionType = true;

      StringArray lineSplitter = new StringArray();
      int lastPart = lineSplitter.
                      makeFieldsFromString( key, '(' );

      key = lineSplitter.getStringAt( 0 );
      }

    if( key.length() == 0 )
      {
      mApp.showStatusAsync( "The key length is zero." );
      return false;
      }

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in setKeyFromString()." );
      mApp.showStatusAsync( e.getMessage() );
      return false;
      }
    }



  public boolean markUpFromString( String in,
                     MacroDictionary macroDictionary,
                     boolean doStrict )
    {
    try
    {
    String originalStr = in;
    markedUpS = in;
      // Remove the line number markers.
    markedUpS = StringsUtility.removeSections(
                                        markedUpS,
                                        Markers.Begin,
                                        Markers.End );

    markedUpS = MarkupString.MarkItUp( mApp,
                                          markedUpS );

    // mApp.showStatusAsync( "markedUpS before split. " + markedUpS );

    String[] splitS = markedUpS.split( "" +
                                   Markers.Begin );

    int last = splitS.length;
    if( last < 2 )
      {
      mApp.showStatusAsync( "This macro has no key marked up." );
      return false;
      }
      
    // The string at zero is what's before the first
    // Begin marker, which is nothing.

    String testKey = splitS[1];
    if( testKey.length() < 2 )
      {
      mApp.showStatusAsync( "The key is missing." );
      mApp.showStatusAsync( markedUpS );
      return false;
      }

    char firstChar = testKey.charAt( 0 ); 
    if( firstChar != Markers.TypeIdentifier )
      {
      mApp.showStatusAsync( "The key is not an identifier." );
      mApp.showStatusAsync( markedUpS );
      return false;
      }

    testKey = Markers.removeAllMarkers( testKey );
    if( !key.equals( testKey ))
      {
      mApp.showStatusAsync( "The key is not equal to the first token." );
      mApp.showStatusAsync( "Key: >" + key + "<" );
      mApp.showStatusAsync( "TestKey: >" + testKey + "<" );
      mApp.showStatusAsync( markedUpS );
      return false;
      }

/*
Do a warning if there are too many loops to replace
macros.  Like infinite indirect references.
What if a macro has the same name as a variable name?

// ==== see if an identifier is self referential.
// Put in a warning when this happens.
// And comment the code.
*/

    StringBuilder sBuilder = new StringBuilder();
    for( int count = 2; count < last; count++ )
      {
      sBuilder.append( "" + Markers.Begin  +
                                  splitS[count] );
      }

    // Put the paramters back, but leave the key out.
    markedUpS = sBuilder.toString();
    // If there are no parameters then markedUpS
    // would have zero length.
    // mApp.showStatusAsync( "markedUpS after toString: " + markedUpS );

    if( isFunctionType )
      {
      markedUpS = putFParametersInArray( markedUpS );
      if( markedUpS.length() == 0 )
        {
        mApp.showStatusAsync( "putFPameters had an error." );
        return false;
        }
      }

    // If there are any parameters.
    if( markedUpS.length() > 0 )
      {
      for( int count = 0; count < 100; count++ )
        {
        if( count > 10 )
          mApp.showStatusAsync( "count > 10 to replace macros.\nKey: " + key + "\n" + originalStr );


        String testS = markedUpS;
        // mApp.showStatusAsync( "Before replace: " + markedUpS );
        markedUpS = replaceMacros( mApp, markedUpS,
                                    macroDictionary );

        if( markedUpS.length() == 0 )
          {
          mApp.showStatusAsync( "replaceMacros returned nothing." );
          return false;
          }

        // If it hasn't replaced anything.
        if( testS.equals( markedUpS ))
          break;

        }
      }

    // if( originalStr.contains( "Partitions" ))
      // mApp.showStatusAsync( "\nKey: " + key + "\n" + originalStr );

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in markUpFromString()." );
      mApp.showStatusAsync( e.getMessage() );
      return false;
      }
    }



  private String putFParametersInArray( String in )
    {
    mApp.showStatusAsync( "putFParam: " + in );

    paramArray = new StringArray();

    // #define WINAPI_FAMILY_PARTITION(Partitions)
    //        (Partitions)

    // #if WINAPI_FAMILY_PARTITION(WINAPI_PARTITION_APP)
    //    && !WINAPI_FAMILY_PARTITION(WINAPI_PARTITION_DESKTOP)

    StringBuilder sBuilder = new StringBuilder();
    String[] splitS = in.split( "" + Markers.Begin );
    int last = splitS.length;
    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      String partS = splitS[count];
      if( partS.length() < 2 )
        continue;

      if( inside >= 2 )
        {
        sBuilder.append( partS );
        continue;
        }

      char firstChar = partS.charAt( 0 );

      if( firstChar == Markers.TypeOperator )
        {
        char secondChar = partS.charAt( 1 );
        if( secondChar == '(' )
          {
          inside++;
          continue;
          }

        if( secondChar == ')' )
          {
          inside++;
          continue;
          }
        }

      if( inside != 1 )
        {
        mApp.showStatusAsync( "Macro params isInside != 1.\n" + in );
        return "";
        }

      if( firstChar == Markers.TypeIdentifier )
        {
        String param = Markers.removeAllMarkers( 
                                              partS );

        mApp.showStatusAsync( "Adding param: " + param );
        paramArray.appendString( param );
        continue;
        }

      // Ignoring comma that delimits the parameters.
      // if( firstChar == Markers.TypeOperator )
      mApp.showStatusAsync( "Comma: " + partS );
      }

    return sBuilder.toString();   
    }



  public static String replaceMacros( MainApp mApp,
                                String in,
                                MacroDictionary
                                macroDictionary )
    {
    // mApp.showStatusAsync( "Input to replaceMacros: " + in );

    StringBuilder sBuilder = new StringBuilder();

    String[] splitS = in.split( "" + Markers.Begin );
    int last = splitS.length;
      
    // The string at zero is what's before the first
    // Begin marker, which is nothing.
    for( int count = 1; count < last; count++ )
      {
      String partS = splitS[count];
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      String originalPartS = partS;

      char firstChar = partS.charAt( 0 ); 
      if( firstChar == Markers.TypeLineNumber )
        {
        sBuilder.append( "" + Markers.Begin  + 
                                      originalPartS );
        continue;
        }

      if( firstChar != Markers.TypeIdentifier )
        {
        sBuilder.append( "" + Markers.Begin +
                                     originalPartS );
        continue;
        }

      partS = Markers.removeAllMarkers( partS );
      if( macroDictionary.keyExists( partS ))
        {
        Macro replaceMacro = macroDictionary.
                                   getMacro( partS );

        sBuilder.append( replaceMacro.
                                getMarkedUpString());
        continue;
        }

      sBuilder.append( "" + Markers.Begin +
                                    originalPartS );

      }

    String result = sBuilder.toString();
    // mApp.showStatusAsync( "Result: " + result );
    if( !MarkupString.testMarkers( result, 
                                   "replaceMacros().",
                                   mApp ))
      {
      mApp.showStatusAsync( "testMarkers() returned false." );
      return "";
      }

    return result; 
    }




  public String getMarkedUpString()
    {
    return markedUpS;
    }



  public String getKey()
    {
    return key;
    }


  }
