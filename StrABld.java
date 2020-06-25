// Copyright Eric Chauvin 2020.


/*
 This is the String constructor, in String.java,
 that doesn't work.
    public String(char value[]) {
        this.value = Arrays.copyOf(value, value.length);
    }
*/



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

    // Can't do:
    // String result = new String( aCharArray );
 
    StringBuilder sBuilder = new StringBuilder();
    for( int count = 0; count < valuesLast; count++ )
      sBuilder.append( "" + values[count] );

    return sBuilder.toString();
    }


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



  }

