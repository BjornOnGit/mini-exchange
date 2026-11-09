package dev.minix.common.ids;

import java.util.regex.Pattern;

/** Shared validation for the typed IDs: 1 to 64 letters, digits, dashes or underscores. */
final class Ids {

  private static final Pattern VALID = Pattern.compile("[A-Za-z0-9_-]{1,64}");

  private Ids() {}

  static String requireValid(String value, String type) {
    if (value == null || !VALID.matcher(value).matches()) {
      throw new IllegalArgumentException("invalid " + type + ": " + value);
    }
    return value;
  }
}
