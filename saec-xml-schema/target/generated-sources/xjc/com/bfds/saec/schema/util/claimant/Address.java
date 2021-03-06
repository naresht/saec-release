//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.06 at 03:25:32 PM IST 
//


package com.bfds.saec.schema.util.claimant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Address complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Address">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addressType" type="{http://saec.bfds.com/schema/util/claimant}AddressType"/>
 *         &lt;element name="line1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="line2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="line3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="line4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="line5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="line6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stateCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="zip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zipExt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requiresAddressResearch" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="requiresInfoAgePrescrub" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Address", propOrder = {
    "addressType",
    "line1",
    "line2",
    "line3",
    "line4",
    "line5",
    "line6",
    "city",
    "stateCode",
    "countryCode",
    "zip",
    "zipExt",
    "requiresAddressResearch",
    "requiresInfoAgePrescrub"
})
public class Address {

    @XmlElement(required = true)
    protected AddressType addressType;
    @XmlElement(required = true)
    protected String line1;
    protected String line2;
    protected String line3;
    protected String line4;
    protected String line5;
    protected String line6;
    @XmlElement(required = true)
    protected String city;
    @XmlElement(required = true)
    protected String stateCode;
    @XmlElement(required = true)
    protected String countryCode;
    protected String zip;
    protected String zipExt;
    protected Boolean requiresAddressResearch;
    protected Boolean requiresInfoAgePrescrub;

    /**
     * Gets the value of the addressType property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getAddressType() {
        return addressType;
    }

    /**
     * Sets the value of the addressType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setAddressType(AddressType value) {
        this.addressType = value;
    }

    /**
     * Gets the value of the line1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine1() {
        return line1;
    }

    /**
     * Sets the value of the line1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine1(String value) {
        this.line1 = value;
    }

    /**
     * Gets the value of the line2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine2() {
        return line2;
    }

    /**
     * Sets the value of the line2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine2(String value) {
        this.line2 = value;
    }

    /**
     * Gets the value of the line3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine3() {
        return line3;
    }

    /**
     * Sets the value of the line3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine3(String value) {
        this.line3 = value;
    }

    /**
     * Gets the value of the line4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine4() {
        return line4;
    }

    /**
     * Sets the value of the line4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine4(String value) {
        this.line4 = value;
    }

    /**
     * Gets the value of the line5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine5() {
        return line5;
    }

    /**
     * Sets the value of the line5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine5(String value) {
        this.line5 = value;
    }

    /**
     * Gets the value of the line6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine6() {
        return line6;
    }

    /**
     * Sets the value of the line6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine6(String value) {
        this.line6 = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the stateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * Sets the value of the stateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateCode(String value) {
        this.stateCode = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the zip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the value of the zip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZip(String value) {
        this.zip = value;
    }

    /**
     * Gets the value of the zipExt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZipExt() {
        return zipExt;
    }

    /**
     * Sets the value of the zipExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZipExt(String value) {
        this.zipExt = value;
    }

    /**
     * Gets the value of the requiresAddressResearch property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequiresAddressResearch() {
        return requiresAddressResearch;
    }

    /**
     * Sets the value of the requiresAddressResearch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequiresAddressResearch(Boolean value) {
        this.requiresAddressResearch = value;
    }

    /**
     * Gets the value of the requiresInfoAgePrescrub property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequiresInfoAgePrescrub() {
        return requiresInfoAgePrescrub;
    }

    /**
     * Sets the value of the requiresInfoAgePrescrub property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequiresInfoAgePrescrub(Boolean value) {
        this.requiresInfoAgePrescrub = value;
    }

    public Address withAddressType(AddressType value) {
        setAddressType(value);
        return this;
    }

    public Address withLine1(String value) {
        setLine1(value);
        return this;
    }

    public Address withLine2(String value) {
        setLine2(value);
        return this;
    }

    public Address withLine3(String value) {
        setLine3(value);
        return this;
    }

    public Address withLine4(String value) {
        setLine4(value);
        return this;
    }

    public Address withLine5(String value) {
        setLine5(value);
        return this;
    }

    public Address withLine6(String value) {
        setLine6(value);
        return this;
    }

    public Address withCity(String value) {
        setCity(value);
        return this;
    }

    public Address withStateCode(String value) {
        setStateCode(value);
        return this;
    }

    public Address withCountryCode(String value) {
        setCountryCode(value);
        return this;
    }

    public Address withZip(String value) {
        setZip(value);
        return this;
    }

    public Address withZipExt(String value) {
        setZipExt(value);
        return this;
    }

    public Address withRequiresAddressResearch(Boolean value) {
        setRequiresAddressResearch(value);
        return this;
    }

    public Address withRequiresInfoAgePrescrub(Boolean value) {
        setRequiresInfoAgePrescrub(value);
        return this;
    }

}
