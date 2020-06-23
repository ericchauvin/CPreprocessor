// Copyright Eric Chauvin 2020.



public class StringF
  {

  private static int searchTextMatches( int position,
                                 String textToSearch,
                                 String searchText )
    {
    int sLength = searchText.length();
    if( sLength < 1 )
      return -1;

    if( (position + sLength - 1) >= textToSearch.length() )
      return -1;

    for( int count = 0; count < sLength; count++ )
      {
      if( searchText.charAt( count ) != textToSearch.
                            charAt( position + count ) )
        return -1;

      }

    return position;
    }



  public static String replace( String in,
                                String toFind,
                                String replace )
    {
    if( in == null )
      return "";

    int max = in.length();
    if( max == 0 )
      return "";

    StringBuilder sBuilder = new StringBuilder();
    for( int count = 0; count < max; count++ )
      {
      char testChar = in.charAt( count );


/*
      if( !foundFirst )
        {
        if( testChar == toFind )
          {
          foundFirst = true;
          sBuilder.append( replace );
          continue;
          }
        }
*/
      sBuilder.append( testChar );
      }

    return sBuilder.toString();
    }



  }

