# Usar la imagen oficial de Java 21 (coherente con los otros microservicios)
FROM eclipse-temurin:21-jdk  

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR del microservicio al contenedor
COPY target/signos_vitales_consumidor-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto correcto (8082)
EXPOSE 8082

# Comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]
