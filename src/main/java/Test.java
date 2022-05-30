import com.ex.utils.ConfigFileGenerate;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Test {

    private final static String FILE_LOCATION = ".\\target\\classes\\com\\ex\\generated";

    public static void main(String[] args) throws IOException {
        //generateKafkaListenerProcessor("com.ex.kafka.KafkaData");
        ConfigFileGenerate.main(new String[]{"com.ex.kafka.KafkaData"});
    }

    private static void generateKafkaListenerProcessor(String modelClassName) throws IOException {
        Path temp = Paths.get(FILE_LOCATION );
        Files.createDirectories(temp);

        Path javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), "KafkaDataProcessor" + ".java");
        String code = "package com.ex.generated;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "@Service\n" +
                "public class KafkaDataProcessor {\n" +
                "    void process(" + modelClassName + " data){}\n" +
                "}";
        Files.write(javaSourceFile, code.getBytes());

        GenerateClassFileAndDeleteSourceFile(javaSourceFile);

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
