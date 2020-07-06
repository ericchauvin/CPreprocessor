// Copyright Eric Chauvin 2020.



public class BoolExp
  {
  public static final StrA True = new StrA( 
                     "" +
                     Markers.Begin +
                     Markers.TypeBoolean +
                     "true" + 
                     Markers.End );

    public static final StrA False = new StrA( 
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
                        new StrA( Equal1, Equal1 );

    public static final StrA ConcatNotEqual =
                        new StrA( NotA, Equal1 );

    public static final StrA ConcatGreaterThanEq =
                      new StrA( GreaterThan, Equal1 );

    public static final StrA ConcatLessThanEq =
                        new StrA( LessThan, Equal1 );

    public static final StrA ConcatNotTrue =
                        new StrA( NotA, True );

    public static final StrA ConcatNotFalse =
                        new StrA( NotA, False );


    public static final StrA ConcatTrueAndTrue =
                   new StrA( True, And2, True );

    public static final StrA ConcatFalseAndFalse =
                   new StrA( False, And2, False );

    public static final StrA ConcatTrueAndFalse =
                   new StrA( True, And2, False );

    public static final StrA ConcatFalseAndTrue =
                   new StrA( False, And2, True );

    public static final StrA ConcatTrueOrTrue =
                   new StrA( True, Or2, True );

    public static final StrA ConcatFalseOrFalse =
                   new StrA( False, Or2, False );

    public static final StrA ConcatTrueOrFalse =
                   new StrA( True, Or2, False );

    public static final StrA ConcatFalseOrTrue =
                   new StrA( False, Or2, True );




  public static StrA evaluate( MainApp mApp,
                               StrA in )
    {
    if( in == null )
      return StrA.Empty;

    StrA originalExpr = in;
    StrA result = new StrA( in );

    mApp.showStatusAsync( "\n\nEvaluate top: " +
                                      in.toString() );

    for( int count = 0; count < 1000; count++ )
      {
      if( result.length() < 1 )
        return StrA.Empty;

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
      if( testA.equalTo( result ))
        break;

      if( count > 10 )
        mApp.showStatusAsync( "Count over 10: " + count );

      }

    // mApp.showStatusAsync( "After operators: " + result );


    for( int count = 0; count < 1000; count++ )
      {
      if( result.length() < 1 )
        return StrA.Empty;

      StrA testA = new StrA( result );

      result = result.replace( ConcatNotTrue,
                                    False );

      result = result.replace( ConcatNotFalse,
                                    True );

      if( result.equalTo( testA ))
        break;

      if( count > 10 )
        mApp.showStatusAsync( "Count over 10: " + count );

      }

    // mApp.showStatusAsync( "After not changes: " + result );

    for( int count = 0; count < 1000; count++ )
      {
      if( result.length() < 1 )
        return StrA.Empty;

      StrA testA = new StrA( result );

      result = result.replace( ConcatTrueAndTrue,
                                         True );

      result = result.replace( ConcatFalseAndFalse,
                                         False );

      result = result.replace( ConcatTrueAndFalse,
                                         False );

      result = result.replace( ConcatFalseAndTrue,
                                         False );

      // Combine OR markers.
      result = result.replace( ConcatTrueOrTrue,
                                         True );

      result = result.replace( ConcatFalseOrFalse,
                                         False );

      result = result.replace( ConcatTrueOrFalse,
                                         True );

      result = result.replace( ConcatFalseOrTrue,
                                         True );

      if( result.equalTo( testA ))
        break;

      if( count > 10 )
        mApp.showStatusAsync( "Count over 10: " + count );

      }


    if( !MarkupString.testBeginEnd( mApp, result ))
      {
      mApp.showStatusAsync( "BoolExp.evaluate() The string isn't right: " + result );
      return StrA.Empty;
      }

    // mApp.showStatusAsync( "After true && false changes: " + result );

    mApp.showStatusAsync( "\n\nEvaluate bottom: " + result );

    return result;
    }



  }
