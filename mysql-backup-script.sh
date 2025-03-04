#!/bin/sh

# Variables
DB_NAME="ruben-project"
DB_USER="root"
DB_PASS=$(cat /etc/mysql-secrets/root-password)  # Read password from secret file
BACKUP_DIR="/backup"
BACKUP_FILE="$BACKUP_DIR/$DB_NAME-$(date +%F).sql.gz"

# MinIO Credentials
MC_ALIAS="minio"
MC_ACCESS_KEY=$(cat /etc/minio-secrets/access-key)
MC_SECRET_KEY=$(cat /etc/minio-secrets/secret-key)
MINIO_BUCKET="ruben-project"

# Ensure backup directory exists
mkdir -p $BACKUP_DIR

# Dump MySQL database and compress
mysqldump -u $DB_USER -p$DB_PASS $DB_NAME | gzip > $BACKUP_FILE

# Install MinIO Client (if not present)
if ! command -v mc &> /dev/null; then
  wget https://dl.min.io/client/mc/release/linux-amd64/mc -O /usr/local/bin/mc
  chmod +x /usr/local/bin/mc
fi

# Configure MinIO Client
mc alias set $MC_ALIAS http://minio:9000 $MC_ACCESS_KEY $MC_SECRET_KEY

# Upload the backup to MinIO
mc cp $BACKUP_FILE $MC_ALIAS/$MINIO_BUCKET/

# Delete backups older than 5 days
mc find $MC_ALIAS/$MINIO_BUCKET/ --name "*.sql.gz" --older-than 5d --exec "mc rm {}"

echo "Backup completed: $BACKUP_FILE"
