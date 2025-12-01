package com.t13max.agent.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * 方法后插入
 *
 * @Author t13max
 * @Date 21:21 2025/12/1
 */
public class InsertAfterTransformer implements ClassFileTransformer {

    private final String className;
    private final String methodName;
    private final String insertCode;

    public InsertAfterTransformer(String className, String methodName, String insertCode) {
        this.className = className;
        this.methodName = methodName;
        this.insertCode = insertCode;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        if (getPath(this.className).equals(className)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(this.className);
                if (cc == null) return null;
                if (cc.isFrozen()) cc.defrost();
                CtMethod method = cc.getDeclaredMethod(methodName);
                if (method == null) return null;

                method.insertAfter(insertCode);
                System.out.println("MethodInsertAfterTransformer success class=" + className + " method=" + methodName);

                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String getPath(String className) {
        return className.replace(".", "/");
    }
}