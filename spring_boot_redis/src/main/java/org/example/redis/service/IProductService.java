package org.example.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.redis.entity.ProductInfo;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/9 17:03
 **/
public interface IProductService extends IService<ProductInfo> {
    /**
     * 添加商品
     * @param productInfo
     * @return
     */
    String create(ProductInfo productInfo);

    /**
     * 修改商品
     * @param productInfo
     * @return
     */
    String update(ProductInfo productInfo);

    /**
     * 查询商品
     * @param productId
     * @return
     */
    ProductInfo getProductInfo(Long productId);
}
