# To build:
Command line
Start a terminal 
cd <TRAINING_PROJECT_DIR>/PlatformLab/embeddedIMDGMember
$>chmod +x *.sh

First Check to see if maven is installed

    $>mvn -version

If NOT...
                
    Follow instructions here: 
    https://maven.apache.org/install.html

And setup shell Env

    Unix/Linux/MacOS:
    $> export MAVEN_HOME={INSTAL_DIR}/apache-maven-3.6.3
    $> export PATH=$MAVEN_HOME/bin:$PATH

Notes:

    Change default shell to bash on MacOS
    chsh -s /bin/bash

$> ./build.sh


# Cluster Exercise 1 To run:

$>./startMember.sh

Starts a Hazelcast IMDG node.

Run it two more times, check logs and Management center to see if you've successfully formed a cluster of 3 nodes.

# Cluster Exercise 2 run MapClinet:
$> ./runMapClient.sh

# Cluster Exercise 3 run MessageQueueClinet:
$> ./runMessageQClient.sh
