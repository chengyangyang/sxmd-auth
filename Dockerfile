
# 基于Java 8 进行构建
FROM openjdk:8-jre
# 接收项目mvn打包接受的参数,这个参数只配合 maven 打包镜像的时候使用
# maven 打包使用方式 mvn clean install -Ddockerfile.skip=false
# 会自动根据当前Dockerfile 构建镜像
ARG JAR_FILE
# 在镜像中创建一个目录
WORKDIR /worker
# 系统时间进行统一
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENV appOriginName ${JAR_FILE}
# 将jar 文件复制到工作目录中,并将相应的依赖和配置导入进去
ADD ./target/config/  /worker/config/
ADD ./target/lib/  /worker/lib/
ADD ${JAR_FILE}  /worker/app.jar
VOLUME ["/worker/config"]
# 启动java文件
CMD ["java","-jar","/worker/app.jar","--spring.profiles.active=dev"]