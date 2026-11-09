package dev.minix.common.market;

/** Which side of the book an order is on. */
public enum Side {
  BUY,
  SELL;

  public Side opposite() {
    return this == BUY ? SELL : BUY;
  }
}
