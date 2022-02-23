项目简介:
    mayikt-openFeign 是一个服务之间的调用框架，采用于类似于feign调用的思想。
    
 1.项目入口
 
    ![image](https://user-images.githubusercontent.com/73068457/155290525-729efe5b-bfe0-485b-9d6a-c7aa76d13825.png)
    使用SpringBoot的Import注解中的HttpRequestRegister类去将所有的被HttpRequestClient注解的接口通过动态代理的方式注入bean对象到spring容器中。
 
     服务调用者：
    ![image](https://user-images.githubusercontent.com/73068457/155289978-62f2d72e-4082-49aa-b203-871571d15508.png)
    服务调用者根据服务提供者的相应的ip地址+端口+路由地址进行远程http调用返回结果。
    
    
    
  2.项目最后陈述：
    
    
    项目只是用于相应的学习服务之间调用的框架，目前项目只是实现了基础的服务调用框架，各位可以根据这个框架进行功能上的丰富。
    
    
    
