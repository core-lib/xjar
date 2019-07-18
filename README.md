# XJar [![](https://jitpack.io/v/core-lib/xjar.svg)](https://jitpack.io/#core-lib/xjar)
GitHub: https://github.com/core-lib/xjar
### Spring Boot JAR 安全加密运行工具，同时支持的原生JAR。
### 基于对JAR包内资源的加密以及拓展ClassLoader来构建的一套程序加密启动，动态解密运行的方案，避免源码泄露或反编译。

## 功能特性
* 无需侵入代码，只需要把编译好的JAR包通过工具加密即可。
* 完全内存解密，杜绝源码以及字节码泄露或反编译。
* 支持所有JDK内置加解密算法。
* 可选择需要加解密的字节码或其他资源文件，避免计算资源浪费。

## 环境依赖
JDK 1.7 +

## 使用步骤

```xml
<project>
    <!-- 设置 jitpack.io 仓库 -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    <!-- 添加 XJar 依赖 -->
    <dependencies>
        <dependency>
            <groupId>com.github.core-lib</groupId>
            <artifactId>xjar</artifactId>
            <version>v2.0.6</version>
        </dependency>
    </dependencies>
</project>
```

```java
// Spring-Boot Jar包加密
String password = "io.xjar";
XKey xKey = XKit.key(password);
XBoot.encrypt("/path/to/read/plaintext.jar", "/path/to/save/encrypted.jar", xKey);
```

```java
// 危险加密模式，即不需要输入密码即可启动的加密方式，这种方式META-INF/MANIFEST.MF中会保留密钥，请谨慎使用！
String password = "io.xjar";
XKey xKey = XKit.key(password);
XBoot.encrypt("/path/to/read/plaintext.jar", "/path/to/save/encrypted.jar", xKey, XConstants.MODE_DANGER);
```

```java
// Spring-Boot Jar包解密
String password = "io.xjar";
XKey xKey = XKit.key(password);
XBoot.decrypt("/path/to/read/encrypted.jar", "/path/to/save/decrypted.jar", xKey);
```

```java
// Jar包加密
String password = "io.xjar";
XKey xKey = XKit.key(password);
XJar.encrypt("/path/to/read/plaintext.jar", "/path/to/save/encrypted.jar", xKey);
```

```java
// 危险加密模式，即不需要输入密码即可启动的加密方式，这种方式META-INF/MANIFEST.MF中会保留密钥，请谨慎使用！
String password = "io.xjar";
XKey xKey = XKit.key(password);
XJar.encrypt("/path/to/read/plaintext.jar", "/path/to/save/encrypted.jar", xKey, XConstants.MODE_DANGER);
```

```java
// Jar包解密
String password = "io.xjar";
XKey xKey = XKit.key(password);
XJar.decrypt("/path/to/read/encrypted.jar", "/path/to/save/decrypted.jar", xKey);
```

## 启动命令
```text
// 命令行运行JAR 然后在提示输入密码的时候输入密码后按回车即可正常启动
java -jar /path/to/encrypted.jar
```
```text
// 也可以通过传参的方式直接启动，不太推荐这种方式，因为泄露的可能性更大！
java -jar /path/to/encrypted.jar --xjar.password=PASSWORD
```
```text
// 对于 nohup 或 javaw 这种后台启动方式，无法使用控制台来输入密码，推荐使用指定密钥文件的方式启动
nohup java -jar /path/to/encrypted.jar --xjar.keyfile=/path/to/xjar.key
```

## 参数说明
| 参数名称 | 参数含义 | 缺省值 | 说明 |
| :------- | :------- | :----- | :--- |
| --xjar.password |  密码 |
| --xjar.algorithm | 密钥算法 | AES | 支持JDK所有内置算法，如AES / DES ... |
| --xjar.keysize |   密钥长度 | 128 | 根据不同的算法选取不同的密钥长度。|
| --xjar.ivsize |    向量长度 | 128 | 根据不同的算法选取不同的向量长度。|
| --xjar.keyfile |   密钥文件 | ./xjar.key | 密钥文件相对或绝对路径。|

## 密钥文件
密钥文件采用properties的书写格式：
```properties
password: PASSWORD
algorithm: ALGORITHM
keysize: KEYSIZE
ivsize: IVSIZE
hold: HOLD
```

其中 algorithm/keysize/ivsize/hold 均有缺省值，当 hold 值不为 true | 1 | yes | y 时，密钥文件在读取后将自动删除。

| 参数名称 | 参数含义 | 缺省值 | 说明 |
| :------- | :------- | :----- | :--- |
| password |  密码 | 无 | 密码字符串 |
| algorithm | 密钥算法 | AES | 支持JDK所有内置算法，如AES / DES ... |
| keysize |   密钥长度 | 128 | 根据不同的算法选取不同的密钥长度。|
| ivsize |    向量长度 | 128 | 根据不同的算法选取不同的向量长度。|
| hold | 是否保留 | false | 读取后是否保留密钥文件。|

## 进阶用法
默认情况下，即没有提供过滤器的时候，将会加密所有资源其中也包括项目其他依赖模块以及第三方依赖的 JAR 包资源，
框架提供使用过滤器的方式来灵活指定需要加密的资源或排除不需要加密的资源。

* #### 硬编码方式
```java
// 假如项目所有类的包名都以 com.company.project 开头，那只加密自身项目的字节码即可采用以下方式。
XBoot.encrypt(
        "/path/to/read/plaintext.jar", 
        "/path/to/save/encrypted.jar", 
        "io.xjar", 
        (entry) -> {
            String name = entry.getName();
            String pkg = "com/company/project/";
            return name.startsWith(pkg);
        }
    );
```
* #### 表达式方式
```java
// 1. 采用Ant表达式过滤器更简洁地来指定需要加密的资源。
XBoot.encrypt(plaintext, encrypted, password, new XJarAntEntryFilter("com/company/project/**"));

XBoot.encrypt(plaintext, encrypted, password, new XJarAntEntryFilter("mapper/*Mapper.xml"));

XBoot.encrypt(plaintext, encrypted, password, new XJarAntEntryFilter("com/company/project/**/*API.class"));

// 2. 采用更精确的正则表达式过滤器。
XBoot.encrypt(plaintext, encrypted, password, new XJarRegexEntryFilter("com/company/project/(.+)"));

XBoot.encrypt(plaintext, encrypted, password, new XJarRegexEntryFilter("mapper/(.+)Mapper.xml"));

XBoot.encrypt(plaintext, encrypted, password, new XJarRegexEntryFilter("com/company/project/(.+)/(.+)API.class"));
```
* #### 混合方式
当过滤器的逻辑复杂或条件较多时可以将过滤器分成多个，并且使用 XKit 工具类提供的多个过滤器混合方法混合成一个，XKit 提供 “与” “或” “非” 三种逻辑运算的混合。
```java
// 1. 与运算，即所有过滤器都满足的情况下才满足，mix() 方法返回的是this，可以继续拼接。
XEntryFilter and = XKit.and()
    .mix(new XJarAntEntryFilter("com/company/project/**"))
    .mix(new XJarAntEntryFilter("*/**.class"));

XEntryFilter all = XKit.all()
    .mix(new XJarAntEntryFilter("com/company/project/**"))
    .mix(new XJarAntEntryFilter("*/**.class"));

// 2. 或运算，即任意一个过滤器满足的情况下就满足，mix() 方法返回的是this，可以继续拼接。
XEntryFilter or = XKit.or()
    .mix(new XJarAntEntryFilter("com/company/project/**"))
    .mix(new XJarAntEntryFilter("mapper/*Mapper.xml"));

XEntryFilter any = XKit.any()
    .mix(new XJarAntEntryFilter("com/company/project/**"))
    .mix(new XJarAntEntryFilter("mapper/*Mapper.xml"));

// 3. 非运算，即除此之外都满足，该例子中即排除项目或其他模块和第三方依赖jar中的静态文件。
XEntryFilter not  = XKit.not(
        XKit.or()
            .mix(new XJarAntEntryFilter("static/**"))
            .mix(new XJarAntEntryFilter("META-INF/resources/**"))
);
```

## 注意事项
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <!-- 需要将executable和embeddedLaunchScript参数删除，目前还不能支持对该模式Jar的加密！后面将会支持该方式的打包。 
    <configuration>
        <executable>true</executable>
        <embeddedLaunchScript>...</embeddedLaunchScript>
    </configuration>
    -->
</plugin>
```

## Spring Boot + JPA(Hibernate) 问题
如果项目中使用了 JPA 且实现为Hibernate时，由于Hibernate自己解析加密后的Jar文件，所以无法正常启动，
可以采用以下解决方案
1. clone [XJar-Agent-Hibernate](https://github.com/core-lib/xjar-agent-hibernate) ，使用 mvn clean package 编译出 xjar-agent-hibernate-${version}.jar 文件
2. 采用 java -javaagent:xjar-agent-hibernate-${version}.jar -jar your-spring-boot-app.jar 命令启动

## 静态文件浏览器无法加载完成问题
由于静态文件被加密后文件体积变大，Spring Boot 会采用文件的大小作为 Content-Length 头返回给浏览器，
但实际上通过 XJar 加载解密后文件大小恢复了原本的大小，所以浏览器认为还没接收完导致一直等待服务端。
由此我们需要在加密时忽略静态文件的加密，实际上静态文件也没加密的必要，因为即便加密了用户在浏览器
查看源代码也是能看到完整的源码的。通常情况下静态文件都会放在 static/ 和 META-INF/resources/ 目录下，
我们只需要在加密时通过过滤器排除这些资源即可，可以采用以下的过滤器：
```java
XKit.not(
        XKit.or()
            .mix(new XJarAntEntryFilter("static/**"))
            .mix(new XJarAntEntryFilter("META-INF/resources/**"))
);
```
或通过插件配置排除
```xml
<plugin>
    <groupId>com.github.core-lib</groupId>
    <artifactId>xjar-maven-plugin</artifactId>
    <version>v2.0.6</version>
    <executions>
        <execution>
            <goals>
                <goal>build</goal>
            </goals>
            <phase>package</phase>
            <!-- 或使用
            <phase>install</phase>
            -->
            <configuration>
                <password>io.xjar</password>
                <excludes>
                    <exclude>static/**</exclude>
                    <exclude>META-INF/resources/**</exclude>
                </excludes>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## 插件集成
[XJar-Maven-Plugin](https://github.com/core-lib/xjar-maven-plugin)
GitHub: https://github.com/core-lib/xjar-maven-plugin

#### 对于Spring Boot 项目或模块，该插件要后于 spring-boot-maven-plugin 插件执行，有两种方式：
* 将插件放置于 spring-boot-maven-plugin 的后面，因为其插件的默认 phase 也是 package
* 将插件的 phase 设置为 install（默认值为：package），打包命令采用 mvn clean install
```xml
<project>
    <!-- 设置 jitpack.io 插件仓库 -->
    <pluginRepositories>
        <pluginRepository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
    </pluginRepositories>
    <!-- 添加 XJar Maven 插件 -->
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.core-lib</groupId>
                <artifactId>xjar-maven-plugin</artifactId>
                <version>v2.0.6</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>build</goal>
                        </goals>
                        <phase>package</phase>
                        <!-- 或使用
                        <phase>install</phase>
                        -->
                        <configuration>
                            <password>io.xjar</password>
                            <includes>
                                <include>com/company/project/**</include>
                                <include>mapper/*Mapper.xml</include>
                            </includes>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>

```
#### 也可以通过Maven命令执行
```text
mvn xjar:build -Dxjar.password=io.xjar
mvn xjar:build -Dxjar.password=io.xjar -Dxjar.targetDir=/directory/to/save/target.xjar
```

#### 但通常情况下是让XJar插件绑定到指定的phase中自动执行，这样就能在项目构建的时候自动构建出加密的包。
```text
mvn clean package -Dxjar.password=io.xjar
mvn clean install -Dxjar.password=io.xjar -Dxjar.targetDir=/directory/to/save/target.xjar
```

## 参数说明
| 参数名称 | 命令参数名称 | 参数说明 | 参数类型 | 缺省值 | 示例值 |
| :------ | :----------- | :------ | :------ | :----- | :----- |
| password | -Dxjar.password | 密码字符串 | String | 必须 | 任意字符串，io.xjar |
| algorithm | -Dxjar.algorithm | 加密算法名称 | String | AES | JDK内置加密算法，如：AES / DES |
| keySize | -Dxjar.keySize | 密钥长度 | int | 128 | 根据加密算法而定，56，128，256 |
| ivSize | -Dxjar.ivSize | 密钥向量长度 | int | 128 | 根据加密算法而定，128 |
| mode | -Dxjar.mode | 加密模式 | int | 0 | 0：普通模式 1：危险模式（免密码启动）|
| sourceDir | -Dxjar.sourceDir | 源jar所在目录 | File | ${project.build.directory} | 文件目录 |
| sourceJar | -Dxjar.sourceJar | 源jar名称 | String | ${project.build.finalName}.jar | 文件名称 |
| targetDir | -Dxjar.targetDir | 目标jar存放目录 | File | ${project.build.directory} | 文件目录 |
| targetJar | -Dxjar.targetJar | 目标jar名称 | String | ${project.build.finalName}.xjar | 文件名称 |
| includes | -Dxjar.includes | 需要加密的资源路径表达式 | String[] | 无 | com/company/project/** , mapper/*Mapper.xml , 支持Ant表达式 |
| excludes | -Dxjar.excludes | 无需加密的资源路径表达式 | String[] | 无 | static/** , META-INF/resources/** , 支持Ant表达式 |

#### 注意：
当 includes 和 excludes 同时使用时即加密在includes的范围内且排除了excludes的资源。

更多文档：[XJar-Maven-Plugin](https://github.com/core-lib/xjar-maven-plugin)

## 版本记录
* v2.0.6
    1. 解决多jar包启动时无法找到准确的MANIFEST.MF导致无法正常启动的问题
* v2.0.5
    1. 升级[LoadKit](https://github.com/core-lib/loadkit)依赖版本
    2. 修复ANT表达式无法正确匹配**/*通配符的问题
* v2.0.4
    1. 解决危险模式不支持ubuntu系统的问题
* v2.0.3
    1. 过滤器泛型协变支持
    2. xjar-maven-plugin 支持 includes 与 excludes 同时起效，当同时设置时即加密在includes范围内但又不在excludes范围内的资源
* v2.0.2
    1. 原生jar增加密钥文件的启动方式，解决类似 nohup 和 javaw 的后台启动方式无法通过控制台输入密码的问题
* v2.0.1
    1. 增加密钥文件的启动方式，解决类似 nohup 和 javaw 的后台启动方式无法通过控制台输入密码的问题
    2. 修复解密后没有删除危险模式中在MANIFEST.MF中保留的密钥信息
* v2.0.0
    1. 支持内嵌JAR包资源的过滤加解密
    2. 不兼容v1.x.x的过滤器表达式，统一采用相对于 classpath 资源URL的过滤表达式
* v1.1.4
    1. 支持 Spring-Boot 以ZIP方式打包，即依赖外部化方式启动。
    2. 修复无加密资源时无法启动问题
* v1.1.3
    1. 实现危险模式加密启动，即不需要输入密码！
    2. 修复无法使用 System.console(); 时用 new Scanner(System.in) 替代。
* v1.1.2
    1. 避免用户由于过滤器使用不当造成无法启动的风险
* v1.1.1
    1. 修复bug
* v1.1.0
    1. 整理目录结构
    2. 增加正则表达式/Ant表达式过滤器和“非”(!)逻辑运算过滤器
    3. 将XEntryFilters工具类整合在XKit类中
    4. 缺省过滤器情况下Spring-Boot JAR包加密的资源只限定在 BOOT-INF/classes/ 下
* v1.0.9
    1. 修复对Spring-Boot 版本依赖的bug
* v1.0.8
    1. 支持以Maven插件方式集成
* v1.0.7
    1. 将sprint-boot-loader依赖设为provide
    2. 将XEntryFilter#filter(E entry); 变更为XEntryFilter#filtrate(E entry);
    3. 将Encryptor/Decryptor的构造函数中接收多个过滤器参数变成接收一个，外部提供XEntryFilters工具类来实现多过滤器混合成一个，避免框架自身的逻辑限制了使用者的过滤逻辑实现。
* v1.0.6
    1. 采用[LoadKit](https://github.com/core-lib/loadkit)作为资源加载工具
* v1.0.5
    1. 支持并行类加载，需要JDK1.7+的支持，可提升多线程环境类加载的效率
    2. Spring-Boot JAR 包加解密增加一个安全过滤器，避免无关资源被加密造成无法运行
    3. XBoot / XJar 工具类中增加多个按文件路径加解密的方法，提升使用便捷性
* v1.0.4 小优化
* v1.0.3 增加Spring-Boot的FatJar加解密时的缺省过滤器，避免由于没有提供过滤器时加密后的JAR包不能正常运行。
* v1.0.2 修复中文及空格路径的问题
* v1.0.1 升级detector框架
* v1.0.0 第一个正式版发布

## 协议声明
[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

## 加入群聊
QQ 950956093 [![](https://pub.idqqimg.com/wpa/images/group.png)](https://shang.qq.com/wpa/qunwpa?idkey=e567db1c32de4b02da480d895566757b3df73e3f8827ed6c9149e2859e4cdc93)
