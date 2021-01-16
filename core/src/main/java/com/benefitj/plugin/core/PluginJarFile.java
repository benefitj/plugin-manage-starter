package com.benefitj.plugin.core;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginJarFile extends JarFile {

  public static final String MAIN_CLASS = "Main-Class";
  /**
   * URL
   */
  private final URL url;
  /**
   * jar包文件
   */
  private final File jar;

  public PluginJarFile(File jar) throws IOException {
    super(jar);
    this.jar = jar;
    this.url = jar.toURI().toURL();
  }

  public File getJar() {
    return jar;
  }

  public URL getUrl() {
    return url;
  }

  public String getJarName() {
    return getJar().getName();
  }

  @Override
  public Manifest getManifest() throws IllegalStateException {
    try {
      return super.getManifest();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * 获取属性
   */
  public Attributes getAttributes(String name) {
    return this.getManifest().getAttributes(name);
  }

  /**
   * 获取主属性
   */
  public Attributes getMainAttributes() {
    return this.getManifest().getMainAttributes();
  }

  /**
   * 获取 Manifest 下的Main值
   *
   * @param name 名称
   * @return 返回对应的值
   */
  public String getMainAttributeValue(String name) {
    return getMainAttributes().getValue(name);
  }

  /**
   * 获取主类
   */
  @Nullable
  public String getMainClass() {
    return this.getMainAttributeValue(MAIN_CLASS);
  }

}
