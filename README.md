# Ticket Agency
This project is intended to run in [Wildfly](http://wildfly.org/) application server.

## Setup
1. Download Wildlfy at [widlfly.org](http://wildfly.org/downloads/) and follow [wildfly installation](https://docs.jboss.org/author/display/WFLY10/Getting+Started+Guide#GettingStartedGuide-Installation).
2. Set `JBOSS_HOME` environment variable point to your Wildlfy root folder from step before.
```
 $ export JBOSS_HOME=/path/to/wildfly
```

3. Compile and package whole project:
```
 $ mvn clean package
```

4. Using maven [wildfly maven plugin](https://docs.jboss.org/wildfly/plugins/maven/latest) start Wildfly and deploy application:
```
 $ mvn wildfly:start wildfly:deploy
```

5. Clean up:
```
 $ mvn wildfly:undeploy wildfly:shutdown
```

---
This project is based on book [Java EE 7 Development in Wildfly](https://www.packtpub.com/application-development/java-ee-7-development-wildfly) writen by **Michał Ćmil**, **Michał Matłoka**, **Francesco Marchioni**.
