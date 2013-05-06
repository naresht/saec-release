package com.bfds.saec.rip.transformer;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.StringUtils;

import com.bfds.saec.rip.domain.RipObject;
import com.bfds.saec.rip.util.VelocityBean;
import com.google.common.base.Preconditions;

@Configurable
public abstract class AbstractRipObjectTransformer<T extends RipObject> implements RipObjectTransformer<T> {
	
	
	public abstract String getClasspathTemplateFile();
	
	/* (non-Javadoc)
	 * @see com.bfds.saec.rip.transformer.RipObjectTransformer#buildRip(T)
	 */
	@Override
	public String buildRip(T ripObject) {	
		
		Preconditions.checkNotNull(ripObject, "Rip Object is null");	
		
		preprocess(ripObject);
		
		Preconditions.checkState(StringUtils.hasText(getClasspathTemplateFile()), String.format("Template file must be provided for RipObject - %s.", ripObject.getClass()));
		Preconditions.checkArgument((StringUtils.hasText(((RipObject)ripObject).getWorkType())), String.format("worktype must be provided for RipObject - %s.", ripObject.getClass()));
		Preconditions.checkArgument((StringUtils.hasText(((RipObject)ripObject).getCreatedByUser())), String.format("createdByUser must be provided for RipObject - %s.", ripObject.getClass()));				
		
		VelocityBean.initVelocity();
		VelocityContext context = new VelocityContext();
		context.put("e", ripObject);
		StringWriter w = new StringWriter();
		try {
			Velocity.mergeTemplate(
					 getClasspathTemplateFile(),
					"ISO-8859-1", context, w);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return w.toString();
	}

	protected void preprocess(T ropObject) {

	}

}
