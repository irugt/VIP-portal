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
                        pwd
                        ll
                        mv target/*.war /var/lib/apache-tomcat9/webapps/
                        systemctl restart tomcat9 
                   '''
            }
        }
    }
}
