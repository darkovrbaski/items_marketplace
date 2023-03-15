FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY .mvn .mvn
COPY mvnw pom.xml checkstyle.xml ./
COPY src src

RUN apk add dos2unix=7.4.3-r1 --no-cache
RUN dos2unix mvnw

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre-alpine
RUN addgroup usergroup; adduser  --ingroup usergroup --disabled-password user
USER user

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","me.darkovrbaski.items.marketplace.ItemsMarketplaceApplication"]
