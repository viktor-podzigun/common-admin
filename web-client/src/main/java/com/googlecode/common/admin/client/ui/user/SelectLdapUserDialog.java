
package com.googlecode.common.admin.client.ui.user;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.panel.BaseOkCancelDialog;
import com.googlecode.common.admin.protocol.user.LdapUserDTO;


/**
 * Select LDAP user dialog.
 */
public final class SelectLdapUserDialog extends BaseOkCancelDialog {

    private static final ProvidesKey<LdapUserDTO> KEY_PROVIDER = 
        new ProvidesKey<LdapUserDTO>() {
        @Override
        public Object getKey(LdapUserDTO item) {
                return item == null ? null : item.getLogin();
        }
    };

    private CellTable<LdapUserDTO> table;

    private final ListDataProvider<LdapUserDTO>     tableData = 
        new ListDataProvider<LdapUserDTO>();
    
    private final SingleSelectionModel<LdapUserDTO> tableSelModel = 
        new SingleSelectionModel<LdapUserDTO>();

    private boolean isSelectEnabled = false;
    
    
    public SelectLdapUserDialog(List<LdapUserDTO> dataList) {
        super("Select User", "Select");
        
        setOkIcon(ButtonImages.INSTANCE.dbSave(), 
                ButtonImages.INSTANCE.dbSaveDisabled());
        
        table = new CellTable<LdapUserDTO>(KEY_PROVIDER);
        table.setSelectionModel(tableSelModel);
        
        tableSelModel.addSelectionChangeHandler(new Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                onSelectionChanged();
            }
        });

        // connect the table to the data provider
        tableData.addDataDisplay(table);
        
        // Create a Pager to control the table.
//        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
//        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//        pager.setDisplay(table);
        
        ListHandler<LdapUserDTO> sortHandler = new ListHandler<LdapUserDTO>(
                tableData.getList());
        table.addColumnSortHandler(sortHandler);
            
        initColumns(sortHandler);
        setData(dataList);
        
//        VerticalPanel vPanel = new VerticalPanel();
//        vPanel.add(table);
//        vPanel.add(pager);
        
        setContent(table);
//        table.setPixelSize(860, 400);
  
        setOkEnabled(false);
    }

    public LdapUserDTO getLdapUserDTO() {
        return tableSelModel.getSelectedObject();
    }
    
    public void setData(List<LdapUserDTO> dataList) {
        if (dataList == null) {
            dataList = Collections.emptyList();
        }
    
        int pageSize = dataList.size();
        if (pageSize <= 0) {
            table.setPageSize(1);
        } else {
            table.setPageSize(pageSize);
        }
        
        tableData.getList().clear();
        for (LdapUserDTO dto : dataList) {
            tableData.getList().add(dto);
        }
    }
    
    private void initColumns(ListHandler<LdapUserDTO> sortHandler) {
        TextColumn<LdapUserDTO> login = new TextColumn<LdapUserDTO>() {
            @Override
            public String getValue(LdapUserDTO dto) {
                return dto.getLogin();
            }
        };
        
        login.setSortable(true);
        sortHandler.setComparator(login, new Comparator<LdapUserDTO>() {
          @Override
          public int compare(LdapUserDTO o1, LdapUserDTO o2) {
            return o1.safeGetLogin().compareTo(o2.safeGetLogin());
          }
        });
        
        TextColumn<LdapUserDTO> domain = new TextColumn<LdapUserDTO>() {
            @Override
            public String getValue(LdapUserDTO dto) {
                return dto.getDomain();
            }
        };
        
        domain.setSortable(true);
        sortHandler.setComparator(domain, new Comparator<LdapUserDTO>() {
            @Override
            public int compare(LdapUserDTO o1, LdapUserDTO o2) {
                return o1.safeGetDomain().compareTo(o2.safeGetDomain());
            }
        });

        TextColumn<LdapUserDTO> fullName = new TextColumn<LdapUserDTO>() {
            @Override
            public String getValue(LdapUserDTO dto) {
                return dto.getFullName();
            }
        };
        
        fullName.setSortable(true);
        sortHandler.setComparator(fullName, new Comparator<LdapUserDTO>() {
            @Override
            public int compare(LdapUserDTO o1, LdapUserDTO o2) {
                return o1.safeGetFullName().compareTo(o2.safeGetFullName());
            }
        });
        
        TextColumn<LdapUserDTO> mail = new TextColumn<LdapUserDTO>() {
            @Override
            public String getValue(LdapUserDTO dto) {
                return dto.getMail();
            }
        };

        mail.setSortable(true);
        sortHandler.setComparator(mail, new Comparator<LdapUserDTO>() {
            @Override
            public int compare(LdapUserDTO o1, LdapUserDTO o2) {
                return o1.safeGetMail().compareTo(o2.safeGetMail());
            }
        });

        // add the columns
        table.addColumn(login,      "Login");
        table.addColumn(domain,     "Domain");
        table.addColumn(fullName,   "Full name");
        table.addColumn(mail,       "Email");

        // set the width of the table and the columns
        table.setWidth("100%", true);
        table.setColumnWidth(login, 120.0, Unit.PX);
        table.setColumnWidth(domain, 180.0, Unit.PX);
        table.setColumnWidth(fullName, 250.0, Unit.PX);
        table.setColumnWidth(mail, 200.0, Unit.PX);
    }
    
    private void onSelectionChanged() {
        LdapUserDTO dto = tableSelModel.getSelectedObject();
        if (!isSelectEnabled && dto != null) {
            setOkEnabled(true);
            isSelectEnabled = true;
        } else if (isSelectEnabled && dto == null) {
            setOkEnabled(false);
            isSelectEnabled = false;
        }
    }
}
