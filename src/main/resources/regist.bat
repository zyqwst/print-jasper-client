rem 注册start.bat到注册表
@echo off
set v=%~dp0
echo Windows Registry Editor Version 5.00 >regist.reg
echo [HKEY_CLASSES_ROOT\albertprint] >>regist.reg
echo "URL Protocol"="%v:\=\\%start.bat" >>regist.reg
echo @="AlbertPrintProtocol" >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\DefaultIcon] >>regist.reg
echo @="%v:\=\\%start.bat,1" >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\shell] >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\shell\open] >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\shell\open\command] >>regist.reg
echo @="\"%v:\=\\%start.bat\" \"%%1\"" >>regist.reg
regedit /s %v%regist.reg
del /q regist.reg