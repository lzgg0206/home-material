package com.hirain.material.common;

import lombok.Getter;

/**
 * 业务异常，携带业务码。
 *
 * @author lingzhi.Wang
 */
@Getter
public class BizException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final int code;

  /**
   * 构造业务异常（默认 500）。
   *
   * @param message 异常提示信息
   */
  public BizException(String message) {
    this(500, message);
  }

  /**
   * 构造业务异常（自定义码）。
   *
   * @param code    业务码
   * @param message 异常提示信息
   */
  public BizException(int code, String message) {
    super(message);
    this.code = code;
  }
}
