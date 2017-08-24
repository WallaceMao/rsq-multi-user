package com.rishiqing.domain.user

class TeamUser extends User{
    Boolean checkEmail = false
    String realName

    static belongsTo = [team:Team]
    static constraints = {
        realName nullable: true
    }

    def toMap() {
        return [
                id: this.id,
                username: this.username,
                notCommonUser:true,
        ]
    }
}
