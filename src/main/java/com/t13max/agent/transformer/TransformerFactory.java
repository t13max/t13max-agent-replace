package com.t13max.agent.transformer;

import com.t13max.agent.config.TransformerEntry;
import com.t13max.agent.match.MatcherFactory;
import lombok.experimental.UtilityClass;

import java.lang.instrument.ClassFileTransformer;

/**
 * 工厂
 *
 * @author t13max
 * @since 21:22 2025/12/1
 */
@UtilityClass
public class TransformerFactory {

    public ClassFileTransformer createTransformer(String name, TransformerEntry transformerEntry) {
        String className = transformerEntry.getClassName();
        String methodName = transformerEntry.getMethodName();
        String replace = transformerEntry.getReplace();
        switch (name) {
            case "BodyTransformer" -> {

                return new BodyTransformer(className, methodName, replace);
            }
            case "InsertAfterTransformer" -> {
                return new InsertAfterTransformer(className, methodName, replace);
            }
            case "InsertBeforeTransformer" -> {
                return new InsertBeforeTransformer(className, methodName, replace);
            }
            case "InvokeTransformer" -> {
                return new InvokeTransformer(className, methodName, MatcherFactory.createMatcher(transformerEntry.getMatcher().getName(), transformerEntry.getMatcher().getArgs()), replace);
            }
            default -> {
                throw new IllegalArgumentException("TransformerFactory.createTransformer, name=" + name);
            }
        }
    }
}
