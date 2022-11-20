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

package io.spring.initializr.generator.buildsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;

/**
 * 多模块构建.
 *
 * @author 王红 @date2022/9/13 20:29
 */
public class ModuleContainer {

	private final BuildItemResolver buildItemResolver;

	public ModuleContainer(BuildItemResolver buildItemResolver) {
		this.buildItemResolver = buildItemResolver;
	}

	/**
	 * 模块集合.
	 *
	 * @author 王红
	 * @date 2022/9/13 20:47
	 */
	private final List<String> modules = new ArrayList<>();

	/**
	 * 每个模块的pom文件.
	 *
	 * @author 王红
	 * @date 2022/9/13 20:47
	 */
	private final Map<String, MavenBuild> moduleBuildMap = new HashMap<>();

	/**
	 * Specify if this container is empty.
	 * @return {@code true} if no module build is registered
	 */
	public boolean isEmptyModuleBuildMap() {
		return this.modules.isEmpty();
	}

	/**
	 * Register a module with the specified {@code module}.
	 * @param module the name of a module
	 * @param mavenBuild the name of a module build
	 * @return this container
	 */
	public ModuleContainer moduleBuild(String module, MavenBuild mavenBuild) {
		this.moduleBuildMap.put(module, mavenBuild);
		return this;
	}

	/**
	 * Obtain a module with the specified {@code module}.
	 * @param module the name of a module
	 * @return this MavenBuild
	 */
	public MavenBuild obtainModuleBuild(String module) {
		MavenBuild moduleBuild = this.moduleBuildMap.get(module);
		if (moduleBuild == null) {
			moduleBuild = new MavenBuild(this.buildItemResolver);
			this.moduleBuildMap.put(module, moduleBuild);
		}
		return moduleBuild;
	}

	/**
	 * Specify if this container is empty.
	 * @return {@code true} if no module is registered
	 */
	public boolean isEmpty() {
		return this.modules.isEmpty();
	}

	/**
	 * Register a module with the specified {@code module}.
	 * @param module the name of a module
	 * @return this container
	 */
	public ModuleContainer module(String module) {
		this.modules.add(module);
		return this;
	}

	/**
	 * Return the registered properties. Does not contain registered versions.
	 * @return the module entries
	 */
	public Stream<String> values() {
		return this.modules.stream();
	}

}
