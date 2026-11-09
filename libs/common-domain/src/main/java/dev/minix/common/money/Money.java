package dev.minix.common.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Pattern;

/** An amount of a specific asset. The amount is always stored at the asset's scale. */
public record Money(Asset asset, BigDecimal amount) implements Comparable<Money> {

  private static final Pattern PLAIN_DECIMAL = Pattern.compile("-?\\d+(\\.\\d+)?");

  public Money {
    Objects.requireNonNull(asset, "asset");
    Objects.requireNonNull(amount, "amount");
    if (amount.stripTrailingZeros().scale() > asset.scale()) {
      throw new IllegalArgumentException(
          "too many fractional digits for " + asset.symbol() + ": " + amount.toPlainString());
    }
    amount = amount.setScale(asset.scale(), RoundingMode.UNNECESSARY);
  }

  /** Parses a plain decimal such as {@code 1.23}; rejects exponents and extra digits. */
  public static Money parse(Asset asset, String text) {
    Objects.requireNonNull(asset, "asset");
    Objects.requireNonNull(text, "text");
    if (!PLAIN_DECIMAL.matcher(text).matches()) {
      throw new IllegalArgumentException("not a plain decimal amount: " + text);
    }
    int dot = text.indexOf('.');
    int fractionDigits = dot < 0 ? 0 : text.length() - dot - 1;
    if (fractionDigits > asset.scale()) {
      throw new IllegalArgumentException(
          "too many fractional digits for " + asset.symbol() + ": " + text);
    }
    return new Money(asset, new BigDecimal(text));
  }

  /** The amount as a plain decimal string at the asset's scale, never in exponent form. */
  public String toPlainString() {
    return amount.toPlainString();
  }

  public Money plus(Money other) {
    requireSameAsset(other);
    return new Money(asset, amount.add(other.amount));
  }

  public Money minus(Money other) {
    requireSameAsset(other);
    return new Money(asset, amount.subtract(other.amount));
  }

  public Money negate() {
    return new Money(asset, amount.negate());
  }

  public boolean isZero() {
    return amount.signum() == 0;
  }

  public boolean isNegative() {
    return amount.signum() < 0;
  }

  public boolean isPositive() {
    return amount.signum() > 0;
  }

  @Override
  public int compareTo(Money other) {
    requireSameAsset(other);
    return amount.compareTo(other.amount);
  }

  private void requireSameAsset(Money other) {
    Objects.requireNonNull(other, "other");
    if (!asset.equals(other.asset)) {
      throw new IllegalArgumentException(
          "asset mismatch: " + asset.symbol() + " vs " + other.asset.symbol());
    }
  }
}
