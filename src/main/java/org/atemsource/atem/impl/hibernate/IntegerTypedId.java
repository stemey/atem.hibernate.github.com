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


public class IntegerTypedId extends AbstractTypedId implements TypedId
{

	private Integer id;

	public IntegerTypedId(final String code, final Class entityClass)
	{
		super(code, entityClass);

	}

	public IntegerTypedId(final String code, final Class entityClass, final int id)
	{
		super(code, entityClass);
		this.id = id;

	}

	public Serializable getEntityId()
	{
		return id;
	}

	public String getIdAsString()
	{
		return String.valueOf(id);
	}

	public Object getPrimaryKey()
	{
		return id;
	}

	public boolean isPersistent()
	{
		// FIXME this is bullshit!
		return id != 0;
	}

}
