package com.rishiqing.domain.user

class Team {

    long id
    /** 记录创建时间 */
    Date dateCreated
    /** 公司地址 */
    String address
    /** 公司名 */
    String name               //中文名称
    /** 公司邮箱 */
    String email
    /** 公司电话 */
    String phoneNumber
    /** 创建者的 id */
    String createdBy         //由哪个普通用户创建,存的用户id
    /** 公司联系人 */
    String contacts
    /** 公司的 token 码，用来标识唯一的公司信息 */
    String joinToken = UUID.randomUUID().toString().replaceAll('-', '')  //用于加入团队的链接
    /**
     * 点数 <br>
     *     <p> 用来标记企业会员属性。每个dian 代表一天，例如：百年版用户，dian = 36500 </p>
     */
    Integer dian = 0
    /** 企业类型标识 <br>
     * <p> 用来标识企业购买的类型，显示钻石，购买记录等属性 </p>
     * <p> 现存企业用户标识 （可以使用的）:
     * 更新时间　2017-04-21 10:38:51
     * "teamType4"   // 众筹版本 （无限制永久版）
     * "teamType10"  // 企业高级版（20人限制）一年期
     * "teamType11"  // 企业高级版（20人限制）两年期
     * "teamType12"  // 企业高级版（20人限制）三年期
     * "teamType13"  // 企业高级版（无限制）一年期
     * "teamType14"  // 企业高级版（无限制）两年期
     * "teamType15"  // 企业高级版（无限制）三年期
     * "teamType16"  // 企业永久版 （无限制） 18000
     * "teamType17"  // 企业高级版（无限制）六个福利+5年服务 19999
     * "teamType18"  // 企业高级版（无限制）2天内训+3年服务 27600
     * "teamType19"  // 企业高级版（无限制）2天内训+3年服务 28560
     * 'teamType20'  // 企业按人付费版 一年期
     * 'teamType21'  // 企业按人付费版 两年期
     * 'teamType22'  // 企业按人付费版 三年期
     * 'teamType23'  // 企业不限制人数 一年期
     * 'teamType24'  // 企业不限制人数 两年期
     * 'teamType25'  // 企业不限制人数 三年期
     * </p>
     */
    String teamVipType
    /** 企业当前人数 */
    Integer userLimit = 10;
    /** 公司 logo */
    Long logo
    Long teamLogoFile
    /** 关注模块 */
    String attentionModule
    /** 意见建议 */
    String opinion
    /** 生存状态 */
    String survivalStatus
    /** 行业 */
    String industry
    /** 地区 */
    String area
    /** 客户备注 */
    String customRemark;
    /** */
    Boolean isShowInvite = false
    /** */
    String saleSchedule
    /**  */
    String salePerson
    /** 省市区 */
    String region
    /** 规模 */
    String size
    /** 删除公司时公司里所有的员工的id */
    String deletedTeamUser
    /** 公司设置表　*/
    Long teamSetup
    /** 公司简介 */
    String introduction = "";

    /** 自动注册公司记录的第三方数据 */
    String outerId
    String fromApp

    Team(String name) {
        this.name = name
    }

    static hasMany = [
            users:CommonUser]
    static hasOne = [owner:TeamUser]

    static mapping = {
        id name: "id", column: "id"
    }

    static constraints = {
        name nullable: true
        email email: true,nullable: true
        phoneNumber nullable: true
        owner nullable: true
        address nullable: true
        logo nullable: true
        contacts nullable: true
        attentionModule nullable: true
        opinion nullable: true
        survivalStatus nullable: true
        industry nullable: true
        area nullable: true
        customRemark nullable: true
        createdBy nullable: true
        saleSchedule nullable: true
        salePerson nullable: true
        region nullable: true
        size nullable: true
        deletedTeamUser nullable: true
        teamLogoFile nullable: true
        teamSetup nullable: true
        outerId nullable: true
        fromApp nullable: true
        teamVipType nullable: true
        teamVipType inList: [
                'teamType4', // 众筹版本 （无限制永久版）
                'teamType10',// 企业高级版（20人限制）一年期
                'teamType11',// 企业高级版（20人限制）两年期
                'teamType12',// 企业高级版（20人限制）三年期
                'teamType13',// 企业高级版（无限制）一年期
                'teamType14',// 企业高级版（无限制）两年期
                'teamType15',// 企业高级版（无限制）三年期
                'teamType16',// 企业永久版 （无限制） 18000
                'teamType17',// 企业高级版（无限制）六个福利+5年服务 19999
                'teamType18',// 企业高级版（无限制）2天内训+3年服务 27600
                'teamType19',// 企业高级版（无限制）2天内训+3年服务 28560
                // 改版 2017-04-21 11:36:40
                'teamType20',// 企业按人付费版 一年期
                'teamType21',// 企业按人付费版 两年期
                'teamType22',// 企业按人付费版 三年期
                'teamType23',// 企业不限制人数 一年期
                'teamType24',// 企业不限制人数 两年期
                'teamType25' // 企业不限制人数 三年期
        ]
        userLimit nullable :true
    }
}
