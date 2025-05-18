#!/bin/bash

# Çıkış dosyası yolu (3 klasör yukarıdan terraform dizinine gider)
OUTPUT_FILE="../../../ansible/ec2_ids"

echo "📤 Terraform output değerleri yazılıyor: $OUTPUT_FILE"

# Terraform çıktısını JSON formatında al
terraform output -json > temp_output.json

# EC2 IP'leri içeren 'all_ips' alanını ayıkla ve dosyaya yaz
jq -r '.all_ips.value | to_entries[] | "\(.key)=\(.value)"' temp_output.json > "$OUTPUT_FILE"

# Geçici dosyayı sil
rm temp_output.json

echo "✅ Yazıldı: $(cat $OUTPUT_FILE)"
