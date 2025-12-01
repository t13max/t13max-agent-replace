package com.t13max.agent;

import com.t13max.agent.config.TransformerConfig;
import com.t13max.agent.config.TransformerEntry;
import com.t13max.agent.match.MatcherFactory;
import com.t13max.agent.transformer.InvokeTransformer;
import com.t13max.agent.transformer.TransformerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * @author t13max
 * @since 15:15 2025/7/23
 */
public class AgentMain {

    private final static String PROPERTY_NAME = "transformer.yml.path";

    private final static String PROPERTY_OPEN = "transformer.open";

    private static TransformerConfig TRANSFORMER_CONFIG;

    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println("------------------------------------------------");
        System.out.println("AgentMain.permain begin!!");

        String open = System.getProperty(PROPERTY_OPEN);
        if (open != null && open.equals("false")) {
            System.out.println("AgentMain.permain transformer closed!!");
            return;
        }

        try {
            System.out.println("AgentMain.permain loadConfig!!");
            loadConfig();
        } catch (Exception exception) {
            System.err.println("AgentMain.permain loadConfig error!!");
            exception.printStackTrace();
        }

        System.out.println("AgentMain.permain loadConfig success!!");

        System.out.println("AgentMain.permain addTransformers!!");
        addTransformers(inst);
    }

    private static void addTransformers(Instrumentation inst) {
        for (TransformerEntry transformerEntry : TRANSFORMER_CONFIG.getEntries()) {
            addTransformer(inst, TransformerFactory.createTransformer(transformerEntry.getTransformer(), transformerEntry));
        }
    }

    private static void addTransformer(Instrumentation inst, ClassFileTransformer transformer) {
        inst.addTransformer(transformer);
    }

    private static void loadConfig() throws Exception {

        String confName = System.getProperty(PROPERTY_NAME);
        InputStream inputStream;

        if (confName != null && new File(confName).exists()) {
            inputStream = new FileInputStream(confName);
        } else {
            inputStream = AgentMain.class.getClassLoader().getResourceAsStream("transformer.yml");
        }

        Yaml yaml = new Yaml();

        TRANSFORMER_CONFIG = yaml.loadAs(inputStream, TransformerConfig.class);

    }
}
