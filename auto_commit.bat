@echo off
echo "start to sync"
git ci -am 'update'
git pull --rebase
git push
echo "done..."
