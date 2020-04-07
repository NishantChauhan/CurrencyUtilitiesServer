FROM maven:3.6.3-jdk-11  AS BUILD
WORKDIR /currency-server
COPY . /currency-server
RUN mvn -B -DskipTests clean package && mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:12-alpine AS PACKAGE
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG DEPENDENCY=/currency-server/target/dependency
COPY --from=BUILD ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=BUILD ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=BUILD ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.nishant.springboot.currencyutilities.CurrencyUtilitiesApplication"]