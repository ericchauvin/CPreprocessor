// Copyright Eric Chauvin 2020.



public class CharA
  {
  char[] values;
  int valuesLast = 0;


  private CharA()
    {
    }


  public CharA( int howLong )
    {
    // Test:
    values = new char[2];
    // values = new char[howLong];
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



  public void append( char c )
    {
    if( valuesLast >= values.length )
      resizeArray( 1024 * 4 );

    values[valuesLast] = c;
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
      append( in[count] );

    }



  public String toString()
    {
    if( valuesLast == 0 )
      return "";

    char[] resultA = new char[valuesLast];
    // System.arraycopy();
    for( int count = 0; count < valuesLast; count++ )
      resultA[count] = values[count];

    String result = new String( resultA );
    return result;
    }


  }
