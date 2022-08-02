#! /bin/bash

echo "[sync] start"
cd $(dirname "$0")
git ci -am 'update'
git pull --rebase
git push origin
echo "[sync] done"
