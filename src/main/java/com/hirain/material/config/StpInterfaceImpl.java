package com.hirain.material.config;

import cn.dev33.satoken.stp.StpInterface;
import com.hirain.material.entity.User;
import com.hirain.material.enums.UserRoleEnum;
import com.hirain.material.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token 权限/角色提供者：按用户角色放行 B 端管理接口。
 *
 * <p>每次校验按 loginId 查一次用户（B 端请求量小，可接受）。
 * 普通用户返回空角色列表，{@code /admin/**} 的 checkRole("admin") 会抛 NotRoleException。</p>
 *
 * @author lingzhi.Wang
 */
@Component
public class StpInterfaceImpl implements StpInterface {

  /** Sa-Token 内置的管理员角色标识 */
  private static final String ROLE_ADMIN = "admin";

  @Autowired
  private UserMapper userMapper;

  /**
   * 返回权限列表（当前项目仅按角色控制，权限粒度暂未使用）。
   *
   * @param loginId   登录主键
   * @param loginType 登录类型
   * @return 权限码列表
   */
  @Override
  public List<String> getPermissionList(Object loginId, String loginType) {
    return List.of();
  }

  /**
   * 返回角色列表，仅管理员返回 ["admin"]。
   *
   * @param loginId   登录主键
   * @param loginType 登录类型
   * @return 角色码列表
   */
  @Override
  public List<String> getRoleList(Object loginId, String loginType) {
    User u = userMapper.selectById(Long.parseLong(String.valueOf(loginId)));
    return UserRoleEnum.isAdmin(u == null ? null : u.getRole())
        ? List.of(ROLE_ADMIN) : List.of();
  }
}
