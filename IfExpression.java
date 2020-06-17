// Copyright Eric Chauvin 2020.




public class IfExpression
  {
  public static final String trueStr = "" +
                     Markers.Begin +
                     Markers.TypeBoolean +
                     "true" + 
                     Markers.End;

    public static final String falseStr = "" +
                     Markers.Begin +
                     Markers.TypeBoolean +
                     "false" + 
                     Markers.End;

  public static final String notStr = "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     "!" +
                     Markers.End;


    public static final String andOp1 = "" +
                               Markers.Begin +
                               Markers.TypeOperator +
                               "&" +
                               Markers.End;

    public static final String andOp2 = "" +
                                 Markers.Begin +
                                 Markers.TypeOperator +
                                 "&&" +
                                 Markers.End;

    public static final String orOp1 = "" +
                                 Markers.Begin +
                                 Markers.TypeOperator +
                                 "|" +
                                 Markers.End;

    public static final String orOp2 = "" +
                                 Markers.Begin +
                                 Markers.TypeOperator +
                                 "||" +
                                 Markers.End;

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

    mApp.showStatusAsync( " " );
    mApp.showStatusAsync( markedUp );
    if( markedUp.contains( "defined" ))
      {
      markedUp = setDefineValues( mApp,
                                  macroDictionary,
                                  markedUp );

      if( markedUp.length() == 0 )
        return "";

      }

    mApp.showStatusAsync( markedUp );

    if( markedUp.equals( trueStr ))
      return "true";

    if( markedUp.equals( falseStr ))
      return "false";

    markedUp = combineBoolean( mApp, markedUp );
    mApp.showStatusAsync( "Combined: " + markedUp );

    if( markedUp.equals( trueStr ))
      return "true";

    if( markedUp.equals( falseStr ))
      return "false";


// float a = Float.parseFloat( SomeString );

    // mApp.showStatusAsync( markedUp );

    return "false";
    }



  private static String setDefineValues( MainApp mApp,
                      MacroDictionary macroDictionary,
                                     String markedUp )
    {
    // There might be no parentheses after 'defined'
    // like this:
    // # if defined __GNUC__ && defined __GNUC_MINOR__

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
          String macroName = part.replace( "" +
                        Markers.TypeIdentifier, "" );

          macroName = macroName.replace( "" +
                                   Markers.End, "" );

          if( macroDictionary.keyExists( macroName ))
            {
            sBuilder.append( trueStr );
            }
          else
            {
            sBuilder.append( falseStr );
            }

          continue;
          }
        }

      sBuilder.append( "" + Markers.Begin + part );
      }
    
    String result = sBuilder.toString();

    String trueIn1 = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" + 
                         Markers.End +
                         trueStr;


    String trueIn2 = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" + Markers.End +
                         Markers.Begin +
                         Markers.TypeOperator + "(" +
                         Markers.End +
                         trueStr +
                         Markers.Begin +
                         Markers.TypeOperator + ")" +
                         Markers.End;

    String falseIn1 = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" +
                         Markers.End +
                         falseStr;
      
    String falseIn2 = "" + Markers.Begin + 
                         Markers.TypeIdentifier +
                         "defined" + Markers.End +
                         Markers.Begin +
                         Markers.TypeOperator + "(" +
                         Markers.End +
                         falseStr +
                         Markers.Begin +
                         Markers.TypeOperator + ")" +
                         Markers.End;

    String trueOut = "" + Markers.Begin +
                     Markers.TypeBoolean +
                     "true" + 
                     Markers.End;

    String falseOut = "" + Markers.Begin +
                     Markers.TypeBoolean +
                     "false" + 
                     Markers.End;

    result = result.replace( trueIn1, trueOut );
    result = result.replace( trueIn2, trueOut );
 
    result = result.replace( falseIn1, falseOut ); 
    result = result.replace( falseIn2, falseOut ); 
    mApp.showStatusAsync( result );
    return result;
    }



  private static String combineBoolean( MainApp mApp,
                                         String in )
    {
    String result = in;
 
    int startLength = 0;
    int newLength = 0;

    for( int count = 0; count < 1000; count++ )
      {
      startLength = result.length(); 
      if( startLength < 1 )
        return "";

      result = result.replace( andOp1 + andOp1, andOp2 );
      result = result.replace( orOp1 + orOp1, orOp2 );
      newLength = result.length();
      if( newLength == startLength )
        break;

      if( count > 100 )
        mApp.showStatusAsync( "Count over 100: " + count );

      }

    mApp.showStatusAsync( "After operators: " + result );

    for( int count = 0; count < 1000; count++ )
      {
      startLength = result.length(); 
      if( startLength < 1 )
        return "";

      // Notice that this can't replace something
      // with a parentheses in front of it.
      result = result.replace( notStr + trueStr,
                                         falseStr );

      result = result.replace( notStr + falseStr,
                                         trueStr );

      newLength = result.length();
      if( newLength == startLength )
        break;

      if( count > 100 )
        mApp.showStatusAsync( "Count over 100: " + count );

      }

    mApp.showStatusAsync( "After not changes: " + result );

    // Do true or true
    //   true and false and all that.

    return result;
    }



  }
