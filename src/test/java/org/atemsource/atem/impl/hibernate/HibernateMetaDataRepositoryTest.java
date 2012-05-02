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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.attribute.primitive.BooleanType;
import org.atemsource.atem.api.attribute.primitive.DateType;
import org.atemsource.atem.api.attribute.primitive.DoubleType;
import org.atemsource.atem.api.attribute.primitive.FloatType;
import org.atemsource.atem.api.attribute.primitive.IntegerType;
import org.atemsource.atem.api.attribute.primitive.LongType;
import org.atemsource.atem.api.attribute.primitive.DateType.Precision;
import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
import org.atemsource.atem.api.attribute.relation.SetAssociationAttribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/hibernate.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateMetaDataRepositoryTest
{
	@Resource
	private EntityTypeRepository entityTypeRepository;

	@Test
	public void testBooleanProperty()
	{
		testProperty(EntityA.class, "booleanProperty", BooleanType.class, boolean.class);
	}

	@Test
	public void testDateProperty()
	{
		SingleAttribute attribute = testProperty(EntityA.class, "dateProperty", DateType.class, Date.class);
		Precision precision = ((DateType) attribute.getTargetType()).getPrecision();
		Assert.assertEquals(Precision.DATE, precision);
	}

	@Test
	public void testDatetimeProperty()
	{
		SingleAttribute attribute = testProperty(EntityA.class, "timestampProperty", DateType.class, Calendar.class);
		Precision precision = ((DateType) attribute.getTargetType()).getPrecision();
		Assert.assertEquals(Precision.DATETIME, precision);
	}

	@Test
	public void testDoubleProperty()
	{
		testProperty(EntityA.class, "doubleProperty", DoubleType.class, double.class);
	}

	@Test
	public void testFloatProperty()
	{
		testProperty(EntityA.class, "wfloatProperty", FloatType.class, Float.class);
	}

	@Test
	public void testGetEntityTypeByClass()
	{
		Assert.assertEquals(EntityA.class, entityTypeRepository.getEntityType(EntityA.class).getJavaType());
	}

	@Test
	public void testGetEntityTypeByName()
	{
		Assert.assertEquals(EntityA.class, entityTypeRepository.getEntityType(EntityA.class.getName()).getJavaType());
	}

	@Test
	public void testIntProperty()
	{
		testProperty(EntityA.class, "intProperty", IntegerType.class, int.class);
	}

	@Test
	public void testListAssociation()
	{
		final EntityType<EntityB> entityType = entityTypeRepository.getEntityType(EntityB.class);
		final ListAssociationAttribute<EntityA> attribute =
			(ListAssociationAttribute<EntityA>) entityType.getAttribute("listProperty");
		Assert.assertNotNull(attribute);
		EntityB entity = entityType.createEntity();
		EntityType<EntityA> targetType = (EntityType<EntityA>) attribute.getTargetType();
		EntityA associatedEntity = targetType.createEntity();
		attribute.setValue(entity, new ArrayList<EntityA>());
		attribute.addElement(entity, associatedEntity);
		Object associatedEntity2 = attribute.getValue(entity).get(0);
		Assert.assertEquals(associatedEntity, associatedEntity2);
	}

	@Test
	public void testLongProperty()
	{
		testProperty(EntityA.class, "longProperty", LongType.class, long.class);
	}

	@Test
	public void testMapAssociation()
	{
		final EntityType<?> entityType = entityTypeRepository.getEntityType(EntityB.class);
		final MapAttribute<String, EntityA, Map> attribute =
			(MapAttribute<String, EntityA, Map>) entityType.getAttribute("map");
		Assert.assertNotNull(attribute);
		EntityB entity = (EntityB) entityType.createEntity();
		EntityA associatedEntity = (EntityA) ((EntityType<EntityA>) attribute.getTargetType()).createEntity();
		attribute.setValue(entity, new HashMap<String, EntityA>());
		attribute.putElement(entity, "key1", associatedEntity);
		Object associatedEntity2 = attribute.getValue(entity).get("key1");
		Assert.assertEquals(associatedEntity, associatedEntity2);
	}

	public SingleAttribute testProperty(Class entityClass, String property, Class<? extends Type<?>> expectedType,
		Class expectedJavaType)
	{
		final EntityType entityType = entityTypeRepository.getEntityType(entityClass);
		SingleAttribute attribute = (SingleAttribute) entityType.getAttribute(property);
		Assert.assertTrue(expectedType.isAssignableFrom(attribute.getTargetType().getClass()));
		Assert.assertEquals(expectedJavaType, attribute.getTargetType().getJavaType());
		return attribute;
	}

	@Test
	public void testPropertyCount()
	{
		final EntityType entityType = entityTypeRepository.getEntityType(EntityB.class);

		Assert.assertEquals(4, entityType.getAttributes().size());
	}

	@Test
	public void testSetAssociation()
	{
		final EntityType<EntityB> entityType = entityTypeRepository.getEntityType(EntityB.class);
		final SetAssociationAttribute<EntityA> attribute =
			(SetAssociationAttribute<EntityA>) entityType.getAttribute("setProperty");
		Assert.assertNotNull(attribute);
		Object entity = entityType.createEntity();
		EntityType<EntityA> targetType = (EntityType<EntityA>) attribute.getTargetType();
		EntityA associatedEntity = targetType.createEntity();
		attribute.setValue(entity, new HashSet<Object>());
		attribute.addElement(entity, associatedEntity);
		Object associatedEntity2 = attribute.getValue(entity).iterator().next();
		Assert.assertEquals(associatedEntity, associatedEntity2);
	}

	@Test
	public void testSingleAssociation()
	{
		final EntityType<EntityB> entityType = entityTypeRepository.getEntityType(EntityB.class);
		final SingleAttribute<EntityA> attribute =
			(SingleAttribute<EntityA>) entityType.getAttribute("singleAssociation");
		Assert.assertNotNull(attribute);
		EntityB entity = entityType.createEntity();
		EntityType<EntityA> targetType = (EntityType<EntityA>) attribute.getTargetType();
		EntityA associatedEntity = targetType.createEntity();
		attribute.setValue(entity, associatedEntity);
		Object associatedEntity2 = attribute.getValue(entity);
		Assert.assertEquals(associatedEntity, associatedEntity2);
	}

	@Test
	public void testSubClass()
	{
		final EntityType entityType = entityTypeRepository.getEntityType(EntityB.class);
		Set subEntityTypes = entityType.getSubEntityTypes(false);
		Assert.assertEquals(1, subEntityTypes.size());
	}

	@Test
	public void testSubClassPropertyCount()
	{
		final EntityType<?> entityType = entityTypeRepository.getEntityType(EntityB.class);
		Set<EntityType> subEntityTypes = entityType.getSubEntityTypes(false);
		Assert.assertEquals(1, subEntityTypes.iterator().next().getAttributes().size());
	}

	@Test
	public void testSuperType()
	{
		final EntityType entityType = entityTypeRepository.getEntityType(EntityB.class);
		Attribute attribute = entityType.getAttribute("superProperty");
		EntityType superEntityType = entityType.getSuperEntityType();
		Assert.assertNotNull(superEntityType);
	}

	@Test
	public void testSuperTypePropertyCount()
	{
		final EntityType entityType = entityTypeRepository.getEntityType(SuperEntity.class);

		Assert.assertEquals(1, entityType.getAttributes().size());
	}

	@Test
	public void testTimeProperty()
	{
		SingleAttribute attribute = testProperty(EntityA.class, "timeProperty", DateType.class, Date.class);
		Precision precision = ((DateType) attribute.getTargetType()).getPrecision();
		Assert.assertEquals(Precision.TIME, precision);
	}

	@Test
	public void testwBooleanProperty()
	{
		testProperty(EntityA.class, "wbooleanProperty", BooleanType.class, Boolean.class);
	}

	@Test
	public void testwDoubleProperty()
	{
		testProperty(EntityA.class, "wdoubleProperty", DoubleType.class, Double.class);
	}

	@Test
	public void testwFloatProperty()
	{
		testProperty(EntityA.class, "wfloatProperty", FloatType.class, Float.class);
	}

	@Test
	public void testwIntProperty()
	{
		testProperty(EntityA.class, "wintProperty", IntegerType.class, Integer.class);
	}

	@Test
	public void testwLongProperty()
	{
		testProperty(EntityA.class, "wLongProperty", LongType.class, Long.class);
	}

}
