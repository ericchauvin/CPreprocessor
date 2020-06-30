// Copyright Eric Chauvin 2020.



public class FunctionMacro
  {


  public static String replaceMacrosOnce( MainApp mApp,
                                   String in,
                                   MacroDictionary
                                   macroDictionary )
    {
    if( in.length() == 0 )
      return "";

    if( !in.contains( "" + Markers.Begin ))
      {
      mApp.showStatusAsync( "\n\nNo begin marker in FunctionMacro.replaceMacrosOnce(): " + in );
      return "";
      }

    String[] splitParts = in.split( "" +
                                      Markers.Begin );
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

    // mApp.showStatusAsync( "\n\n" + in );

    StringBuilder lineBuilder = new StringBuilder();
    StringBuilder functionBuilder = new StringBuilder();

    Macro replaceMacro = null;
    boolean isInFunction = false;    
    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      String partS = splitParts[count];
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      // #define mult( x, y )(x * y)
      // mult( 5, 7 ) is replaced by (5 * 7)
      // mult( (5 + 2), 7 );
      char firstChar = partS.charAt( 0 );
      char secondChar = partS.charAt( 1 );
      String linePart = "" + Markers.Begin + partS;

      if( !isInFunction )
        {
        if( firstChar != Markers.TypeIdentifier )
          {
          lineBuilder.append( linePart );
          continue;
          }

        String macName = Markers.removeAllMarkers(
                                              partS );

        if( !macroDictionary.keyExists( macName ))
          {
          lineBuilder.append( linePart );
          continue;
          }
        
        replaceMacro = macroDictionary.
                                  getMacro( macName );

        if( replaceMacro == null )
          {
          mApp.showStatusAsync( "This should not return null since the key exists." );
          return "";
          }

        if( !replaceMacro.getIsFunctionType())
          {
          lineBuilder.append( linePart );
          continue;
          }

        isInFunction = true;
        functionBuilder.setLength( 0 );
        functionBuilder.append( linePart );
        continue;
        }

      // Test:
      if( !isInFunction )
        {
        mApp.showStatusAsync( "!isInFunction" );
        return "";
        }

      if( replaceMacro == null )
        {
        mApp.showStatusAsync( "replaceMacro can't be null here." );
        return "";
        }

      if( firstChar == Markers.TypeOperator )
        {
        if( secondChar == '(' )
          {
          inside++;
          functionBuilder.append( linePart );
          continue;
          }

        if( secondChar == ')' )
          {
          inside--;
          if( inside < 0 )
            {
            mApp.showStatusAsync( "inside < 0 ) in replaceMacrosOnce()." );
            return "";
            }

          if( inside > 0 )
            {
            functionBuilder.append( linePart );
            continue;
            }

          // inside is zero.
          isInFunction = false;
          functionBuilder.append( linePart );
          String fLine = functionBuilder.toString();
          String fReturned = replaceOneFunctionString(
                                  mApp,
                                  fLine,
                                  replaceMacro );
      
          lineBuilder.append( fReturned );
          functionBuilder.setLength( 0 );
          continue;
          }
        }

      functionBuilder.append( linePart );
      continue;
      }

    String result = lineBuilder.toString();
    return result;
    }




  private static String replaceOneFunctionString(
                              MainApp mApp,
                              String in,
                              Macro replaceMacro )
    {
    if( in.length() == 0 )
      return "";

    // mApp.showStatusAsync( "\n\nReplacing one function: " +
    //                                      in );
    if( !in.contains( "" + Markers.Begin ))
      {
      mApp.showStatusAsync( "\n\nNo begin marker in FunctionMacro.replaceOneFunctionString(): " + in );
      return "";
      }

    String[] splitParts = in.split( "" +
                                      Markers.Begin );
    int last = splitParts.length;
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in FunctionMacro.replaceOneFunctionString()." );
      return "";
      } 
      
    String testNothing = splitParts[0];
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing in FunctionMacro.replaceOneFunctionString(): " + testNothing );
      return "";
      }

    StringBuilder paramBuilder = new StringBuilder();
    StrArray paramArray = new StrArray();
    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      String partS = splitParts[count];
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      // #define mult( x, y ) (x * y)
      // mult( 5, 7 ) is replaced by (5 * 7)
      // mult( (5 + 2), 7 );
      char firstChar = partS.charAt( 0 );
      char secondChar = partS.charAt( 1 );
      String linePart = "" + Markers.Begin + partS;

      if( firstChar == Markers.TypeIdentifier )
        {
        if( inside == 0 )
          continue;

        paramBuilder.append( linePart );
        continue;
        }

      if( firstChar == Markers.TypeOperator )
        {
        if( secondChar == '(' )
          {
          inside++;
          if( inside > 1 )
            paramBuilder.append( linePart );

          continue;
          }

        if( secondChar == ')' )
          {
          inside--;
          if( inside == 0 )
            {
            if( paramBuilder.length() > 0 )
              {
              paramArray.append( new StrA( paramBuilder.
                                        toString()));
              }

            break;
            }

          paramBuilder.append( linePart );
          continue;
          }

        if( secondChar == ',' )
          {
          paramArray.append( new StrA( paramBuilder.
                                        toString()));
          paramBuilder.setLength( 0 );
          continue;
          }
        }

      paramBuilder.append( linePart );
      }

    int replaceLength = replaceMacro.getParamArrayLength();
    int lastP = paramArray.length();
    if( lastP != replaceLength )
      {
      mApp.showStatusAsync( "\n\nThe two parameter arrays don't have the same length." );
      return "";
      }

    if( lastP == 0 )
      {
      mApp.showStatusAsync( "\n\nFunction has no parameters. " + in );
      return "";
      } 

    String openParenth = "" + Markers.Begin +
                            Markers.TypeOperator +
                            '(' +
                            Markers.End;

    String closeParenth = "" + Markers.Begin +
                            Markers.TypeOperator +
                            ')' +
                            Markers.End;

    String macBody = replaceMacro.getMarkedUpString();
    // mApp.showStatusAsync( "macBody: " + macBody );

    for( int count = 0; count < lastP; count++ )
      {
      String bodyParam = new StrA( paramArray.
                               getStrAt( count )).toString();

      String macParam = replaceMacro.
                         getParamArrayValue( count );

      // mApp.showStatusAsync( "macParam: " + macParam );
      // mApp.showStatusAsync( "bodyParam: " + bodyParam );
      // Just make sure, with the parentheses.
      bodyParam = openParenth + bodyParam +
                                        closeParenth;
      macBody = macBody.replace( macParam, bodyParam );
      }

    // mApp.showStatusAsync( "Replaced macBody: " + macBody );

    return macBody;
    }



  }
