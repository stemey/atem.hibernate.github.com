/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.ValidationMetaData;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.MetaLogs;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.impl.hibernate.attributetype.AttributeType;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.atemsource.atem.spi.EntityTypeSubrepository;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;


public class HibernateMetaDataRepository extends AbstractMetaDataRepository<Object> implements
	EntityTypeSubrepository<Object>
{

	private List<org.atemsource.atem.impl.hibernate.attributetype.AttributeType> attributeTypes;

	@Autowired
	private BeanCreator beanCreator;

	private boolean failIfPropertyNotMappable = true;

	private boolean fieldWasNotMapped;

	private Set<String> initializedMappedSuperClass;

	private SessionFactoryImplementor sessionFactory;

	private ValidationMetaData validationMetaData;

	@Override
	public void afterFirstInitialization(EntityTypeRepository entityTypeRepositoryImpl)
	{
	}

	@Override
	public void afterInitialization()
	{
	}

	private Set<Attribute> createAttributes(ClassMetadata classMetadata, AbstractEntityType entityType)
	{
		Set<Attribute> attributes = new HashSet<Attribute>();
		for (String propertyName : classMetadata.getPropertyNames())
		{
			Type type = classMetadata.getPropertyType(propertyName);
			PropertyDescriptor propertyDescriptor =
				PropertyDescriptor.createInstance(entityType.getJavaType(), propertyName);
			if (propertyDescriptor == null)
			{
				MetaLogs.LOG.warn("attribute " + classMetadata.getEntityName() + "." + propertyName + " of type "
					+ type.getReturnedClass().getName() + " cannot be mapped");
				fieldWasNotMapped = true;
				fieldWasNotMapped = true;
				continue;
			}
			if (propertyDescriptor.getField() != null)
			{
				Attribute attribute = null;
				for (AttributeType attributeType : attributeTypes)
				{
					if (attributeType.canCreate(propertyDescriptor, type, sessionFactory))
					{
						attribute =
							attributeType.create(validationMetaData, entityType, getEntityTypeCreationContext(),
								propertyDescriptor, type, sessionFactory);
						if (propertyDescriptor.getDeclaringClass() != entityType.getJavaType())
						{
							attribute = null;
						}
						MetaLogs.LOG.debug("added property " + attribute.getCode() + " to " + entityType.getCode() + "");
						break;
					}
				}
				if (attribute == null)
				{
					MetaLogs.LOG.warn("attribute " + classMetadata.getEntityName() + "." + propertyName + " of type "
						+ type.getReturnedClass().getName() + " cannot be mapped");
					fieldWasNotMapped = true;
				}
				else
				{
					attributes.add(attribute);
				}
			}

		}
		return attributes;
	}

	public HibernateEntityType createComponentType(ComponentType type)
	{

		HibernateEntityType entityType = (HibernateEntityType) nameToEntityTypes.get(type.getName());
		if (entityType != null)
		{
			return entityType;
		}
		entityType = new HibernateComponentType();
		entityType.setEntityClass(type.getReturnedClass());
		entityType.setCode(type.getName());

		nameToEntityTypes.put(type.getName(), entityType);

		// TODO use subtypes

		MetaLogs.LOG.debug("Initializing properties of " + type.getName() + "");
		Type[] propertyTypes = type.getSubtypes();// (Type[]) ReflectionUtils.getField(type, "propertyTypes");
		int index = 0;
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (String propertyName : type.getPropertyNames())
		{
			Type propertyType = propertyTypes[index];
			index++;
			PropertyDescriptor propertyDescriptor =
				PropertyDescriptor.createInstance(type.getReturnedClass(), propertyName);
			if (propertyDescriptor == null)
			{
				continue;
			}
			if (propertyDescriptor.getReadMethod() != null)
			{
				for (AttributeType attributeType : attributeTypes)
				{
					if (attributeType.canCreate(propertyDescriptor, propertyType, sessionFactory))
					{
						Attribute attribute =
							attributeType.create(validationMetaData, entityType, getEntityTypeCreationContext(),
								propertyDescriptor, propertyType, sessionFactory);
						attributes.add(attribute);
						MetaLogs.LOG.debug("added property " + attribute.getCode() + " to " + propertyType.getName() + "");
						break;
					}
				}
			}

		}

		entityType.setAttributes(attributes);
		attacheServicesToEntityType(entityType);

		entityTypes.add(entityType);
		classToEntityTypes.put(entityType.getEntityClass(), entityType);

		return entityType;
	}

	private HibernateEntityType createEntityType(final ClassMetadata classMetadata)
	{
		Type identifierType = classMetadata.getIdentifierType();
		HibernateEntityType entityType = null;
		entityType = beanCreator.create(HibernateEntityType.class);
		entityType.setPrimaryKeyNullable(!identifierType.getReturnedClass().isPrimitive());
		entityType.setEntityClass(classMetadata.getMappedClass(EntityMode.POJO));
		attacheServicesToEntityType(entityType);
		return entityType;
	}

	private EntityType createSuperEntityType(Class superClass)
	{
		if (superClass == Object.class)
		{
			return null;
		}
		String entityName = superClass.getName();
		AbstractEntityType<Object> abstractEntityType = nameToEntityTypes.get(entityName);
		if (abstractEntityType != null)
		{
			return abstractEntityType;
		}
		HibernateSuperEntityType superEntityType = beanCreator.create(HibernateSuperEntityType.class);
		superEntityType.setEntityClass(superClass);
		superEntityType.setCode(superClass.getName());
		nameToEntityTypes.put(superEntityType.getCode(), superEntityType);
		EntityType superSuperEntityType = createSuperEntityType(superClass.getSuperclass());
		if (superSuperEntityType != null)
		{
			initializeEntityTypeHierachy(superEntityType, superSuperEntityType.getCode());
		}
		return superEntityType;
	}

	@Override
	public EntityType<Object> getEntityType(Object entity)
	{
		return getEntityType(entity.getClass());
	}

	private HibernateEntityTypeCreationContext getEntityTypeCreationContext()
	{
		return (HibernateEntityTypeCreationContext) entityTypeCreationContext;
	}

	public void init()
	{

		Map classMetadataMap = sessionFactory.getAllClassMetadata();
		// initialize entity types.
		for (Object entry : classMetadataMap.entrySet())
		{
			String entityName = (String) ((Map.Entry) entry).getKey();
			ClassMetadata classMetadata = (ClassMetadata) ((Map.Entry) entry).getValue();
			if (entityTypeCreationContext.hasEntityTypeReference(classMetadata.getMappedClass(EntityMode.POJO)))
			{
				continue;
			}
			HibernateEntityType entityType = createEntityType(classMetadata);
			entityType.setEntityClass(classMetadata.getMappedClass(EntityMode.POJO));
			entityType.setCode(entityName);
			nameToEntityTypes.put(entityName, entityType);
			entityType.setClassMetadata(classMetadata);
		}

		// initialize type hierachy.
		for (Object entry : classMetadataMap.entrySet())
		{
			ClassMetadata classMetadata = (ClassMetadata) ((Map.Entry) entry).getValue();
			if (entityTypeCreationContext.hasEntityTypeReference(classMetadata.getMappedClass(EntityMode.POJO)))
			{
				continue;
			}
			String entityName = (String) ((Map.Entry) entry).getKey();
			AbstractEntityType entityType = nameToEntityTypes.get(entityName);
			Class mappedClass = classMetadata.getMappedClass(EntityMode.POJO);
			Class superClass = mappedClass.getSuperclass();
			ClassMetadata superClassMetadata = sessionFactory.getClassMetadata(superClass);
			if (superClassMetadata != null)
			{
				String superTypeCode = superClassMetadata.getEntityName();
				initializeEntityTypeHierachy(entityType, superTypeCode);
			}
			else
			{
				EntityType superEntityType = createSuperEntityType(superClass);
				if (superEntityType != null)
				{
					initializeEntityTypeHierachy(entityType, superEntityType.getCode());
				}
			}
		}

		// initialize mapped superclasses.
		initializedMappedSuperClass = new HashSet<String>();
		for (Object entry : classMetadataMap.entrySet())
		{
			String entityName = (String) ((Map.Entry) entry).getKey();
			ClassMetadata classMetadata = (ClassMetadata) ((Map.Entry) entry).getValue();
			if (entityTypeCreationContext.hasEntityTypeReference(classMetadata.getMappedClass(EntityMode.POJO)))
			{
				continue;
			}
			AbstractEntityType entityType = nameToEntityTypes.get(entityName);
			MetaLogs.LOG.debug("Initializing properties of " + entityName + "");
			Set<Attribute> attributes = createAttributes(classMetadata, entityType);
			initializeHibernateSuperEntityType(entityType, classMetadata);
			entityType.setAttributes(new ArrayList(attributes));
		}

		// intialize lookups
		initializeLookups();
		if (failIfPropertyNotMappable && fieldWasNotMapped)
		{
			throw new IllegalStateException("some fields were not mapped");
		}
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext)
	{
		this.entityTypeCreationContext = new HibernateEntityTypeCreationContext(entityTypeCreationContext, this);
		init();
	}

	private void initializeHibernateSuperEntityType(EntityType entityType, ClassMetadata classMetadata)
	{
		if (entityType.getSuperEntityType() == null)
		{
			return;
		}
		AbstractEntityType superEntityType = (AbstractEntityType) entityType.getSuperEntityType();
		if (superEntityType instanceof HibernateSuperEntityType)
		{
			if (!initializedMappedSuperClass.contains(superEntityType.getCode()))
			{
				HibernateSuperEntityType hibernateSuperEntityType = ((HibernateSuperEntityType) superEntityType);
				Set<Attribute> attributes = createAttributes(classMetadata, hibernateSuperEntityType);
				hibernateSuperEntityType.setAttributes(new ArrayList(attributes));
				initializedMappedSuperClass.add(hibernateSuperEntityType.getCode());
				initializeHibernateSuperEntityType(hibernateSuperEntityType, classMetadata);
			}
		}

	}

	public boolean isFailIfPropertyNotMappable()
	{
		return failIfPropertyNotMappable;
	}

	public void setAttributeTypes(List<org.atemsource.atem.impl.hibernate.attributetype.AttributeType> attributeTypes)
	{
		this.attributeTypes = attributeTypes;
	}

	public void setFailIfPropertyNotMappable(boolean failIfPropertyNotMappable)
	{
		this.failIfPropertyNotMappable = failIfPropertyNotMappable;
	}

	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		this.sessionFactory = (SessionFactoryImplementor) sessionFactory;
	}

	public void setValidationMetaData(final ValidationMetaData validationMetaData)
	{
		this.validationMetaData = validationMetaData;
	}

}
