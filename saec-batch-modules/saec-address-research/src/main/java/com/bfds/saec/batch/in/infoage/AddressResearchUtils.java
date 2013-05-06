package com.bfds.saec.batch.in.infoage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Utility class
 */
public class AddressResearchUtils {
    final static Logger log = LoggerFactory.getLogger(AddressResearchUtils.class);
        
    public static final String INFOAGE_DATE_FORMAT = "yyyyMMdd";
        
    public static Date convertToDate(String date) {
        if(date == null || !StringUtils.hasText(date)) {
            return null;
        }
        // format is yyyyMM--, so replace "--" with 01 as default
        String _date = date.replaceAll("--","01");
        SimpleDateFormat formatter = new SimpleDateFormat(INFOAGE_DATE_FORMAT);
        try {
            return formatter.parse(_date);
        } catch (ParseException e) {
            log.error("Error converting infoage.AddressDateReported (date): " + date, e);
        }
        return null;
    }
}
