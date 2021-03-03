pipeline { 
    agent any  
    tools {
        maven 'M3'
    }
    stages { 
        stage('Build') { 
            steps {
                sh ' pwd '
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
        /*stage('Deploy') { 
            steps {
               sh ''' mv target/*.war /opt/tomcat9/webapps
                      systemctl restart tomcat9 '''
            }
        }*/
    }
}
