

# 设计系统提示词
``` markdown
# openhands/agenthub/codeact_agent/prompts/system_prompt.j2
你是一个专业的软件工程师，具有以下能力：
- 理解复杂的软件架构
- 遵循最佳实践和设计模式
- 生成高质量、可维护的代码
- 提供详细的技术建议和解释
```
# Overall Flow
``` markdown
# openhands/agenthub/codeact_agent/codeact_agent.py
class CodeActAgent(Agent):
    def step(self, state: 'State') -> 'Action':
        """
        1. 分析任务需求
        2. 规划执行步骤
        3. 执行代码生成
        4. 验证生成结果
        """
        # 获取当前状态和历史
        context = self._get_context(state)
        
        # 生成下一步行动
        response = self.llm.chat_completion(
            messages=self._prepare_messages(context),
            functions=FUNCTIONS  # 定义了可执行的操作
        )
        
        # 执行操作并验证
        return self._process_response(response)
```



# 最佳实践
``` mermaid
graph TD
    A[接收Spring Cloud需求] --> B[分析项目结构]
    B --> C[生成基础框架]
    C --> D[验证项目结构]
    D --> E{是否符合要求?}
    E -->|否| F[优化调整]
    F --> C
    E -->|是| G[生成具体服务]
    G --> H[验证服务功能]
    H --> I{功能正确?}
    I -->|否| J[修复问题]
    J --> G
    I -->|是| K[完成项目]
```


#质量保证机制
``` markdown
# openhands/agenthub/codeact_agent/codeact_agent.py
class CodeActAgent(Agent):
    def step(self, state: 'State') -> 'Action':
        """
        1. 分析任务需求
def validate_project_structure(self):
    """验证项目结构和配置的正确性"""
    checks = [
        self._check_maven_structure(),
        self._check_dependencies(),
        self._check_service_configuration(),
        self._check_service_communication()
    ]
    return all(checks)

def _check_maven_structure(self):
    """检查Maven项目结构"""
    required_files = [
        'pom.xml',
        'src/main/java',
        'src/main/resources/application.yml'
    ]
    return all(os.path.exists(f) for f in required_files)
```

最佳实践
# 最佳实践执行流程
``` markdown
async def generate_spring_cloud_project(self, requirements):
    """
    按照最佳实践生成Spring Cloud项目
    1. 创建父项目
    2. 创建各个微服务模块
    3. 配置依赖管理
    4. 实现服务功能
    5. 配置服务通信
    6. 验证项目完整性
    """
    steps = [
        self._create_parent_project(),
        self._create_service_modules(),
        self._configure_dependencies(),
        self._implement_services(),
        self._setup_service_communication(),
        self._validate_project()
    ]
    
    for step in steps:
        success = await step
        if not success:
            return self._handle_failure()
```



# 错误处理和修复
``` markdown
def handle_generation_error(self, error, context):
    """
    处理生成过程中的错误
    1. 分析错误原因
    2. 提供修复建议
    3. 执行修复操作
    """
    # 分析错误
    error_type = self._analyze_error(error)
    
    # 获取修复建议
    fix_suggestion = self.llm.get_fix_suggestion(error, context)
    
    # 执行修复
    return self._apply_fix(fix_suggestion)

```


# 专业性保证
知识引导：通过系统提示词引导LLM使用专业知识
结构验证：确保生成的项目符合标准结构
代码质量：通过多轮验证确保代码质量
最佳实践：强制执行行业最佳实践
错误处理：提供专业的错误处理机制
通过这些机制，即使没有专门的Spring Cloud模板，OpenHands也能保证生成的项目具有专业性：
利用LLM的知识库确保架构设计合理
通过迭代验证确保实现正确
强制执行标准的项目结构
提供专业的错误处理和修复
遵循行业最佳实践
这种方式实际上比固定模板更灵活，能够适应不同的需求变化，同时通过严格的验证机制确保项目质量。





## AI Prompt Engineer 设计 Prompt
``` markdown
# Role
你是一个专业的Prompt Engineer， 你擅长设计Prompt， 你擅长设计Prompt Pattern， 你擅长设计Prompt Template， 你擅长设计Prompt Structure， 你擅长设计Prompt Flow， 你擅长设计Prompt Quality Assurance Mechanism， 你擅长设计Prompt Error Handling and Repair Mechanism， 你擅长设计Prompt Professionalism Guarantee Mechanism。

# 任务
你的任务是设计一个Prompt， 这个Prompt 的具体信息如下：
- 这个Prompt 需要生成一个 分布式会话管理方案 . 这个 Prompt 需要 按照最佳实践执行流程 去设计 Prompt 的任务步骤； 这个 - - Prompt 的背景是：“微服务架构的企业应用系统：在微服务架构中，一个业务流程可能涉及多个微服务之间的协作，每个微服务可能部署在不同的服务器上。用户的会话数据需要在多个微服务之间共享和传递。
问题：微服务之间的调用关系复杂，如何确保会话数据在不同微服务之间的一致性和安全性，是一个挑战。如果没有有效的会话管理，可能会出现数据不一致、权限验证失败等问题。
解决方案：可以结合使用多种分布式会话管理方案。例如，使用 JWT Token 在微服务之间传递用户的身份和权限信息，确保每个微服务都能正确验证用户的请求。同时，对于一些需要持久化存储的会话数据，如业务流程中的中间状态，可以使用分布式缓存或数据库进行存储和管理。通过这种方式，实现微服务架构下分布式会话的有效管理。”

# 要求
- 这个Prompt 需要 按照最佳实践执行流程 去设计 Prompt 的任务步骤；
- 这个Prompt 需要 按照最佳实践执行流程 去设计 Prompt 的质量保证机制；
- 这个Prompt 需要 按照最佳实践执行流程 去设计 Prompt 的错误处理和修复机制；
- 这个Prompt 需要 按照最佳实践执行流程 去设计 Prompt 的专业性保证；

```







