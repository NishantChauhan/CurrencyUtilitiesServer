pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-11'
            args '-v /root/.m2:/root/.m2 -u root -p 5000:5000 -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker -v /usr/local/bin/docker:/usr/local/bin/docker'
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
				sh 'chmod +x ./jenkins/scripts/*.sh'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
       stage('Package') {
            steps {
                sh './jenkins/scripts/package.sh'
                sh './jenkins/scripts/kill.sh'
            }
        }
        stage('Deliver') {
             environment {
                 DOCKER_HUB_CREDS = credentials('docker-hub-credential')
             }
            steps {
                sh './jenkins/scripts/deliver.sh'
            }
        }
    }
}