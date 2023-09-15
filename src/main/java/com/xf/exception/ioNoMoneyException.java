package com.xf.exception;

public class ioNoMoneyException extends RuntimeException {
  public ioNoMoneyException() {}

  public ioNoMoneyException(String s) {
        super(s);
    }
}
