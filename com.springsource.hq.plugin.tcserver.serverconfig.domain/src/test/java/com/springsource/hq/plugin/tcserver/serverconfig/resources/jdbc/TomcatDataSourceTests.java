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

package com.springsource.hq.plugin.tcserver.serverconfig.resources.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import com.springsource.hq.plugin.tcserver.serverconfig.resources.jdbc.DataSource;
import com.springsource.hq.plugin.tcserver.serverconfig.resources.jdbc.TomcatDataSource;

/**
 * Unit tests for {@link TomcatDataSource}
 */
public class TomcatDataSourceTests {

    private TomcatDataSource tomcatDataSource;

    @Before
    public void setup() {
        tomcatDataSource = new TomcatDataSource();
        tomcatDataSource.getConnection().setObscuredPassword("");
    }

    @Test
    public void testDataSource() {
        assertTrue(tomcatDataSource instanceof DataSource);
    }

    @Test
    public void testApplyParentToChildren() {
        tomcatDataSource.applyParentToChildren();
        assertSame(tomcatDataSource, tomcatDataSource.getConnection().parent());
        assertSame(tomcatDataSource, tomcatDataSource.getGeneral().parent());
        assertSame(tomcatDataSource, tomcatDataSource.getConnectionPool().parent());
    }

    @Test
    public void testValidator() {
        assertTrue(tomcatDataSource instanceof Validator);
    }

    @Test
    public void testSupports() {
        assertTrue(tomcatDataSource.supports(tomcatDataSource.getClass()));
    }

    @Test
    public void testValidate_noContext() {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(tomcatDataSource, "tomcatDataSource");
        tomcatDataSource.validate(tomcatDataSource, errors);
        assertEquals(5, errors.getFieldErrorCount());
        assertEquals("resource.dataSource.general.jndiName.required", errors.getFieldError("general.jndiName").getCode());
        assertEquals("resource.dataSource.connection.username.required", errors.getFieldError("connection.username").getCode());
        assertEquals("resource.dataSource.connection.password.required", errors.getFieldError("connection.obscuredPassword").getCode());
        assertEquals("resource.dataSource.connection.url.required", errors.getFieldError("connection.url").getCode());
        assertEquals("resource.dataSource.connection.driverClassName.required", errors.getFieldError("connection.driverClassName").getCode());
    }

}
