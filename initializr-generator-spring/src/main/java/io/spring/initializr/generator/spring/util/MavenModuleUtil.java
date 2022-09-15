/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.spring.util;

import java.nio.file.Path;

/**
 * 多模块构建util.
 *
 * @author wang hong
 */
public final class MavenModuleUtil {

	/**
	 * none: only have noe pom file.
	 */
	public static final String NONE_ARCHITECTURE;

	/**
	 * mvc: has many module, many pom file.
	 */
	public static final String MVC_ARCHITECTURE;

	/**
	 * main class location module.
	 */
	public static final String START;

	static {
		MVC_ARCHITECTURE = "mvc";
		START = "start";
		NONE_ARCHITECTURE = "none";
	}

	private MavenModuleUtil() {
	}

	public static Path obtainMavenModulePath(String architecture, Path projectRoot) {
		if (MVC_ARCHITECTURE.equals(architecture)) {
			projectRoot = projectRoot.resolve(MavenModuleUtil.START + "/");
		}
		return projectRoot;
	}

}
