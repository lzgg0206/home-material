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
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /** 业务异常 */
  @ExceptionHandler(BizException.class)
  public Result<?> handleBiz(BizException e) {
    log.warn("业务异常: {}", e.getMessage());
    return Result.fail(e.getCode(), e.getMessage());
  }

  /** 未登录 */
  @ExceptionHandler(NotLoginException.class)
  public Result<?> handleNotLogin(NotLoginException e) {
    return Result.fail(401, "未登录或登录已过期");
  }

  /** 权限/角色不足 */
  @ExceptionHandler(NotRoleException.class)
  public Result<?> handleNotRole(NotRoleException e) {
    return Result.fail(403, "权限不足");
  }

  /** @RequestBody 参数校验失败 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<?> handleValid(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldErrors().stream()
        .map(f -> f.getField() + ": " + f.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return Result.fail(400, msg);
  }

  /** 表单参数校验失败 */
  @ExceptionHandler(BindException.class)
  public Result<?> handleBind(BindException e) {
    String msg = e.getFieldErrors().stream()
        .map(f -> f.getField() + ": " + f.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return Result.fail(400, msg);
  }

  /** 兜底异常 */
  @ExceptionHandler(Exception.class)
  public Result<?> handleException(Exception e) {
    log.error("系统异常", e);
    return Result.fail("系统开小差了，请稍后再试");
  }
}
