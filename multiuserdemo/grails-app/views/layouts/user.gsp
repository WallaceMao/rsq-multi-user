<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>用户页面</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
		<asset:stylesheet src="semantic.min.css"/>
		<asset:javascript src="jquery-3.2.1.min.js"/>
		<asset:javascript src="semantic.min.js"/>
		<g:layoutHead/>
	</head>
	<body>
	<div>
		<div style="height:50px;line-height:50px;background:#0ff;overflow:hidden;">
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
		<div style="margin:10px;">
			<div class="ui grid">
				<div class="four wide column">
					<div class="ui vertical fluid tabular menu">
						<a class="item ${'index'.equals(tag)?'active':''}" href="${createLink(controller: 'userInfo', action: 'index')}">
							基本信息
						</a>
						<a class="item ${'listTeam'.equals(tag)?'active':''}" href="${createLink(controller: 'userInfo', action: 'listTeam')}">
							列出公司
						</a>
						<a class="item ${'createTeam'.equals(tag)?'active':''}" href="${createLink(controller: 'userInfo', action: 'createTeam')}">
							创建公司
						</a>
						<a class="item ${'quitTeam'.equals(tag)?'active':''}" href="${createLink(controller: 'userInfo', action: 'quitTeam')}">
							退出公司
						</a>
						<a class="item ${'dismissTeam'.equals(tag)?'active':''}" href="${createLink(controller: 'userInfo', action: 'dismissTeam')}">
							解散公司
						</a>
						<a class="item ${'setMainTeam'.equals(tag)?'active':''}" href="${createLink(controller: 'userInfo', action: 'setMainTeam')}">
							设置主公司
						</a>
						<a class="item ${'invite'.equals(tag)?'active':''}" href="${createLink(controller: 'userInfo', action: 'invite')}">
							邀请成员
						</a>
					</div>
				</div>
				<div class="twelve wide stretched column">
					<div class="ui segment">
						<g:layoutBody/>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="footer" role="contentinfo">

				</div>
			</div>
		</div>
	</div>
	</body>
</html>
