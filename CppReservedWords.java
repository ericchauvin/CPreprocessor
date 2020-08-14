// Copyright Eric Chauvin 2020.



public class CppReservedWords
  {
  private final static String[] reservedWords = {
      "_Alignas",
      "_Alignof",
      "_Atomic",
      "_Bool",
      "_Complex",
      "_Generic",
      "_Imaginary",
      "_Noreturn",
      "_Static_assert",
      "_Thread_local",
      "alignas",
      "alignof",
      "and",
      "and_eq",
      "asm",
      "atomic_cancel",
      "atomic_commit",
      "atomic_noexcept",
      "auto",
      "bitand",
      "bitor",
      "bool",
      "break",
      "case",
      "catch",
      "char",
      "char8_t",
      "char16_t",
      "char32_t",
      "class",
      "compl",
      "concept",
      "const",
      "consteval",
      "constexpr",
      "constinit",
      "const_cast",
      "continue",
      "co_await",
      "co_return",
      "co_yield",
      "decltype",
      "default",
      "delete",
      "do",
      "double",
      "dynamic_cast",
      "else",
      "enum",
      "explicit",
      "export",
      "extern",
      "false",
      "float",
      "for",
      "fortran", // Like asm apparently.
      "friend",
      "goto",
      "if",
      "inline",
      "int",
      "long",  // Also, long long is like one key word.
      "mutable",
      "namespace",
      "new",
      "noexcept",
      "not",
      "not_eq",
      "nullptr",
      "operator",
      "or",
      "or_eq",
      "private",
      "protected",
      "public",
      "reflexpr",
      "register",
      "reinterpret_cast",
      "requires",
      "restrict",
      "return",
      "s",
      "short",
      "signed",
      "sizeof",
      "static",
      "static_assert",
      "static_cast",
      "struct",
      "switch",
      "synchronized",
      "template",
      "this",
      "thread_local",
      "throw",
      "true",
      "try",
      "typedef",
      "typeid",
      "typename",
      "union",
      "unsigned",
      "using",
      "virtual",
      "void",
      "volatile",
      "wchar_t",
      "while",
      "xor",
      "xor_eq",
      "final",
      "override",
      "transaction_safe",
      "transaction_safe_dynamic" };
    


  public static boolean isReserved( String in )
    {
    final int last = reservedWords.length;
    for( int count = 0; count < last; count++ )
      {
      if( in.compareTo( reservedWords[count] ) == 0 )
        return true;

      }

    return false;
    }




/*

Java key words.

abstract continue for new switch
assert default goto* package synchronized
boolean do if private this
break double implements protected throw
byte else import public throws
case enum instanceof return transient
catch extends int short try
char final interface static void
class finally long strictfp volatile
const* float native super while
* Not used.

*/

  }
