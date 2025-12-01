package com.t13max.agent.config;


/**
 * @author t13max
 * @since 18:23 2025/7/23
 */
public class TransformerEntry {

    private String className;

    private String methodName;

    private TransformerMatcher matcher;

    private String replace;

    private String transformer;

    public TransformerEntry() {
        // 给 YAML 用的无参构造
    }

    public void setClassName(String className) {
        if (this.className != null) throw new IllegalStateException("className 已设置");
        this.className = className;
    }

    public void setMethodName(String methodName) {
        if (this.methodName != null) throw new IllegalStateException("methodName 已设置");
        this.methodName = methodName;
    }

    public void setMatcher(TransformerMatcher matcher) {
        if (this.matcher != null) throw new IllegalStateException("matcher 已设置");
        this.matcher = matcher;
    }

    public void setReplace(String replace) {
        if (this.replace != null) throw new IllegalStateException("replace 已设置");
        this.replace = replace;
    }

    public void setTransformer(String transformer) {
        if (this.transformer != null) throw new IllegalStateException("transformer 已设置");
        this.transformer = transformer;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public TransformerMatcher getMatcher() {
        return matcher;
    }

    public String getReplace() {
        return replace;
    }

    public String getTransformer() {
        return transformer;
    }
}