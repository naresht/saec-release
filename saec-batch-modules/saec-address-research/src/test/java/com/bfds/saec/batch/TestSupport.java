package com.bfds.saec.batch;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.activity.Activity;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Utility metods for tests.
 */
public class TestSupport {

    /**
     * Returns the activity of the given activityType if present else null.
     * @param claimant - The claimant whose activity list has to be searched.
     * @param activityType - The type of activity to look for.
     * @return activity of the given activity
     * @throws IllegalStateException if more than one activity of the given type exists.
     */
    public Activity getActivityByType(Claimant claimant, Class<? extends Activity> activityType) {
        Activity  ret = null;
        for(Activity activity : claimant.getActivity()) {
            if(activity.getClass().isAssignableFrom(activityType)) {
                if(ret != null) {
                    throw new IllegalStateException("More than one activity of type " + activityType);
                }
                ret = activity;
            }
        }
        return ret;
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
}
