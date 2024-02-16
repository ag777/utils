@echo off
setlocal enabledelayedexpansion

:: 设置要读取的文件路径
set "propertiesFile=src\main\resources\utils.properties"

:: 检查文件是否存在
if not exist "%propertiesFile%" (
    echo 指定的文件不存在：%propertiesFile%
    exit /b 1
)

:: 初始化变量以存储versionName的值
set "versionNameValue="

:: 读取文件并查找versionName配置项
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
    echo 未找到versionName配置项。
    exit /b 1
) else (
    echo versionName的值是：%versionNameValue%
    :: 构建jar文件路径
    set jarFile=target\utils-!versionNameValue!.jar
    echo jar包的路径是：!jarFile!
    
    if not exist "!jarFile!" (
        echo 指定的jar文件不存在：!jarFile!
        echo 正在尝试通过执行 mvn clean install 来生成jar文件...
        :: 执行mvn clean install命令
        call mvn clean install
        echo 重新检查jar文件是否存在...
        if not exist "!jarFile!" (
            echo 再次检查后，仍然没有找到jar文件：!jarFile!
            exit /b 1
        )
    )
    
    :: 构建mvn命令并存储在变量中
    set mvnCommand=mvn install:install-file -Dfile="!jarFile!" -DgroupId=com.ag777 -DartifactId=utils -Dversion=!versionNameValue! -Dpackaging=jar
    
    :: 输出即将执行的mvn命令
    echo 即将执行的命令是:
    echo !mvnCommand!
    
    :: 询问用户是否执行
    set /p Execute="是否执行此命令？(Y/N):"
    if /i "!Execute!"=="Y" (
        :: 执行mvn命令
        !mvnCommand!
    ) else (
        echo 已取消执行。
    )
)

endlocal