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
  echo "✅ Testler başarılı. Push devam edebilir."
  exit 0
fi
