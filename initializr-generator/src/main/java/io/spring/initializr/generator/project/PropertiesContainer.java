/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.initializr.generator.project;

import java.util.Map;
import java.util.TreeMap;

/**
 * A container for properties. Arbitrary properties can be specified as well as ones that.
 *
 * @author wang hong
 */
public class PropertiesContainer {

	private final Map<String, String> properties = new TreeMap<>();

	/**
	 * Specify if this container is empty.
	 * @return {@code true} if no property is registered
	 */
	public boolean isEmpty() {
		return this.properties.isEmpty();
	}

	/**
	 * Specify if this container has a property with the specified name.
	 * @param name the name of a property
	 * @return {@code true} if a property with the specified {@code name} is registered
	 */
	public boolean has(String name) {
		return this.properties.containsKey(name);
	}

	/**
	 * Register a property with the specified {@code name} and {@code value}. If a
	 * property with that {@code name} already exists, its value is overridden by the
	 * specified {@code value}.
	 * @param name the name of a property
	 * @param value the value of the property
	 * @return this container
	 */
	public PropertiesContainer property(String name, String value) {
		this.properties.put(name, value);
		return this;
	}

	/**
	 * Return the registered properties. Does not contain registered versions.
	 * @return the property entries
	 */
	public Map<String, String> values() {
		return this.properties;
	}

}
