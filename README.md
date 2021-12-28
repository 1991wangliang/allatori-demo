# 关于java软件license授权与代码混淆的研究

lorne 2021-12-28

# 代码混淆

代码混淆采用的是`allatori` 方案。 

[Allatori Java Obfuscator - Professional Java Obfuscation](http://www.allatori.com/)

demo下载地址: [http://www.allatori.com/downloads/Allatori-8.0-Demo.zip](http://www.allatori.com/downloads/Allatori-8.0-Demo.zip)

使用过程如下：

- 创建混淆策略配置文件

`allatori` 使用过程就是首先创建`allatori.xml` 配置文件，声明混淆的策略方式。

- 执行混淆处理

然后执行 `java -jar allatori.jar allatori.xml` 完成代码混淆的处理过程。

## maven 下使用步骤

1. 添加lib到工程中 （allatori.jar）

```
.
├── HELP.md
├── allatori-demo.iml
├── demo.iml
├── lib
│   ├── allatori-annotations.jar
│   └── allatori.jar
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── codingapi
    │   │           └── allatori
    │   │               └── demo
    │   │                   ├── DemoApplication.java
    │   │                   ├── license
    │   │                   │   └── MyLicense.java
    │   │                   └── model
    │   │                       └── Demo.java
    │   └── resources
    │       ├── allatori.xml
    │       ├── application.properties
    │       ├── license
    │       │   ├── license.base64
    │       │   └── license.bin
    │       ├── static
    │       └── templates
    └── test
        ├── java
        │   └── com
        │       └── codingapi
        │           └── allatori
        │               └── demo
        │                   ├── DemoApplicationTests.java
        │                   └── model
        │                       └── DemoTest.java
        └── resources
            └── xxx.csv

```

2. 配置aliiatori.xml 文件

```java
<config>
    <!-- 输入输出混淆的jar文件 -->
    <input>
        <jar in="allatori-demo-0.0.1-SNAPSHOT.jar" out="allatori-demo-0.0.1-SNAPSHOT-obfuscated.jar"/>
    </input>

    <!-- 忽略混淆的classes文件 -->
    <ignore-classes>
        <class template="class *Application*" />
        <class template="class org.springframework*"/>
    </ignore-classes>

    <!-- 保持不混淆的策略文件 -->
    <keep-names>
        
        <!-- protected/public保护的类名及方法名称都保留不混淆 -->
        <class access="protected+">
            <field access="protected+" />
            <method access="protected+" />
        </class>
    </keep-names>

    <!-- 混淆过程的日志文件 -->
    <property name="log-file" value="log.xml"/>
</config>
```

3. maven 配置

```java
            <!-- allatori 文件copy -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-allatori</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>./target/</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>src/main/resources</directory>
                                    <includes>
                                        <include>allatori.xml</include>
                                    </includes>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!-- exec 执行allatori jar 混淆处理 -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>1.2.1</version>
                <executions>
                    <execution>
                        <id>run-allatori</id>
                        <phase>package</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <executable>java</executable>
                    <arguments>
                        <argument>-jar</argument>
                        <argument>./lib/allatori.jar</argument>
                        <argument>${basedir}/target/allatori.xml</argument>
                    </arguments>
                </configuration>
            </plugin>
```

# License授权

License授权采用 license3j 的框架

[https://github.com/verhas/License3j](https://github.com/verhas/License3j)

license3j 有两部分组成：

一是license3j的验证使用maven库。 git地址：‣

```java
<dependency>
    <groupId>com.javax0.license3j</groupId>
    <artifactId>license3j</artifactId>
    <version>3.1.5</version>
</dependency>
```

二是导出授权文件的`license3jrepl.jar` git地址：[https://github.com/verhas/license3jrepl](https://github.com/verhas/license3jrepl)  (由于 license3jrepl 代码采用了jdk11的语法，因此在没有jdk11的环境下无法直接运行jar，可采用docker环境执行) 

```java
# java
java -jar license3jrepl.jar

# docker
docker container run -it -v `pwd`:/opt  verhas/license3jrepl
```

license3j 使用步骤如下

- 创建并导出授权文件
- 配置key验证授权

操作过程如下：

1. 创建并导出授权文件
    1. 执行 `docker container run -it -v `pwd`:/opt verhas/license3jrepl`
        
        ```java
        (base) lorne@lorne license % ll
        total 0
        drwxr-xr-x   2 lorne  staff   64 12 28 11:52 ./
        drwxr-xr-x  19 lorne  staff  608 12 28 11:52 ../
        (base) lorne@lorne license % docker container run -it -v `pwd`:/opt  verhas/license3jrepl
        License3j REPL application 3.1.5
        CDW is /opt/.
        type 'help' for help
        Startup file .license3j was not found.
        L3j> $
        ```
        
    2. 创建公私密钥 `generateKeys algorithm=RSA size=1024 format=BINARY public=public.key private=private.key`
        
        ```java
        L3j> $ generateKeys algorithm=RSA size=1024 format=BINARY public=public.key private=private.key
        No license in memory
        Public key in memory.
        Private key in memory.
        [INFO] Private key saved to /opt/private.key
        [INFO] Public key saved to /opt/public.key
        L3j> $
        ```
        
        文件将会在所在的文件夹下创建出来。应该docker默认映射了当前路径到opt下  `-v `pwd`:/opt`
        
        ```java
        (base) lorne@lorne license % ll
        total 16
        drwxr-xr-x   4 lorne  staff  128 12 28 11:54 ./
        drwxr-xr-x  19 lorne  staff  608 12 28 11:52 ../
        -rw-r--r--   1 lorne  staff  638 12 28 11:54 private.key
        -rw-r--r--   1 lorne  staff  166 12 28 11:54 public.key
        (base) lorne@lorne license %
        ```
        
    3.  查看程序（license3j）验证所需要的PublicKey,执行 `dumpPublicKey`
        
        ```java
        L3j> $ dumpPublicKey
        No license in memory
        Public key in memory.
        Private key in memory.
        [INFO] 
        --KEY DIGEST START
        byte [] digest = new byte[] {
        (byte)0x5E, 
        (byte)0x3E, (byte)0xEA, (byte)0x5A, (byte)0x5E, (byte)0xEC, (byte)0x46, (byte)0x42, (byte)0x5D, 
        (byte)0xBD, (byte)0xB2, (byte)0x38, (byte)0x85, (byte)0x35, (byte)0x33, (byte)0x26, (byte)0xDE, 
        (byte)0x06, (byte)0x2C, (byte)0xA2, (byte)0x83, (byte)0x53, (byte)0xDF, (byte)0x9F, (byte)0x54, 
        (byte)0xD5, (byte)0x12, (byte)0x63, (byte)0x92, (byte)0xB1, (byte)0x8D, (byte)0xA0, (byte)0x3B, 
        (byte)0x11, (byte)0x4A, (byte)0x59, (byte)0x78, (byte)0x9F, (byte)0x56, (byte)0x4F, (byte)0xA7, 
        (byte)0x09, (byte)0x3C, (byte)0xA8, (byte)0xB1, (byte)0xDC, (byte)0x24, (byte)0x80, (byte)0x7B, 
        (byte)0x33, (byte)0xA3, (byte)0x2A, (byte)0x6F, (byte)0x12, (byte)0x19, (byte)0xD6, (byte)0xE4, 
        (byte)0x97, (byte)0xFB, (byte)0x39, (byte)0x08, (byte)0xAA, (byte)0xCC, (byte)0xA4, 
        };
        ---KEY DIGEST END
        --KEY START
        byte [] key = new byte[] {
        (byte)0x52, 
        (byte)0x53, (byte)0x41, (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x9F, (byte)0x30, (byte)0x0D, 
        (byte)0x06, (byte)0x09, (byte)0x2A, (byte)0x86, (byte)0x48, (byte)0x86, (byte)0xF7, (byte)0x0D, 
        (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x05, (byte)0x00, (byte)0x03, (byte)0x81, (byte)0x8D, 
        (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x89, (byte)0x02, (byte)0x81, (byte)0x81, (byte)0x00, 
        (byte)0xCE, (byte)0xE8, (byte)0x0D, (byte)0x69, (byte)0xF2, (byte)0x86, (byte)0x3D, (byte)0xF9, 
        (byte)0x17, (byte)0x1E, (byte)0x36, (byte)0x8B, (byte)0x32, (byte)0xFD, (byte)0x8E, (byte)0x4D, 
        (byte)0xFB, (byte)0x3C, (byte)0xD6, (byte)0x8F, (byte)0x32, (byte)0x97, (byte)0xBB, (byte)0x3D, 
        (byte)0xEC, (byte)0xCC, (byte)0x46, (byte)0x7F, (byte)0x6F, (byte)0x6A, (byte)0x59, (byte)0x40, 
        (byte)0x6D, (byte)0xB1, (byte)0x1F, (byte)0x8C, (byte)0xE5, (byte)0x54, (byte)0xF0, (byte)0xB1, 
        (byte)0x27, (byte)0x09, (byte)0xBB, (byte)0x14, (byte)0x64, (byte)0xB1, (byte)0x4C, (byte)0x8E, 
        (byte)0x9B, (byte)0xF8, (byte)0xB5, (byte)0x07, (byte)0xF1, (byte)0x7E, (byte)0x1C, (byte)0x52, 
        (byte)0x65, (byte)0x6C, (byte)0x75, (byte)0x18, (byte)0x3C, (byte)0xBD, (byte)0x40, (byte)0x10, 
        (byte)0x4E, (byte)0x80, (byte)0xD0, (byte)0xC3, (byte)0xF1, (byte)0x8A, (byte)0x24, (byte)0x85, 
        (byte)0x1E, (byte)0x24, (byte)0x67, (byte)0x23, (byte)0x9D, (byte)0x47, (byte)0xF6, (byte)0xC6, 
        (byte)0x88, (byte)0x9C, (byte)0x14, (byte)0x0A, (byte)0x0E, (byte)0x0F, (byte)0x63, (byte)0x62, 
        (byte)0x8E, (byte)0x1F, (byte)0x3A, (byte)0x81, (byte)0xD8, (byte)0x89, (byte)0x15, (byte)0x97, 
        (byte)0x0C, (byte)0x40, (byte)0x09, (byte)0x3C, (byte)0xFC, (byte)0xD0, (byte)0x6B, (byte)0xAB, 
        (byte)0x24, (byte)0x33, (byte)0x82, (byte)0x76, (byte)0x05, (byte)0x04, (byte)0x63, (byte)0x9D, 
        (byte)0x86, (byte)0xDF, (byte)0x2B, (byte)0x36, (byte)0xB1, (byte)0x23, (byte)0x82, (byte)0xD1, 
        (byte)0x6B, (byte)0x1B, (byte)0xE8, (byte)0x16, (byte)0xFA, (byte)0x4C, (byte)0xB3, (byte)0x3F, 
        (byte)0x02, (byte)0x03, (byte)0x01, (byte)0x00, (byte)0x01, 
        };
        ---KEY END
        
        L3j> $
        ```
        
        保持上述的代码信息，后面验证程序中将需要用到
        
    4. 开始设置导出license授权文件 执行 `newLicense`
        
        ```java
        L3j> $ newLicense
        License w/o owner is in memory.
        Public key in memory.
        Private key in memory.
        L3j> $
        ```
        
        设置自定义的内容 `feature` colunm:type = value 
        
        https://github.com/verhas/License3j#what-is-a-license-in-license3j 
        
        ```java
        L3j> $ feature id:INT=100
        License w/o owner is in memory.
        Public key in memory.
        Private key in memory.
        L3j> $ feature name:STRING=马斯克
        License w/o owner is in memory.
        Public key in memory.
        Private key in memory.
        L3j> $
        ```
        
        执行`sign`签名指令
        
        ```java
        L3j> $ sign
        License w/o owner is in memory.
        Public key in memory.
        Private key in memory.
        L3j> $
        ```
        
        创建并保持授权文件 `saveLicense`
        
        ```java
        L3j> $ saveLicense format=BASE64 license.bin
        License w/o owner is in memory.
        Public key in memory.
        Private key in memory.
        [INFO] License was saved into the file /opt/license.bin
        L3j> $
        ```
        
        在当前路径中可见 license.bin
        
        ```java
        (base) lorne@lorne license % ll
        total 24
        drwxr-xr-x   5 lorne  staff  160 12 28 12:02 ./
        drwxr-xr-x  19 lorne  staff  608 12 28 11:52 ../
        -rw-r--r--   1 lorne  staff   68 12 28 12:02 license.bin
        -rw-r--r--   1 lorne  staff  638 12 28 11:54 private.key
        -rw-r--r--   1 lorne  staff  166 12 28 11:54 public.key
        (base) lorne@lorne license %
        ```
        
        检测验证授权文件 `verify`  License is properly signed.
        
        ```java
        L3j> $ verify
        License w/o owner is in memory.
        Public key in memory.
        Private key in memory.
        [INFO] License is properly signed.
        L3j> $
        ```
        
    
2.  工程中使用授权文件验证
    1. 导入maven文件
        
        ```java
                <dependency>
                    <groupId>com.javax0.license3j</groupId>
                    <artifactId>license3j</artifactId>
                    <version>3.1.5</version>
                </dependency>
        ```
        
    2. 编写授权控制代码
        
        ```java
        package com.codingapi.allatori.demo.license;
        
        import javax0.license3j.License;
        import javax0.license3j.io.IOFormat;
        import javax0.license3j.io.LicenseReader;
        
        import java.io.IOException;
        
        /**
         * @author lorne
         * @since 1.0.0
         */
        public class MyLicense {
        
            public static void main(String[] args) {
                License license = new License();
                try {
                    license= new LicenseReader(MyLicense.class.getResource("/").getPath() + "/license/license.bin")
                            .read(IOFormat.BASE64);
        
                    byte [] key = new byte[] {
                            (byte)0x52,
                            (byte)0x53, (byte)0x41, (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x9F, (byte)0x30, (byte)0x0D,
                            (byte)0x06, (byte)0x09, (byte)0x2A, (byte)0x86, (byte)0x48, (byte)0x86, (byte)0xF7, (byte)0x0D,
                            (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x05, (byte)0x00, (byte)0x03, (byte)0x81, (byte)0x8D,
                            (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x89, (byte)0x02, (byte)0x81, (byte)0x81, (byte)0x00,
                            (byte)0xCE, (byte)0xE8, (byte)0x0D, (byte)0x69, (byte)0xF2, (byte)0x86, (byte)0x3D, (byte)0xF9,
                            (byte)0x17, (byte)0x1E, (byte)0x36, (byte)0x8B, (byte)0x32, (byte)0xFD, (byte)0x8E, (byte)0x4D,
                            (byte)0xFB, (byte)0x3C, (byte)0xD6, (byte)0x8F, (byte)0x32, (byte)0x97, (byte)0xBB, (byte)0x3D,
                            (byte)0xEC, (byte)0xCC, (byte)0x46, (byte)0x7F, (byte)0x6F, (byte)0x6A, (byte)0x59, (byte)0x40,
                            (byte)0x6D, (byte)0xB1, (byte)0x1F, (byte)0x8C, (byte)0xE5, (byte)0x54, (byte)0xF0, (byte)0xB1,
                            (byte)0x27, (byte)0x09, (byte)0xBB, (byte)0x14, (byte)0x64, (byte)0xB1, (byte)0x4C, (byte)0x8E,
                            (byte)0x9B, (byte)0xF8, (byte)0xB5, (byte)0x07, (byte)0xF1, (byte)0x7E, (byte)0x1C, (byte)0x52,
                            (byte)0x65, (byte)0x6C, (byte)0x75, (byte)0x18, (byte)0x3C, (byte)0xBD, (byte)0x40, (byte)0x10,
                            (byte)0x4E, (byte)0x80, (byte)0xD0, (byte)0xC3, (byte)0xF1, (byte)0x8A, (byte)0x24, (byte)0x85,
                            (byte)0x1E, (byte)0x24, (byte)0x67, (byte)0x23, (byte)0x9D, (byte)0x47, (byte)0xF6, (byte)0xC6,
                            (byte)0x88, (byte)0x9C, (byte)0x14, (byte)0x0A, (byte)0x0E, (byte)0x0F, (byte)0x63, (byte)0x62,
                            (byte)0x8E, (byte)0x1F, (byte)0x3A, (byte)0x81, (byte)0xD8, (byte)0x89, (byte)0x15, (byte)0x97,
                            (byte)0x0C, (byte)0x40, (byte)0x09, (byte)0x3C, (byte)0xFC, (byte)0xD0, (byte)0x6B, (byte)0xAB,
                            (byte)0x24, (byte)0x33, (byte)0x82, (byte)0x76, (byte)0x05, (byte)0x04, (byte)0x63, (byte)0x9D,
                            (byte)0x86, (byte)0xDF, (byte)0x2B, (byte)0x36, (byte)0xB1, (byte)0x23, (byte)0x82, (byte)0xD1,
                            (byte)0x6B, (byte)0x1B, (byte)0xE8, (byte)0x16, (byte)0xFA, (byte)0x4C, (byte)0xB3, (byte)0x3F,
                            (byte)0x02, (byte)0x03, (byte)0x01, (byte)0x00, (byte)0x01,
                    };
                    if( !license.isOK(key) ){
                        // if not signed, stop the application
                        System.out.println("license not signed");
                    } else {
                        System.out.println("license signed");
        
                        int id = license.get("id").getInt();
                        System.out.println("Extracted feature id:" + id );
                        String name = license.get("name").getString();
                        System.out.println("Extracted feature name:" + name );
                    }
        
                } catch (IOException e) {
                    System.out.println("Error reading license file " + e);
                }
            }
        }
        ```
        
        输出结果:
        
        ```java
        license signed
        Extracted feature id:100
        Extracted feature name:马斯克
        ```
