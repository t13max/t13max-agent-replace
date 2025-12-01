package com.t13max.agent.transformer;

import com.t13max.agent.match.IMethodCallMatcher;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * 方法调用替换Transformer
 *
 * @author t13max
 * @since 18:17 2025/7/23
 */
public class InvokeTransformer implements ClassFileTransformer {

    private final String className;

    private final String methodName;

    private final IMethodCallMatcher methodCallMatcher;

    private final String replace;

    public InvokeTransformer(String className, String methodName, IMethodCallMatcher methodCallMatcher, String replace) {
        this.className = className;
        this.methodName = methodName;
        this.methodCallMatcher = methodCallMatcher;
        this.replace = replace;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        if (getPath(this.className).equals(className)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(this.className);
                if (cc == null) {
                    System.err.println("CommonTransformer.transform error! class is null, className=" + className + " methodName=" + methodName);
                    return null;
                }
                if (cc.isFrozen()) cc.defrost();
                CtMethod method = cc.getDeclaredMethod(methodName);
                if (method == null) {
                    System.err.println("CommonTransformer.transform error! method is null, className=" + className + " methodName=" + methodName);
                    return null;
                }
                method.instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (methodCallMatcher.match(m)) {
                            m.replace(replace);
                            System.out.println("CommonTransformer.transform replace success, className=" + className + " methodName=" + methodName);
                        }
                    }
                });

                return cc.toBytecode();
            } catch (Exception e) {
                System.err.println("CommonTransformer.transform error! className=" + className + " methodName=" + methodName);
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getPath(String className) {
        return className.replace(".", "/");
    }
}
