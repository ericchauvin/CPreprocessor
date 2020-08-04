// Copyright Eric Chauvin 2020.




public class IfExpression
  {



  public static StrA evaluateExpression(
                                         MainApp mApp,
                                         StrA expr,
                     MacroDictionary macroDictionary )
    {
    if( expr == null )
      return StrA.Empty;

    StrA originalExpr = expr;
    // Remove line number markers.
    expr = expr.removeSections( Markers.Begin,
                                        Markers.End );

    expr = expr.trim();
    if( expr.length() == 0 )
      {
      mApp.showStatusAsync( "If expression is empty." );
      return StrA.Empty;
      }

    if( expr.equalTo( new StrA( "1" )))
      return BoolExp.True;

    if( expr.equalTo( new StrA( "0" )))
      return BoolExp.False;

    StrA markedUp = MarkupString.MarkItUp( 
                              mApp, expr );

    if( markedUp.length() == 0 )
      return StrA.Empty;

    // mApp.showStatusAsync( " " );
    // mApp.showStatusAsync( markedUp );
    if( markedUp.containsStrA( new StrA( "defined" )))
      {
      markedUp = setDefineValues( mApp,
                                  macroDictionary,
                                  markedUp );

      if( markedUp.length() == 0 )
        return StrA.Empty;

      if( !MarkupString.testMarkers( markedUp,
            new StrA(  "After setDefineValues()." ),
                                     mApp ))
        return StrA.Empty;

      }

    // mApp.showStatusAsync( markedUp );

    if( markedUp.equalTo( BoolExp.True ))
      return BoolExp.True;

    if( markedUp.equalTo( BoolExp.False ))
      return BoolExp.False;

    markedUp = BoolExp.evaluate( mApp, markedUp );

    if( markedUp.equalTo( BoolExp.True ))
      return BoolExp.True;

    if( markedUp.equalTo( BoolExp.False ))
      return BoolExp.False;



/*
    // mApp.showStatusAsync( "Before replaceMacros." );
    markedUp = Macro.replaceMacros( mApp,
                                    "", // no key
                                    markedUp,
                                    macroDictionary );

    if( markedUp.equalTo( BoolExp.True ))
      return BoolExp.True;

    if( markedUp.equalTo( BoolExp.False ))
      return BoolExp.False;


    // 100 != 100 && 100 != 2 && 100 != 3 &&
    // 100 != 4 && 100 != 6 &&
    // 100 != 5

*/


    // mApp.showStatusAsync( "\n\nIfExpression isn't done:\n" + markedUp );
    // mApp.showStatusAsync( "originalExpr: " + originalExpr );


// float a = Float.parseFloat( SomeString );

    // mApp.showStatusAsync( markedUp );

    return BoolExp.False;
    }




  private static StrA setDefineValues( MainApp mApp,
                      MacroDictionary macroDictionary,
                                     StrA markedUp )
    {
    // Really strings for this part.

    final String DefinedPart = "" + 
                         Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" + 
                         Markers.End;
 
    final String TrueIn1Str = DefinedPart + 
                              BoolExp.True;


    final String TrueIn2Str = DefinedPart +
                         "" + Markers.Begin +
                         Markers.TypeOperator +
                         "(" +
                         Markers.End +
                         BoolExp.True.toString() +
                         Markers.Begin +
                         Markers.TypeOperator +
                         ")" +
                         Markers.End;

    final String FalseIn1Str = DefinedPart +
                         BoolExp.False.toString();
      
    final String FalseIn2Str = DefinedPart +
                         Markers.Begin +
                         Markers.TypeOperator +
                         "(" +
                         Markers.End +
                         BoolExp.False.toString() +
                         Markers.Begin +
                         Markers.TypeOperator +
                         ")" +
                         Markers.End;

    final StrA trueIn1 = new StrA( TrueIn1Str );
    final StrA trueIn2 = new StrA( TrueIn2Str );
    final StrA falseIn1 = new StrA( FalseIn1Str );
    final StrA falseIn2 = new StrA( FalseIn2Str );

    StrArray exprParts = markedUp.splitChar(  
                                     Markers.Begin );

    final int last = exprParts.length();
    boolean isInside = false;
    StrABld sBuilder = new StrABld( 1024 );
    // The value at zero is before the first
    // Begin marker.  Which is nothing.
    // Starts at 1:
    for( int count = 1; count < last; count++ )
      {
      StrA part = exprParts.getStrAt( count );

      if( part.startsWith( new StrA( "" + 
                           Markers.TypeIdentifier +
                           "defined" )))
        {
        sBuilder.appendStrA( new StrA(
                        "" + Markers.Begin + part ));
        isInside = true;
        continue;
        }

      if( isInside )
        {
        // The first identifier after the 'defined'
        // key word.
        if( part.startsWith( new StrA( "" + 
                             Markers.TypeIdentifier )))
          {
          isInside = false;
          StrA macroName = Markers.removeAllMarkers( 
                                              part );

          if( macroDictionary.keyExists( macroName ))
            {
            sBuilder.appendStrA( BoolExp.True );
            }
          else
            {
            sBuilder.appendStrA( BoolExp.False );
            }

          continue;
          }
        }

      sBuilder.appendStrA( new StrA(
                        "" + Markers.Begin + part ));
      }

    StrA result = sBuilder.toStrA();

    // mApp.showStatusAsync( "\n\nBefore replace:\n" + result.toString() );

    // As a rule, replace bigger strings before
    // replacing smaller strings.  In case the
    // smaller string is a substring of the bigger
    // string.
    result = result.replace( trueIn2, BoolExp.True );
    result = result.replace( trueIn1, BoolExp.True );

    // mApp.showStatusAsync( "\n\nBefore falsein2: " + result.toString() );
    // mApp.showStatusAsync( "\n\nTo replace: " + falseIn2.toString() );

    result = result.replace( falseIn2, BoolExp.False ); 
    // mApp.showStatusAsync( "\n\nfalsein2: " + result.toString() );
    result = result.replace( falseIn1, BoolExp.False ); 
    // mApp.showStatusAsync( "\n\nfalsein1: " + result.toString() );


    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      mApp.showStatusAsync( "IfExpression. The string isn't right: " + result );
      return StrA.Empty;
      }

    // mApp.showStatusAsync( "\n\n" + result.toString() );

    return result;
    }



  }
