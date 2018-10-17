@echo off
call %~dp0\_oimsetenv.cmd
groovy -DXL.HomeDir=. -Djava.security.auth.login.config=%~dp0\script\conf\authwl.conf %~dp0\script\oimtool.groovy %*
