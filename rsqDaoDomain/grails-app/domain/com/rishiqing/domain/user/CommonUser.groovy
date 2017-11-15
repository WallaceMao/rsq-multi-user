package com.rishiqing.domain.user

class CommonUser extends User{
    transient grailsApplication
    //用户应该显示的名字，一次session请求对应一个
    transient def sn = null
    //用户所属部门，一次session请求对应一个
    transient def department = null
    /** 用户昵称 */
	String realName = "rsq_${getLongRandomNum(7)}"
    /** 用户手机号码 */
    String phoneNumber
    /** 关注人列表 */
	String superviseConfig
    /**
     * 向哪些人公开的列表id <br>
     *     <p> 以如下形式标识：”-1-2-3-“，如果是对所有人公开，则为“all”，如果为仅自己，则为“none” </p>
     */
    String openTo = "all"
    /**
     * 帐号由哪个端创建 <br>
     *     <p> 帐号可以由 android phone\iphone\browser\android pad\ipad 端创建 </p>
     */
    String createdByClient = "web"
    /** 用户所属的公司 */
	Team team
    /** 是否验证邮箱 */
    Boolean checkEmail = false
    /** 不知道 */
    Date dateTeamFunctionExpired
    /** 头像位置 */
    String avatarURL
    /** 用户的 qq */
    String qq
    /** 用户的性别 */
    String sex
    /** 用户的生日 */
    Date birth
    /** 用户在公司的职位 */
    String professional
    /** 个人充值点数 */
    Long dian = 0
    /** 个人充值类型 */
    String personVipType
    /**
     * 用来标记是否隐藏引导页面 <br>
     *     0：显示引导页面
     *     1：隐藏引导页面
     */
    Long userGuideFlag = 0
    /** 最后一次发帖的时间.bbs中新增字段 */
    Date lastPublishTopic
    /** 标记注册时来自的渠道商 */
    String fromAgent
    /**  */
    Boolean isCloseAd = false
    /**  */
    Boolean autoGenerate = false
    /**  */
    String  preContext = "time"
    /**  */
    Boolean summaryRemark    = false
    /**  */
    String todoPage = "day"
    /**  */
    String userFrom
    /** qq 登录唯一标识 */
    String qqOpenId
    /** 新浪登录唯一标识 */
    String sinaOpenId
    /**
     * 微信登录唯一标识 <br>
     *     <p> 同一设备登录微信号时，这个id值是不变化的，当更换设备后，这个id的值可能变化，
     *     即无法标识微信帐号的唯一性。故微信帐号的登录操作需要使用 unionID  </p>
     */
    String wxOpenId
    /**  */
    String publicWxOpenId
    /**  */
    String xmOpenid
    /**  */
    String systemAvatar
    /**  */
    Boolean showGetVip = false
    /** 已赠送过的点数 */
    Long giftDian = 0
    /**
     * 微信帐号唯一标识 <br>
     *     <p> 每一个微信帐号有一个唯一的 unionId 进行标识，微信登录帐号时使用 </p>
     */
    String unionId
    /**  */
    transient  def isOauthLogin = false
    /** 邀请的手机用户登陆后提示是否加入团队 */
    String joinToken
    /** 邀请的用户加入到指定部门 */
    @Deprecated
    Long inviteDeptId
    /** 个人设置表，以后的关于用户设置的数据记录在这张表里 */
    Long setUp
    /** 默认文集 */
    Long defaultCorpus
    /**  */
    Integer readCollageTaskCount = 30
    /**  */
    String fromClient
    /** 头像时间戳 */
    Long avatarTimestamp = new Date().getTime();

    CommonUser(String username, String password, String realName) {
        super(username, password)
        this.realName = realName
    }
/** 一对多关系 */
//	static hasMany = [
//
//    ]
//    defaultKanban: 该用户在看板页面默认显示的看板
    /** 一对一关系 */
    static belongsTo = [team:Team]
//    static mappedBy = [
//    ]
    /** 惯例配置 */
    static mapping = {
        openTo defaultValue: "'all'"
        readCollageTaskCount defaultValue: "30"
        avatarTimestamp defaultValue : "1500429425" // 给定一个默认值
    }
    /** 约束配置 */
    static constraints = {
//        用户名验证，2~15位，以字母开头，从第二位起以数字、下划线或字母
        username matches: /^[a-zA-Z0-9_+.-]+\@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/
		superviseConfig nullable:true
//		realName blank: false,nullable: true
        phoneNumber nullable: true,unique: true
        team nullable: true
        dateTeamFunctionExpired nullable:true
        avatarURL nullable: true
        openTo nullable: true
        lastPublishTopic nullable: true
        fromAgent nullable: true
        isCloseAd nullable: true
        dian nullable: true
        autoGenerate nullable: true
        preContext nullable:true
        summaryRemark nullable:true
        todoPage nullable:true
        userFrom  nullable:true
        qqOpenId nullable:true
        sinaOpenId nullable:true
        wxOpenId nullable:true
        xmOpenid nullable:true
        systemAvatar  nullable:true
        showGetVip nullable:true
        giftDian nullable:true
        qq nullable: true
        sex nullable: true
        birth nullable: true
        professional nullable: true
        unionId nullable: true
        joinToken nullable: true
        publicWxOpenId nullable: true
        setUp nullable: true
        defaultCorpus nullable: true
        userGuideFlag nullable: true
        fromClient nullable: true
        inviteDeptId nullable: true
        personVipType nullable :true
        avatarTimestamp nullable : true
    }

    /**
     * 生成制定位数的随机数，给位数，获取这个位数长度的随机码
     * @param places 位数
     */
    public static Long getLongRandomNum(Integer places){
        StringBuffer val = new StringBuffer("");
        Random random = new Random();
        for(int i = 0; i < places; i++) {
            val.append(String.valueOf(random.nextInt(10)));
        }
        return val.toString().toLong();
    }
}