
package com.googlecode.common.admin.client.ui.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gwt.user.cellview.client.TextColumn;
import com.googlecode.common.client.ui.PagingTablePanel;
import com.googlecode.common.client.util.DateHelpers;
import com.googlecode.common.admin.protocol.user.UserDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;


/**
 * Users grid panel.
 */
public abstract class UserGridPanel extends PagingTablePanel<UserDTO> {

    
    public UserGridPanel(boolean allUsers, boolean systemUsers) {
        initColumns(allUsers, systemUsers);
    }
    
    private void initColumns(boolean allUsers, boolean systemUsers) {
        TextColumn<UserDTO> login = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO dto) {
                return dto.getLogin();
            }
        };
        
        TextColumn<UserDTO> created = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO dto) {
                return DateHelpers.formatDate(dto.getCreated());
            }
        };
        
        TextColumn<UserDTO> lastLogin = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO dto) {
                Date lastLogin = dto.getLastLogin();
                return (lastLogin != null ? 
                        DateHelpers.formatDate(lastLogin) : null);
            }
        };

        TextColumn<UserDTO> company = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO dto) {
                String companyString = dto.getCompanyName();
                return (companyString != null ? companyString : "");
            }
        };

        TextColumn<UserDTO> group = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO dto) {
                return dto.getParentName();
            }
        };
        
        TextColumn<UserDTO> active = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO dto) {
                return (String.valueOf(dto.isActive()));
            }
        };
        
        // add the columns
        table.addColumn(login,      "Login");
        table.addColumn(active,     "Active");
        table.addColumn(created,    "Created");
        table.addColumn(lastLogin,  "Last logged in");

        if (systemUsers) {
            table.addColumn(company, "Company");
            if (allUsers) {
                table.addColumn(group, "Group");
            }
        } else if (allUsers) {
            table.addColumn(company, "Company");
        }
    }
    
    public void refreshData(int start, int length, UserListResponse resp) {
        List<UserDTO> dataList = resp.safeGetDataList();
        
        setRangeData(resp.getTotalCount(), start, length, dataList);
    }

    public void addUser(UserDTO dto) {
        // need to create new list here, because previous is read-only
        List<UserDTO> list = new ArrayList<UserDTO>(getList());
        list.add(dto);
        
        setList(list);
        setSelected(dto, true);
    }

}
