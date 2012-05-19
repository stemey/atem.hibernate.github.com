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


import javax.persistence.MapKey;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.ValidationMetaData;
import org.atemsource.atem.api.attribute.annotation.Association;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.attribute.MapAttributeImpl;
import org.atemsource.atem.impl.hibernate.HibernateAccessor;
import org.atemsource.atem.impl.hibernate.HibernateEntityTypeCreationContext;
import org.atemsource.atem.impl.hibernate.PropertyDescriptor;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.spi.EntityTypeRepositoryListener;
import org.atemsource.atem.spi.Phase;
import org.atemsource.atem.spi.PhaseEvent;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.MapType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;


public class MapAssociationAttributeFactory extends AttributeType
{
	@Autowired
	private BeanCreator beanCreator;

	@Override
	public boolean canCreate(PropertyDescriptor propertyDescriptor, Type attributeType,
		SessionFactoryImplementor sessionFactoryImplementor)
	{
		// TODO also check that key attribute is primitive
		return attributeType instanceof MapType && propertyDescriptor.getAnnotation(MapKey.class) != null;
	}

	@Override
	public Attribute create(ValidationMetaData validationMetaData, EntityType entityType,
		HibernateEntityTypeCreationContext ctx, PropertyDescriptor propertyDescriptor, Type attributeType,
		SessionFactoryImplementor sessionFactoryImplementor)
	{
		MapType mapType = (MapType) attributeType;
		Association association = propertyDescriptor.getAnnotation(Association.class);
		final MapAttributeImpl mapAssociationAttributeImpl = beanCreator.create(MapAttributeImpl.class);
		mapAssociationAttributeImpl.setAccessor(new HibernateAccessor(propertyDescriptor.getField(), propertyDescriptor
			.getReadMethod(), propertyDescriptor.getWriteMethod()));
		mapAssociationAttributeImpl.setCode(propertyDescriptor.getName());
		mapAssociationAttributeImpl.setEntityType(entityType);

		final String targeTypeName = mapType.getElementType(sessionFactoryImplementor).getName();
		final EntityType targetType = ctx.getEntityTypeReference(targeTypeName);
		mapAssociationAttributeImpl.setTargetType(targetType);
		MapKey jpaMapKey = propertyDescriptor.getAnnotation(MapKey.class);
		final String mapKeyPropertyName = jpaMapKey.name();

		ctx.addListener(new EntityTypeRepositoryListener()
		{
			@Override
			public void onEvent(PhaseEvent event)
			{
				if (event.getPhase() == Phase.ENTITY_TYPES_INITIALIZED)
				{
					final Attribute keyAttribute = targetType.getAttribute(mapKeyPropertyName);
					mapAssociationAttributeImpl.setKeyType(keyAttribute.getTargetType());

				}
			}
		});
		// TODO add subtypes here
		mapAssociationAttributeImpl.setWriteable(true);
		return mapAssociationAttributeImpl;
	}
}
