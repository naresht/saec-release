package com.bfds.saec.batch.util;


import com.bfds.saec.batch.file.domain.FileRecord;
import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.*;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.reference.Lov;
import com.bfds.wss.domain.*;
import com.bfds.wss.domain.reference.AdditionalQuestions;
import com.bfds.wss.domain.reference.AdditionalQuestionsResponses;
import com.bfds.wss.domain.reference.QuestionGroup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


@Component
public class TestDataUtil {

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager batchFilesEntityManager;

    /**
     * Deletes all entitites created during tests. Entity are deleted in the correct order to avoid any FK violations.
     * This method must be updates as new types of entities are created during tests.
     */
    @Transactional
    public void deleteData() {
        deleteEvents();
        deleteActivity();
        deletePayments();
        deleteLetters();
        deleteClaimUserResponseGroups();
        //deleteClaimFileUploaded();
        //deleteClaimantClaims();
        deleteClaimants();
        deleteLetterCodes();
        deleteQuestionGroups();
        deleteTranch();
        deleteLov();
        
    }
    
    public void deleteRemindersData()
    {
    	List<ClaimantReminder> clr=ClaimantReminder.findAllClaimantReminders();
    	 for (ClaimantReminder p : clr) {
             p.remove();
         }
    	 deleteData();
    }

    private static void deleteEvents() {
        List<Event> events = Event.findAllEvents();
        for (Event c : events) {
            c.remove();
        }
        (new Claimant()).flush();
    }

    private void deleteActivity() {
        List<Activity> activity = Activity.findAllActivitys();
        for(Activity c : activity){
            c.remove();
        }
        (new Claimant()).flush();
    }

    private void deletePayments() {
        List<Payment> payments = getAllReissues();
        for (Payment p : payments) {
            p.remove();
        }

        payments = Payment.findAllPayments();
        for (Payment p : payments) {
            p.remove();
        }
        (new Claimant()).flush();
    }

    /**
     *
     * @return payments that are re-issues.
     */
    public List<Payment> getAllReissues() {
        return   Payment.entityManager().createQuery("select c from Payment as c where c.reissueOf is not null", Payment.class).getResultList();
    }

    private void deleteLetters() {
        List<Letter> mails = Letter.findAllLetters();
        for (Letter l : mails) {
            l.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteClaimantClaims() {
        List<ClaimantClaim> list = ClaimantClaim.findAllClaimantClaims();
        for (ClaimantClaim c : list) {
            c.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteClaimFileUploaded() {
        deleteClaimFileLob();
        List<ClaimFileUploaded> list = ClaimFileUploaded.findAllClaimFileUploadeds();
        for (ClaimFileUploaded c : list) {
            c.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteClaimFileLob() {
        List<ClaimFileLob> list = ClaimFileLob.findAllClaimFileLobs();
        for (ClaimFileLob c : list) {
            c.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteClaimants() {
        List<Claimant> list = getAllAlternatePayees();
        for (Claimant c : list) {
            c.remove();
        }
        list = Claimant.findAllClaimants();
        for (Claimant c : list) {
            c.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteLetterCodes() {
        List<LetterCode> list = LetterCode.findAllLetterCodes();
        for (LetterCode l : list) {
            l.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteQuestionGroups() {
        List<QuestionGroup> list = QuestionGroup.findAllQuestionGroups();
        for (QuestionGroup l : list) {
            l.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteClaimUserResponseGroups() {
        List<ClaimUserResponseGroup> list = ClaimUserResponseGroup.findAllClaimUserResponseGroups();
        for (ClaimUserResponseGroup l : list) {
            l.remove();
        }
        (new Claimant()).flush();
    }

    public static void deleteLov() {
        List<Lov> list = Lov.findAllLovs();
        for (Lov l : list) {
            l.remove();
        }
        (new Claimant()).flush();
    }

    public void deleteTranch() {
        List<Tranch> list = Tranch.findAllTranches();
        for (Tranch l : list) {
            l.remove();
        }
        (new Claimant()).flush();
    }

    private static List<Claimant> getAllAlternatePayees() {
        return   Claimant.entityManager().createQuery("select c from Claimant as c where c.parentClaimant is not null", Claimant.class).getResultList();
    }

    @Transactional(value = "batchFilesTransactionManager")
    public void deleteAllFileRecords(Class<?> clazz) {
        for(Object obj :  batchFilesEntityManager.createQuery("from "+clazz.getName()).getResultList()) {
            batchFilesEntityManager.remove(obj);
        }
        batchFilesEntityManager.flush();
        batchFilesEntityManager.clear();
    }

    /**
     *
     * @param list  - The list that must match the dataArray from index 1 to end of array.
     * @param dataArray - An Object[][] where each Object[] has the property name in index 0 and the data in the other indexes.
     */
    public void verifyData(List<?> list, Object[][] dataArray) {
        for(Object[] dataRec : dataArray) {
            String propertyName =  (String) dataRec[0];
            Object[] data = new Object[dataRec.length - 1];
            System.arraycopy(dataRec, 1, data, 0, data.length);
            assertThat(list).onProperty(propertyName).containsSequence(data);
        }
    }

    public <T> List<T> findAllFileRecords(Class<T> requiredType) {
        return (List<T>) batchFilesEntityManager.createQuery(" from "+requiredType.getName()).getResultList();
    }
}
