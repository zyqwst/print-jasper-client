rem 打印启动入口
@echo off
cd %~dp0
start javaw -Dfile.encoding=UTF-8 -jar  PrintClient-0.0.0.jar %1