pipeline { 
    agent any  
    tools {
        maven 'M3'
    }
    stages { 
        stage('Build') { 
            steps {
                sh ' mvn compile '
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
                        mvn clean verify
                        pwd
                        ls target/
                        mv vip-portal/target/vip-portal-2.0-local.war /var/lib/apache-tomcat9/webapps/
                        systemctl restart tomcat9 
                   '''
            }
        }
    }
}
