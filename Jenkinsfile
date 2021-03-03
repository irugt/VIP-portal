pipeline { 
    agent any  
    tools {
        maven 'M3'
    }
    stages { 
        stage('Build') { 
            steps {
                sh 'mvn clean verify'
            }
        }
        stage('Deploy') { 
            steps {
                //Pour le Debug
                sh '''
                        pwd
                        ls -l vip-portal/target/
                        ls -l /usr/local/apache-tomcat9/webapps
                        whoami
                        touch /home/irugt/testjenkins
                   ''' 
                sh '''
                        mv vip-portal/target/vip-portal-2.0-local.war /usr/local/apache-tomcat9/webapps/
                        systemctl restart tomcat9 
                   '''
            }
        }
    }
}
