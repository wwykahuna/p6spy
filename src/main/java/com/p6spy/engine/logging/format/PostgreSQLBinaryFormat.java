package com.p6spy.engine.logging.format;

/**
 * Transforms binary data to PostgreSQL hex encoded strings, for example {@code \x8088BAD639F}
 * 
 * @see <a href="https://www.postgresql.org/docs/current/datatype-binary.html#id-1.5.7.12.9">PostgreSQL documentation</a>
 */
public class PostgreSQLBinaryFormat implements BinaryFormat {

  /**
   * Reserve space for the two prefix chars \ and x
   */
  private static final int PREFIX_LENGTH = 2;

  /**
   * The space needed for the opening and closing quote character.
   */
  private static final int QUOTE_COUNT = 2;

  @Override
  public String toString(byte[] input) {
    char[] result = new char[PREFIX_LENGTH + QUOTE_COUNT + input.length * 2];
    int i = 0;
    result[i++] = '\''; // opening quote
    result[i++] = '\\'; // PostgreSQL binary...
    result[i++] = 'x'; //  ...data prefix
    HexEncodedBinaryFormat.hexEncode(input, result, i);
    result[result.length - 1] = '\''; // closing quote
    return new String(result);
  }
}