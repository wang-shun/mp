package com.fiberhome.mos.core.openapi.rop.client;

import org.springframework.core.io.AbstractResource;

public class ResourceConvertor implements RopConverter<AbstractResource, AbstractResource> {

	@Override
	public AbstractResource convert(AbstractResource source) {
		return source;
	}

	@Override
	public AbstractResource unconvert(AbstractResource target) {
		return target;
	}

	@Override
	public Class<AbstractResource> getSourceClass() {
		return AbstractResource.class;
	}

	@Override
	public Class<AbstractResource> getTargetClass() {
		return AbstractResource.class;
	}

	


}
