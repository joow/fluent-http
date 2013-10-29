/**
 * Copyright (C) 2013 all@code-story.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package net.codestory.http.routes;

import static net.codestory.http.routes.Match.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.*;

import net.codestory.http.io.*;
import net.codestory.http.payload.*;

import org.simpleframework.http.*;

class StaticRoute implements Route {
  @Override
  public Match apply(String uri, Request request, Response response) throws IOException {
    if (uri.endsWith("/")) {
      return apply(uri + "index", request, response);
    }

    Path path = Resources.findExistingPath(Paths.get(uri));
    if (path == null) {
      return WRONG_URL;
    }

    if (!Resources.isPublic(path)) {
      return WRONG_URL;
    }

    if (!"GET".equalsIgnoreCase(request.getMethod())) {
      return WRONG_METHOD;
    }

    new Payload(path).writeTo(response);
    return OK;
  }
}
