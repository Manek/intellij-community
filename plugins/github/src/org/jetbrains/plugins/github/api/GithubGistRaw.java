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
package org.jetbrains.plugins.github.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author Aleksey Pivovarov
 */
@SuppressWarnings("UnusedDeclaration")
class GithubGistRaw implements Serializable {
  public String id;
  public String description;

  @SerializedName("public")
  public Boolean isPublic;

  public String url;
  public String htmlUrl;
  public String gitPullUrl;
  public String gitPushUrl;

  public Map<String, GistFileRaw> files;

  public GithubUserRaw user;

  public Date createdAt;

  public static class GistFileRaw {
    public Long size;
    public String filename;
    public String content;

    public String raw_url;

    public String type;
    public String language;
  }
}
