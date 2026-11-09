package dev.minix.common.money;

import java.util.Locale;

/** A tradable asset: an uppercase symbol and the number of decimal places it supports. */
public record Asset(String symbol, int scale) {

  public Asset {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("symbol must not be blank");
    }
    if (!symbol.equals(symbol.toUpperCase(Locale.ROOT))) {
      throw new IllegalArgumentException("symbol must be uppercase: " + symbol);
    }
    if (scale < 0 || scale > 18) {
      throw new IllegalArgumentException("scale must be between 0 and 18: " + scale);
    }
  }
}
