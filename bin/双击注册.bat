@echo off
echo =====================注册打印控件===================
echo 注册打印控件到注册表，360等杀毒软件误报请“允许运行”
echo ====================================================
echo 请按任意键开始注册
pause>nul
set v=%~dp0
echo Windows Registry Editor Version 5.00 >regist.reg
echo [HKEY_CLASSES_ROOT\albertprint] >>regist.reg
echo "URL Protocol"="%v:\=\\%PrintClient.exe" >>regist.reg
echo @="AlbertPrintProtocol" >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\DefaultIcon] >>regist.reg
echo @="%v:\=\\%PrintClient.exe,1" >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\shell] >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\shell\open] >>regist.reg
echo [HKEY_CLASSES_ROOT\albertprint\shell\open\command] >>regist.reg
echo @="\"%v:\=\\%PrintClient.exe\" \"%%1\"" >>regist.reg
regedit /s %v%regist.reg
del /q regist.reg
echo 打印控件注册成功,按任意键退出
pause>nul