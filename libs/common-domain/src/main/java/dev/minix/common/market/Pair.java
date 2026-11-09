package dev.minix.common.market;

import dev.minix.common.money.Asset;
import java.util.Map;
import java.util.Objects;

/** A trading pair: the base asset priced in the quote asset, written like {@code BTC-USDT}. */
public record Pair(Asset base, Asset quote) {

  public Pair {
    Objects.requireNonNull(base, "base");
    Objects.requireNonNull(quote, "quote");
  }

  public String symbol() {
    return base.symbol() + "-" + quote.symbol();
  }

  /** Parses {@code BASE-QUOTE}, looking both symbols up in {@code assets}. */
  public static Pair parse(String text, Map<String, Asset> assets) {
    Objects.requireNonNull(text, "text");
    Objects.requireNonNull(assets, "assets");
    String[] parts = text.split("-", -1);
    if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
      throw new IllegalArgumentException("malformed pair: " + text);
    }
    return new Pair(lookup(parts[0], assets), lookup(parts[1], assets));
  }

  private static Asset lookup(String symbol, Map<String, Asset> assets) {
    Asset asset = assets.get(symbol);
    if (asset == null) {
      throw new IllegalArgumentException("unknown asset: " + symbol);
    }
    return asset;
  }
}
