package org.openmrs.module.idcards;

import java.util.Date;

import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.util.OpenmrsUtil;

/**
 * A GeneratedIdentifier represents a number that was calculated ahead of time and at some point
 * after that was printed (and then used). <br/>
 * <br/>
 * The table underneath this object is expected to have, say, 1 million rows in it of pregenerated
 * numbers. The numbers will encompass the full range of startnum-->startnum+million (with
 * checkdigits), but the order will be random. The internalId column is database generated and helps
 * to orient the module as to where in the table it currently is.
 */
public class GeneratedIdentifier {
	
	private Integer id;
	
	private User generator;
	
	private Date dateGenerated;
	
	private Boolean printed = Boolean.FALSE;
	
	private User printedBy;
	
	private Date datePrinted;
	
	private IdcardsTemplate printedTemplate;
	
	/**
	 * Empty constructor
	 */
	public GeneratedIdentifier() {
	};
	
	/**
	 * Convenience constructor
	 * 
	 * @param identifierWithoutCheckdigit the number to add a check digit to and then save
	 * @param generator the user who generated this
	 * @param generatedDate the date this was generated
	 */
	public GeneratedIdentifier(Integer identifierWithoutCheckdigit, User generator, Date generatedDate) {
		this.id = identifierWithoutCheckdigit;
		this.generator = generator;
		this.dateGenerated = generatedDate;
	}
	
	/**
	 * @return the internalId
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Convenience method to turn the id for this object into a checkdigited identifier
	 * 
	 * @return the identifier
	 */
	public String getIdentifier() {
		int checkdigit;
		try {
			checkdigit = IdcardsUtil.getCheckDigit(String.valueOf(id));
		}
		catch (Exception e) {
			throw new APIException("Unable to get checkdigit for: " + id, e);
		}
		return id + "-" + checkdigit;
	}
	
	/**
	 * @return the generator
	 */
	public User getGenerator() {
		return generator;
	}
	
	/**
	 * @param generator the generator to set
	 */
	public void setGenerator(User generator) {
		this.generator = generator;
	}
	
	/**
	 * @return the dateGenerated
	 */
	public Date getDateGenerated() {
		return dateGenerated;
	}
	
	/**
	 * @param dateGenerated the dateGenerated to set
	 */
	public void setDateGenerated(Date dateGenerated) {
		this.dateGenerated = dateGenerated;
	}
	
	/**
	 * @return the printed
	 */
	public Boolean getPrinted() {
		return printed;
	}
	
	/**
	 * @param printed the printed to set
	 */
	public void setPrinted(Boolean printed) {
		this.printed = printed;
	}
	
	/**
	 * @return the printedBy
	 */
	public User getPrintedBy() {
		return printedBy;
	}
	
	/**
	 * @param printedBy the printedBy to set
	 */
	public void setPrintedBy(User printedBy) {
		this.printedBy = printedBy;
	}
	
	/**
	 * @return the datePrinted
	 */
	public Date getDatePrinted() {
		return datePrinted;
	}
	
	/**
	 * @param datePrinted the datePrinted to set
	 */
	public void setDatePrinted(Date datePrinted) {
		this.datePrinted = datePrinted;
	}
	
	/**
	 * @return the printedTemplate
	 */
	public IdcardsTemplate getPrintedTemplate() {
		return printedTemplate;
	}
	
	/**
	 * @param printedTemplate the printedTemplate to set
	 */
	public void setPrintedTemplate(IdcardsTemplate printedTemplate) {
		this.printedTemplate = printedTemplate;
	}
	
	public String toString() {
		return "GeneratedIdentifier: " + getIdentifier();
	}
}
