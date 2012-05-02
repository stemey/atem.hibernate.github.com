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


import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.ValidationMetaData;
import org.atemsource.atem.api.attribute.primitive.PrimitiveType;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.attribute.AbstractAttribute;
import org.atemsource.atem.impl.common.attribute.collection.AbstractCollectionAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.CollectionAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.ListAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.SetAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.atemsource.atem.impl.hibernate.HibernateEntityTypeCreationContext;
import org.atemsource.atem.impl.hibernate.PropertyDescriptor;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.impl.pojo.attribute.PojoAccessor;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.BagType;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ListType;
import org.hibernate.type.SetType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;


public class PrimitiveCollectionAttributeType extends AttributeType
{

	@Autowired
	private BeanCreator beanCreator;

	@Autowired
	private PrimitiveTypeFactory primitiveTypeFactory;

	@Override
	public boolean canCreate(PropertyDescriptor propertyDescriptor, Type attributeType,
		SessionFactoryImplementor sessionFactoryImplementor)
	{
		if (attributeType instanceof ListType)
		{
			ListType listType = (ListType) attributeType;
			Type elementType = listType.getElementType(sessionFactoryImplementor);
			return !elementType.isEntityType();
		}
		else if (attributeType instanceof BagType)
		{
			BagType listType = (BagType) attributeType;
			Type elementType = listType.getElementType(sessionFactoryImplementor);
			return !elementType.isEntityType();
		}
		else if (attributeType instanceof SetType)
		{
			SetType listType = (SetType) attributeType;
			Type elementType = listType.getElementType(sessionFactoryImplementor);
			return !elementType.isEntityType();
		}
		else
		{
			return false;
		}
	}

	@Override
	public Attribute create(ValidationMetaData validationMetaData, EntityType entityType,
		HibernateEntityTypeCreationContext ctx, PropertyDescriptor propertyDescriptor, Type attributeType,
		SessionFactoryImplementor sessionFactoryImplementor)
	{
		CollectionType collectionType = (CollectionType) attributeType;
		Class<?> propertyType = propertyDescriptor.getPropertyType();
		AbstractAttribute attribute;

		if (List.class.isAssignableFrom(propertyType))
		{
			attribute = beanCreator.create(ListAttributeImpl.class);
		}
		else if (Set.class.isAssignableFrom(propertyType))
		{
			attribute = beanCreator.create(SetAttributeImpl.class);
		}
		else if (Collection.class.isAssignableFrom(propertyType))
		{
			attribute = beanCreator.create(CollectionAttributeImpl.class);
		}
		else
		{
			throw new IllegalStateException("properyType " + propertyType.getName()
				+ " cannot be used for multi associations");
		}
		Type elementType = collectionType.getElementType(sessionFactoryImplementor);
		PrimitiveType primitiveType = primitiveTypeFactory.getPrimitiveType(elementType.getReturnedClass());
		((AbstractCollectionAttributeImpl) attribute).setTargetType(primitiveType);
		setStandardProperties(entityType, propertyDescriptor, attribute);
		((AbstractCollectionAttributeImpl) attribute).setAccessor(new PojoAccessor(propertyDescriptor.getField(),
			propertyDescriptor.getReadMethod(), propertyDescriptor.getWriteMethod(), false));

		return attribute;
	}

}
