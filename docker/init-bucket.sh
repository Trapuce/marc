#!/bin/sh

# Configurer l'alias MinIO
mc alias set myminio http://minio:9000 minio password

# Vérifier si le bucket existe déjà
BUCKET_NAME=${MINIO_BUCKET_NAME:-trybucket}
if ! mc ls myminio/$BUCKET_NAME 2>/dev/null; then
  echo "Création du bucket $BUCKET_NAME"
  mc mb myminio/$BUCKET_NAME
  echo "Bucket créé avec succès"
else
  echo "Le bucket $BUCKET_NAME existe déjà."
fi
