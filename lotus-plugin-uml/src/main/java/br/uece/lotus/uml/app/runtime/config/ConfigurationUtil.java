/**
 * The MIT License
 * Copyright © 2017 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.uml.app.runtime.config;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationUtil {
	
//	public static ConfigurationBuilder newConfiguration() {
//		return Configuration.builder();
//	}
	
	public static Configuration load(Path configPath) {
		Configuration config = null;
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		try {
			config = gson.fromJson(new FileReader(configPath.toFile()), Configuration.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	public static Configuration load(String jsonConfig) {
		Configuration config = null;
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		config = gson.fromJson(jsonConfig, Configuration.class);
		
		return config;
	}
	
	public static Configuration save(Path configPath, Configuration config) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(gson.toJson(config));
		
		String json = gson.toJson(jsonElement);
				
		try {
			Files.write(configPath, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return config;
	}

}
