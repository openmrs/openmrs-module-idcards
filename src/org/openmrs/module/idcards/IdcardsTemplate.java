package org.openmrs.module.idcards;

import java.util.Date;

import org.openmrs.User;

/**
 * Template for Idcards.
 * Data for idcards should be added to the xml.
 * Format for idcards should be in the xslt.
 * Example: one template may be used for generating the
 * back of new idcards that has a new, unused patient identifier
 * and bar code.  A second template is for generating the front
 * side of new id cards and has blank spaces to fill in patient
 * information.  A third template may be for reprinting id cards
 * for existing patients.  This has identifier and bar code on the left
 * and patient information on the right so it can be folded and
 * laminated.
 */
public class IdcardsTemplate {
    private Integer templateId;
    private String name;
    private String type;
    private String description;
    private String xml;
    private String xslt;
    private User creator;
    private Date dateCreated;
    private User changedBy;
    private Date dateChanged;
    
    /**
     * Default empty constructor 
     */
    public IdcardsTemplate() { }
    
    /**
     * @param templateId
     */
    public IdcardsTemplate(Integer templateId) {
    	this.templateId = templateId;
    }
    
    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof IdcardsTemplate) {
			if (templateId == null)
				return false;
			
			IdcardsTemplate c = (IdcardsTemplate) obj;
			return (templateId.equals(c.getTemplateId()));
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (this.getTemplateId() == null)
			return super.hashCode();
		return templateId.hashCode();
	}
	
    /**
     * Autogenerated id for the template
     * @param templateId
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * Autogenerated id for the template.
     * @return
     */
    public Integer getTemplateId() {
        return this.templateId;
    }

    /**
     * Name for the template.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Name for the template.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set a type on the template
     * to distinguish its use from other
     * templates.
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the type of template which
     * may determine how it's used 
     * differently than other template
     * types.
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * Description of the template's purpose.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Description of the template's purpose.
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Holds the data to be put into the idcards
     * in xml and Velocity format.
     * @param xml
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * Holds the data to be put into the idcards
     * in xml and Velocity format.
     * @return
     */
    public String getXml() {
        return this.xml;
    }

    /**
     * Holds the layout for the idcards in
     * xsl-fo format.
     * @param xslt
     */
    public void setXslt(String xslt) {
        this.xslt = xslt;
    }

    /**
     * Holds the layout for the idcards in
     * xsl-fo format.
     * @return
     */
    public String getXslt() {
        return this.xslt;
    }

    /**
     * User who created the template.
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * User who created the template.
     * @return
     */
    public User getCreator() {
        return this.creator;
    }

    /**
     * Date the template was created.
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Date the template was created.
     * @return
     */
    public Date getDateCreated() {
        return this.dateCreated;
    }

    /**
     * Last User to change the template.
     * @param changedBy
     */
    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
    }

    /**
     * Last User to change the template.
     * @return
     */
    public User getChangedBy() {
        return this.changedBy;
    }

    /**
     * Date the template was changed last.
     * @param dateChanged
     */
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    /**
     * Date the template was changed last.
     * @return
     */
    public Date getDateChanged() {
        return this.dateChanged;
    }
}
