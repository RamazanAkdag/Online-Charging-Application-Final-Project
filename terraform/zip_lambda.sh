#!/bin/bash

LAMBDA_DIR="modules/lambda"
ZIP_FILE="$LAMBDA_DIR/efs_backup_lambda.zip"

echo "📦 Zipping Lambda function..."

# Eski ZIP varsa sil
[ -f "$ZIP_FILE" ] && rm "$ZIP_FILE"

# Zip oluştur
cd "$LAMBDA_DIR" || exit
zip -r "$(basename "$ZIP_FILE")" index.py > /dev/null

echo "✅ Lambda ZIP created at: $ZIP_FILE"
