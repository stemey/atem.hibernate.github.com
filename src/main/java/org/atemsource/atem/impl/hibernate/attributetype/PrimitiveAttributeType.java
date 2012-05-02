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


import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.ValidationMetaData;
import org.atemsource.atem.api.attribute.primitive.PrimitiveType;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.attribute.PrimitiveAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.atemsource.atem.impl.hibernate.HibernateAccessor;
import org.atemsource.atem.impl.hibernate.HibernateEntityTypeCreationContext;
import org.atemsource.atem.impl.hibernate.PropertyDescriptor;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;


public class PrimitiveAttributeType extends AttributeType
{

	@Autowired
	private PrimitiveTypeFactory primitiveTypeFactory;

	@Override
	public boolean canCreate(final PropertyDescriptor propertyDescriptor, final Type attributeType,
		final SessionFactoryImplementor sessionFactoryImplementor)
	{
		PrimitiveType primitiveType = primitiveTypeFactory.getPrimitiveType(propertyDescriptor.getPropertyType());
		return primitiveType != null;
	}

	@Override
	public Attribute create(final ValidationMetaData validationMetaData, final EntityType entityType,
		final HibernateEntityTypeCreationContext ctx, final PropertyDescriptor propertyDescriptor,
		final Type attributeType, final SessionFactoryImplementor sessionFactoryImplementor)
	{
		PrimitiveType primitiveType = primitiveTypeFactory.getPrimitiveType(propertyDescriptor.getPropertyType());
		PrimitiveAttributeImpl<?> attribute = new PrimitiveAttributeImpl();
		attribute.setAccessor(new HibernateAccessor(propertyDescriptor.getField(), propertyDescriptor.getReadMethod(),
			propertyDescriptor.getWriteMethod()));
		setStandardProperties(entityType, propertyDescriptor, attribute);
		attribute.setTargetType(primitiveType);
		attribute.setRequired(propertyDescriptor.getPropertyType().isPrimitive());
		return attribute;
	}

}
