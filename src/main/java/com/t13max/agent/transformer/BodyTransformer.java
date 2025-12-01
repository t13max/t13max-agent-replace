package com.t13max.agent.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * 替换方法体
 *
 * @Author t13max
 * @Date 21:21 2025/12/1
 */
public class BodyTransformer implements ClassFileTransformer {

    private final String className;
    private final String methodName;
    private final String newBody;

    public BodyTransformer(String className, String methodName, String newBody) {
        this.className = className;
        this.methodName = methodName;
        this.newBody = newBody;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        if (getPath(this.className).equals(className)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                cp.insertClassPath(new LoaderClassPath(loader)); // 加入当前ClassLoader路径，保证能解析 next 和 call 类型
                CtClass cc = cp.get(this.className);
                if (cc == null) return null;
                if (cc.isFrozen()) cc.defrost();
                CtMethod method = cc.getDeclaredMethod(methodName);
                if (method == null) return null;

                method.setBody(newBody);
                System.out.println("MethodBodyReplaceTransformer success class=" + className + " method=" + methodName);

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