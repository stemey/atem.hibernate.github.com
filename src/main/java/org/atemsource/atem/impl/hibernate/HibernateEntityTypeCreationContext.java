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
import java.util.List;

import org.atemsource.atem.api.EntityTypeCreationContext;
import org.atemsource.atem.api.EntityTypeRepositoryListener;
import org.atemsource.atem.api.Phase;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.hibernate.type.ComponentType;


public class HibernateEntityTypeCreationContext implements EntityTypeCreationContext
{
	private HibernateMetaDataRepository repository;

	private EntityTypeCreationContext wrapped;

	private List<EntityTypeRepositoryListener> listeners = new ArrayList<EntityTypeRepositoryListener>();

	public HibernateEntityTypeCreationContext(EntityTypeCreationContext wrapped, HibernateMetaDataRepository repository)
	{
		super();
		this.wrapped = wrapped;
		this.repository = repository;
	}

	@Override
	public void addIncomingAssociation(EntityType entityType, Attribute<?, ?> incomingRelation)
	{
		wrapped.addIncomingAssociation(entityType, incomingRelation);
	}

	@Override
	public void addListener(EntityTypeRepositoryListener entityTypeRepositoryListener)
	{
		wrapped.addListener(entityTypeRepositoryListener);
	}

	public HibernateEntityType createComponentType(ComponentType type)
	{
		return repository.createComponentType(type);
	}

	@Override
	public EntityType<?> getEntityTypeReference(Class<?> propertyType)
	{
		return wrapped.getEntityTypeReference(propertyType);
	}

	@Override
	public EntityType<?> getEntityTypeReference(String entityName)
	{
		return wrapped.getEntityTypeReference(entityName);
	}

	@Override
	public Type<?> getTypeReference(Class<?> propertyType)
	{
		return wrapped.getTypeReference(propertyType);
	}

	@Override
	public boolean hasEntityTypeReference(Class mappedClass)
	{
		return wrapped.hasEntityTypeReference(mappedClass);
	}

	@Override
	public void lazilyInitialized(EntityType entityType)
	{
		wrapped.lazilyInitialized(entityType);
	}

	@Override
	public void onPhase(Phase phase)
	{
		wrapped.onPhase(phase);
	}

}
