// Copyright Eric Chauvin 2020.



public class StringA
  {

   // public String substring(int beginIndex,
   //                         int endIndex)
      


  public static String replace( String in,
                                String toFind,
                                String replace )
    {
    if( in.length() == 0 )
      return "";

    String testS = in.replace( toFind, replace );
    return testS;

/*
    if( toFind.length() == 0 )
      return in;

    // Replace with nothing.  replace( in, "this", "" );
    // if( replace.length() == 0 )

    if( in.length() < toFind.length() )
      return in;
    
    char[] inA = in.toCharArray();
    char[] toFindA = toFind.toCharArray();
    char[] replaceA = replace.toCharArray();

    CharA resultA = new CharA( inA.length + 2048 );

    char firstChar = toFindA[0];    
    int last = inA.length;
    int skip = 0;
    for( int count = 0; count < last; count++ )
      {
      if( skip > 0 )
        {
        skip--;
        if( skip > 0 )
          continue;

        }

      char testChar = inA[count];
      if( testChar != firstChar )
        {
        resultA.append( testChar );
        continue;
        }

      if( !searchTextMatches( count, inA, toFindA ))
        {
        resultA.append( testChar );
        continue;
        }


      skip = toFindA.length + 1;
      if( replaceA.length > 0 )
        resultA.appendArray( replaceA );

      }

    String result = resultA.toString();
    if( result == null )
      return "\n\nResult was null.";

    if( result.length() == 0 )
      return "\n\nResult length was zero.";
 
    if( !result.equals( testS ))
      return "\n\nBad replace: " + result +
                              "\ntestS: " + testS;

    return result;
*/
    }




  private static boolean searchTextMatches( int position,
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



  }
