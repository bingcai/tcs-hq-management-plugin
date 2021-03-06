/*
 * Copyright (C) 2009-2015  Pivotal Software, Inc
 *
 * This program is is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.springsource.hq.plugin.tcserver.plugin.serverconfig.resources.jdbc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.springsource.hq.plugin.tcserver.plugin.serverconfig.AbstractXmlElementConverter;
import com.springsource.hq.plugin.tcserver.plugin.serverconfig.AttributeNotFoundException;
import com.springsource.hq.plugin.tcserver.plugin.serverconfig.InvalidNodeException;
import com.springsource.hq.plugin.tcserver.plugin.serverconfig.XmlElementConverter;
import com.springsource.hq.plugin.tcserver.serverconfig.resources.jdbc.DataSource;

public class DataSourcesConverter extends AbstractXmlElementConverter implements XmlElementConverter<Set<DataSource>> {

    private static final String DATA_SOURCE_TYPE = "javax.sql.DataSource";

    private final DataSourceConverter dataSourceConverter = new DataSourceConverter();

    private final Log logger = LogFactory.getLog(DataSourcesConverter.class);

    private static final Set<DataSource> EMPTY_DATA_SOURCES = new HashSet<DataSource>(0, 1);

    public void convert(final Document document, Element server, Set<DataSource> from, Properties catalinaProperties) {
        final List<Element> globalNamingResourcesList = getChildElements(server, "GlobalNamingResources");
        if (globalNamingResourcesList.size() > 1) {
            throw new InvalidNodeException("Unable to determine existing DataSources.  Found multiple GlobalNamingResource Elements in server.xml");
        }
        final Set<String> dataSourceIds = new HashSet<String>(from.size());
        for (DataSource dataSource : from) {
            dataSourceIds.add(dataSource.getId());
        }
        final Map<String, Element> dataSourceElements = new HashMap<String, Element>();
        Element globalNamingResources;
        if (globalNamingResourcesList.isEmpty()) {
            globalNamingResources = document.createElement("GlobalNamingResources");
            server.appendChild(globalNamingResources);
        } else {
            globalNamingResources = globalNamingResourcesList.get(0);
            final List<Element> resources = getChildElements(globalNamingResources, "Resource");
            for (int i = 0; i < resources.size(); i++) {
                Element resource = (Element) resources.get(i);
                if (DATA_SOURCE_TYPE.equals(resource.getAttribute("type"))) {
                    final String name = parseProperties(resource.getAttribute("name"), catalinaProperties);
                    if (dataSourceIds.contains(name)) {
                        dataSourceElements.put(name, resource);
                    } else if (dataSourceConverter.isDataSourceFactorySupported(resource, catalinaProperties)) {
                        globalNamingResources.removeChild(resource);
                    }
                }
            }
        }
        for (DataSource dataSource : from) {
            if (dataSourceElements.get(dataSource.getId()) == null) {
                final Element dataSourceElement = document.createElement("Resource");
                dataSourceElement.setAttribute("type", DATA_SOURCE_TYPE);
                dataSourceConverter.convert(document, dataSourceElement, dataSource, catalinaProperties);
                globalNamingResources.appendChild(dataSourceElement);
            } else {
                dataSourceConverter.convert(document, dataSourceElements.get(dataSource.getId()), dataSource, catalinaProperties);
            }
        }
    }

    public Set<DataSource> convert(final Element server, Properties catalinaProperties) {
        final List<Element> globalNamingResourcesList = getChildElements(server, "GlobalNamingResources");
        if (globalNamingResourcesList.size() == 0) {
            return EMPTY_DATA_SOURCES;
        }
        if (globalNamingResourcesList.size() > 1) {
            logger.warn("Unable to determine existing DataSources.  Found multiple GlobalNamingResource Elements in server.xml");
            return EMPTY_DATA_SOURCES;
        }
        final Element globalNamingResources = (Element) globalNamingResourcesList.get(0);
        final Set<DataSource> dataSources = new LinkedHashSet<DataSource>();
        final List<Element> resources = getChildElements(globalNamingResources, "Resource");
        for (int i = 0; i < resources.size(); i++) {
            Element resource = (Element) resources.get(i);
            if (DATA_SOURCE_TYPE.equals(resource.getAttribute("type"))) {
                try {
                    final DataSource dataSource = dataSourceConverter.convert(resource, catalinaProperties);
                    if (dataSource != null) {
                        dataSources.add(dataSource);
                    }
                } catch (AttributeNotFoundException e) {
                    logger.warn("Error reading tc Runtime configuration.  Ignoring invalid Data Source found in server.xml.  Cause: " + e.getMessage());
                }
            }
        }
        return dataSources;
    }

}
