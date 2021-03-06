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

package com.springsource.hq.plugin.tcserver.plugin.serverconfig.services.engine;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.springsource.hq.plugin.tcserver.plugin.serverconfig.AbstractXmlElementConverter;
import com.springsource.hq.plugin.tcserver.plugin.serverconfig.XmlElementConverter;
import com.springsource.hq.plugin.tcserver.serverconfig.Hierarchical;
import com.springsource.hq.plugin.tcserver.serverconfig.services.engine.Logging;

public class AccessLogValveConverter<T extends Hierarchical<?>> extends AbstractXmlElementConverter implements XmlElementConverter<Logging<T>> {

    private static final String SUFFIX = "suffix";

    private static final String PREFIX = "prefix";

    private static final String PATTERN = "pattern";

    private static final String FILE_DATE_FORMAT = "fileDateFormat";

    private static final String DIRECTORY = "directory";

    public Logging<T> convert(Element valve, Properties catalinaProperties) {
        final Logging<T> logging = new Logging<T>();
        String directory = parseProperties(valve.getAttribute(DIRECTORY), catalinaProperties);
        if (!(EMPTY_STRING.equals(directory))) {
            logging.setDirectory(directory);
        }
        String fileDateFormat = parseProperties(valve.getAttribute(FILE_DATE_FORMAT), catalinaProperties);
        if (!(EMPTY_STRING.equals(fileDateFormat))) {
            logging.setFileDateFormat(fileDateFormat);
        }
        String pattern = parseProperties(valve.getAttribute(PATTERN), catalinaProperties);
        if (!(EMPTY_STRING.equals(pattern))) {
            logging.setPattern(pattern);
        }
        String prefix = parseProperties(valve.getAttribute(PREFIX), catalinaProperties);
        if (!(EMPTY_STRING.equals(prefix))) {
            logging.setPrefix(prefix);
        }
        String suffix = parseProperties(valve.getAttribute(SUFFIX), catalinaProperties);
        if (!(EMPTY_STRING.equals(suffix))) {
            logging.setSuffix(suffix);
        }
        logging.setEnabled(true);
        return logging;
    }

    public void convert(Document document, Element accessLogValve, Logging<T> from, Properties catalinaProperties) {
        setAttribute(accessLogValve, DIRECTORY, from.getDirectory(), catalinaProperties, false);
        setAttribute(accessLogValve, FILE_DATE_FORMAT, from.getFileDateFormat(), catalinaProperties, false);
        setAttribute(accessLogValve, PATTERN, from.getPattern(), catalinaProperties, false);
        setAttribute(accessLogValve, PREFIX, from.getPrefix(), catalinaProperties, false);
        setAttribute(accessLogValve, SUFFIX, from.getSuffix(), catalinaProperties, false);
    }
}
