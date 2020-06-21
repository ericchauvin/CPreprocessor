// Copyright Eric Chauvin 2020.


public class FunctionMacro
  {
  private MainApp mApp;
  private String[] splitParts = null;
  private MacroDictionary macroDictionary;



  private FunctionMacro()
    {
    }


  public FunctionMacro( MainApp useApp,
                      MacroDictionary useDictionary )
    {
    mApp = useApp;
    macroDictionary = useDictionary;
    }



  public String replaceMacrosOnce( String in,
                                   MacroDictionary
                                   macroDictionary )
    {
    // #define WINAPI_FAMILY_PARTITION(Partitions)
    //        (Partitions)

    // A function-like macro can be replaced in the
    // expression for an #if statement.
    // #if WINAPI_FAMILY_PARTITION(WINAPI_PARTITION_APP)
    //    && !WINAPI_FAMILY_PARTITION(WINAPI_PARTITION_DESKTOP)7

    if( !in.contains( "" + Markers.Begin ))
      {
      mApp.showStatusAsync( "\n\nNo begin marker in FunctionMacro.replaceMacrosOnce(): " + in );
      return "";
      }

    splitParts = in.split( "" + Markers.Begin );
    int last = splitParts.length;
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in FunctionMacro.replaceMacrosOnce()." );
      return "";
      } 
      
    String testNothing = splitParts[0];
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing in FunctionMacro.replaceMacrosOnce(): " + testNothing );
      return "";
      }
    
    boolean foundFMacro = false;
    boolean foundIdentifier = false;
    for( int count = 1; count < last; count++ )
      {
      String partS = splitParts[count];
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      char firstChar = partS.charAt( 0 );
      if( firstChar == Markers.TypeIdentifier )
        {
        foundIdentifier = true;
        partS = Markers.removeAllMarkers( partS );
        if( !macroDictionary.keyExists( partS ))
          continue;

        Macro testMacro = macroDictionary.
                                   getMacro( partS );

        if( testMacro == null )
          {
          mApp.showStatusAsync( "This should not return null since the key exists." );
          return "";
          }

        if( testMacro.getIsFunctionType())
          foundFMacro = true;

        }
      }

    if( !foundFMacro )
      return in;

    if( !foundIdentifier )
      return in;

    String result = replaceFunctionMacros();


    return result;
    }




  private String replaceFunctionMacros()
    {
    if( splitParts == null )
      {
      mApp.showStatusAsync( "splitParts is null in replaceFunctionMacros()." );
      return "";
      }

    mApp.showStatusAsync( "\n\n" );

    StringBuilder sBuilder = new StringBuilder();
    int last = splitParts.length;
    for( int count = 1; count < last; count++ )
      {
      String partS = splitParts[count];
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      char firstChar = partS.charAt( 0 );
      if( firstChar == Markers.TypeIdentifier )
        {
        partS = Markers.removeAllMarkers( partS );
        mApp.showStatusAsync( "PartS: " + partS );
        if( !macroDictionary.keyExists( partS ))
          continue;

        Macro replaceMacro = macroDictionary.
                                   getMacro( partS );

        if( replaceMacro == null )
          {
          mApp.showStatusAsync( "This should not return null since the key exists." );
          return "";
          }

        if( !replaceMacro.getIsFunctionType())
          continue;

        String replaceMarked = replaceMacro.getMarkedUpString();


        mApp.showStatusAsync( "replaceMarked: " + replaceMarked );
     
        int lastParam = replaceMacro.
                                getParamArrayLength();

        for( int countP = 0; countP < lastParam; countP++ )
          {
          String toShow = replaceMacro.
                        getParamArrayValue( countP );

          mApp.showStatusAsync( "Param: " + toShow );
          }
        }
      }

    // String result = sBuilder.toString();
    return ""; // result;
    }



  }

