package com.bfds.saec.config.validators;

import com.bfds.validation.message.ValidationMessageContext;
import com.google.common.base.Preconditions;

import com.google.common.collect.Sets;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Properties;
import java.util.Set;

/**
 * A helper call for all {@link com.bfds.validation.Validator}s
 */
public class ValidationUtil {

    private  ValidationUtil() {
    }

    /**
     * Checks if there exists a property with given name and value.
     *
     * @param propertyName The name of the property.
     * @param properties - The {@link Properties} instance that must contain a property with the given propertyName
     * @param expected - The expected value of the property
     * @param messageContext - The {@link ValidationMessageContext} for recording messages.
     * @return  true if a property exits with the given name and value.
     */
    public static boolean verifyPropertyEquals(String propertyName, Properties properties, String expected, ValidationMessageContext messageContext) {
        String actual = (String) properties.get(propertyName);
        if (!expected.equals(actual)) {
            messageContext.error("saec property %s must be %s. It is %s", propertyName, expected, actual);
            return false;
        }
        return true;
    }

    /**
     * Checks if there exists a property with given name and a non-empty value.
     *
     * @param propertyName The name of the property.
     * @param properties - The {@link Properties} instance that must contain a property with the given propertyName
     * @param messageContext - The {@link ValidationMessageContext} for recording messages.
     * @return  true if a property exits with the given name and a non-empty value.
     */
    public static boolean verifyPropertyExists(String propertyName, Properties properties, ValidationMessageContext messageContext) {
        String actual = (String) properties.get(propertyName);
        if (!StringUtils.hasText(actual)) {
            messageContext.error("saec property %s is eiter not present or has an empty value", propertyName);
            return false;
        }
        return true;
    }

    /**
     * Checks if the property value represent a directory that can be written to.
     *
     * @param propertyName The name of the property.
     * @param properties - The {@link Properties} instance that must contain a property with the given propertyName
     * @param messageContext  - The {@link ValidationMessageContext} for recording messages.
     * @return true if a property value is a directory that can be written to, else false.
     */
    public static boolean verifyCanWrite(String propertyName, Properties properties, ValidationMessageContext messageContext) {
        return verifyCanWrite(propertyName, (String) properties.get(propertyName), messageContext);
    }
    
    /**
     * Checks if the property value represent a directory that can be written to.
     *
     * @param propertyName The name of the property.
     * @param properties - The {@link Properties} instance that must contain a property with the given propertyName
     * @param messageContext  - The {@link ValidationMessageContext} for recording messages.
     * @return true if a property value is a directory that can be written to, else false.
     */
    
    /**
     * Checks if dir represents a directory that can be written to.
     * @param dirId An id for the drectory. Could be the name of a property like "awd.dir" whose value is the directory path.
     * @param dir the absolute path to the directory
     * @param messageContext The {@link ValidationMessageContext} for recording messages.
     * @return true if the directory can be written to, else false.
     */
    public static boolean verifyCanWrite(String dirId, String dirPath, ValidationMessageContext messageContext) {    	
        return verifyAccess(dirId, dirPath, AccessType.WRITE, messageContext);
    }
    
    /**
     * Checks if dir represents a directory from which files can be read.
     * @param dirId An id for the drectory. Could be the name of a property like "awd.dir" whose value is the directory path.
     * @param dir the absolute path to the directory
     * @param messageContext The {@link ValidationMessageContext} for recording messages.
     * @return true if files in the directory can be read.
     */
    public static boolean verifyCanRead(String dirId, String dirPath, ValidationMessageContext messageContext) {
        return verifyAccess(dirId, dirPath, AccessType.READ, messageContext);
    }
    
    /**
     * Checks if dir represents a directory to which access is allowed. 
     * @param dirId An id for the drectory. Could be the name of a property like "awd.dir" whose value is the directory path.
     * @param dir the absolute path to the directory
     * @param accessType the type of access required.
     * @param messageContext The {@link ValidationMessageContext} for recording messages.
     * @return true if files in the directory can be accessed.
     */
    public static boolean verifyAccess(String dirId, String dirPath, AccessType accessType,  ValidationMessageContext messageContext) {
    	
    	Preconditions.checkNotNull(accessType, "accessType is null");
        if (!StringUtils.hasText(dirPath)) {
            messageContext.error("%s is not set. %s. ", dirId, dirPath);
            return false;
        }
        Resource resource = new FileSystemResource(dirPath);
        if (!resource.exists()) {
            messageContext.error("%s does not exist. %s. ", dirId, dirPath);
            return false;
        }
        try {
            File file = resource.getFile();
            if (!file.exists()) {
                messageContext.error("%s does not exist. %s. ", dirId, dirPath);
                return false;
            } else if (!file.isDirectory()) {
                messageContext.error("%s is not a directory. %s. ", dirId, dirPath);
                return false;
            } else if (accessType ==  AccessType.READ && !file.canRead()) {
                messageContext.error("Cannot read from %s. %s. ", dirId, dirPath);
                return false;
            } else if (accessType ==  AccessType.WRITE && !file.canRead()) {
                messageContext.error("Cannot write to %s. %s. ", dirId, dirPath);
                return false;
            }
        } catch (Exception e) {
            messageContext.error("Error accessing %s %s. %s", dirId, dirPath, e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * Performs set subtraction.
     *
     * @param arg1 - The set to subtract from. Must not be null.
     * @param arg2  - The set to subtract. Must not be null.
     * @return  arg1 - arg2
     */
    public static <T> Set<T> subtract(Set<T> arg1, Set<T> arg2) {
        Set<T> op1 = Sets.newHashSetWithExpectedSize(arg1.size());
        op1.addAll(arg1);
        Set<T> op2 = Sets.newHashSetWithExpectedSize(arg2.size());
        op2.addAll(arg2);

        op1.removeAll(op2);
        return op1;
    }
    public static enum AccessType {
    	READ, WRITE;
    }
}