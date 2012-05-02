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
package org.atemsource.atem.impl.hibernate.attributetype;


import java.util.Set;

import javax.annotation.Resource;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.ValidationMetaData;
import org.atemsource.atem.api.attribute.annotation.Association;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.hibernate.HibernateAccessor;
import org.atemsource.atem.impl.hibernate.HibernateEntityTypeCreationContext;
import org.atemsource.atem.impl.hibernate.PropertyDescriptor;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.Type;


public class SingleAssociationAttributeType extends AttributeType
{

	@Resource
	private BeanCreator beanCreator;

	@Override
	public boolean canCreate(final PropertyDescriptor propertyDescriptor, final Type attributeType,
		final SessionFactoryImplementor sessionFactoryImplementor)
	{
		return attributeType instanceof ManyToOneType || attributeType instanceof OneToOneType;
	}

	@Override
	public Attribute create(final ValidationMetaData validationMetaData, final EntityType entityType,
		final HibernateEntityTypeCreationContext ctx, final PropertyDescriptor propertyDescriptor,
		final Type attributeType, final SessionFactoryImplementor sessionFactoryImplementor)
	{
		org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute attribute =
			beanCreator.create(org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute.class);
		String associatedEntityName;
		if (attributeType instanceof ManyToOneType)
		{
			associatedEntityName = ((ManyToOneType) attributeType).getAssociatedEntityName();
		}
		else if (attributeType instanceof OneToOneType)
		{
			associatedEntityName = ((OneToOneType) attributeType).getAssociatedEntityName();
		}
		else
		{
			throw new IllegalStateException("unknown single association type " + attributeType.getClass());
		}

		EntityType associatedEntityType = ctx.getEntityTypeReference(associatedEntityName);
		Association association = propertyDescriptor.getAnnotation(Association.class);
		Class[] includedTypes = null;
		Class[] excludedTypes = null;

		Set<EntityType> validTypesSet = createValidtypesSet(associatedEntityType, includedTypes, excludedTypes, ctx);
		attribute.setTargetType(associatedEntityType);
		attribute.setAccessor(new HibernateAccessor(propertyDescriptor.getField(), propertyDescriptor.getReadMethod(),
			propertyDescriptor.getWriteMethod()));

		setStandardProperties(entityType, propertyDescriptor, attribute);
		// if (manyToOneType.isNullable()) {
		//
		// }
		if (!attribute.isRequired())
		{
			// attribute.setRequired(validationMetaData.isRequired(attribute));
		}
		return attribute;
	}

}
