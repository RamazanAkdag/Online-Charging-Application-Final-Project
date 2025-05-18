import boto3
import os

def handler(event, context):
    s3 = boto3.client('s3')
    bucket = os.environ['TARGET_BUCKET']
    path = "/mnt/efs/backup.sql"
    s3.upload_file(path, bucket, "backup.sql")
    return {"status": "done"}