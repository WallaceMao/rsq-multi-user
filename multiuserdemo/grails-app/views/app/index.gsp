<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>todo app</title>
    <asset:stylesheet src="base.css" />
    <asset:stylesheet src="rsq.css" />
    <asset:javascript src="vue.min.js" />
    <asset:javascript src="vue-resource.js" />
    <style type="text/css">
        .nav_header {position: fixed; height:50px;line-height:50px;background:#fff;overflow:hidden;width:100%;z-index:1;}
        .main_app { position: relative;padding-top:50px; overflow:hidden; width: 100%;height: 100%;}
        .main_app > * { box-sizing: border-box; float: left;}
        .corp_info_panel {
            width: 60%;height:100%;
            padding: 50px;
            background: rgba(255, 0, 0, 0.3)
        }
        .corp_list_panel {
            width: 40%;height:100%;
            padding: 20px;
            background: rgba(0, 255, 0, 0.3)
        }
        .corp_list {list-style: none;}
        .corp_list li {margin-top:10px;cursor:pointer;}
        .corp_list li.show_hover:hover {color:#0ff;}
        .corp_create_panel {
            position: absolute;
            border: solid 1px #00f;background: #fff;
            width: 400px; margin-left: -200px;
            height: 400px; margin-top: -200px;
            left: 50%;top:50%;
        }
        .corp_edit_field {padding: 20px;}
        .corp_edit_field > * {display: block;margin-bottom: 20px;}
    </style>
    <script type="text/javascript">
        var info = {
            basePath: '${request.contextPath}'
        }
    </script>
</head>

<body>
    <div class="mainBody">
        <div class="nav_header">
            <div style="float:left;height:100%;">
                <a href="http://grails.org"><asset:image src="grails_logo.png" alt="Grails"/></a>
            </div>

            <div style="float:right;margin-right: 20px;height:100%;">
                <g:link controller="logout">logout</g:link>
            </div>
            <div style="float:right;margin-right:20px;height:100%;">
                <div>用户名: ${user.username}</div>
            </div>
            <div style="float:right;margin-right:20px;height:100%;">
                <g:if test="${user.team}">
                    <div>公司名: ${user.team.name}</div>
                </g:if>
                <g:else>
                    <div>个人账号</div>
                </g:else>
            </div>
        </div>
        <div id="mainApp" class="main_app">
            <div class="corp_info_panel">
                <div v-if="me.team">
                    当前团队：{{me.team.name}}
                    <button @click="quitCurrentTeam">退出当前团队</button>
                </div>
                <div v-else>
                    个人用户
                </div>
            </div>
            <div class="corp_list_panel">
                <ul class="corp_list">
                    <li v-for="user in siblings" :key="user.id" @click="switchTeam(user)"
                        :class="{show_hover: me.id != user.id}">
                        <p v-if="user.team">{{user.team.name}}<span v-if="isMe(user)">(当前用户)</span></p>
                        <p v-else>{{user.username}}<span v-if="isMe(user)">(当前用户)</span></p>
                    </li>
                </ul>
                <button @click="showEditTeam" v-if="me.team">编辑团队</button>
                <button @click="showCreateTeam" v-else>创建团队</button>
            </div>
            <div class="corp_create_panel" v-show="isShowEdit">
                <div class="corp_edit_field">
                    <label for="editTeamName">团队名称</label>
                    <input type="text" v-model="teamEdit.name" id="editTeamName" />
                    <button @click="submitTeam">确定</button>
                    <button @click="closeEditTeam">关闭</button>
                </div>
            </div>
        </div>
    </div>
<asset:javascript src="rsq.js" />
</body>
</html>