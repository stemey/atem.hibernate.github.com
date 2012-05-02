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
package org.atemsource.atem.impl.hibernate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;


public class PropertyDescriptor
{

	public static PropertyDescriptor createInstance(final Class clazz, String propertyName)
	{
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor();
		try
		{
			propertyDescriptor.declaringClass = clazz;
			Class currentClass = clazz;
			// this is probably necessary because the field is named exactly like the getter
			if (propertyName.startsWith("is"))
			{
				propertyName = propertyName.substring(2, 3).toLowerCase() + propertyName.substring(3);
			}
			else if (propertyName.startsWith("get"))
			{
				propertyName = propertyName.substring(3, 4).toLowerCase() + propertyName.substring(4);
			}
			java.beans.PropertyDescriptor propertyDescriptor2 = BeanUtils.getPropertyDescriptor(clazz, propertyName);
			if (propertyDescriptor2 != null)
			{
				propertyDescriptor.readMethod = propertyDescriptor2.getReadMethod();
				propertyDescriptor.writeMethod = propertyDescriptor2.getWriteMethod();

			}
			else
			{
				PropertyDescriptor propertyDescriptor3 = new PropertyDescriptor();
				while (currentClass != null)
				{
					try
					{
						propertyDescriptor3.field = clazz.getDeclaredField(propertyName);
						propertyDescriptor3.propertyName = propertyName;
						propertyDescriptor3.declaringClass = currentClass;
						return propertyDescriptor3;
					}
					catch (NoSuchFieldException e)
					{
					}
					currentClass = currentClass.getSuperclass();
				}
			}

			while (currentClass != null)
			{
				try
				{
					propertyDescriptor.field = clazz.getDeclaredField(propertyName);
				}
				catch (NoSuchFieldException e)
				{
				}
				currentClass = currentClass.getSuperclass();
			}
		}
		catch (SecurityException e)
		{
			return null;
		}
		propertyDescriptor.propertyName = propertyName;
		return propertyDescriptor;
	}

	private Class<?> declaringClass;

	private String propertyName;

	private Method readMethod;

	private Field field;

	private Method writeMethod;

	public PropertyDescriptor()
	{

	}

	public <A extends Annotation> A getAnnotation(final Class<A> clazz)
	{
		A a = null;

		if (readMethod != null)
		{
			a = readMethod.getAnnotation(clazz);
		}
		if (a == null && field != null)
		{
			a = field.getAnnotation(clazz);
		}
		return a;
	}

	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}

	public Field getField()
	{
		return field;
	}

	public String getName()
	{
		return propertyName;
	}

	public Class getPropertyType()
	{
		if (field != null)
		{
			return field.getType();
		}
		else if (readMethod != null)
		{
			return readMethod.getReturnType();
		}
		else
		{
			return null;
		}
	}

	public Method getReadMethod()
	{
		return readMethod;
	}

	public Method getWriteMethod()
	{
		return writeMethod;
	}

	public boolean isWritable()
	{
		return writeMethod != null || field != null;
	}

}
