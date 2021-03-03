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
        /*stage('Test') { 
            steps {
                junit 
                sh ''' mkdir localTest
                       unzip vip-local/src/test/ressources/local-config.zip localTest 
                       '''
            }
        }*/
        stage('Deploy') { 
            steps {
                sh '''
                        ls vip-portal/target/
                        ls /usr/local/apache-tomcat9/webapps
                        mv vip-portal/target/vip-portal-2.0-local.war /var/lib/apache-tomcat9/webapps/
                        systemctl restart tomcat9 
                   '''
            }
        }
    }
}
