@echo off
echo "[sync] start"
cd %~dp0
git ci -am 'update'
git pull --rebase
git push origin
echo "[sync] done"
