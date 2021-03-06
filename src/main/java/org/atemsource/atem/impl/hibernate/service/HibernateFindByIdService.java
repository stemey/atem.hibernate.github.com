/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.hibernate.service;

import java.io.Serializable;

import javax.annotation.Resource;

import org.atemsource.atem.api.service.FindByTypedIdService;
import org.atemsource.atem.api.type.EntityType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Scope("prototype")
@Component
public class HibernateFindByIdService implements FindByTypedIdService
{
	private EntityType entityType;

	@Resource
	private HibernateDao hibernateDao;

	@Override
	public Object findByTypedId(final EntityType<?> entityType, Serializable id)
	{
		return hibernateDao.findById(entityType.getEntityClass(), id);
	}

}
