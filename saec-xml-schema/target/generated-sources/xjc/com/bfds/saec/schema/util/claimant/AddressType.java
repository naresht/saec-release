//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.06 at 03:25:32 PM IST 
//


package com.bfds.saec.schema.util.claimant;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddressType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AddressType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADDRESS_OF_RECORD"/>
 *     &lt;enumeration value="SEASONAL_ADDRESS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AddressType")
@XmlEnum
public enum AddressType {

    ADDRESS_OF_RECORD,
    SEASONAL_ADDRESS;

    public String value() {
        return name();
    }

    public static AddressType fromValue(String v) {
        return valueOf(v);
    }

}
