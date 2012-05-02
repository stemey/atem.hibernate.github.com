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

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;


@Entity
public class EntityB extends SuperEntity
{
	@Id
	private int id;

	@ManyToOne(optional = true)
	private EntityA singleAssociation;

	@OneToMany(targetEntity = EntityA.class)
	private List<EntityA> listProperty;

	@OneToMany(targetEntity = EntityA.class)
	private Set<EntityA> setProperty;

	@OneToMany(targetEntity = EntityA.class)
	@JoinColumn(name = "mapId")
	@MapKey(name = "textPropertyA")
	private Map<String, EntityA> map;

	public List<EntityA> getListProperty()
	{
		return listProperty;
	}

	public Map<String, EntityA> getMap()
	{
		return map;
	}

	public Set<EntityA> getSetProperty()
	{
		return setProperty;
	}

	public EntityA getSingleAssociation()
	{
		return singleAssociation;
	}

	public void setListProperty(List<EntityA> listProperty)
	{
		this.listProperty = listProperty;
	}

	public void setMap(Map<String, EntityA> map)
	{
		this.map = map;
	}

	public void setSetProperty(Set<EntityA> setProperty)
	{
		this.setProperty = setProperty;
	}

	public void setSingleAssociation(EntityA singleAssociation)
	{
		this.singleAssociation = singleAssociation;
	}

}
