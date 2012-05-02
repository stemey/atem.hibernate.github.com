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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.atemsource.atem.api.attribute.Accessor;


public class HibernateAccessor implements Accessor
{
	private Field field;

	private Method readMethod;

	private Method writeMethod;

	public HibernateAccessor(Field field, Method readMethod)
	{
		super();
		this.readMethod = readMethod;
		this.field = field;
		if (readMethod == null)
		{
			field.setAccessible(true);
		}
	}

	public HibernateAccessor(Field field, Method readMethod, Method writeMethod)
	{
		super();
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
		this.field = field;
		if (readMethod == null)
		{
			field.setAccessible(true);
		}
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation getAnnotationAnnotatedBy(Class<? extends Annotation> annotationClass)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends Annotation> getAnnotations()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Field getField()
	{
		return field;
	}

	public Method getReadMethod()
	{
		return readMethod;
	}

	@Override
	public Object getValue(Object entity)
	{
		try
		{
			if (readMethod == null)
			{
				return field.get(entity);
			}
			else
			{
				Object[] args;
				args = new Object[0];
				return readMethod.invoke(entity, args);
			}
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalStateException("please check correct implementation of property at startup time", e);
		}
		catch (InvocationTargetException e)
		{
			throw new IllegalStateException("please check correct implementation of property at startup time", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalStateException("please check correct implementation of property at startup time", e);
		}
		catch (SecurityException e)
		{
			throw new IllegalStateException("please check correct implementation of property at startup time", e);
		}
	}

	public Method getWriteMethod()
	{
		return writeMethod;
	}

	@Override
	public boolean isReadable()
	{
		return true;
	}

	@Override
	public boolean isWritable()
	{
		return true;
	}

	public void setReadMethod(Method readMethod)
	{
		this.readMethod = readMethod;
	}

	@Override
	public void setValue(Object entity, Object value)
	{
		if (writeMethod == null)
		{
			field.setAccessible(true);
			try
			{
				field.set(entity, value);
			}
			catch (IllegalArgumentException e)
			{
				throw new IllegalStateException("cannot change field value", e);
			}
			catch (IllegalAccessException e)
			{
				throw new IllegalStateException("cannot change field value", e);
			}
		}
		else
		{
			Object[] args;
			args = new Object[]{value};
			try
			{
				writeMethod.invoke(entity, args);
			}
			catch (IllegalArgumentException e)
			{
				// Logs.META.error("cannot set property " + writeMethod.getName(), e);
			}
			catch (IllegalAccessException e)
			{
				throw new IllegalStateException("please check correct implementation of property at startup time", e);
			}
			catch (InvocationTargetException e)
			{
				// TODO this is caught because Category.setParent throws exception when value is null
				// Logs.META.error("cannot set property " + writeMethod.getName(), e);
			}
		}
	}

	public void setWriteMethod(Method writeMethod)
	{
		this.writeMethod = writeMethod;
	}
}
