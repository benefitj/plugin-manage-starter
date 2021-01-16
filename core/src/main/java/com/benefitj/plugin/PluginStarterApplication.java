package com.benefitj.plugin;

import com.benefitj.plugin.core.PluginJarClassLoader;
import com.benefitj.plugin.core.PluginJarFile;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class PluginStarterApplication {
  public static void main(String[] args) {
//    SpringApplication.run(PluginStarterApplication.class, args);

    // Main-Class
    // vertx-iot-stater-1.0.0-SNAPSHOT.jar
    File pluginDir = new File("./plugin-libs");
    List<File> jarFiles = Stream.of(Objects.requireNonNull(pluginDir.listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".jar"))))
        .filter(File::isFile)
        .collect(Collectors.toList());

    List<PluginJarFile> jarFileList = jarFiles.stream()
        .map(PluginStarterApplication::create)
        .collect(Collectors.toList());

    System.err.println(jarFileList.stream()
        .map(f -> f.getJarName() + " ==>: " + f.getMainClass())
        .collect(Collectors.joining("\n")));

    final Thread mainThread = Thread.currentThread();
    final ClassLoader mainClassLoader = mainThread.getContextClassLoader();
    jarFileList.forEach(jarFile -> startPlugin(jarFile, mainClassLoader));

  }

  private static void startPlugin(PluginJarFile jarFile, ClassLoader mainClassLoader) {
    Thread jarThread = new Thread(() -> {
      try {
        PluginJarClassLoader classLoader = new PluginJarClassLoader(new URL[]{jarFile.getUrl()}, mainClassLoader);
        Class<?> clazz = classLoader.loadClass(jarFile.getMainClass());
        Method mainMethod = clazz.getMethod("main", String[].class);
        mainMethod.invoke(null, new Object[]{new String[0]});
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    String name = jarFile.getJarName();
    jarThread.setName(name.substring(0, name.lastIndexOf(".jar")));
    jarThread.start();
  }

  private static PluginJarFile create(File file) {
    try {
      return new PluginJarFile(file);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
