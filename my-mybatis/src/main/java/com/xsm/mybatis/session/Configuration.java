package com.xsm.mybatis.session;

import com.xsm.mybatis.binding.MapperMethod;
import com.xsm.mybatis.binding.MapperRegistry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author xsm
 * @Date 2021/1/17 17:46
 */
public class Configuration {

    private InputStream inputStream;

    MapperRegistry mapperRegistry = new MapperRegistry();

    public void loadConfigurations() throws IOException {
        try {
            Document document = new SAXReader().read(inputStream);
            Element root = document.getRootElement();
            List<Element> mappers = root.element("mappers").elements("mapper");
            for (Element mapper : mappers) {
                if (mapper.attribute("resource") != null) {
                    mapperRegistry.setKnowMappers(loadXMLConfiguration(mapper.attribute("resource").getText()));
                }
                if (mapper.attribute("class") != null) {

                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }

    private Map<String, MapperMethod> loadXMLConfiguration(String resource) {
        return null;
    }
}
