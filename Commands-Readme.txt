* to execute and run test cases

  mvn clean install exec:java -Dexec.mainClass="mainapp.MyApp" -DskipTests=true

git config --global user.email ""
git config --global user.name ""
====================================================================================
git init
echo "first line" >> history_file.txt
git add history_file.txt
git commit -m "First commit"
git checkout -b feature_branch
echo "Second commit message" >> history_file.txt
git add history_file.txt
git commit -m "Second commit"
echo "Third commit message" >> history_file.txt
git add history_file.txt
git commit -m "Third commit"
git checkout main
git merge feature_branch
git revert HEAD --no-edit
