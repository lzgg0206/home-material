package com.hirain.material.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结构。
 *
 * @param <T> 业务数据类型
 * @author lingzhi.Wang
 */
@Data
public class Result<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 业务码：200 成功，其余失败 */
  private int code;

  /** 提示信息 */
  private String message;

  /** 业务数据 */
  private T data;

  /**
   * 成功（无数据）。
   *
   * @param <T> 业务数据类型
   * @return 成功响应，data 为 null
   */
  public static <T> Result<T> success() {
    return success(null);
  }

  /**
   * 成功（带数据）。
   *
   * @param data 业务数据
   * @param <T>  业务数据类型
   * @return 成功响应，code=200
   */
  public static <T> Result<T> success(T data) {
    Result<T> r = new Result<>();
    r.setCode(200);
    r.setMessage("success");
    r.setData(data);
    return r;
  }

  /**
   * 失败（默认 500）。
   *
   * @param message 提示信息
   * @param <T>     业务数据类型
   * @return 失败响应，code=500
   */
  public static <T> Result<T> fail(String message) {
    return fail(500, message);
  }

  /**
   * 失败（自定义码）。
   *
   * @param code    业务码
   * @param message 提示信息
   * @param <T>     业务数据类型
   * @return 失败响应
   */
  public static <T> Result<T> fail(int code, String message) {
    Result<T> r = new Result<>();
    r.setCode(code);
    r.setMessage(message);
    return r;
  }
}
