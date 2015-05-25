
package com.googlecode.common.admin.service.impl;

import com.googlecode.common.admin.service.AdminResponses;
import com.googlecode.common.admin.service.CompanyService;
import java.util.List;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.service.ex.ValidationFailedException;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.admin.dao.CompanyDao;
import com.googlecode.common.admin.dao.ContactDao;
import com.googlecode.common.admin.domain.Company;
import com.googlecode.common.admin.domain.Contact;
import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.company.ContactDTO;


/**
 * Default implementation of ContactService interface.
 * 
 * @see com.googlecode.common.admin.service.CompanyService
 */
@Service("companyService")
@Singleton
public final class CompanyServiceImpl implements CompanyService {

    protected final Logger      log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ContactDao          contactsDao;

    @Autowired
    private CompanyDao          companiesDao;

    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public ContactDTO createNewContact(Company company, ContactDTO details) {
        if (log.isInfoEnabled()) {
            log.info("Creating contact: \"" + details.getEmail() + "\"");
        }        

        Contact contact = createContactInternal(details, company);
        
        if (log.isTraceEnabled()) {
            log.trace("Contact creation completed: " + contact);
        
        } else if (log.isInfoEnabled()) {
            log.info("Contact creation completed: " 
                    + "contact: \"" + contact.getFirstName() + "\"");
        }
        
        return convertToContactDTO(details, contact);
    }

    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public ContactDTO getContactDTO(long contactId) {
        Contact contact = getContactById(contactId);

        return convertToContactDTO(new ContactDTO(), contact);
    }

    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public Contact getContactById(long contactId) {
        Contact contact = contactsDao.get(contactId);
        if (contact == null) {
            throw new OperationFailedException(
                    AdminResponses.CONTACT_NOT_FOUND,
                    "Contact with specified id (" + contactId + ") not found");
        }
        
        return contact;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public Company getCompanyById(long companyId) {
        Company company = companiesDao.get(companyId);
        if (company == null) {
            throw new OperationFailedException(
                    AdminResponses.COMPANY_NOT_FOUND,
                    "Company with specified id (" + companyId + ") not found");
        }
        
        return company;
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public ContactDTO updateContact(ContactDTO dto) {
        long contactId = dto.safeGetId();
        if (log.isInfoEnabled()) {
            log.info("Updating contact, contactId: " + contactId);
        }        
        
        Contact contact = getContactById(contactId);
        validateContact(contact, dto);
        
        contact = contactsDao.merge(convertToContact(contact, dto));
        
        return convertToContactDTO(dto, contact);
    }

    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> list = null;
        for (Company c : companiesDao.getAllCompanies()) {
            list = CollectionsUtil.addToList(list, convertToCompanyDTO(
                    new CompanyDTO(), c));
        }
        
        return list;
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public Company getCreateCompay(CompanyDTO dto) {
        long id = dto.safeGetId();
        if (id != 0L) {
            return getCompanyById(id);
        }
        
        validateCompany(null, dto);
        
        Company company = new Company();
        company.setName(dto.getName());
        companiesDao.save(company);
        
        return company;
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        validateCompany(null, companyDTO);
        
        Company c = new Company();
        c.setName(companyDTO.getName());
        companiesDao.save(c);
        
        return convertToCompanyDTO(new CompanyDTO(), c);
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public CompanyDTO renameCompany(RenameDTO dto) {
        Company company = getCompanyById(dto.getId());
        String name = dto.getName();
        CompanyDTO companyDTO = new CompanyDTO(name);
        validateCompany(company, companyDTO);
        company.setName(name);

        company = companiesDao.merge(company);
        
        return convertToCompanyDTO(companyDTO, company);
    }

    private Contact createContactInternal(ContactDTO dto, Company company) {
        validateContact(null, dto);
        
        Contact contact = convertToContact(new Contact(), dto);
        contact.setCompany(company);
        contactsDao.save(contact);
        return contact;
    }

    private Contact convertToContact(Contact c, ContactDTO details) {
        c.setFirstName(details.getFirstName());
        c.setMiddleName(details.getMiddleName());
        c.setSecondName(details.getSecondName());
        c.setPhone(details.getPhone());
        c.setEmail(details.getEmail());
        return c;
    }

    private ContactDTO convertToContactDTO(ContactDTO dto, Contact c) {
        dto.setId(c.getId());
        dto.setFirstName(c.getFirstName());
        dto.setMiddleName(c.getMiddleName());
        dto.setSecondName(c.getSecondName());
        dto.setPhone(c.getPhone());
        dto.setEmail(c.getEmail());
        return dto;
    }

    private CompanyDTO convertToCompanyDTO(CompanyDTO dto, Company c) {
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }

    private void validateContact(Contact currContact, ContactDTO details) {
        String firstName = details.getFirstName();
        if (firstName == null
                || (firstName.length() < 2 || firstName.length() > 32)) {

            throw new ValidationFailedException(
                    AdminResponses.CONTACT_INVALID_FIRST_NAME,
                    "First Name field must be from 2 to 32 symbols long");
        }

        String middleName = details.getMiddleName();
        if (middleName != null 
                && (middleName.length() < 2 || middleName.length() > 32)) {
            
            throw new ValidationFailedException(
                    AdminResponses.CONTACT_INVALID_MIDDLE_NAME,
                    "Middle Name field must be from 2 to 32 symbols long");
        }

        String secondName = details.getSecondName();
        if (secondName == null
                || secondName.length() < 2 || secondName.length() > 32) {

            throw new ValidationFailedException(
                    AdminResponses.CONTACT_INVALID_SECOND_NAME,
                    "Second Name field must be from 2 to 32 symbols long");
        }

//        Long phone = details.getPhone();
//        if (phone != null && (phone < 380000000000L || phone > 380999999999L)) {
//            throw new ValidationFailedException(
//                    AdminResponses.CONTACT_INVALID_PHONE,
//                    "Phone field must be in format: 380XXXXXXXXX");
//        }

        String email = details.getEmail();
        if (email == null || email.length() < 4 || email.length() > 255) {
            throw new ValidationFailedException(
                    AdminResponses.CONTACT_INVALID_EMAIL,
                    "Email field must be from 4 to 255 symbols long");
        }
        
        // check contact email for new contact
        if (currContact == null 
                || !email.equalsIgnoreCase(currContact.getEmail())) {
            
            if (contactsDao.getByEmail(email) != null) {
                throw new ValidationFailedException(
                        AdminResponses.CONTACT_ALREADY_EXISTS,
                        "Contact with E-mail (" + email + ") already exists");
            }
        }
    }

    private void validateCompany(Company currCompany, CompanyDTO details) {
        String name = details.getName();
        if (name == null
                || (name.length() < 1 || name.length() > 64)) {

            throw new ValidationFailedException(
                    AdminResponses.COMPANY_INVALID_NAME,
                    "Company Name field should be from 1 to 64 symbols long");
        }

        // check company name
        if ((currCompany == null || !name.equals(currCompany.getName()))
                && companiesDao.getByName(name) != null) {
            
            throw new ValidationFailedException(
                    AdminResponses.COMPANY_ALREADY_EXISTS,
                    "Company with name (" + name + ") already exists");
        }
    }
}
