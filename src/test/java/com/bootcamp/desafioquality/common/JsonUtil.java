package com.bootcamp.desafioquality.common;

import net.minidev.json.JSONValue;

import java.util.function.Function;

public class JsonUtil {
    public static final Function<Object, String> JSON_GENERATOR = JSONValue::toJSONString;
}
