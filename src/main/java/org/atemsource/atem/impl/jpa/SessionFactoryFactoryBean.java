/*******************************************************************************
 * Stefan Meyer, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;


public class SessionFactoryFactoryBean implements FactoryBean, InitializingBean
{
	private EntityManagerFactory entityManagerFactory;

	private SessionFactory sessionFactory;

	public void afterPropertiesSet() throws Exception
	{
		Object o = Proxy.getInvocationHandler(entityManagerFactory);
		Field field = null;
		try
		{
			field = o.getClass().getDeclaredField("targetEntityManagerFactory");
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}
			sessionFactory = ((EntityManagerFactoryImpl) field.get(o)).getSessionFactory();
		}
		catch (NoSuchFieldException e)
		{
			field = o.getClass().getDeclaredField("entityManagerFactoryBean");
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}
			AbstractEntityManagerFactoryBean factory = (AbstractEntityManagerFactoryBean) field.get(o);
			sessionFactory = ((EntityManagerFactoryImpl) factory.nativeEntityManagerFactory).getSessionFactory();
		}

	}

	public Object getObject() throws Exception
	{

		return sessionFactory;
	}

	public Class getObjectType()
	{

		return SessionFactory.class;
	}

	public boolean isSingleton()
	{
		return true;
	}

	public void setEntityManagerFactory(final EntityManagerFactory entityManagerFactory)
	{
		this.entityManagerFactory = entityManagerFactory;
	}

}
