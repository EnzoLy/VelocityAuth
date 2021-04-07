package net.koru.auth.utils;

import java.security.SecureRandom;
import java.util.Random;

public class SaltGenerator {
  private static final Random RANDOM = new SecureRandom();
  
  private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  
  private static final char[] INTS = "0123456789".toCharArray();
  
  private static final int HEX_MAX_INDEX = 16;
  
  public static String generateString() {
    return generateNumbers(CHARS.length);
  }
  
  public static String generateCaptcha(int length) {
    return generateNumb(length);
  }
  
  private static String generateNumbers(int index) {
    StringBuilder stringBuilder = new StringBuilder(10);
    for (byte b = 0; b < 10; b++)
      stringBuilder.append(CHARS[RANDOM.nextInt(index)]);
    return stringBuilder.toString();
  }
  
  private static String generateNumb(int length) {
    StringBuilder stringBuilder = new StringBuilder(length);
    for (byte b = 0; b < length; b++)
      stringBuilder.append(INTS[RANDOM.nextInt(9)]); 
    return stringBuilder.toString();
  }
  
  public static String generateHex(int length) {
    return generateString(length, HEX_MAX_INDEX);
  }
  
  private static String generateString(int length, int index) {
    if (length < 0)
      throw new IllegalArgumentException("Length must be positive but was " + length);
    StringBuilder stringBuilder = new StringBuilder(length);
    for (byte b = 0; b < length; b++)
      stringBuilder.append(CHARS[RANDOM.nextInt(index)]);
    return stringBuilder.toString();
  }
}
