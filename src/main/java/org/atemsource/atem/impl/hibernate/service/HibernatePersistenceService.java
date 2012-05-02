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
package org.atemsource.atem.impl.hibernate.service;


import java.io.Serializable;

import javax.annotation.Resource;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.service.DeletionService;
import org.atemsource.atem.api.service.PersistenceService;
import org.atemsource.atem.api.type.TypedId;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class HibernatePersistenceService extends HibernateDaoSupport implements PersistenceService, DeletionService
{

	@Resource
	private EntityTypeRepository entityTypeRepository;

	@Resource
	private HibernateDao hibernateDao;

	@Override
	public void delete(Object entity)
	{
		hibernateDao.deleteInTransaction(entity);
	}

	public Object getEntity(final TypedId typedId)
	{
		return getSession(true).get(typedId.getEntityClass(), typedId.getEntityId());
	}

	public Serializable getId(final Object entity)
	{
		return getSession(true).getIdentifier(entity);
	}

	public String getTypeCode(final Object entity)
	{
		if (getSession(true).contains(entity))
		{
			if (entity instanceof HibernateProxy)
			{
				// hibernate will load entity if getEntityName is called on proxy
				Object unproxied = ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
				return getSession(true).getEntityName(unproxied);
			}
			else
			{
				return getSession(true).getEntityName(entity);
			}
		}
		else
		{
			return entityTypeRepository.getEntityType(entity.getClass()).getCode();

		}
	}

	@Override
	public void insert(Object entity)
	{
		hibernateDao.saveInTransaction(entity);
	}

	@Override
	public boolean isPersistent(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
