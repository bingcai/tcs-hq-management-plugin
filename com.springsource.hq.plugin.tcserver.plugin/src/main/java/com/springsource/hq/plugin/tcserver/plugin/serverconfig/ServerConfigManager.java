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

package com.springsource.hq.plugin.tcserver.plugin.serverconfig;

import java.util.List;

import org.hyperic.hq.product.PluginException;
import org.hyperic.util.config.ConfigResponse;

import com.springsource.hq.plugin.tcserver.serverconfig.Settings;

public interface ServerConfigManager {

    void copyFile(ConfigResponse config) throws PluginException;

    boolean fileExists(ConfigResponse config) throws PluginException;

    Settings getConfiguration(ConfigResponse config) throws PluginException;

    String getFile(ConfigResponse config) throws PluginException;

    List<String> getJvmOpts(ConfigResponse config) throws PluginException;

    void prepareFile(ConfigResponse config) throws PluginException;

    void putFile(ConfigResponse config) throws PluginException;

    void putJvmOpts(ConfigResponse config) throws PluginException;

    void revertToPreviousConfiguration(ConfigResponse config) throws PluginException;

    void saveConfiguration(ConfigResponse config) throws PluginException;

}
