Clone Git repo for the scheduler with required branch
git clone --depth=1 ssh://git@git.source.akamai.com:7999/sipho/sipho-core.git -b {branch}
Check that correct code branch fetched.
cd sipho-core
git status
Build/Package
# Maven Install Sipho Core Application
cd sipho-core/siphocore
mvn clean install
