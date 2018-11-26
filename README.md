# **XJar** [![](https://jitpack.io/v/core-lib/xjar.svg)](https://jitpack.io/#core-lib/xjar)
#### Java JAR 包加密安全运行工具，支持Spring Boot的Fat Jar方式，避免本地部署的应用被反编译。

## **功能特性**
* 无需侵入代码，只需要把编译好的JAR包通过工具加密即可。
* 完全内存解密，杜绝源码以及字节码泄露以及反编译。
* 支持所有JDK内置加解密算法。
* 可选择需要加解密的字节码或其他资源文件，避免计算资源浪费。

## **环境依赖**
JDK 1.7 +

## **使用步骤**

```xml
<project>
    <!-- 设置 jitpack.io 仓库 -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://www.jitpack.io</url>
        </repository>
    </repositories>
    <!-- 添加 HttpDoc 依赖 -->
    <dependencies>
        <dependency>
            <groupId>com.github.core-lib</groupId>
            <artifactId>xjar</artifactId>
            <version>v1.0.0</version>
        </dependency>
    </dependencies>
</project>
```

```java
// Spring-Boot Jar包加密
public static void main(String[] args) {
    String password = "io.xjar";
    File plaintext = new File("/path/to/read/plaintext.jar");
    File encrypted = new File("/path/to/save/encrypted.jar");
    XBoot.encrypt(plaintext, encrypted, password);
}
```

```java
// Spring-Boot Jar包解密
public static void main(String[] args) {
    String password = "io.xjar";
    File encrypted = new File("/path/to/read/encrypted.jar");
    File decrypted = new File("/path/to/save/decrypted.jar");
    XBoot.decrypt(encrypted, decrypted, password);
}
```

```java
// Jar包加密
public static void main(String[] args) {
    String password = "io.xjar";
    File plaintext = new File("/path/to/read/plaintext.jar");
    File encrypted = new File("/path/to/save/encrypted.jar");
    XJar.encrypt(plaintext, encrypted, password);
}
```

```java
// Jar包解密
public static void main(String[] args) {
    String password = "io.xjar";
    File encrypted = new File("/path/to/read/encrypted.jar");
    File decrypted = new File("/path/to/save/decrypted.jar");
    XJar.decrypt(encrypted, decrypted, password);
}
```

```text
// 命令行运行JAR
java -jar /path/to/encrypted.jar
// 在提示输入密码的时候输入密码后按回车即可正常启动，也可以通过传参的方式直接启动
java -jar /path/to/encrypted.jar --xjar.password=PASSWORD
```

## **参数说明**
* --xjar.algorithm  加解密算法名称，缺省为AES，支持JDK所有内置算法，如AES / DES ...
* --xjar.keysize    密钥长度，缺省为128，根据不同的算法选取不同的密钥长度。
* --xjar.ivsize     向量长度，缺省为128，根据不同的算法选取不同的向量长度。
* --xjar.password   密码

## **进阶用法**
```java
// 只加密自身项目及模块的源码不加密第三方依赖，可以通过XJarArchiveEntryFilter来定制需要加密的JAR包内资源
public static void main(String[] args) {
    String password = "io.xjar";
    File plaintext = new File("/path/to/read/plaintext.jar");
    File encrypted = new File("/path/to/save/encrypted.jar");
    XBoot.encrypt(plaintext, encrypted, password, new XJarArchiveEntryFilter() {
        @Override
        public boolean filter(JarArchiveEntry entry) {
            return entry.getName().startsWith("/BOOT-INF/classes/")
             || entry.getName().startsWith("/BOOT-INF/lib/jar-need-encrypted");
        }
    });
}
```

## 变更记录
* v1.0.0 第一个正式版发布

## 协议声明
[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

## 联系作者
QQ 646742615 不会钓鱼的兔子