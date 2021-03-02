pipeline { 
    agent any  
    tools {
        maven 'Maven 4.0.0'
        jdk 'jdk11'
    }
    stages { 
        stage('Build') { 
            steps {
               echo 'This is a minimal pipeline.'
               sh ' mvn compile '
            }
        }
    }
}
