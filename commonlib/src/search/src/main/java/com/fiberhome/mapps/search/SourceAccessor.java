package com.fiberhome.mapps.search;

import java.util.List;

public interface SourceAccessor<T> {
	Object getEntity(T id);
	
	List<?> getEntities(List<T> ids);
	
	List<?> getEntities(int limit, int offset);
}
