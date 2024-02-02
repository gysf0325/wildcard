# 文件路径通配符实现
>目前市面的全链路监控系统基本都是参考Google的Dapper来做的，本专题主要通过六个章节的代码实战，来介绍如何使用javaagent以及字节码应用，来实现一个简单的java代码链路流程监控。
>根据spring的AntPathMatcher模仿而来的文件路径通配符匹配
>      *代表一个或多个字符
>      **代表一层或多层路径
# 源码文件
>PathFileWildcard.java		为通配符具体实现类，参考TestPath.java中用法即可
>TestPath.java				为测试代码，直接运行main函数即可


