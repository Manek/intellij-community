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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Aleksey Pivovarov
 */
@SuppressWarnings("UnusedDeclaration")
public class GithubIssueComment {
  @NotNull private Long id;

  @NotNull private String url;
  @NotNull private String htmlUrl;
  @NotNull private String body;

  @NotNull private Date createdAt;
  @NotNull private Date updatedAt;

  @NotNull private GithubUser user;

  @NotNull
  public static GithubIssueComment create(@Nullable GithubIssueCommentRaw raw) throws JsonException {
    try {
      if (raw == null) throw new JsonException("raw is null");
      if (raw.url == null) throw new JsonException("url is null");
      if (raw.htmlUrl == null) throw new JsonException("htmlUrl is null");
      if (raw.id == null) throw new JsonException("id is null");
      if (raw.body == null) throw new JsonException("body is null");

      if (raw.createdAt == null) throw new JsonException("createdAt is null");
      if (raw.updatedAt == null) throw new JsonException("updatedAt is null");

      GithubUser user = GithubUser.create(raw.user);

      return new GithubIssueComment(raw.id, raw.url, raw.htmlUrl, raw.body, raw.createdAt, raw.updatedAt, user);
    }
    catch (JsonException e) {
      throw new JsonException("GithubIssueComment parse error", e);
    }
  }

  private GithubIssueComment(@NotNull Long id,
                             @NotNull String url,
                             @NotNull String htmlUrl,
                             @NotNull String body,
                             @NotNull Date createdAt,
                             @NotNull Date updatedAt,
                             @NotNull GithubUser user) {
    this.id = id;
    this.url = url;
    this.htmlUrl = htmlUrl;
    this.body = body;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.user = user;
  }

  @NotNull
  public Long getId() {
    return id;
  }

  @NotNull
  public String getUrl() {
    return url;
  }

  @NotNull
  public String getHtmlUrl() {
    return htmlUrl;
  }

  @NotNull
  public String getBody() {
    return body;
  }

  @NotNull
  public Date getCreatedAt() {
    return createdAt;
  }

  @NotNull
  public Date getUpdatedAt() {
    return updatedAt;
  }

  @NotNull
  public GithubUser getUser() {
    return user;
  }
}
