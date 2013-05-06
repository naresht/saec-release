package com.bfds.saec.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;
import java.util.regex.Pattern;

@RooJavaBean
@RooToString
@Embeddable
public class ZipCode implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	public ZipCode() {
	}

	public ZipCode(final String zip, final String ext) {
		this.zip = zip;
		this.ext = ext;
	}

	@NotNull
	private String zip;

	private String ext;

	/**
	 * Do not change this without ensuring that there is not impact anywhere. 
	 * For example this is used in DSTO check file batch job.
	 */
	public final String toString() {
		if(this.isEmpty()) return "";		
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(getZip()) && StringUtils.hasText(getExt())) {
			sb.append(getZip()).append("-").append(getExt());
		} else if (StringUtils.hasText(getZip())) {			
			sb.append(getZip());
		} else {
			sb.append(getExt());
		}
		return sb.toString().trim();

	}

	public final boolean isEmpty() {
		return !(StringUtils.hasText(zip) || StringUtils.hasText(ext));
	}

	protected final ZipCode clone() {
		try {
			return (ZipCode) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ext == null) ? 0 : ext.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ZipCode other = (ZipCode) obj;
		if (ext == null) {
			if (other.ext != null) {
				return false;
			}
		} else if (!ext.equals(other.ext)) {
			return false;
		}
		if (zip == null) {
			if (other.zip != null) {
				return false;
			}
		} else if (!zip.equals(other.zip)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param zip
	 *  This methods parses the given zipcode based the below rules.
	 *  1.Zip code should have five digits initially.
	 *  2.The last four digits along with the hyphen sign are optional and can be present or absent from the zip code.
	 */
	public static ZipCode parse(String zip)
	{
		ZipCode zipCode=new ZipCode();

		boolean isValid=isAValidZipCode(zip);
		if(isValid)
		{
			if (zip.length() == 10) {
				zipCode.setZip(zip.substring(0, 5));
				zipCode.setExt(zip.substring(6, 10));
			} else {
				zipCode.setZip(zip.substring(0, 5));
				zipCode.setExt(null);
			}
		}
		else{
			throw new IllegalStateException("zipCode is either null or not a valid one.");	
		}
		return zipCode;
	}
	
	public static boolean isAValidZipCode(String zip) {
		final String regex = "^\\d{5}(-\\d{4})?$";
        return Pattern.matches(regex, zip);
    }
}
