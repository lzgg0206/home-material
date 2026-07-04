package com.hirain.material.common;

import lombok.Getter;

/**
 * 业务异常，携带业务码。
 */
@Getter
public class BizException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final int code;

  public BizException(String message) {
    this(500, message);
  }

  public BizException(int code, String message) {
    super(message);
    this.code = code;
  }
}
