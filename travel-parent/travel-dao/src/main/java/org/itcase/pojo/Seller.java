package org.itcase.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller implements Serializable {
    /** 
    * 主键
    */
    private Long id;

    /** 
    * 供应商名称
    */
    private String sellerName;

    /** 
    * 联系方式
    */
    private String telephone;

    /** 
    * 地址
    */
    private String address;

    /** 
    * 账户
    */
    private String username;

    /** 
    * 密码
    */
    private String password;

    /** 
    * 生日
    */
    private Date birthday;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}