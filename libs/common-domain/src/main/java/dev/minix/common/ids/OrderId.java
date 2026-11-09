package dev.minix.common.ids;

/** Typed identifier; see {@link Ids} for the allowed format. */
public record OrderId(String value) {

  public OrderId {
    Ids.requireValid(value, "OrderId");
  }

  public static OrderId parse(String text) {
    return new OrderId(text);
  }

  @Override
  public String toString() {
    return value;
  }
}
