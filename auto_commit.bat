@echo off
echo "[sync] start"
git ci -am 'update'
git pull --rebase
git push
echo "[sync] done"
