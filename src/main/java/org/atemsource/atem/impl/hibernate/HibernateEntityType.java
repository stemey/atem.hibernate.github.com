/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.hibernate;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class HibernateEntityType extends AbstractEntityType<Object>
{

	private ClassMetadata classMetadata;

	private boolean primaryKeyNullable;

	public ClassMetadata getClassMetadata()
	{
		return classMetadata;
	}

	public Attribute[] getIdAttributes()
	{
		return new Attribute[]{getAttribute(classMetadata.getIdentifierPropertyName())};
	}

	@Override
	public Class<Object> getJavaType()
	{
		if (classMetadata == null)
		{
			return getEntityClass();
		}
		else
		{
			return classMetadata.getMappedClass(EntityMode.POJO);
		}
	}

	@Override
	public boolean isAssignableFrom(Object entity)
	{
		return getJavaType().isInstance(entity);
	}

	public boolean isPrimaryKeyNullable()
	{
		return primaryKeyNullable;
	}

	public void setClassMetadata(ClassMetadata classMetadata)
	{
		this.classMetadata = classMetadata;
	}

	public void setPrimaryKeyNullable(boolean primaryKeyNullable)
	{
		this.primaryKeyNullable = primaryKeyNullable;
	}
}
