package cn.xmz.travel.domain;

public class Manager {
    private String mname;
    private String password;
    private Integer mid;

    public Manager() {
    }

    public Manager(String mname, String password, Integer mid) {
        this.mname = mname;
        this.password = password;
        this.mid = mid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
