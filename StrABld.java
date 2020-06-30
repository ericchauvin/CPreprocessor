// Copyright Eric Chauvin 2020.



public class StrABld
  {
  char[] values;
  int valuesLast = 0;


  private StrABld()
    {
    }


  public StrABld( int howLong )
    {
    // Test:
    // values = new char[2];
    values = new char[howLong];
    }


  public int length()
    {
    return valuesLast;
    }


  public void setLength( int in )
    {
    valuesLast = in;
    }



  private void resizeArray( int toAdd )
    {
    int max = values.length;
    char[] tempValues = new char[max + toAdd];
    for( int count = 0; count < valuesLast; count++ )
      {
      tempValues[count] = values[count];
      }

    values = tempValues;
    }



  public void appendChar( char in )
    {
    if( valuesLast >= values.length )
      resizeArray( 1024 * 4 );

    values[valuesLast] = in;
    valuesLast++;
    }


  public void appendArray( char[] in )
    {
    if( in == null )
      return;

    int last = in.length;
    if( last == 0 )
      return;

    for( int count = 0; count < last; count++ )
      appendChar( in[count] );

    }




  public void appendStrA( StrA in )
    {
    if( in == null )
      return;

    int last = in.length();
    if( last == 0 )
      return;

    for( int count = 0; count < last; count++ )
      appendChar( in.charAt( count ));

    }



/*
  public String toString()
    {
    if( valuesLast == 0 )
      return "";

    // Can't do:
    // String result = new String( aCharArray );
 
    StringBuilder sBuilder = new StringBuilder();
    for( int count = 0; count < valuesLast; count++ )
      sBuilder.append( "" + values[count] );

    return sBuilder.toString();
    }
*/


  public StrA toStrA()
    {
    if( valuesLast == 0 )
      return new StrA( "" );

    // Can't do:
    // String result = new String( aCharArray );
    
    char[] result = new char[valuesLast];
    for( int count = 0; count < valuesLast; count++ )
      result[count] = values[count];

    return new StrA( result );
    }


  public StrA getReverse()
    {
    char[] result = new char[valuesLast];
    for( int count = valuesLast - 1; count >= 0; count-- )
      result[count] = values[count];

    return new StrA( result );
    }


  }
