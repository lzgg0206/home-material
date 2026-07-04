package com.hirain.material.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link Result} 统一响应工厂方法单测。
 */
class ResultTest {

  @Test
  void successNoData() {
    Result<Void> r = Result.success();
    assertEquals(200, r.getCode());
    assertEquals("success", r.getMessage());
    assertNull(r.getData());
  }

  @Test
  void successWithData() {
    Result<String> r = Result.success("hi");
    assertEquals(200, r.getCode());
    assertEquals("hi", r.getData());
  }

  @Test
  void failDefaultCode() {
    Result<Void> r = Result.fail("boom");
    assertEquals(500, r.getCode());
    assertEquals("boom", r.getMessage());
    assertNull(r.getData());
  }

  @Test
  void failCustomCode() {
    Result<Void> r = Result.fail(401, "未登录");
    assertEquals(401, r.getCode());
    assertEquals("未登录", r.getMessage());
  }
}
