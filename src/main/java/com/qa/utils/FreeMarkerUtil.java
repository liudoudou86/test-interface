package com.qa.utils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;

import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author LiuZhen
 * @Date 2024/10/28
 * @Description 模板工具类
 */


public class FreeMarkerUtil {

	public FreeMarkerUtil() {
	}

	public static String transformToString(String stringTemplate, Map<?, ?> datas) throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDefaultEncoding(Charset.defaultCharset().displayName());
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		stringTemplateLoader.putTemplate("template", stringTemplate);
		configuration.setTemplateLoader(stringTemplateLoader);
		Template template = configuration.getTemplate("template");
		Writer out = new StringWriter(2048);
		template.process(datas, out);
		return out.toString();
	}
}
