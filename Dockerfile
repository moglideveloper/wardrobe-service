FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine

COPY target/pack/lib app/lib
COPY target/pack/bin app/bin

CMD ["sh", "app/bin/wardrobe-service"]