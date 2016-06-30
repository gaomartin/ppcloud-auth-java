package com.pplive.ppcloud.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public JsonUtil() {
	}

	public static String getPropertiesFromJson(String str, String[] keys) {
		String allKeys = Arrays.toString(keys);
		try {
			JsonParser parser = objectMapper.getFactory().createParser(str);
			JsonNode node = parser.readValueAsTree();
			for (String key : keys) {
				if (node.has(key)) {
					node = node.findValue(key);
				} else {
					throw new IllegalArgumentException("key not exist in json str, keys:" + allKeys);
				}
			}
			return node.toString();
		} catch (JsonParseException e) {
			throw new RuntimeException("parse json error, json=" + str + ", keys:" + allKeys);
		} catch (IOException e) {
			throw new RuntimeException("parse json error, json=" + str + ", keys:" + allKeys);
		}
	}

	public static <T> T getObjectFromJson(InputStream instream, Class<T> cls) {
		try {
			JsonParser parser = objectMapper.getFactory().createParser(instream);
			T t = objectMapper.readValue(parser, cls);
			return t;
		} catch (JsonParseException e) {
			throw new RuntimeException("parse json error");
		} catch (IOException e) {
			throw new RuntimeException("parse json error");
		} finally {
			try {
				instream.close();
			} catch (Exception localException1) {
			}
		}
	}

	public static <T> T getObjectFromJson(String str, Class<T> cls) {
		try {
			JsonParser parser = objectMapper.getFactory().createParser(str);
			return objectMapper.readValue(parser, cls);
		} catch (JsonParseException e) {
			throw new RuntimeException("parse json error, json=" + str + ", class=" + cls.getName(), e);
		} catch (IOException e) {
			throw new RuntimeException("parse json error, json=" + str + ", class=" + cls.getName(), e);
		}
	}

	public static String getValueByFieldFromJson(String json, String field) {
		Map<String, String> mapValue = (Map<String, String>) getObjectFromJson(json, HashMap.class);
		return (String) mapValue.get(field);
	}

	public static String getJsonFromObject(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			throw new RuntimeException("get json error", e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("get json error", e);
		} catch (IOException e) {
			throw new RuntimeException("get json error", e);
		}
	}

	public static <T> List<T> parserJsonList(InputStream instream, Class<T> clsT) {
		try {
			JsonParser parser = objectMapper.getFactory().createParser(instream);

			JsonNode nodes = parser.readValueAsTree();

			List<T> list = new LinkedList();
			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node.asText(), clsT));
			}
			return list;
		} catch (JsonParseException e) {
			throw new RuntimeException("parse json error", e);
		} catch (IOException e) {
			throw new RuntimeException("parse json error", e);
		} finally {
			try {
				instream.close();
			} catch (Exception localException1) {
			}
		}
	}

	public static <T> List<T> parserJsonList(String str, Class<T> clsT) {
		try {
			JsonParser parser = objectMapper.getFactory().createParser(str);

			JsonNode nodes = parser.readValueAsTree();
			List<T> list = new LinkedList();
			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node.asText(), clsT));
			}
			return list;
		} catch (JsonParseException e) {
			throw new RuntimeException("parse json error str:" + str, e);
		} catch (IOException e) {
			throw new RuntimeException("parse json error str:" + str, e);
		}
	}

	public static <T> LinkedHashMap<String, T> parserJsonMap(String str, Class<T> clsT) {
		LinkedHashMap<String, T> map = new LinkedHashMap();
		try {
			JsonParser parser = objectMapper.getFactory().createParser(str);

			JsonToken current = parser.nextToken();
			if (current != JsonToken.START_OBJECT) {
				throw new IllegalArgumentException("parse json error: root should be object, quiting.");
			}

			while (parser.nextToken() != JsonToken.END_OBJECT) {
				String fieldName = parser.getCurrentName();
				current = parser.nextToken();
				T obj = parser.readValueAs(clsT);
				map.put(fieldName, obj);
			}

			return map;
		} catch (JsonParseException e) {
			throw new RuntimeException("parse json error str:" + str, e);
		} catch (IOException e) {
			throw new RuntimeException("parse json error str:" + str, e);
		}
	}
}