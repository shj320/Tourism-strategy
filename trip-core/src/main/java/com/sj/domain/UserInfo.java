package com.sj.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sj.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("userinfo")
//@ApiModel(value="用户",description="平台注册用户模型")
public class UserInfo extends BaseDomain {

    public static final int GENDER_SECRET = 0; //保密
    public static final int GENDER_MALE = 1;   //男
    public static final int GENDER_FEMALE = 2;  //女
    public static final int STATE_NORMAL = 0;  //正常
    public static final int STATE_DISABLE = 1;  //冻结
    //@ApiModelProperty(value="昵称",name="nickName",dataType = "String",required = true)
    private String nickname;  //昵称
    private String phone;  //手机
    private String email;  //邮箱

    @JsonIgnore
    private String password; //密码
    private Integer gender = GENDER_SECRET; //性别
    private Integer level = 0;  //用户级别
    private String city;  //所在城市
    @TableField("headImgUrl")
    private String headImgUrl; //头像
    private String info;  //个性签名
    private Integer state = STATE_DISABLE; //状态

}
