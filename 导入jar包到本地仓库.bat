@echo off
setlocal enabledelayedexpansion

:: ����Ҫ��ȡ���ļ�·��
set "propertiesFile=src\main\resources\utils.properties"

:: ����ļ��Ƿ����
if not exist "%propertiesFile%" (
    echo ָ�����ļ������ڣ�%propertiesFile%
    exit /b 1
)

:: ��ʼ�������Դ洢versionName��ֵ
set "versionNameValue="

:: ��ȡ�ļ�������versionName������
for /f "tokens=1* delims==" %%a in ('type "%propertiesFile%" ^| findstr /i "versionName"') do (
    set "key=%%a"
    set "value=%%b"
    set "key=!key: =!"
    set "value=!value: =!"
    
    if "!key!"=="versionName" (
        set "versionNameValue=!value!"
        goto foundVersionName
    )
)

:foundVersionName
if not defined versionNameValue (
    echo δ�ҵ�versionName�����
    exit /b 1
) else (
    echo versionName��ֵ�ǣ�%versionNameValue%
    :: ����jar�ļ�·��
    set jarFile=target\utils-!versionNameValue!.jar
    echo jar����·���ǣ�!jarFile!
    
    if not exist "!jarFile!" (
        echo ָ����jar�ļ������ڣ�!jarFile!
        echo ���ڳ���ͨ��ִ�� mvn clean install ������jar�ļ�...
        :: ִ��mvn clean install����
        call mvn clean install
        echo ���¼��jar�ļ��Ƿ����...
        if not exist "!jarFile!" (
            echo �ٴμ�����Ȼû���ҵ�jar�ļ���!jarFile!
            exit /b 1
        )
    )
    
    :: ����mvn����洢�ڱ�����
    set mvnCommand=mvn install:install-file -Dfile="!jarFile!" -DgroupId=com.ag777 -DartifactId=utils -Dversion=!versionNameValue! -Dpackaging=jar
    
    :: �������ִ�е�mvn����
    echo ����ִ�е�������:
    echo !mvnCommand!
    
    :: ѯ���û��Ƿ�ִ��
    set /p Execute="�Ƿ�ִ�д����(Y/N):"
    if /i "!Execute!"=="Y" (
        :: ִ��mvn����
        !mvnCommand!
    ) else (
        echo ��ȡ��ִ�С�
    )
)

endlocal