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


import java.io.Serializable;

import org.atemsource.atem.api.type.TypedId;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class LongEntityType extends HibernateEntityType
{

	public TypedId getTypedIdById(final Serializable id)
	{
		return new IntegerTypedId(getCode(), getClass());
	}

	@Override
	public boolean isAssignableFrom(Object entity)
	{
		return getJavaType().isInstance(entity);
	}

}