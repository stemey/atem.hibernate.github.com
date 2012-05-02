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
package org.atemsource.atem.impl.jpa;


import java.io.Serializable;

import javax.annotation.Resource;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.TypedId;


public class JpaPersistenceService extends JpaEntityService
{

	@Resource
	private EntityTypeRepository entityTypeRepository;

	public Object getEntity(final TypedId typedId)
	{
		return getJpaTemplate().find(typedId.getEntityClass(), typedId.getEntityId());
	}

	public Serializable getId(final Object entity)
	{

		EntityType<Object> entityType = getEntityType(entity);
		if (entityType == null)
		{
			return null;
		}
		else
		{
			return entityType.getService(IdentityService.class).getId(entityType, entity);
		}
	}

	public void saveInTransaction(final Object entity)
	{
		getJpaTemplate().persist(entity);
	}

}
