#!/bin/bash
mysqldump -h $MYSQL_HOST -u root -p$MYSQL_ROOT_PASSWORD ruben-project | gzip > /backup/mysql-backup-$(date +\%F).sql.gz

