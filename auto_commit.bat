@echo off
echo "[sync] start"
cd %~dp0
git add .
git ci -m 'update'
git pull --rebase
git push origin
echo "[sync] done"
