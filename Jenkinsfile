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
                        ls vip-portal/target/
                        ls /usr/local/apache-tomcat9/webapps
                        whoami
                   ''' 
                sh '''
                        mv vip-portal/target/vip-portal-2.0-local.war /usr/local/apache-tomcat9/webapps/
                        systemctl restart tomcat9 
                   '''
            }
        }
    }
}
