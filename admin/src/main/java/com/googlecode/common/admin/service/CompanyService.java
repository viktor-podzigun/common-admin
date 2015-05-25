
package com.googlecode.common.admin.service;

import java.util.List;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.admin.domain.Company;
import com.googlecode.common.admin.domain.Contact;
import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.company.ContactDTO;


/**
 * This service responsible for company and contacts creation, retrieving, 
 * and updating.
 */
public interface CompanyService {

    /**
     * Creates new contact with the specified details.
     * 
     * @param company   company entity
     * @param details   contact details
     * @return          updated details
     */
    public ContactDTO createNewContact(Company company, ContactDTO details);

    /**
     * Update contact by the specified contact id.
     * 
     * @param dto       contact info
     * @return          updated contact info
     */
    public ContactDTO updateContact(ContactDTO dto);

    /**
     * Gets contact details by the specified contact id.
     * 
     * @param contactId contact id
     * @return          contact details
     */
    public ContactDTO getContactDTO(long contactId);
    
    /**
     * Gets contact entity by the specified contact id.
     * 
     * @param contactId contact id
     * @return          contact entity
     */
    public Contact getContactById(long contactId);
    
    /**
     * Returns company entity by the given id.
     * 
     * @param companyId company id
     * @return          company entity
     */
    public Company getCompanyById(long companyId);

    /**
     * Returns all companies list.
     * @return all companies list
     */
    public List<CompanyDTO> getAllCompanies();

    /**
     * Returns existing company or creates new company by the given details.
     * 
     * @param company   company details
     * @return existing or created company
     */
    public Company getCreateCompay(CompanyDTO company);

    /**
     * Create new company with specific name
     * 
     * @param company   company details
     * @return          updated details
     */
    public CompanyDTO createCompany(CompanyDTO company);

    /**
     * Rename company with specific name and specific Id
     * 
     * @param company   company details
     * @return          updated details
     */
    public CompanyDTO renameCompany(RenameDTO company);
}
