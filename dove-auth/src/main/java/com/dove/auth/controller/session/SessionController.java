package com.dove.auth.controller.session;

import com.dove.common.web.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会话控制器
 */
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    /**
     * 获取当前会话信息
     */
    @GetMapping("/current")
    public Result<Object> getCurrentSession() {
        // TODO: 实现获取当前会话信息逻辑
        return Result.success();
    }

    /**
     * 获取在线会话列表
     */
    @GetMapping
    public Result<Object> listSessions() {
        // TODO: 实现获取在线会话列表逻辑
        return Result.success();
    }

    /**
     * 强制下线
     */
    @DeleteMapping("/{sessionId}")
    public Result<Void> kickout(@PathVariable String sessionId) {
        // TODO: 实现强制下线逻辑
        return Result.success();
    }

    /**
     * 批量强制下线
     */
    @DeleteMapping("/batch")
    public Result<Void> batchKickout(@RequestParam String[] sessionIds) {
        // TODO: 实现批量强制下线逻辑
        return Result.success();
    }
} 