package com.bfds.saec.batch.in.infoage;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.FileItem;

public class FileRecordItemWriterDecorator implements ItemWriter<FileItem> {

	private static final Logger logger = LoggerFactory
			.getLogger(FileRecordItemWriterDecorator.class);

	private ItemWriter<FileItem> target;

	private EntityManagerFactory entityManagerFactory;

	public void setTarget(ItemWriter<FileItem> target) {
		this.target = target;
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public void write(List<? extends FileItem> items) throws Exception {
		target.write(items);
		saveCashedCheckRec(items);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, value = "batchFilesTransactionManager")
	public void saveCashedCheckRec(List<? extends FileItem> items) {
		EntityManager entityManager = EntityManagerFactoryUtils
				.getTransactionalEntityManager(entityManagerFactory);
		if (entityManager == null) {
			throw new DataAccessResourceFailureException(
					"Unable to obtain a transactional EntityManager");
		}
		for (FileItem rec : items) {
			rec.setProcessed(true);
		}
		doWrite(entityManager, items);
		entityManager.flush();
	}

	protected <T> void doWrite(EntityManager entityManager,
			List<? extends T> items) {

		if (logger.isDebugEnabled()) {
			logger.debug("Writing to JPA with " + items.size() + " items.");
		}

		if (!items.isEmpty()) {
			long mergeCount = 0;
			for (T item : items) {
				if (!entityManager.contains(item)) {
					entityManager.merge(item);
					mergeCount++;
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug(mergeCount + " entities merged.");
				logger.debug((items.size() - mergeCount)
						+ " entities found in persistence context.");
			}
		}
	}

}
