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


import javax.annotation.Resource;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.springframework.orm.jpa.support.JpaDaoSupport;


public class JpaEntityService extends JpaDaoSupport
{

	@Resource
	private EntityTypeRepository entityTypeRepository;

	public <J> EntityType<J> getEntityType(J entity)
	{
		return entityTypeRepository.getEntityType(getTypeCode(entity));
	}

	public <J> Type<J> getType(J entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeCode(final Object entity)
	{
		EntityType entityType = entityTypeRepository.getEntityType(entity.getClass());
		if (entityType == null && entity.getClass().getSuperclass() != null)
			entityType = entityTypeRepository.getEntityType(entity.getClass().getSuperclass());

		return entityType == null ? null : entityType.getCode();
	}

}
