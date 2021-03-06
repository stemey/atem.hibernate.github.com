/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.hibernate;

import org.atemsource.atem.api.attribute.primitive.DateType.Precision;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.impl.common.attribute.primitive.DateTypeImpl;
import org.springframework.stereotype.Component;


@Component
public class DateTypeRegistrar implements org.atemsource.atem.spi.PrimitiveTypeRegistrar
{

	@Override
	public PrimitiveType<?>[] getTypes()
	{
		return new PrimitiveType<?>[]{new DateTypeImpl(Precision.DATETIME, java.util.Date.class)};
	}

}
