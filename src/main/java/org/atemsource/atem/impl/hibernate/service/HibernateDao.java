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
import java.util.Collections;
import java.util.List;

import org.atemsource.atem.api.TechnicalException;
import org.atemsource.atem.api.service.Result;
import org.atemsource.atem.api.service.Sorting;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class HibernateDao extends HibernateDaoSupport
{

	public void deleteInTransaction(Object entity)
	{
		getSession().delete(entity);

	}

	public List find(String hql)
	{
		return getSession().createQuery(hql).list();
	}

	public Object findById(final Class entityClass, final Serializable primaryKey)
	{
		return getSession().get(entityClass, primaryKey);
	}

	public Result getEntities(final Class entityClass, final int startIndex, final int count, final Sorting sorting)
	{
		return getEntities(entityClass, startIndex, count, sorting, Collections.EMPTY_LIST);
	}

	public Result getEntities(Class entityClass, int startIndex, int count, Sorting sorting,
		List<String> associationPaths)
	{
		try
		{
			Criteria query = getSession().createCriteria(entityClass, "entity");
			if (count > 0)
			{
				query.setMaxResults(count);
			}
			if (startIndex >= 0)
			{
				query.setFirstResult(startIndex);
			}

			for (String associationPath : associationPaths)
			{
				query.setFetchMode(associationPath, FetchMode.SELECT);
			}
			List entities = query.list();
			Integer totalCount = entities.size();
			if (count <= 0)
			{
				totalCount =
					((Number) getSession().createQuery("select count(*) from " + entityClass.getName()).uniqueResult())
						.intValue();
			}
			return new Result(entities, totalCount);
		}
		catch (Exception e)
		{
			throw new TechnicalException("cannot get entities of type " + entityClass.getName(), e);
		}
	}

	public void saveInTransaction(final Object entity)
	{
		// Object o =
		// getSession().get("de.sinnerschrader.tcs.hzm.shop.entity.HistoryEntry",
		// 111687);
		getSession().save(entity);
	}
}
