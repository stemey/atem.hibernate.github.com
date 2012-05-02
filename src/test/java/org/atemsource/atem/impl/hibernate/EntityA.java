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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class EntityA
{
	@Id
	private int id;

	@Embedded
	private ComposedEntity singleComposition;

	private String textPropertyA;

	private String textPropertyB;

	private boolean booleanProperty;

	private Boolean wbooleanProperty;

	private int intProperty;

	private Integer wintProperty;

	@Temporal(TemporalType.DATE)
	private Date dateProperty;

	@Temporal(TemporalType.TIME)
	private Date timeProperty;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar timestampProperty;

	private double doubleProperty;

	private Double wdoubleProperty;

	private Float wfloatProperty;

	private float floatProperty;

	private BigDecimal bigDecimalProperty;

	private long longProperty;

	private Long wLongProperty;

	public BigDecimal getBigDecimalProperty()
	{
		return bigDecimalProperty;
	}

	public Date getDateProperty()
	{
		return dateProperty;
	}

	public double getDoubleProperty()
	{
		return doubleProperty;
	}

	public float getFloatProperty()
	{
		return floatProperty;
	}

	public int getId()
	{
		return id;
	}

	public int getIntProperty()
	{
		return intProperty;
	}

	public long getLongProperty()
	{
		return longProperty;
	}

	public ComposedEntity getSingleComposition()
	{
		return singleComposition;
	}

	public String getTextPropertyA()
	{
		return textPropertyA;
	}

	public String getTextPropertyB()
	{
		return textPropertyB;
	}

	public Date getTimeProperty()
	{
		return timeProperty;
	}

	public Calendar getTimestampProperty()
	{
		return timestampProperty;
	}

	public Boolean getWbooleanProperty()
	{
		return wbooleanProperty;
	}

	public Double getWdoubleProperty()
	{
		return wdoubleProperty;
	}

	public Float getWfloatProperty()
	{
		return wfloatProperty;
	}

	public Integer getWintProperty()
	{
		return wintProperty;
	}

	public Long getwLongProperty()
	{
		return wLongProperty;
	}

	public boolean isBooleanProperty()
	{
		return booleanProperty;
	}

	public void setBigDecimalProperty(BigDecimal bigDecimalProperty)
	{
		this.bigDecimalProperty = bigDecimalProperty;
	}

	public void setBooleanProperty(boolean booleanProperty)
	{
		this.booleanProperty = booleanProperty;
	}

	public void setDateProperty(Date dateProperty)
	{
		this.dateProperty = dateProperty;
	}

	public void setDoubleProperty(double doubleProperty)
	{
		this.doubleProperty = doubleProperty;
	}

	public void setFloatProperty(float floatProperty)
	{
		this.floatProperty = floatProperty;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setIntProperty(int intProperty)
	{
		this.intProperty = intProperty;
	}

	public void setLongProperty(long longProperty)
	{
		this.longProperty = longProperty;
	}

	public void setSingleComposition(ComposedEntity singleComposition)
	{
		this.singleComposition = singleComposition;
	}

	public void setTextPropertyA(final String textPropertyA)
	{
		this.textPropertyA = textPropertyA;
	}

	public void setTextPropertyB(final String textPropertyB)
	{
		this.textPropertyB = textPropertyB;
	}

	public void setTimeProperty(java.sql.Date timeProperty)
	{
		this.timeProperty = timeProperty;
	}

	public void setTimestampProperty(Calendar timestampProperty)
	{
		this.timestampProperty = timestampProperty;
	}

	public void setWbooleanProperty(Boolean wbooleanProperty)
	{
		this.wbooleanProperty = wbooleanProperty;
	}

	public void setWdoubleProperty(Double wdoubleProperty)
	{
		this.wdoubleProperty = wdoubleProperty;
	}

	public void setWfloatProperty(Float wfloatProperty)
	{
		this.wfloatProperty = wfloatProperty;
	}

	public void setWintProperty(Integer wintProperty)
	{
		this.wintProperty = wintProperty;
	}

	public void setwLongProperty(Long wLongProperty)
	{
		this.wLongProperty = wLongProperty;
	}

}
