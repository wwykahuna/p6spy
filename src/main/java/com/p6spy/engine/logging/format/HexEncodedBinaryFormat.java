package com.p6spy.engine.logging.format;

public class HexEncodedBinaryFormat implements BinaryFormat {
  private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
      'F' };

  /**
   * The space needed for the opening and closing quote character.
   */
  private static final int QUOTE_COUNT = 2;

  @Override
  public String toString(byte[] input) {
    char[] result = new char[QUOTE_COUNT + input.length * 2];
    int i = 0;
    result[i++] = '\''; // add opening quote
    hexEncode(input, result, i);
    result[result.length - 1] = '\''; // add closing quote
    return new String(result);
  }

  static void hexEncode(byte[] input, char[] output, int outputOffset) {
    int idx = outputOffset;
    for (byte b : input) {
      int temp = (int) b & 0xFF;
      output[idx++] = HEX_CHARS[temp / 16];
      output[idx++] = HEX_CHARS[temp % 16];
    }
  }
}