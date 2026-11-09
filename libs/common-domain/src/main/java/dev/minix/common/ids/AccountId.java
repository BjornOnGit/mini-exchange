package dev.minix.common.ids;

/** Typed identifier; see {@link Ids} for the allowed format. */
public record AccountId(String value) {

  public AccountId {
    Ids.requireValid(value, "AccountId");
  }

  public static AccountId parse(String text) {
    return new AccountId(text);
  }

  @Override
  public String toString() {
    return value;
  }
}
