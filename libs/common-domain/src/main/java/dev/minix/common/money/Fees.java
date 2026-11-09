package dev.minix.common.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Fee arithmetic. Fees always round up, so the exchange never under-charges. */
public final class Fees {

  private static final BigDecimal BPS_DIVISOR = BigDecimal.valueOf(10_000);

  private Fees() {}

  /** Returns {@code ceil(notional * bps / 10_000)} at the notional's asset scale. */
  public static Money fee(Money notional, int bps) {
    Objects.requireNonNull(notional, "notional");
    if (bps < 0) {
      throw new IllegalArgumentException("bps must not be negative: " + bps);
    }
    if (notional.isNegative()) {
      throw new IllegalArgumentException("notional must not be negative");
    }
    BigDecimal fee =
        notional
            .amount()
            .multiply(BigDecimal.valueOf(bps))
            .divide(BPS_DIVISOR, notional.asset().scale(), RoundingMode.CEILING);
    return new Money(notional.asset(), fee);
  }
}
