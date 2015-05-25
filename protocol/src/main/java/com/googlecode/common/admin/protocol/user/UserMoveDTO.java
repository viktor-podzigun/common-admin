
package com.googlecode.common.admin.protocol.user;

import java.util.Arrays;


/**
 * Part of server request, containing users/groups lists.
 */
public final class UserMoveDTO {

    private int     systemId;
    private long    dstParentId;
    
    private int[]   users;
    private long[]  groups;
    

    public UserMoveDTO() {
    }

    public UserMoveDTO(long dstParentId) {
        this.dstParentId = dstParentId;
    }
    
    public int getSystemId() {
        return systemId;
    }
    
    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public long getDstParentId() {
        return dstParentId;
    }
    
    public void setDstParentId(long dstParentId) {
        this.dstParentId = dstParentId;
    }
    
    public int[] getUsers() {
        return users;
    }
    
    public void setUsers(int[] users) {
        this.users = users;
    }
    
    public long[] getGroups() {
        return groups;
    }
    
    public void setGroups(long[] groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{systemId: " + systemId
                + ", dstParentId: " + dstParentId
                + (users != null ? ", users: " + Arrays.toString(users) : "")
                + (groups != null ? ", groups: " + Arrays.toString(groups) : "")
                + "}";
    }
}
