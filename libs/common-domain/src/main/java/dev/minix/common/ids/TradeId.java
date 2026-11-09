package dev.minix.common.ids;

/** Typed identifier; see {@link Ids} for the allowed format. */
public record TradeId(String value) {

  public TradeId {
    Ids.requireValid(value, "TradeId");
  }

  public static TradeId parse(String text) {
    return new TradeId(text);
  }

  @Override
  public String toString() {
    return value;
  }
}
