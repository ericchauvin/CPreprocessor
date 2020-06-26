// Copyright Eric Chauvin 2020.




public class IfExpression
  {



  public static String evaluateExpression(
                                         MainApp mApp,
                                         String expr,
                     MacroDictionary macroDictionary )
    {
    if( expr == null )
      return "";

    String originalExpr = expr;
    // Remove line number markers.
    expr = StringsUtility.removeSections( expr,
                                        Markers.Begin,
                                        Markers.End );

    expr = expr.trim();
    if( expr.length() == 0 )
      {
      mApp.showStatusAsync( "If expression is empty." );
      return "";
      }

    if( expr.equals( "1" ))
      return "true";

    if( expr.equals( "0" ))
      return "false";

    String markedUp = MarkupString.MarkItUp( 
                                         mApp, expr );

    if( markedUp.length() == 0 )
      return "";

    // mApp.showStatusAsync( " " );
    // mApp.showStatusAsync( markedUp );
    if( markedUp.contains( "defined" ))
      {
      markedUp = setDefineValues( mApp,
                                  macroDictionary,
                                  markedUp );

      if( markedUp.length() == 0 )
        return "";

      if( !MarkupString.testMarkers( markedUp, "After setDefineValues().", mApp ))
        return "";

      }

    // mApp.showStatusAsync( markedUp );

    if( markedUp.equals( BoolExp.TrueA ))
      return BoolExp.TrueA.toString();

    if( markedUp.equals( BoolExp.FalseA ))
      return BoolExp.FalseA.toString();

// ======
    // markedUp = BoolExp.evaluate( mApp, markedUp );

    if( markedUp.equals( BoolExp.TrueA ))
      return BoolExp.TrueA.toString();

    if( markedUp.equals( BoolExp.FalseA ))
      return BoolExp.FalseA.toString();


/*
    // mApp.showStatusAsync( "Before replaceMacros." );
    markedUp = Macro.replaceMacros( mApp,
                                    "", // no key
                                    markedUp,
                                    macroDictionary );

    if( markedUp.equals( BoolExp.TrueA ))
      return "true";

    if( markedUp.equals( BoolExp.FalseA ))
      return "false";


    // 100 != 100 && 100 != 2 && 100 != 3 &&
    // 100 != 4 && 100 != 6 &&
    // 100 != 5

*/


    mApp.showStatusAsync( "\n\nIfExpression isn't done:\n" + markedUp );
    mApp.showStatusAsync( "originalExpr: " + originalExpr );


// float a = Float.parseFloat( SomeString );

    // mApp.showStatusAsync( markedUp );

    return "false";
    }




  private static String setDefineValues( MainApp mApp,
                      MacroDictionary macroDictionary,
                                     String markedUp )
    {
    String trueIn1Str = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" + 
                         Markers.End +
                         BoolExp.TrueA.toString();


    String trueIn2Str = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" +
                         Markers.End +
                         Markers.Begin +
                         Markers.TypeOperator +
                         "(" +
                         Markers.End +
                         BoolExp.TrueA.toString() +
                         Markers.Begin +
                         Markers.TypeOperator +
                         ")" +
                         Markers.End;

    String falseIn1Str = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" +
                         Markers.End +
                         BoolExp.FalseA.toString();
      
    String falseIn2Str = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" +
                         Markers.End +
                         Markers.Begin +
                         Markers.TypeOperator +
                         "(" +
                         Markers.End +
                         BoolExp.FalseA.toString() +
                         Markers.Begin +
                         Markers.TypeOperator +
                         ")" +
                         Markers.End;

    StrA trueIn1 = new StrA( trueIn1Str );
    StrA trueIn2 = new StrA( trueIn2Str );
    StrA falseIn1 = new StrA( falseIn1Str );
    StrA falseIn2 = new StrA( falseIn2Str );


// ==== delete these if it tests right.
    if( !MarkupString.testBeginEnd( mApp, trueIn1Str ))
      {
      mApp.showStatusAsync( "The string isn't right: " + trueIn1Str );
      return "";
      }

    if( !MarkupString.testBeginEnd( mApp, trueIn2Str ))
      {
      mApp.showStatusAsync( "The string isn't right: " + trueIn2Str );
      return "";
      }

    if( !MarkupString.testBeginEnd( mApp, falseIn1Str ))
      {
      mApp.showStatusAsync( "The string isn't right: " + falseIn1Str );
      return "";
      }

    if( !MarkupString.testBeginEnd( mApp, falseIn2Str ))
      {
      mApp.showStatusAsync( "The string isn't right: " + falseIn2Str );
      return "";
      }


    String[] exprParts = markedUp.split( "" + 
                                     Markers.Begin );

    int last = exprParts.length;
    boolean isInside = false;
    StringBuilder sBuilder = new StringBuilder();
    // The value at zero is before the first
    // Begin marker.  Which is nothing.
    // Starts at 1:
    for( int count = 1; count < last; count++ )
      {
      String part = exprParts[count];

      if( part.startsWith( "" + 
                           Markers.TypeIdentifier +
                           "defined" ))
        {
        sBuilder.append( "" + Markers.Begin + part );
        isInside = true;
        continue;
        }

      if( isInside )
        {
        // The first identifier after the 'defined'
        // key word.
        if( part.startsWith( "" + 
                             Markers.TypeIdentifier ))
          {
          isInside = false;
          String macroName = Markers.removeAllMarkers( 
                                              part );

          if( macroDictionary.keyExists( macroName ))
            {
            sBuilder.append( BoolExp.TrueA.toString() );
            }
          else
            {
            sBuilder.append( BoolExp.FalseA.toString() );
            }

          continue;
          }
        }

      sBuilder.append( "" + Markers.Begin + part );
      }

    StrA result = new StrA( sBuilder.toString());

    mApp.showStatusAsync( "\n\n" + result.toString() );
    result = result.replace( trueIn1, BoolExp.TrueA );

    mApp.showStatusAsync( "\n\n" + result.toString() );
    result = result.replace( trueIn2, BoolExp.TrueA );
    mApp.showStatusAsync( "\n\n" + result.toString() );

    result = result.replace( falseIn1, BoolExp.FalseA ); 
    mApp.showStatusAsync( "\n\n" + result.toString() );

    result = result.replace( falseIn2, BoolExp.FalseA ); 
    if( !MarkupString.testBeginEnd( mApp, result.toString() ))
      {
      mApp.showStatusAsync( "5 The string isn't right: " + falseIn2Str );
      return "";
      }

    mApp.showStatusAsync( "\n\n" + result.toString() );

    return result.toString();
    }



  }
