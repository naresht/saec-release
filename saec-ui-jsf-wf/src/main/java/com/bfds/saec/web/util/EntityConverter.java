package com.bfds.saec.web.util;

import com.bfds.saec.domain.BaseIdentityEntity;
import com.bfds.saec.domain.Role;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Generic LOV Converter for any Entity that extends LOV
 * 
 */
@Service("entityConverter")
@FacesConverter("com.bfds.saec.web.util.EntityConverter")
public class EntityConverter implements Converter, Serializable {

	static Logger log = LoggerFactory.getLogger(EntityConverter.class);
	private static final long serialVersionUID = -5137676309479323480L;

	@PersistenceContext(unitName="entityManagerFactory")
	protected EntityManager entityManager;

	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) throws ConverterException {

		BaseIdentityEntity entity = null;
		Object ret = null;
		if (value == null) {
			entity = null;
		} else {

			if (component instanceof PickList) {
				Object dualList = ((PickList) component).getValue();
				DualListModel dl = (DualListModel) dualList;

				for (Object o : dl.getSource()) {
					if (o instanceof Role) {
						Role r = (Role) o;
						String id = "" + ((Role) o).getId();
						if (value.equals(id)) {
							ret = o;
							break;
						}
					}
				}

				if (ret == null) {
					for (Object o : dl.getTarget()) {
						if (o instanceof Role) {
							String id = "" + ((Role) o).getId();
							if (value.equals(id)) {
								ret = o;
								break;
							}
						}
					}
				}

				return ret;
			} else {
				Integer id = new Integer(value);
				entity = (BaseIdentityEntity) entityManager.find(
						getClazz(facesContext, component), id);
				if (entity == null) {
					log.debug("There is no entity with id:  " + id);
					// throw new
					// IllegalArgumentException("There is no entity with id:  "
					// + id);
				}
			}
		}
		return entity;
	}

	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) throws ConverterException {

		if (value != null && !(value instanceof BaseIdentityEntity)) {
			throw new IllegalArgumentException(
					"This converter only handles instances of Lov");
		}
		if (value == null) {
			return "";
		}
		if (value instanceof String) {
			return (String) value;
		}
		BaseIdentityEntity entity = (BaseIdentityEntity) value;
		return entity.getId() == null ? "" : entity.getId().toString();
	}

	// Gets the class corresponding to the context in jsf page
	private Class<?> getClazz(FacesContext facesContext, UIComponent component) {

		return component.getValueExpression("value").getType(
				facesContext.getELContext());
	}

}