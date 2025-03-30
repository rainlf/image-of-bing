#! /bin/bash

echo "[sync] start $(pwd)"
git add .
git ci -m 'update'
git pull --rebase
git push origin
echo "[sync] done"
