#! /bin/bash

echo "[sync] start"
cd $(dirname "$0")
git add .
git ci -m 'update'
git pull --rebase
git push origin
echo "[sync] done"
