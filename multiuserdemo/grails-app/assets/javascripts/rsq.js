/**
 * Created by czip on 2017/8/25.
 */

var rsqAction = {
    fetchUsers: function(cb){
        Vue.http.get(info.basePath + '/user/fetchSiblings').then(function(res){
            var json = res.body;
            if(!json.errcode){
                cb(json.result)
            }
        }, function(err){
            alert(err);
        });
    },
    fetchMe: function(cb){
        Vue.http.get(info.basePath + '/user/fetchMe').then(function(res){
            var json = res.body;
            if(!json.errcode){
                cb(json.result)
            }
        }, function(err){
            alert(err);
        });
    },
    createTeam: function(params, cb){
        Vue.http.post(info.basePath + '/user/createTeam', params).then(function(res){
            var json = res.body;
            if(!json.errcode){
                cb(json.result)
            }
        }, function(err){
            alert(err);
        });
    },
    updateTeam: function(params, cb){
        alert('not implemented')
    },
    switchUser: function(params, cb){
        Vue.http.post(info.basePath + '/app/switchUser', params).then(function(res){
            console.log(JSON.stringify(res));
            var json = res.body
            //  注意使用success，而不是errcode
            if(json.success){
                window.location.reload();
            }
        },function(err){
            alert(err);
        });
    },
    quitTeam: function(params, cb){
        Vue.http.post(info.basePath + '/user/quitTeam', params).then(function(res){
            var json = res.body
            if(!json.errcode){
                cb(json);
                window.location.reload();
            }
        },function(err){
            alert(err);
        });
    }
};

var app = new Vue({
    data: {
        me: {},
        siblings: [],
        isShowEdit: false,
        teamEdit: {}
    },
    computed: {},
    methods: {
        isMe: function(user){
            return user.id == this.me.id;
        },
        showCreateTeam: function(){
            this.isShowEdit = true;
        },
        showEditTeam: function(){
            this.teamEdit = this.me.team;
            this.isShowEdit = true;
        },
        closeEditTeam: function () {
            this.isShowEdit = false;
            this.teamEdit = {};
        },
        submitTeam: function(){
            if(!this.teamEdit.name){
                return alert('请输入团队名称！')
            }
            if(this.teamEdit.id){
                rsqAction.updateTeam(this.teamEdit, function(res){

                });
            }else{
                rsqAction.createTeam(this.teamEdit, function(res){

                });
            }
        },
        switchTeam: function(user){
            if(this.isMe(user)){
                return;
            }
            rsqAction.switchUser(user, function(res){});
        },
        quitCurrentTeam: function(){
            var that = this;
            rsqAction.quitTeam(this.me.team, function(res){
            });
        }
    },
    mounted: function(){
        var that = this;
        rsqAction.fetchMe(function(res){
            that.me = res;
        });
        rsqAction.fetchUsers(function(res){
            that.siblings = res;
        });
    }
});

function onHashChange(){

}

window.addEventListener('hashchange', onHashChange);
onHashChange();

app.$mount('#mainApp');