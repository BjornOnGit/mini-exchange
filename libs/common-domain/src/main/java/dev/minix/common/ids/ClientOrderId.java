package dev.minix.common.ids;

/** Typed identifier; see {@link Ids} for the allowed format. */
public record ClientOrderId(String value) {

  public ClientOrderId {
    Ids.requireValid(value, "ClientOrderId");
  }

  public static ClientOrderId parse(String text) {
    return new ClientOrderId(text);
  }

  @Override
  public String toString() {
    return value;
  }
}
