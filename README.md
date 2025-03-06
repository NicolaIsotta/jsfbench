# jsfbench

Inspired by https://github.com/lu4242/performance-comparison-java-web-frameworks

I took the JSF application, ported it to CDI and created profiles for MyFaces 4 and Mojarra 4.
The JMeter config is preconfigured for 100 threads and 200 loops.

Its also quite interesting compared to another case (http://tandraschko.blogspot.com/2019/03/way-to-myfaces-30.html), where the results of Mojarra 2.3 and MyFaces 2.3 were almost the same and MyFaces 2.3-next was ~15% faster as both Mojarra 2.3 and MyFaces 2.3.
The big difference is that this benchmark simulates a real-world application with Hibernate and a bit business logic.

NOTE:
MyFaces and Mojarra are both configured for better performance. It does not test stateless view or MyFaces ViewPooling. There should not be a big difference.

## Results 2024-03-06

|            | Average     | Median      | 90th pct    | Throughput |
| --- | ---: | ---: | ---: | ---: |
| Mojarra 4  |      2.56ms |      3.00ms |      4.00ms |    1789.30 |
| MyFaces 4  |      1.30ms |      2.00ms |      3.00ms |    2027.43 |

Test configuration:
- AMD Ryzen 7 8845HS w/ Radeon 780M Graphics 3.80 GHz
- 32 GB DDR5 6400 MHz
- Windows 11 Home 26100.3194
- OpenJDK Runtime Environment Temurin-17.0.12+7 (build 17.0.12+7)

## How to run it? 

Requires Java >= 11 and Maven to build the project.

### Configure Tomcat
1) download Tomcat 10.1.X
2) extract it 2 times (tomcat-myfaces, tomcat-mojarra)
3) copy the server.xml file from ./tomcat/conf to all 2 instances, which disables autoDeployment and accessLogValve

### Build the application
1) go the ./application
2) mvn clean package -Pmyfaces, copy the generated war and extract it to ./tomcat-myfaces/webapps/jsfbench
4) mvn clean package -Pmojarra, copy the generated war and extract it to ./tomcat-mojarra/webapps/jsfbench

### Run via JMeter
1) download JMeter 5.6+
2) Increase MaxUserPort if you are on Windows: https://deploymentresearch.com/research/post/532/fix-for-windows-10-exhausted-pool-of-tcp-ip-ports
3) run one of the tomcats (e.g. ./tomcat-myfaces/bin/startup.bat)
4) run jmeter (./jmeter/bin/jmeter.bat)
5) open the ./application.jmx via JMeter
6) run it once for a warmup
7) clean the results
8) run again and see the results under Test Plan/Thread Group/Aggregate Graph
