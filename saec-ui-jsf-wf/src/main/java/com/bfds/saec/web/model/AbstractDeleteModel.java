package com.bfds.saec.web.model;

import java.util.Collection;
import java.util.Set;

import org.primefaces.component.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * 
 * A view model for deleting objects from table
 * 
 */
public abstract class AbstractDeleteModel<T> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	transient DataTable datatable;

	final static Logger log = LoggerFactory
			.getLogger(AbstractDeleteModel.class);

	private Set<T> toBeDeleted;

	public AbstractDeleteModel() {
		this.toBeDeleted = this.newSet();
	}

	public boolean isDelete() {
		return isMarkedTobeDeleted((T) datatable.getRowData());
	}

	public void setDelete(boolean delete) {
		if (delete) {
			markTobeDeleted((T) datatable.getRowData());
		} else {
			undoDelete((T) datatable.getRowData());
		}
	}

	public Set<T> getToBeDeleted() {
		return this.toBeDeleted;
	}

	/**
	 * accumulate the objects to be deleted. Note: the objects are not dirty
	 * yet.simply added to the set.
	 * 
	 * @param obj
	 *            - the value in the set associated with the key
	 * @return T
	 * @see Set#add(V)
	 */
	public boolean markTobeDeleted(T obj) {
		return this.toBeDeleted.add(obj);
	}

	/**
	 * is an entry available
	 * 
	 * @param obj
	 *            - obj in set
	 * @return @see Set#contains(K)
	 */
	public boolean isMarkedTobeDeleted(T obj) {
		return this.toBeDeleted.contains(obj);
	}

	/**
	 * removes the object from the collection of objects to be deleted, simply
	 * remove them from Set.
	 * 
	 * @param obj
	 *            - entry in the set
	 * @return boolean
	 * 
	 * @see Set#remove(Object)
	 */
	public boolean undoDelete(T obj) {
		return this.toBeDeleted.remove(obj);
	}

	/**
	 * Values in the set should get dirty for delete
	 */
	public void deleteSelected() {
		this.persistDelete(this.toBeDeleted);
	}
	/**
	 * reset the set to a new collection, so that the to be deleted objects will
	 * not be processed for persist
	 * 
	 */
	public void reset() {
		this.toBeDeleted = newSet();
	}

	protected Set<T> newSet() {
		return Sets.newHashSet();
	}
	public DataTable getDatatable() {
		return datatable;
	}

	public void setDatatable(DataTable datatable) {
		this.datatable = datatable;
	}
	/**
	 * its up to the client code to support delete all or not
	 */
	protected void persistDeleteAll() {
		// NOOP
	}
	protected abstract void persistDelete(Collection<T> values);

}
