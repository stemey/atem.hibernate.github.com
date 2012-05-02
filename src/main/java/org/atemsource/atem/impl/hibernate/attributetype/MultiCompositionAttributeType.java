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
import org.atemsource.atem.impl.common.attribute.collection.AbstractCollectionAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.ListAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.SetAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.atemsource.atem.impl.hibernate.HibernateAccessor;
import org.atemsource.atem.impl.hibernate.HibernateEntityTypeCreationContext;
import org.atemsource.atem.impl.hibernate.PropertyDescriptor;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.BagType;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.ListType;
import org.hibernate.type.SetType;
import org.hibernate.type.Type;


public class MultiCompositionAttributeType extends AttributeType
{

	private static final int ORDER = 10;

	@Resource
	private PrimitiveTypeFactory primitiveTypeFactory;

	@Override
	public boolean canCreate(final PropertyDescriptor propertyDescriptor, final Type attributeType,
		final SessionFactoryImplementor sessionFactoryImplementor)
	{
		if (attributeType instanceof ListType)
		{
			ListType listType = (ListType) attributeType;
			Type elementType = listType.getElementType(sessionFactoryImplementor);
			Association association = propertyDescriptor.getReadMethod().getAnnotation(Association.class);
			return !elementType.isEntityType()
				&& primitiveTypeFactory.getPrimitiveType(elementType.getReturnedClass()) == null;
		}
		else if (attributeType instanceof SetType)
		{
			SetType listType = (SetType) attributeType;
			Type elementType = listType.getElementType(sessionFactoryImplementor);
			Association association = propertyDescriptor.getReadMethod().getAnnotation(Association.class);
			return !elementType.isEntityType()
				&& primitiveTypeFactory.getPrimitiveType(elementType.getReturnedClass()) == null;
		}
		else if (attributeType instanceof BagType)
		{
			BagType listType = (BagType) attributeType;
			Type elementType = listType.getElementType(sessionFactoryImplementor);
			Association association = propertyDescriptor.getReadMethod().getAnnotation(Association.class);
			return !elementType.isEntityType()
				&& primitiveTypeFactory.getPrimitiveType(elementType.getReturnedClass()) == null;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Attribute create(final ValidationMetaData validationMetaData, final EntityType entityType,
		final HibernateEntityTypeCreationContext ctx, final PropertyDescriptor propertyDescriptor,
		final Type attributeType, final SessionFactoryImplementor sessionFactoryImplementor)
	{
		CollectionType listType = (CollectionType) attributeType;
		Type elementType = listType.getElementType(sessionFactoryImplementor);
		EntityType elementEntityType = ctx.createComponentType((ComponentType) elementType);
		Set<EntityType> validTypesSet = createValidtypesSet(elementEntityType, null, null, ctx);

		AbstractCollectionAttributeImpl attribute;
		if (listType instanceof ListType || listType instanceof BagType)
		{
			attribute = create(ListAttributeImpl.class);
		}
		else if (listType instanceof SetType)
		{
			attribute = create(SetAttributeImpl.class);
		}
		else
		{
			throw new IllegalStateException("unknown type " + attributeType.getName());
		}

		setStandardProperties(entityType, propertyDescriptor, attribute);
		attribute.setTargetType(elementEntityType);
		attribute.setAccessor(new HibernateAccessor(propertyDescriptor.getField(), propertyDescriptor.getReadMethod(),
			propertyDescriptor.getWriteMethod()));
		return attribute;
	}

}
