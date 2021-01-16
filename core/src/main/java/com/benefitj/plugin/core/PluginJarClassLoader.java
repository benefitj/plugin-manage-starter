package com.benefitj.plugin.core;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginJarClassLoader extends URLClassLoader {

  private final Object packageLock = new Object();

  public PluginJarClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public PluginJarClassLoader(URL[] urls) {
    super(urls);
  }


}
