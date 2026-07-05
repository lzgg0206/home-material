package com.hirain.material.common;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理，统一转为 {@link Result}。
 *
 * @author lingzhi.Wang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 处理业务异常，按异常携带的业务码返回。
   *
   * @param e 业务异常
   * @return 统一响应
   */
  @ExceptionHandler(BizException.class)
  public Result<?> handleBiz(BizException e) {
    log.warn("业务异常: {}", e.getMessage());
    return Result.fail(e.getCode(), e.getMessage());
  }

  /**
   * 处理未登录异常，返回 401。
   *
   * @param e 未登录异常
   * @return 统一响应
   */
  @ExceptionHandler(NotLoginException.class)
  public Result<?> handleNotLogin(NotLoginException e) {
    return Result.fail(401, "未登录或登录已过期");
  }

  /**
   * 处理角色不足异常，返回 403。
   *
   * @param e 角色不足异常
   * @return 统一响应
   */
  @ExceptionHandler(NotRoleException.class)
  public Result<?> handleNotRole(NotRoleException e) {
    return Result.fail(403, "权限不足");
  }

  /**
   * 处理 @RequestBody 参数校验失败，返回 400 及字段错误明细。
   *
   * @param e 参数校验异常
   * @return 统一响应
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<?> handleValid(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldErrors().stream()
        .map(f -> f.getField() + ": " + f.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return Result.fail(400, msg);
  }

  /**
   * 处理表单参数校验失败，返回 400 及字段错误明细。
   *
   * @param e 绑定异常
   * @return 统一响应
   */
  @ExceptionHandler(BindException.class)
  public Result<?> handleBind(BindException e) {
    String msg = e.getFieldErrors().stream()
        .map(f -> f.getField() + ": " + f.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return Result.fail(400, msg);
  }

  /**
   * 兜底处理未知异常，记录错误日志并返回通用提示。
   *
   * @param e 未知异常
   * @return 统一响应
   */
  @ExceptionHandler(Exception.class)
  public Result<?> handleException(Exception e) {
    log.error("系统异常", e);
    return Result.fail("系统开小差了，请稍后再试");
  }
}
