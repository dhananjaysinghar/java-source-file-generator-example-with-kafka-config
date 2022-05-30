package com.ex.utils;

import lombok.SneakyThrows;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigFileGenerate {

     private final static String FILE_LOCATION = ".\\src\\main\\java\\com\\ex\\kafka\\generated";
   // private final static String FILE_LOCATION = ".\\target\\classes\\com\\ex\\kafka\\generated";
    private final static String KAFKA_PRODUCER_CONFIG_CLASS = "KafkaProducerConfig";
    private final static String KAFKA_CONSUMER_CONFIG_CLASS = "KafkaConsumerConfig";

    private final static String KAFKA_PRODUCER_CLASS = "KafkaDataSender";

    private final static String KAFKA_LISTENER_CLASS = "KafkaDataListener";

    private final static String KAFKA_LISTENER_PROCESSOR_CLASS = "KafkaDataProcessor";

    @SneakyThrows
    public static void main(String[] args) {
        generateProducerConfigClass(args[0]);
        generateConsumerConfigClass(args[0]);
        generateKafkaProducer(args[0]);
        generateKafkaListener(args[0]);
        generateKafkaListenerProcessor(args[0]);
    }

    private static void generateProducerConfigClass(String modelClassName) throws IOException {
        Path temp = Paths.get(FILE_LOCATION + "\\config");
        Files.createDirectories(temp);

        Path javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), KAFKA_PRODUCER_CONFIG_CLASS + ".java");
        String code = "package com.ex.kafka.generated.config;\n" +
                "import org.apache.kafka.clients.producer.ProducerConfig;\n" +
                "import org.apache.kafka.common.serialization.StringSerializer;\n" +
                "import org.springframework.beans.factory.annotation.Value;\n" +
                "import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.kafka.annotation.EnableKafka;\n" +
                "import org.springframework.kafka.core.DefaultKafkaProducerFactory;\n" +
                "import org.springframework.kafka.core.KafkaTemplate;\n" +
                "import org.springframework.kafka.core.ProducerFactory;\n" +
                "import org.springframework.kafka.support.serializer.JsonSerializer;\n" +
                "import org.springframework.retry.annotation.EnableRetry;\n" +
                "\n" +
                "import javax.annotation.PostConstruct;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "@Configuration\n" +
                "@EnableKafka\n" +
                "@EnableRetry\n" +
                "@ConditionalOnProperty(prefix = \"app\", name = \"kafka.enabled\", havingValue = \"true\")\n" +
                "public class KafkaProducerConfig {\n" +
                "\n" +
                "    @Value(\"${app.kafka.host}\")\n" +
                "    private String kafkaHost;\n" +
                "\n" +
                "    private static final Map<String, Object> DEFAULT_PRODUCER_CONFIG_MAP = new HashMap<>();\n" +
                "\n" +
                "    @PostConstruct\n" +
                "    public void configureKafkaProperties() {\n" +
                "        DEFAULT_PRODUCER_CONFIG_MAP.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);\n" +
                "        DEFAULT_PRODUCER_CONFIG_MAP.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);\n" +
                "        DEFAULT_PRODUCER_CONFIG_MAP.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);\n" +
                "    }\n" +
                "\n" +
                "    @Bean\n" +
                "    public KafkaTemplate<String, " + modelClassName + "> kafkaTemplate() {\n" +
                "        return new KafkaTemplate<>(userProducerFactory());\n" +
                "    }\n" +
                "\n" +
                "    @Bean\n" +
                "    public ProducerFactory<String, " + modelClassName + "> userProducerFactory() {\n" +
                "        JsonSerializer<" + modelClassName + "> userJsonSerializer = new JsonSerializer<>();\n" +
                "        userJsonSerializer.setAddTypeInfo(false);\n" +
                "        return new DefaultKafkaProducerFactory<>(DEFAULT_PRODUCER_CONFIG_MAP, new StringSerializer(), userJsonSerializer);\n" +
                "    }" +
                "}";
        Files.write(javaSourceFile, code.getBytes());
       // GenerateClassFileAndDeleteSourceFile(javaSourceFile);
    }


    private static void generateConsumerConfigClass(String modelClassName) throws IOException {
        Path temp = Paths.get(FILE_LOCATION + "\\config");
        Files.createDirectories(temp);

        Path javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), KAFKA_CONSUMER_CONFIG_CLASS + ".java");
        String code = "package com.ex.kafka.generated.config;\n" +
                "import org.apache.kafka.clients.consumer.ConsumerConfig;\n" +
                "import org.apache.kafka.common.serialization.StringDeserializer;\n" +
                "import org.springframework.beans.factory.annotation.Value;\n" +
                "import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.kafka.annotation.EnableKafka;\n" +
                "import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;\n" +
                "import org.springframework.kafka.core.ConsumerFactory;\n" +
                "import org.springframework.kafka.core.DefaultKafkaConsumerFactory;\n" +
                "import org.springframework.kafka.support.serializer.JsonDeserializer;\n" +
                "import org.springframework.kafka.support.serializer.JsonSerializer;\n" +
                "import org.springframework.retry.annotation.EnableRetry;\n" +
                "\n" +
                "import javax.annotation.PostConstruct;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "@Configuration\n" +
                "@EnableKafka\n" +
                "@EnableRetry\n" +
                "@ConditionalOnProperty(prefix = \"app\", name = \"kafka.enabled\", havingValue = \"true\")\n" +
                "public class KafkaConsumerConfig {\n" +
                "\n" +
                "    @Value(\"${app.kafka.host}\")\n" +
                "    private String kafkaHost;\n" +
                "\n" +
                "    @Value(\"${app.kafka.max-poll-record:1000}\")\n" +
                "    private int maxPollRecord;\n" +
                "\n" +
                "    @Value(\"${app.kafka.consumer-group-id:user-consumer-test}\")\n" +
                "    private String kafkaConsumerGroupId;\n" +
                "\n" +
                "    private static final Map<String, Object> DEFAULT_CONSUMER_CONFIG_MAP = new HashMap<>();\n" +
                "\n" +
                "    @PostConstruct\n" +
                "    public void configureKafkaProperties() {\n" +
                "        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);\n" +
                "        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);\n" +
                "        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);\n" +
                "        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecord);\n" +
                "    }\n" +
                "\n" +
                "    @Bean\n" +
                "    public ConsumerFactory<String, " + modelClassName + "> userConsumerFactory() {\n" +
                "        Map<String, Object> map = new HashMap<>(DEFAULT_CONSUMER_CONFIG_MAP);\n" +
                "        map.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId);\n" +
                "        JsonDeserializer<" + modelClassName + "> userJsonDeserializer = new JsonDeserializer<>(" + modelClassName + ".class);\n" +
                "        userJsonDeserializer.ignoreTypeHeaders();\n" +
                "        return new DefaultKafkaConsumerFactory<>(map, new StringDeserializer(), userJsonDeserializer);\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    @Bean\n" +
                "    public ConcurrentKafkaListenerContainerFactory<String, " + modelClassName + "> concurrentKafkaListenerUserConsumerFactory() {\n" +
                "        ConcurrentKafkaListenerContainerFactory<String, " + modelClassName + "> factory = new ConcurrentKafkaListenerContainerFactory<>();\n" +
                "        factory.setConsumerFactory(userConsumerFactory());\n" +
                "        return factory;\n" +
                "    }" +
                "}";
        Files.write(javaSourceFile, code.getBytes());
       // GenerateClassFileAndDeleteSourceFile(javaSourceFile);
    }

    private static void generateKafkaProducer(String modelClassName) throws IOException {
        Path temp = Paths.get(FILE_LOCATION + "\\producer");
        Files.createDirectories(temp);

        Path javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), KAFKA_PRODUCER_CLASS + ".java");
        String code = "package com.ex.kafka.generated.producer;\n" +
                "import lombok.extern.slf4j.Slf4j;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                "import org.springframework.beans.factory.annotation.Value;\n" +
                "import org.springframework.kafka.core.KafkaTemplate;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "\n" +
                "import java.util.UUID;\n" +
                "\n" +
                "@Component\n" +
                "@Slf4j\n" +
                "public class KafkaDataSender {\n" +
                "\n" +
                "    @Autowired\n" +

                "    private KafkaTemplate<String, " + modelClassName + "> kafkaTemplate;\n" +
                "\n" +
                "    @Value(\"${app.kafka.topic:test_topic}\")\n" +
                "    private String topicName;\n" +
                "\n" +
                "    @Value(\"${app.kafka.enabled:false}\")\n" +
                "    private boolean isKafkaEnabled;\n" +
                "\n" +
                "\n" +
                "    public String send(" + modelClassName + " data) {\n" +
                "        log.info(\"sending data='{}={}' to topic='{}'\", data.hashCode(), data, topicName);\n" +
                "        String key = UUID.randomUUID().toString();\n" +
                "        if (isKafkaEnabled) {\n" +
                "            kafkaTemplate.send(topicName, key, data);\n" +
                "            return \"data sent successfully\";\n" +
                "        }\n" +
                "        return \"kafka is disabled in application\";\n" +
                "    }\n" +
                "}";
        Files.write(javaSourceFile, code.getBytes());
       // GenerateClassFileAndDeleteSourceFile(javaSourceFile);
    }

    private static void generateKafkaListener(String modelClassName) throws IOException {
        Path temp = Paths.get(FILE_LOCATION + "\\listener");
        Files.createDirectories(temp);

        Path javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), KAFKA_LISTENER_CLASS + ".java");
        String code = "package com.ex.kafka.generated.listener;\n" +
                "import lombok.extern.slf4j.Slf4j;\n" +
                "import com.ex.kafka.generated.listener.processor.KafkaDataProcessor;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.kafka.annotation.KafkaListener;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "\n" +
                "@Component\n" +
                "@Slf4j\n" +
                "public class KafkaDataListener {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private KafkaDataProcessor kafkaDataProcessor;\n" +
                "\n" +
                "\n" +
                "    @KafkaListener(topics = \"${app.kafka.topic}\", groupId = \"${app.kafka.consumer-group-id}\", containerFactory = \"concurrentKafkaListenerUserConsumerFactory\")\n" +
                "    public void receive(" + modelClassName + " data) {\n" +
                "        log.info(\"Received data in kafka listener : {}\", data);\n" +
                "        kafkaDataProcessor.process(data);\n" +
                "    }\n" +
                "}";
        Files.write(javaSourceFile, code.getBytes());
       // GenerateClassFileAndDeleteSourceFile(javaSourceFile);
    }

    private static void generateKafkaListenerProcessor(String modelClassName) throws IOException {
        Path temp = Paths.get(FILE_LOCATION + "\\listener\\processor");
        Files.createDirectories(temp);

        Path javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), KAFKA_LISTENER_PROCESSOR_CLASS + ".java");
        String code = "package com.ex.kafka.generated.listener.processor;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "@Service\n" +
                "public interface KafkaDataProcessor {\n" +
                "    Object process(" + modelClassName + " data);\n" +
                "}";
        Files.write(javaSourceFile, code.getBytes());
        //GenerateClassFileAndDeleteSourceFile(javaSourceFile);
    }


    private static void GenerateClassFileAndDeleteSourceFile(Path javaSourceFile) throws IOException {
        File[] files = {javaSourceFile.toFile()};
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        task.call();
        for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics())
            System.out.format("Error on line %d in %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getSource());
        fileManager.close();
        Files.delete(javaSourceFile);
    }
}
