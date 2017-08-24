package com.rishiqing.domain.user

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired = false
	boolean accountLocked = false
	boolean passwordExpired = false

	Date dateCreated
	Date lastLoginDate //最后一次的登录时间
	long loginCount = 0L   //登录次数

	//自动注册公司记录的第三方数据
	String outerId

	static belongsTo = [
			superUser: SuperUser,
	        team: Team
	]

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
		team nullable: true
		outerId nullable: true
		lastLoginDate nullable: true
		superUser nullable: true
	}

	static mapping = {
		password column: '`password`'
		loginCount defaultValue: 0
	}

	User(String username, String password) {
		this()
		this.username = username
		this.password = password
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
