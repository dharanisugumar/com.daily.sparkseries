#Sipho-core

This is a common framework which can be used across all the Flink applications and there will be a continuous enhancements happening as part of this repo.

#Steps to deploy
Clone Git repo for the scheduler with required branch

git clone --depth=1 ssh://git@git.source.akamai.com:7999/sipho/sipho-core.git -b {branch}

Check that correct code branch fetched.
cd sipho-core

git status

# Maven Install Sipho Core Application

mvn clean install
