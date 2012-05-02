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

import org.atemsource.atem.api.type.TypedId;


public abstract class AbstractTypedId implements TypedId
{

	private Class entityClass;

	private String code;

	protected AbstractTypedId(final String code, final Class entityClass)
	{
		super();
		this.entityClass = entityClass;
		this.code = code;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof TypedId)
		{
			TypedId other = (TypedId) obj;
			if (other.getEntityId() != null && other.getEntityId().equals(getEntityId()))
			{
				return other.getTypeCode().equals(code);
			}
		}
		return false;
	}

	public Class<Object> getEntityClass()
	{
		return entityClass;
	}

	public String getTypeAsString()
	{
		return entityClass.getName();
	}

	public String getTypeCode()
	{
		return code;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((getEntityId() == null) ? 0 : getEntityId().hashCode());
		return result;
	}

}
