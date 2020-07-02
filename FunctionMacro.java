// Copyright Eric Chauvin 2020.



public class FunctionMacro
  {


  public static StrA replaceMacrosOnce( MainApp mApp,
                                   StrA in,
                                   MacroDictionary
                                   macroDictionary )
    {
    if( in.length() == 0 )
      return new StrA( "" );

    if( !in.containsChar( Markers.Begin ))
      {
      mApp.showStatusAsync( "\n\nNo begin marker in FunctionMacro.replaceMacrosOnce(): " + in );
      return new StrA( "" );
      }

    StrArray splitParts = in.splitChar( Markers.Begin );
    int last = splitParts.length();
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in FunctionMacro.replaceMacrosOnce()." );
      return new StrA( "" );
      } 
      
    StrA testNothing = splitParts.getStrAt( 0 );
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing in FunctionMacro.replaceMacrosOnce(): " + testNothing );
      return new StrA( "" );
      }

    // mApp.showStatusAsync( "\n\n" + in );

    StrABld lineBuilder = new StrABld( 1024 );
    StrABld functionBuilder = new StrABld( 1024 );

    Macro replaceMacro = null;
    boolean isInFunction = false;    
    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      StrA partS = splitParts.getStrAt( count );
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      // #define mult( x, y )(x * y)
      // mult( 5, 7 ) is replaced by (5 * 7)
      // mult( (5 + 2), 7 );
      char firstChar = partS.charAt( 0 );
      char secondChar = partS.charAt( 1 );
      StrA linePart = new StrA( "" + Markers.Begin +
                                             partS );

      if( !isInFunction )
        {
        if( firstChar != Markers.TypeIdentifier )
          {
          lineBuilder.appendStrA( linePart );
          continue;
          }

        StrA macName = Markers.removeAllMarkers(
                                              partS );

        if( !macroDictionary.keyExists( macName ))
          {
          lineBuilder.appendStrA( linePart );
          continue;
          }
        
        replaceMacro = macroDictionary.
                                  getMacro( macName );

        if( replaceMacro == null )
          {
          mApp.showStatusAsync( "This should not return null since the key exists." );
          return new StrA( "" );
          }

        if( !replaceMacro.getIsFunctionType())
          {
          lineBuilder.appendStrA( linePart );
          continue;
          }

        isInFunction = true;
        functionBuilder.clear();
        functionBuilder.appendStrA( linePart );
        continue;
        }

      // Test:
      if( !isInFunction )
        {
        mApp.showStatusAsync( "!isInFunction" );
        return new StrA( "" );
        }

      if( replaceMacro == null )
        {
        mApp.showStatusAsync( "replaceMacro can't be null here." );
        return new StrA( "" );
        }

      if( firstChar == Markers.TypeOperator )
        {
        if( secondChar == '(' )
          {
          inside++;
          functionBuilder.appendStrA( linePart );
          continue;
          }

        if( secondChar == ')' )
          {
          inside--;
          if( inside < 0 )
            {
            mApp.showStatusAsync( "inside < 0 ) in replaceMacrosOnce()." );
            return new StrA( "" );
            }

          if( inside > 0 )
            {
            functionBuilder.appendStrA( linePart );
            continue;
            }

          // inside is zero.
          isInFunction = false;
          functionBuilder.appendStrA( linePart );
          StrA fLine = functionBuilder.toStrA();
          StrA fReturned = replaceOneFunctionString(
                                  mApp,
                                  fLine,
                                  replaceMacro );
      
          lineBuilder.appendStrA( fReturned );
          functionBuilder.clear();
          continue;
          }
        }

      functionBuilder.appendStrA( linePart );
      continue;
      }

    StrA result = lineBuilder.toStrA();
    return result;
    }




  private static StrA replaceOneFunctionString(
                              MainApp mApp,
                              StrA in,
                              Macro replaceMacro )
    {
    if( in.length() == 0 )
      return new StrA( "" );

    // mApp.showStatusAsync( "\n\nReplacing one function: " +
    //                                      in );
    if( !in.containsChar( Markers.Begin ))
      {
      mApp.showStatusAsync( "\n\nNo begin marker in FunctionMacro.replaceOneFunctionString(): " + in );
      return new StrA( "" );
      }

    StrArray splitParts = in.splitChar( Markers.Begin );
    int last = splitParts.length();
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in FunctionMacro.replaceOneFunctionString()." );
      return new StrA( "" );
      } 
      
    StrA testNothing = splitParts.getStrAt( 0 );
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing in FunctionMacro.replaceOneFunctionString(): " + testNothing );
      return new StrA( "" );
      }

    StrABld paramBuilder = new StrABld( 1024 );
    StrArray paramArray = new StrArray();
    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      StrA partS = splitParts.getStrAt( count );
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      // #define mult( x, y ) (x * y)
      // mult( 5, 7 ) is replaced by (5 * 7)
      // mult( (5 + 2), 7 );
      char firstChar = partS.charAt( 0 );
      char secondChar = partS.charAt( 1 );
      StrA linePart = new StrA( "" + Markers.Begin +
                                            partS );

      if( firstChar == Markers.TypeIdentifier )
        {
        if( inside == 0 )
          continue;

        paramBuilder.appendStrA( linePart );
        continue;
        }

      if( firstChar == Markers.TypeOperator )
        {
        if( secondChar == '(' )
          {
          inside++;
          if( inside > 1 )
            paramBuilder.appendStrA( linePart );

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

          paramBuilder.appendStrA( linePart );
          continue;
          }

        if( secondChar == ',' )
          {
          paramArray.append( new StrA( paramBuilder.
                                        toString()));
          paramBuilder.clear();
          continue;
          }
        }

      paramBuilder.appendStrA( linePart );
      }

    int replaceLength = replaceMacro.getParamArrayLength();
    int lastP = paramArray.length();
    if( lastP != replaceLength )
      {
      mApp.showStatusAsync( "\n\nThe two parameter arrays don't have the same length." );
      return new StrA( "" );
      }

    if( lastP == 0 )
      {
      mApp.showStatusAsync( "\n\nFunction has no parameters. " + in );
      return new StrA( "" );
      } 

    StrA openParenth = new StrA( "" + Markers.Begin +
                            Markers.TypeOperator +
                            '(' +
                            Markers.End );

    StrA closeParenth = new StrA( "" + Markers.Begin +
                            Markers.TypeOperator +
                            ')' +
                            Markers.End );

    StrA macBody = replaceMacro.getMarkedUpS();
    // mApp.showStatusAsync( "macBody: " + macBody );

    for( int count = 0; count < lastP; count++ )
      {
      StrA bodyParam = paramArray.getStrAt( count );

      StrA macParam = replaceMacro.
                         getParamArrayValue( count );

      // mApp.showStatusAsync( "macParam: " + macParam );
      // mApp.showStatusAsync( "bodyParam: " + bodyParam );
      // Just make sure, with the parentheses.
      bodyParam = openParenth;
      bodyParam = bodyParam.concat( bodyParam );
      bodyParam = bodyParam.concat( closeParenth );
      macBody = macBody.replace( macParam, bodyParam );
      }

    // mApp.showStatusAsync( "Replaced macBody: " + macBody );

    return macBody;
    }



  }
