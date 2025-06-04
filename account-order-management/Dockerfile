# OpenJDK 17 kullanarak temel imajı seçiyoruz
FROM eclipse-temurin:17-jdk

# Çalışma dizinini oluştur
WORKDIR /app

# Bağımlılıkları ve uygulama JAR dosyasını container'a kopyala
COPY target/account-order-management-0.0.1-SNAPSHOT.jar app.jar

# Port tanımla (Opsiyonel, eğer farklı bir port kullanıyorsan değiştir)
EXPOSE 8080

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]
