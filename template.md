2. 安全管理
   - 支持密码策略管理(密码复杂度、有效期)
   - 支持登录策略管理(失败次数、锁定时间)
   - 支持MFA多因素认证(短信、邮件、TOTP)
   - 支持会话管理与控制(单点登录)
   - 支持审计日志记录(操作日志、安全日志)
   - 支持安全事件管理(异常登录、密码修改)

3. 系统管理
   - 支持系统配置管理(全局配置、租户配置)
   - 支持字典数据管理(系统字典、业务字典)
   - 支持参数配置管理(系统参数、业务参数)
   - 支持通知消息管理(系统通知、业务通知)
   - 支持操作日志管理(访问日志、变更日志)
   - 支持系统监控管理(性能监控、业务监控)

4. 业务扩展
   - 支持多租户隔离(独立schema、共享表)
   - 支持国际化配置(多语言、多时区)
   - 支持自定义字段(动态表单、工作流)
   - 支持工作流集成(审批流、业务流)
   - 支持第三方集成(SSO、API)
   - 支持插件扩展(功能扩展、业务扩展)

   
2. 安全管理相关表
   - dove_security_policy: 安全策略表
     * id: bigint(20) - 主键,雪花算法生成
     * policy_type: varchar(50) - 策略类型
     * policy_name: varchar(100) - 策略名称
     * policy_content: text - 策略内容(JSON)
     * status: tinyint - 状态(0-禁用,1-启用)
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_security_log: 安全日志表
     * id: bigint(20) - 主键,雪花算法生成
     * user_id: bigint(20) - 用户ID
     * event_type: varchar(50) - 事件类型
     * event_time: datetime - 事件时间
     * event_status: tinyint - 事件状态
     * ip_address: varchar(50) - IP地址
     * location: varchar(255) - 地理位置
     * device_info: varchar(255) - 设备信息
     * event_detail: text - 事件详情
     * tenant_id: bigint(20) - 租户ID
     * created_time: datetime - 创建时间

   - dove_operation_log: 操作日志表
     * id: bigint(20) - 主键,雪花算法生成
     * user_id: bigint(20) - 用户ID
     * module: varchar(50) - 操作模块
     * operation: varchar(50) - 操作类型
     * method: varchar(255) - 方法名
     * params: text - 请求参数
     * time: bigint - 执行时长(ms)
     * ip_address: varchar(50) - IP地址
     * location: varchar(255) - 地理位置
     * status: tinyint - 状态(0-失败,1-成功)
     * error_msg: text - 错误信息
     * tenant_id: bigint(20) - 租户ID
     * created_time: datetime - 创建时间

   - dove_login_log: 登录日志表
     * id: bigint(20) - 主键,雪花算法生成
     * user_id: bigint(20) - 用户ID
     * login_type: varchar(50) - 登录类型
     * login_time: datetime - 登录时间
     * ip_address: varchar(50) - IP地址
     * location: varchar(255) - 地理位置
     * browser: varchar(50) - 浏览器
     * os: varchar(50) - 操作系统
     * status: tinyint - 状态(0-失败,1-成功)
     * msg: varchar(255) - 提示消息
     * tenant_id: bigint(20) - 租户ID
     * created_time: datetime - 创建时间

3. 系统管理相关表
   - dove_config: 系统配置表
     * id: bigint(20) - 主键,雪花算法生成
     * config_name: varchar(100) - 配置名称
     * config_key: varchar(100) - 配置键名
     * config_value: text - 配置键值
     * config_type: tinyint - 配置类型
     * status: tinyint - 状态(0-禁用,1-启用)
     * remark: varchar(255) - 备注说明
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_dict: 字典表
     * id: bigint(20) - 主键,雪花算法生成
     * dict_name: varchar(100) - 字典名称
     * dict_type: varchar(100) - 字典类型
     * status: tinyint - 状态(0-禁用,1-启用)
     * remark: varchar(255) - 备注说明
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_param: 参数表
     * id: bigint(20) - 主键,雪花算法生成
     * param_name: varchar(100) - 参数名称
     * param_key: varchar(100) - 参数键名
     * param_value: text - 参数键值
     * param_type: tinyint - 参数类型
     * status: tinyint - 状态(0-禁用,1-启用)
     * remark: varchar(255) - 备注说明
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_notice: 通知消息表
     * id: bigint(20) - 主键,雪花算法生成
     * notice_title: varchar(255) - 通知标题
     * notice_type: tinyint - 通知类型
     * notice_content: text - 通知内容
     * status: tinyint - 状态
     * sender_id: bigint(20) - 发送者ID
     * receiver_ids: varchar(1000) - 接收者ID列表
     * send_time: datetime - 发送时间
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_monitor: 系统监控表
     * id: bigint(20) - 主键,雪花算法生成
     * monitor_type: varchar(50) - 监控类型
     * monitor_target: varchar(255) - 监控目标
     * monitor_value: text - 监控数据
     * status: tinyint - 状态
     * alarm_threshold: varchar(255) - 告警阈值
     * tenant_id: bigint(20) - 租户ID
     * created_time: datetime - 创建时间
     * updated_time: datetime - 更新时间

4. 业务扩展相关表
   - dove_tenant: 租户表
     * id: bigint(20) - 主键,雪花算法生成
     * tenant_code: varchar(50) - 租户编码
     * tenant_name: varchar(100) - 租户名称
     * domain: varchar(255) - 域名
     * license_key: varchar(100) - 授权码
     * expire_time: datetime - 过期时间
     * user_count: int - 用户数量限制
     * status: tinyint - 状态(0-禁用,1-启用)
     * contact_user: varchar(50) - 联系人
     * contact_phone: varchar(20) - 联系电话
     * address: varchar(255) - 联系地址
     * created_time: datetime - 创建时间
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_i18n: 国际化配置表
     * id: bigint(20) - 主键,雪花算法生成
     * lang_code: varchar(10) - 语言编码
     * lang_key: varchar(100) - 翻译键名
     * lang_value: text - 翻译内容
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_custom_field: 自定义字段表
     * id: bigint(20) - 主键,雪花算法生成
     * model_name: varchar(100) - 模型名称
     * field_name: varchar(50) - 字段名称
     * field_label: varchar(100) - 字段标签
     * field_type: varchar(50) - 字段类型
     * field_length: int - 字段长度
     * field_dict: varchar(100) - 字段字典
     * is_required: tinyint - 是否必填
     * default_value: varchar(255) - 默认值
     * validation_rule: varchar(255) - 验证规则
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_workflow: 工作流表
     * id: bigint(20) - 主键,雪花算法生成
     * flow_code: varchar(50) - 流程编码
     * flow_name: varchar(100) - 流程名称
     * flow_version: varchar(20) - 流程版本
     * flow_type: varchar(50) - 流程类型
     * flow_content: text - 流程定义(JSON)
     * status: tinyint - 状态(0-禁用,1-启用)
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号

   - dove_plugin: 插件表
     * id: bigint(20) - 主键,雪花算法生成
     * plugin_code: varchar(50) - 插件编码
     * plugin_name: varchar(100) - 插件名称
     * plugin_version: varchar(20) - 插件版本
     * plugin_type: varchar(50) - 插件类型
     * main_class: varchar(255) - 主类名
     * plugin_config: text - 插件配置(JSON)
     * status: tinyint - 状态(0-禁用,1-启用)
     * tenant_id: bigint(20) - 租户ID
     * created_by: bigint(20) - 创建人ID
     * created_time: datetime - 创建时间
     * updated_by: bigint(20) - 更新人ID
     * updated_time: datetime - 更新时间
     * version: int - 乐观锁版本号