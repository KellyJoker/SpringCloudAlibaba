package org.example.redis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 商品信息
 * @Author danxiaodong
 * @Date 2023/9/9 17:04
 **/
@TableName("product_info")
public class ProductInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //品牌id
    @TableField(value = "brand_id")
    private Long brandId;

    //商品分类id
    @TableField(value = "product_category_id")
    private Long productCategoryId;

    //商品属性分类id
    @TableField(value = "product_attribute_category_id")
    private Long productAttributeCategoryId;

    //商品名称
    @TableField(value = "product_name")
    private String productName;

    //商品图片
    @TableField(value = "product_pic")
    private String productPic;

    //货号
    @TableField(value = "product_sn")
    private String productSn;

    //删除状态：0->未删除；1->已删除
    @TableField(value = "delete_status")
    private String deleteStatus;

    //上架状态：0->下架；1->上架
    @TableField(value = "publish_status")
    private String publishStatus;

    //新品状态:0->不是新品；1->新品
    @TableField(value = "new_status")
    private String newStatus;

    //推荐状态；0->不推荐；1->推荐
    @TableField(value = "recommend_status")
    private String recommendStatus;

    //审核状态：0->未审核；1->审核通过
    @TableField(value = "verify_status")
    private String verifyStatus;

    //排序
    private String sort;

    //销量
    private String sale;

    //商品价格
    private String price;

    //促销价格
    @TableField(value = "promotion_price")
    private String promotionPrice;

    //赠送的成长值
    @TableField(value = "gift_growth")
    private String giftGrowth;

    //赠送的积分
    @TableField(value = "gift_point")
    private String giftPoint;

    //限制使用的积分数
    @TableField(value = "use_point_limit")
    private String usePointLimit;

    //副标题
    @TableField(value = "sub_title")
    private String subTitle;

    //市场价
    @TableField(value = "original_price")
    private String originalPrice;

    //单位
    private String unit;

    //商品重量，默认为克
    private String weight;

    //是否为预告商品：0->不是；1->是
    @TableField(value = "preview_status")
    private String previewStatus;

    //以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
    @TableField(value = "service_ids")
    private String serviceIds;

    //关键字
    private String keywords;

    //备注
    private String note;

    //画册图片，连产品图片限制为5张，以逗号分割
    @TableField(value = "album_pics")
    private String albumPics;

    //详细标题
    @TableField(value = "detail_title")
    private String detailTitle;

    //促销开始时间
    @TableField(value = "promotion_start_time")
    private Date promotionStartTime;

    //促销结束时间
    @TableField(value = "promotion_end_time")
    private Date promotionEndTime;

    //活动限购数量
    @TableField(value = "promotion_per_limit")
    private String promotionPerLimit;

    //促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
    @TableField(value = "promotion_type")
    private String promotionType;

    //品牌名称
    @TableField(value = "brand_name")
    private String brandName;

    //商品分类名称
    @TableField(value = "product_category_name")
    private String productCategoryName;

    //商品描述
    private String description;

    //详细描述
    @TableField(value = "detail_desc")
    private String detailDesc;

    //库存
    private String stock;

    //库存预警值
    @TableField(value = "low_stock")
    private String lowStock;

    //产品详情网页内容
    @TableField(value = "detail_html")
    private String detailHtml;

    //移动端网页详情
    @TableField(value = "detail_mobile_html")
    private String detailMobileHtml;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProductAttributeCategoryId() {
        return productAttributeCategoryId;
    }

    public void setProductAttributeCategoryId(Long productAttributeCategoryId) {
        this.productAttributeCategoryId = productAttributeCategoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(String recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(String promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getGiftGrowth() {
        return giftGrowth;
    }

    public void setGiftGrowth(String giftGrowth) {
        this.giftGrowth = giftGrowth;
    }

    public String getGiftPoint() {
        return giftPoint;
    }

    public void setGiftPoint(String giftPoint) {
        this.giftPoint = giftPoint;
    }

    public String getUsePointLimit() {
        return usePointLimit;
    }

    public void setUsePointLimit(String usePointLimit) {
        this.usePointLimit = usePointLimit;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPreviewStatus() {
        return previewStatus;
    }

    public void setPreviewStatus(String previewStatus) {
        this.previewStatus = previewStatus;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAlbumPics() {
        return albumPics;
    }

    public void setAlbumPics(String albumPics) {
        this.albumPics = albumPics;
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public Date getPromotionStartTime() {
        return promotionStartTime;
    }

    public void setPromotionStartTime(Date promotionStartTime) {
        this.promotionStartTime = promotionStartTime;
    }

    public Date getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(Date promotionEndTime) {
        this.promotionEndTime = promotionEndTime;
    }

    public String getPromotionPerLimit() {
        return promotionPerLimit;
    }

    public void setPromotionPerLimit(String promotionPerLimit) {
        this.promotionPerLimit = promotionPerLimit;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getLowStock() {
        return lowStock;
    }

    public void setLowStock(String lowStock) {
        this.lowStock = lowStock;
    }

    public String getDetailHtml() {
        return detailHtml;
    }

    public void setDetailHtml(String detailHtml) {
        this.detailHtml = detailHtml;
    }

    public String getDetailMobileHtml() {
        return detailMobileHtml;
    }

    public void setDetailMobileHtml(String detailMobileHtml) {
        this.detailMobileHtml = detailMobileHtml;
    }
}
