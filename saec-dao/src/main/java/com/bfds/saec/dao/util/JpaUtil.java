package com.bfds.saec.dao.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class JpaUtil {
	public static Expression<String> concat(CriteriaBuilder cb,
			List<Path<String>> path) {
		if (path.size() == 1) {
			return path.get(0);
		}
		Expression<String> ret = null;
		for (Path<String> p : path) {
			ret = ret == null ? p : cb.concat(ret, p);
		}
		return ret;
	}
	
	public static <T> List<Path<T>> getPaths(Root r, String path) {
		final List<Path<T>> ret = new ArrayList<Path<T>>();
		if (path.contains(",")) {
			for (String p : path.split(",")) {
				ret.add(JpaUtil.<T> getPath(r, p));
			}
		} else {
			ret.add(JpaUtil.<T> getPath(r, path));
		}
		return ret;
	}
	
	private static <T> Path<T> getPath(Root r, String path) {
		if (path.contains(".")) {
			String[] paths = path.split("\\.");
			Path p = r.get(paths[0]);
			for (int i = 1; i < paths.length; i++) {
				p = p.get(paths[i]);
			}
			return p;
		} else {
			return r.get(path);
		}
	}
}
