// Copyright Eric Chauvin 2020.



public class BoolExp
  {
  public static final StrA TrueA = new StrA( 
                     "" +
                     Markers.Begin +
                     Markers.TypeBoolean +
                     "true" + 
                     Markers.End );

    public static final StrA FalseA = new StrA( 
                     "" +
                     Markers.Begin +
                     Markers.TypeBoolean +
                     "false" + 
                     Markers.End );

  public static final StrA NotA = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     "!" +
                     Markers.End );

  public static final StrA Equal1 = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     "=" +
                     Markers.End );

  public static final StrA Equal2 = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     "==" +
                     Markers.End );

  public static final StrA NotEqual = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     "!=" +
                     Markers.End );

  public static final StrA GreaterThan = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     ">" +
                     Markers.End );

  public static final StrA GreaterThanOrEq = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     ">=" +
                     Markers.End );

  public static final StrA LessThan = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     "<" +
                     Markers.End );

  public static final StrA LessThanOrEq = new StrA(
                     "" +
                     Markers.Begin +
                     Markers.TypeOperator +
                     "<=" +
                     Markers.End );

    public static final StrA And1 = new StrA(
                               "" +
                               Markers.Begin +
                               Markers.TypeOperator +
                               "&" +
                               Markers.End );

    public static final StrA And2 = new StrA(
                                 "" +
                                 Markers.Begin +
                                 Markers.TypeOperator +
                                 "&&" +
                                 Markers.End );

    public static final StrA Or1 = new StrA(
                                 "" +
                                 Markers.Begin +
                                 Markers.TypeOperator +
                                 "|" +
                                 Markers.End );

    public static final StrA Or2 = new StrA(
                                 "" +
                                 Markers.Begin +
                                 Markers.TypeOperator +
                                 "||" +
                                 Markers.End );

    public static final StrA ConcatAnd =
                        new StrA( And1, And1 );

    public static final StrA ConcatOr =
                        new StrA( Or1, Or1 );

    public static final StrA ConcatEqual =
                        new StrA( Equal1,
                                  Equal1 );

    public static final StrA ConcatNotEqual =
                        new StrA( NotA,
                                  Equal1 );

    public static final StrA ConcatGreaterThanEq =
                        new StrA( GreaterThan,
                                  Equal1 );

    public static final StrA ConcatLessThanEq =
                        new StrA( LessThan,
                                  Equal1 );


  public static String evaluate( MainApp mApp,
                                 String in )
    {
    if( in == null )
      return "";

    String originalExpr = in;
    StrA result = new StrA( in );

    mApp.showStatusAsync( "\n\nEvaluate top: " + in );
    for( int count = 0; count < 1000; count++ )
      {
      if( result.length() < 1 )
        return "";

      StrA testA = new StrA( result );
      result = result.replace( ConcatAnd, And2 );
      result = result.replace( ConcatOr, Or2 );
      result = result.replace( ConcatEqual,
                               Equal2 );
      result = result.replace( ConcatNotEqual,
                               NotEqual );
      result = result.replace( ConcatGreaterThanEq,
                               GreaterThanOrEq );

      result = result.replace( ConcatLessThanEq,
                               LessThanOrEq );

      // If nothing was replaced.
      if( testA.equals( result ))
        break;

      if( count > 10 )
        mApp.showStatusAsync( "Count over 10: " + count );

      }

    // mApp.showStatusAsync( "After operators: " + result );




/*
    for( int count = 0; count < 1000; count++ )
      {
      startLength = result.length(); 
      if( startLength < 1 )
        return "";

      // This won't replace !(trueStr... with the
      // parentheses.
      result = result.replace( notStr + trueStr,
                                    falseStr );

      result = result.replace( notStr + falseStr,
                                    trueStr );

      newLength = result.length();
      if( newLength == startLength )
        break;

      if( count > 10 )
        mApp.showStatusAsync( "Count over 10: " + count );

      }

    // mApp.showStatusAsync( "After not changes: " + result );
*/



/*
    // Do true or true
    //   true and false and all that.
    for( int count = 0; count < 1000; count++ )
      {
      startLength = result.length(); 
      if( startLength < 1 )
        return "";

      // Combine AND markers.
      result = result.replace( trueStr + andOp2 + trueStr,
                                         trueStr );

      result = result.replace( falseStr + andOp2 + falseStr,
                                         falseStr );

      result = result.replace( trueStr + andOp2 + falseStr,
                                         falseStr );

      result = result.replace( falseStr + andOp2 + trueStr,
                                         falseStr );

      // Combine OR markers.
      result = result.replace( trueStr + orOp2 + trueStr,
                                         trueStr );

      result = result.replace( falseStr + orOp2 + falseStr,
                                         falseStr );

      result = result.replace( trueStr + orOp2 + falseStr,
                                         trueStr );

      result = result.replace( falseStr + orOp2 + trueStr,
                                         trueStr );

      newLength = result.length();
      if( newLength == startLength )
        break;

      if( count > 10 )
        mApp.showStatusAsync( "Count over 10: " + count );

      }
*/

    // mApp.showStatusAsync( "After true && false changes: " + result );

    mApp.showStatusAsync( "\n\nEvaluate bottom: " + in );


    return result.toString();
    }



  }

