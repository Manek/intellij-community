/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.openapi.util.io.win32;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Do not use this class directly.
 *
 * @author Dmitry Avdeev
 * @since 12.0
 */
public class IdeaWin32 {
  private static final Logger LOG = Logger.getInstance("#com.intellij.openapi.util.io.win32.IdeaWin32");
  private static final boolean DEBUG_ENABLED = LOG.isDebugEnabled();

  private static final IdeaWin32 ourInstance;

  static {
    boolean available = false;
    if (SystemInfo.isWin2kOrNewer) {
      String libName = SystemInfo.is64Bit ? "IdeaWin64.dll" : "IdeaWin32.dll";
      try {
        String path = PathManager.getBinPath() + "/" + libName;
        if (!new File(path).exists()) {
          path = PathManager.getHomePath() + "/community/bin/win/" + libName;
          if (!new File(path).exists()) {
            path = PathManager.getHomePath() + "/bin/win/" + libName;
            if (!new File(path).exists()) {
              path = PathManager.getHomePathFor(IdeaWin32.class) + "/bin/" + libName;
              if (!new File(path).exists()) {
                throw new FileNotFoundException("Native filesystem .dll is missing (path=" + PathManager.getBinPath() +
                                                " content=" + Arrays.toString(new File(PathManager.getBinPath()).list()) + ")");
              }
            }
          }
        }
        LOG.debug("Loading " + path);
        System.load(path);
        available = true;
      }
      catch (Throwable t) {
        LOG.error("Failed to load native filesystem for Windows", t);
      }
    }

    IdeaWin32 instance = null;
    if (available) {
      try {
        instance = new IdeaWin32();
        LOG.info("Native filesystem for Windows is operational");
      }
      catch (Throwable t) {
        LOG.error("Failed to initialize native filesystem for Windows", t);
      }
    }
    ourInstance = instance;
  }

  public static boolean isAvailable() {
    return ourInstance != null;
  }

  @NotNull
  public static IdeaWin32 getInstance() {
    if (!isAvailable()) {
      throw new IllegalStateException("Native filesystem for Windows is not loaded");
    }
    return ourInstance;
  }

  private IdeaWin32() {
    initIDs();
  }

  private static native void initIDs();

  @Nullable
  public FileInfo getInfo(@NotNull String path) {
    path = path.replace('/', '\\');
    if (DEBUG_ENABLED) {
      long t = System.nanoTime();
      final FileInfo result = getInfo0(path);
      t = (System.nanoTime() - t) / 1000;
      LOG.debug("getInfo(" + path + "): " + t + " mks");
      return result;
    }
    else {
      return getInfo0(path);
    }
  }

  @Nullable
  public String resolveSymLink(@NotNull String path) {
    path = path.replace('/', '\\');
    if (DEBUG_ENABLED) {
      long t = System.nanoTime();
      final String result = resolveSymLink0(path);
      t = (System.nanoTime() - t) / 1000;
      LOG.debug("resolveSymLink(" + path + "): " + t + " mks");
      return result;
    }
    else {
      return resolveSymLink0(path);
    }
  }

  @Nullable
  public FileInfo[] listChildren(@NotNull String path) {
    path = path.replace('/', '\\');
    if (DEBUG_ENABLED) {
      long t = System.nanoTime();
      FileInfo[] children = listChildren0(path);
      t = (System.nanoTime() - t) / 1000;
      LOG.debug("list(" + path + "): " + children.length + " children, " + t + " mks");
      return children;
    }
    else {
      return listChildren0(path);
    }
  }

  private native FileInfo getInfo0(String path);

  private native String resolveSymLink0(String path);

  private native FileInfo[] listChildren0(String path);
}
