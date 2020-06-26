// Copyright Eric Chauvin 2020.



// split()
// splitChar()
// substring(int beginIndex, int endIndex)



public class StrA
  {
  private final char[] values;


  private StrA()
    {
    values = new char[0];
    }


  public StrA( String in )
    {
    // The length might be zero.
    int last = in.length();

    // values is final so it can be assigned once.
    values = new char[last];
    for( int count = 0; count < last; count++ )
      values[count] = in.charAt( count );

    }



  public StrA( char[] in )
    {
    // Make a copy of the character array in.

    // The length might be zero.
    int last = in.length;

    values = new char[last];
    for( int count = 0; count < last; count++ )
      values[count] = in[count];

    }



  public StrA( StrA in )
    {
    // Make a copy of the character array in.

    // The length might be zero.
    int last = in.values.length;

    values = new char[last];
    for( int count = 0; count < last; count++ )
      values[count] = in.values[count];

    }



  public StrA( StrA in1, StrA in2 )
    {
    int max = in1.values.length + in2.values.length;
    char[] both = new char[max];

    int where = 0;
    int last = in1.values.length;
    for( int count = 0; count < last; count++ )
      {
      both[where] = in1.values[count];
      where++;
      }
    
    last = in2.values.length;
    for( int count = 0; count < last; count++ )
      {
      both[where] = in2.values[count];
      where++;
      }
    
    values = both;
    }



  private char[] stringToCharArray( String in )
    {
    if( in == null )
      return new char[0];

    // last can be zero.
    int last = in.length();
    char[] result = new char[last];
    for( int count = 0; count < last; count++ )
      result[count] = in.charAt( count );

    return result;
    }



  public int length()
    {
    return values.length;
    }


  public char charAt( int where )
    {
    if( where < 0 )
      return Markers.ErrorPoint;

    if( where >= values.length )
      return Markers.ErrorPoint;

    return values[where];
    }



  public char[] getCopyOfValues()
    {
    int last = values.length;
    char[] result = new char[last];
    for( int count = 0; count < last; count++ )
      result[count] = values[count];

    return result;
    }



  public char[] toCharArray()
    {
    return getCopyOfValues();
    }



 /*
  public void testFinal( String in )
    {
    int last = in.length();

    // This causes a compile time error.
    // values = new char[last];

    // This does not cause an error.
    for( int count = 0; count < last; count++ )
      values[count] = in.charAt( count );
 
    }
*/



  public String toString()
    {
    int last = values.length;
    if( last == 0 )
      return "";

    // Can't do:
    // String result = new String( values );

    StringBuilder sBuilder = new StringBuilder();
    // Don't use System.arraycopy() since it's going
    // to be translated in to C++.

    // This has to have the ("" +) in it so it works
    // right with Marker characters.
    for( int count = 0; count < last; count++ )
      sBuilder.append( "" + values[count] );

    return sBuilder.toString();
    }



  private boolean searchTextMatches( int position,
                                     char[] toSearch,
                                     char[] toFind )
    {
    int findLength = toFind.length;
    if( findLength < 1 )
      return false;

    // Count goes from 0 to sLength - 1.
    if( (position + findLength - 1) >= toSearch.length )
      return false;

    for( int count = 0; count < findLength; count++ )
      {
      if( toSearch[position + count] != toFind[count] )
        return false;

      }

    return true;
    }



  public String replace( String toFind,
                         String replaceS )
    {
    if( toFind.length() == 0 )
      return toString();

    if( toFind.length() == 0 )
      return toString();

    // ReplaceS might be an empty string.
    // replace( in, "this", "" );
    // if( replaceS.length() == 0 )

    if( values.length < toFind.length() )
      return toString();

    String testS = toString().replace( toFind, replaceS );
   
    StrA toFindA = new StrA( toFind );
    StrA replaceA = new StrA( replaceS );

    StrA resultA = replace( toFindA,
                           replaceA );


    String result = resultA.toString();
    if( !result.equals( testS ))
      return "Bad string: " + result + " and testS " + testS;

    return result;
    }



  public StrA replace( StrA toFind,
                       StrA replace )
    {
    if( values.length == 0 )
      return new StrA( "" );

    if( toFind.length() == 0 )
      return new StrA( values );

    // Replace might be an empty string.
    // replace( in, "this", "" );
    // if( replace.length() == 0 )

    if( values.length < toFind.length() )
      return  new StrA( values );
    
    StrABld resultBld = new StrABld( values.length + 2048 );

    char firstChar = toFind.charAt( 0 );    
    int last = values.length;
    int skip = 0;
    for( int count = 0; count < last; count++ )
      {
      if( skip > 0 )
        {
        skip--;
        if( skip > 0 )
          continue;

        }

      char testChar = values[count];
      if( testChar != firstChar )
        {
        resultBld.append( testChar );
        continue;
        }

      if( !searchTextMatches( count,
                              values, // getCopyOfValues(),
                              toFind.values ))
        {
        resultBld.append( testChar );
        continue;
        }

      skip = toFind.length() + 1;
      if( replace.length() > 0 )
        resultBld.appendArray( replace.values );

      }

    StrA result = resultBld.toStrA();
    return result;
    }


  public StrA replaceChar( char toFind, char replace )
    {
    if( values.length == 0 )
      return new StrA( "" );

    int last = values.length;
    char[] toArray = new char[last];
    for( int count = 0; count < last; count++ )
      {
      char testChar = values[count];
      if( testChar == toFind )
        toArray[count] = replace;
      else
        toArray[count] = testChar;

      }

    StrA result = new StrA( toArray );
    return result;
    }


  public StrA concat( StrA in )
    {
    if( in.length() == 0 )
      return this; // new StrA( values );

    int max = values.length + in.length();
    char[] both = new char[max];

    int where = 0;
    int last = values.length;
    for( int count = 0; count < last; count++ )
      {
      both[where] = values[count];
      where++;
      }
    
    last = in.length();
    for( int count = 0; count < last; count++ )
      {
      both[where] = in.charAt( count );
      where++;
      }

    return new StrA( both );  
    }



  public boolean equals( StrA in )
    {
    int last = values.length;
    if( last != in.values.length )
      return false;

    for( int count = 0; count < last; count++ )
      {
      if( values[count] != in.values[count] )
        return false;

      }

    return true;
    }



  }
