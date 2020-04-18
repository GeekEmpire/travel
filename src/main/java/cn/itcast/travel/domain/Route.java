package cn.itcast.travel.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 旅游线路商品实体类
 */
public class Route implements Serializable {

    private int rid;//线路id，必输
    private String rname;//线路名称，必输
    private double price;//价格，必输
    private String routeIntroduce;//线路介绍
    private String rflag;   //是否上架，必输，0代表没有上架，1代表是上架
    private String rdate;   //上架时间
    private String isThemeTour;//是否主题旅游，必输，0代表不是，1代表是
    private int count;//收藏数量
    private int cid;//所属分类，必输
    private String rimage;//缩略图
    private Integer sid;//所属商家
    private Integer sourceId;//数据的来源用户id
    private String detail; //详情

    private Category category;//所属分类
    private Seller seller;//所属商家
    private User user;//发表用户
    private List<RouteImg> routeImgList;//商品详情图片列表



    /**
     * 无参构造方法
     */
    public Route(){}


    /**
     * 有参构造方法
     * @param rid
     * @param rname
     * @param price
     * @param routeIntroduce
     * @param rflag
     * @param rdate
     * @param isThemeTour
     * @param count
     * @param cid
     * @param rimage
     * @param sid
     * @param sourceId
     * @param detail
     */
    public Route(int rid, String rname, double price, String routeIntroduce, String rflag, String rdate, String isThemeTour, int count, int cid, String rimage, Integer sid, Integer sourceId, String detail) {
        this.rid = rid;
        this.rname = rname;
        this.price = price;
        this.routeIntroduce = routeIntroduce;
        this.rflag = rflag;
        this.rdate = rdate;
        this.isThemeTour = isThemeTour;
        this.count = count;
        this.cid = cid;
        this.rimage = rimage;
        this.sid = sid;
        this.sourceId = sourceId;
        this.detail = detail;
    }


    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRouteIntroduce() {
        return routeIntroduce;
    }

    public void setRouteIntroduce(String routeIntroduce) {
        this.routeIntroduce = routeIntroduce;
    }

    public String getRflag() {
        return rflag;
    }

    public void setRflag(String rflag) {
        this.rflag = rflag;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getIsThemeTour() {
        return isThemeTour;
    }

    public void setIsThemeTour(String isThemeTour) {
        this.isThemeTour = isThemeTour;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getRimage() {
        return rimage;
    }

    public void setRimage(String rimage) {
        this.rimage = rimage;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<RouteImg> getRouteImgList() {
        return routeImgList;
    }

    public void setRouteImgList(List<RouteImg> routeImgList) {
        this.routeImgList = routeImgList;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
