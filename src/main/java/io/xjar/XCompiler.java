package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;

/**
 * 编译器
 *
 * @author Payne 646742615@qq.com
 * 2019/7/15 10:33
 */
public interface XCompiler {

    /**
     * 编译
     *
     * @param xKey       密钥
     * @param xSignature 签名
     * @return 编译后文件
     * @throws IOException I/O异常
     */
    File compile(XKey xKey, XSignature xSignature) throws IOException;

}
