package dev.minix.common.market;

import dev.minix.common.money.Money;
import java.math.BigDecimal;
import java.util.Objects;

/** Trading rules for one pair: price step, quantity step and minimum order value. */
public record PairSpec(Pair pair, BigDecimal tickSize, BigDecimal lotSize, BigDecimal minNotional) {

  public PairSpec {
    Objects.requireNonNull(pair, "pair");
    requirePositive(tickSize, "tickSize");
    requirePositive(lotSize, "lotSize");
    requirePositive(minNotional, "minNotional");
  }

  /** Converts a price to a whole number of ticks; the price must be an exact multiple. */
  public long priceToTicks(BigDecimal price) {
    return toUnits(price, "price", tickSize, "tickSize");
  }

  /** Converts a positive whole number of ticks back to the exact price. */
  public BigDecimal ticksToPrice(long ticks) {
    return fromUnits(ticks, tickSize, "ticks");
  }

  /** Converts a quantity to a whole number of lots; the quantity must be an exact multiple. */
  public long qtyToLots(BigDecimal quantity) {
    return toUnits(quantity, "quantity", lotSize, "lotSize");
  }

  /** Converts a positive whole number of lots back to the exact quantity. */
  public BigDecimal lotsToQty(long lots) {
    return fromUnits(lots, lotSize, "lots");
  }

  /** The value of an order in the quote asset: price in ticks times quantity in lots. */
  public Money notional(long priceTicks, long lots) {
    if (priceTicks <= 0) {
      throw new IllegalArgumentException("priceTicks must be positive: " + priceTicks);
    }
    if (lots <= 0) {
      throw new IllegalArgumentException("lots must be positive: " + lots);
    }
    BigDecimal units = BigDecimal.valueOf(Math.multiplyExact(priceTicks, lots));
    return new Money(pair.quote(), units.multiply(tickSize).multiply(lotSize));
  }

  private static long toUnits(BigDecimal value, String name, BigDecimal step, String stepName) {
    Objects.requireNonNull(value, name);
    if (value.signum() <= 0) {
      throw new IllegalArgumentException(name + " must be positive: " + value.toPlainString());
    }
    BigDecimal[] quotientAndRemainder = value.divideAndRemainder(step);
    if (quotientAndRemainder[1].signum() != 0) {
      throw new IllegalArgumentException(
          name + " is not a multiple of " + stepName + ": " + value.toPlainString());
    }
    try {
      return quotientAndRemainder[0].longValueExact();
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(name + " too large: " + value.toPlainString(), e);
    }
  }

  private static BigDecimal fromUnits(long units, BigDecimal step, String unitName) {
    if (units <= 0) {
      throw new IllegalArgumentException(unitName + " must be positive: " + units);
    }
    return BigDecimal.valueOf(units).multiply(step);
  }

  private static void requirePositive(BigDecimal value, String name) {
    Objects.requireNonNull(value, name);
    if (value.signum() <= 0) {
      throw new IllegalArgumentException(name + " must be positive: " + value.toPlainString());
    }
  }
}
