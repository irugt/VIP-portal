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
        stage('Test') { 
            steps {
               sh ' mvn compile '
            }
        }
        /*stage('Deploy') { 
            steps {
               sh ''' mv target/*.war /opt/tomcat9/webapps
                      systemctl restart tomcat9 '''
            }
        }*/
    }
}
