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
import java.util.List;
import java.util.stream.Stream;

/**
 * 多模块构建.
 *
 * @author 王红 @date2022/9/13 20:29
 *
 */
public class ModuleContainer {

	/**
	 * 模块集合.
	 * @author 王红
	 * @date 2022/9/13 20:47
	 *
	 */
	private final List<String> modules = new ArrayList<>();

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
