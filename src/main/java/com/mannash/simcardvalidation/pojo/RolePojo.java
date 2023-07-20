package com.mannash.simcardvalidation.pojo;

public class RolePojo {
    private int id;
    private String roleName;

    @Override
    public String toString() {
        return "RolePojo{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


}
