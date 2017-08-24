package com.rishiqing.domain.user

class UserJoinTeamHistory {

    transient public static String TYPE_CREATE_TEAM = "c"
    transient public static String TYPE_JOIN_TEAM = "j"
    transient public static String TYPE_DISMISS_TEAM = "d"
    transient public static String TYPE_QUIT_TEAM = "q"
    transient public static String TYPE_REMOVE_TEAM = "r"

    Date dateCreated

    String type

    User user
    SuperUser superUser
    Team team

    static constraints = {
        type inList: [
                TYPE_CREATE_TEAM,
                TYPE_JOIN_TEAM,
                TYPE_DISMISS_TEAM,
                TYPE_QUIT_TEAM,
                TYPE_REMOVE_TEAM
        ]
    }
}
