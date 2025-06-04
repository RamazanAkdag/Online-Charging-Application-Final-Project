#!/bin/bash

echo "🔍 Projedeki tüm *Test.java dosyaları aranıyor..."

# Tüm *Test.java dosyalarının class isimlerini al
test_classes=$(find . -name "*Test.java" | sed 's|.*/||; s|\.java$||' | paste -sd, -)

if [ -z "$test_classes" ]; then
  echo "⚠️ Hiç test sınıfı bulunamadı. Push iptal edilmedi ama dikkat et!"
  exit 0
fi

echo "🧪 Çalıştırılacak test sınıfları:"
echo "$test_classes"
echo ""

# Hariç tutulacak modüller
ignored_modules=("traffic-generator-function" "common")

# Tüm alt modülleri bul (root '.' hariç)
all_modules=$(find . -mindepth 2 -name "pom.xml" -exec dirname {} \; | sed 's|^\./||')

# Hariç tutulanları filtrele
included_modules=""
for module in $all_modules; do
  skip=false
  for ignore in "${ignored_modules[@]}"; do
    if [[ "$module" == "$ignore" ]]; then
      skip=true
      break
    fi
  done
  if ! $skip; then
    included_modules+="${module},"
  fi
done

# Sondaki virgülü sil
included_modules=$(echo "$included_modules" | sed 's/,$//')

echo "📦 Testler aşağıdaki modüllerde çalıştırılacak:"
echo "$included_modules"
echo ""
echo "🚀 Testler başlatılıyor..."

mvn -pl "$included_modules" -Dtest="$test_classes" test

if [ $? -ne 0 ]; then
  echo "❌ Testler başarısız oldu. Push iptal edildi."
  exit 1
else
  echo "✅ Testler başarılı. Tüm modüller derleniyor..."
  mvn clean install -DskipTests

  if [ $? -ne 0 ]; then
    echo "❌ Derleme sırasında hata oluştu. .jar dosyaları üretilemedi."
    exit 1
  fi

  echo ""
  echo "🚀 Modüller çalıştırılıyor..."

IGNITE_JVM_OPTS=$(tr '\n' ' ' < ignite-jvm-opts.txt)

IGNITE_JVM_OPTS="--add-opens=java.base/jdk.internal.access=ALL-UNNAMED \
--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED \
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
--add-opens=java.base/sun.util.calendar=ALL-UNNAMED \
--add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED \
--add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED \
--add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED \
--add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED \
--add-opens=java.base/java.io=ALL-UNNAMED \
--add-opens=java.base/java.nio=ALL-UNNAMED \
--add-opens=java.base/java.net=ALL-UNNAMED \
--add-opens=java.base/java.util=ALL-UNNAMED \
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED \
--add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED \
--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED \
--add-opens=java.base/java.lang=ALL-UNNAMED \
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED \
--add-opens=java.base/java.math=ALL-UNNAMED \
--add-opens=java.sql/java.sql=ALL-UNNAMED \
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
--add-opens=java.base/java.time=ALL-UNNAMED \
--add-opens=java.base/java.text=ALL-UNNAMED \
--add-opens=java.management/sun.management=ALL-UNNAMED \
--add-opens=java.desktop/java.awt.font=ALL-UNNAMED \
-DIGNITE_PEER_CLASS_LOADING=true -Djava.net.preferIPv4Stack=true"

echo "🚀 Modüller çalıştırılıyor..."

IGNITE_JVM_OPTS=$(tr '\n' ' ' < ignite-jvm-opts.txt)

IFS=',' read -ra modules <<< "$included_modules"

for module in "${modules[@]}"; do
  jar_path=$(find "$module/target" -maxdepth 1 -name '*.jar' ! -name '*sources.jar' | head -n 1)

  if [[ "$module" == "account-order-management" || "$module" == "online-charging-system" ]]; then
    echo "🚀 $module (with Ignite opts) başlatılıyor"
    java $IGNITE_JVM_OPTS -jar "$jar_path" &
  else
    echo "🚀 $module başlatılıyor"
    java -jar "$jar_path" &
  fi
done



  wait
  echo "🏁 Tüm modüller çalıştırıldı."
  exit 0
fi